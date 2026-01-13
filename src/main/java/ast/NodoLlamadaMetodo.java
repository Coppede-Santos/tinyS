package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaParametro;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

import static ast.AstJsonBuilder.*;

public class NodoLlamadaMetodo extends NodoVar{

    LinkedList<NodoExp> parametros = new LinkedList<>();

    public NodoLlamadaMetodo(String lex, int linea, int columna){
        super(lex, linea, columna);
    }

    // getCompany().getAddress().getStreet().getNumber();

    public void agregarParametro(NodoExp nodoExp){
        parametros.add(nodoExp);

    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";
        boolean esConstructor = false;
        EntradaClase claseActual;
        EntradaMetodo metodoReferenciado;
        EntradaParametro parametroReferenciado;

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoLlamadaEncadenado") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";

        if (st.buscarClase(lexema) != null) {
            esConstructor = true;
            claseActual = st.buscarClase(lexema);
            metodoReferenciado  = claseActual.getConstructor();
        } else {
            claseActual = st.getClassActual();
            metodoReferenciado = claseActual.buscarMetodo(lexema);
        }

        if (metodoReferenciado == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "el metodo " + lexema + " no existe en la clase " + claseActual.getLexema(), "");
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "la cantidad de parametros en la llamada al metodo " + lexema + " no coincide con la cantidad esperada", "");
        }

        salida += tabs(profundidad + 1) + claveJson("parametros") + "[\n";
        for (int i = 0; i < parametros.size(); i++) {

            NodoExp parametroActual = parametros.get(i);

            salida += tabs(profundidad + 2) + "{\n";
            salida += parametroActual.chequeoDeSentencias(entradaMetodo, st, profundidad + 2);
            if (i != parametros.size() - 1) {
                salida += tabs(profundidad + 2) + "},\n";
            } else {
                salida += tabs(profundidad + 2) + "}\n";
            }

            parametroReferenciado = metodoReferenciado.buscarParametroPorPosicion(i);

            if (parametroReferenciado == null) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "no se encontro el parametro en la posicion " + i + " para el metodo " + lexema, "");
            }

            EntradaClase entradaTipoParametroAcutal = st.buscarClase(parametroActual.tipo);

            if (entradaTipoParametroAcutal == null){
                throw new ClaseNoDeclaradaError(parametroActual.posicion.getColumna(),parametroActual.posicion.getLinea(), parametroActual.tipo);
            }

            if (!entradaTipoParametroAcutal.buscarAncestro(parametroReferenciado.getTipo().getLexema())) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
            }


//            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
//                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
//                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
//            }

            if (i != parametroReferenciado.getPosicionParametro()){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el orden de los parametros no coincide con el orden esperado en el metodo " + lexema, "");
            }

        }
        salida += tabs(profundidad + 1) + "],\n";

        EntradaClase tipoRetorno = metodoReferenciado.getTipoRetorno();

        if (tipoRetorno == null) {
            if (esConstructor) {
                this.tipo = claseActual.getLexema();
            } else {
                this.tipo = "nil";
            }
        } else {
            this.tipo = tipoRetorno.getLexema();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("esEstatico") + valorJson(String.valueOf(false)) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}";

        if (encadenado != null){
            salida += ",\n";
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );

            this.tipo = encadenado.getTipo();
        } else {
            salida += "\n";
        }
        
        return salida;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoLlamadaEncadenado") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("lexema") + valorJson(lexema) + ",\n";

        EntradaClase claseActual = st.buscarClase(tipoEncadenadoPrev);
        EntradaMetodo metodoReferenciado = claseActual.buscarMetodo(lexema);
        EntradaParametro parametroReferenciado;

        if (metodoReferenciado == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "el metodo " + lexema + " no existe en la clase " + claseActual.getLexema(), "");
        }

        if (esEstatico) {
            if (!metodoReferenciado.esEstatico()) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "no se puede llamar de forma estatica al metodo no estatico " + lexema, "");
            }
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "la cantidad de parametros en la llamada al metodo " + lexema + " no coincide con la cantidad esperada", "");
        }

        salida += tabs(profundidad + 1) + claveJson("parametros") + "[\n";

        for (int i = 0; i < parametros.size(); i++) {

            NodoExp parametroActual = parametros.get(i);

            salida += tabs(profundidad + 2) + "{\n";
            salida += parametroActual.chequeoDeSentencias(entradaMetodo, st, profundidad + 2);
            if (i != parametros.size() - 1) {
                salida += tabs(profundidad + 2) + "},\n";
            } else {
                salida += tabs(profundidad + 2) + "}\n";
            }

            parametroReferenciado = metodoReferenciado.buscarParametroPorPosicion(i);

            if (parametroReferenciado == null) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "no se encontro el parametro en la posicion " + i + " para el metodo " + lexema, "");
            }

            EntradaClase entradaTipoParametroAcutal = st.buscarClase(parametroActual.tipo);

            if (entradaTipoParametroAcutal == null){
                throw new ClaseNoDeclaradaError(parametroActual.posicion.getColumna(),parametroActual.posicion.getLinea(), parametroActual.tipo);
            }

            if (!entradaTipoParametroAcutal.buscarAncestro(parametroReferenciado.getTipo().getLexema())) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
            }

//            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
//                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
//                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
//            }

            if (i != parametroReferenciado.getPosicionParametro()){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el orden de los parametros no coincide con el orden esperado en el metodo " + lexema, "");
            }

        }

        salida += tabs(profundidad + 1) + "],\n";

        EntradaClase tipoRetorno = metodoReferenciado.getTipoRetorno();

        if (tipoRetorno == null) {
            this.tipo = "nil";
        } else {
            this.tipo = tipoRetorno.getLexema();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(this.tipo) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("esEstatico") + valorJson(String.valueOf(esEstatico)) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion") + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}";

        if (encadenado != null){
            salida += ",\n";
            salida += tabs(profundidad + 1) + claveJson("encadenado") + "\n";
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo,
                    profundidad + 1
            );
            this.tipo = encadenado.getTipo();
        } else {
            salida += "\n";
        }

        return salida.toString();


    }

}
