package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaParametro;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import ast.Errores.MetodoNoDeclaradoError;
import ast.Errores.ParametroTipoError;
import ast.Errores.ParametrosCantidadError;
import ast.Errores.EstaticoMetodoError;

import java.util.LinkedList;

import static ast.AstJsonBuilder.*;

public class NodoLlamadaMetodo extends NodoVar{

    LinkedList<NodoExp> parametros = new LinkedList<>();
    String clase = ""; //Clase que contiene el metodo

    public NodoLlamadaMetodo(String lex, int linea, int columna){
        super(lex, linea, columna);
    }

    // getCompany().getAddress().getStreet().getNumber();

    public void agregarParametro(NodoExp nodoExp){
        parametros.add(nodoExp);

    }

    public LinkedList<NodoExp> getParametros() {
        return parametros;
    }

    public String getClase() {
        return clase;
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
            // Es un constructor
            esConstructor = true;
            claseActual = st.buscarClase(lexema);
            metodoReferenciado  = claseActual.getConstructor();


            //Seteamos para la gc
            clase = claseActual.getLexema();
        } else {
            // Es un metodo
            claseActual = st.getClassActual();
            metodoReferenciado = claseActual.buscarMetodo(lexema);

            //Seteamos para la gc
            clase = claseActual.getLexema();

        }

        if (metodoReferenciado == null) {
            throw new MetodoNoDeclaradoError(posicion, lexema);
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ParametrosCantidadError(posicion,lexema);
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
                throw new ParametrosCantidadError(posicion,lexema);
            }

            EntradaClase entradaTipoParametroAcutal = st.buscarClase(parametroActual.tipo);

            if (entradaTipoParametroAcutal == null){
                throw new ClaseNoDeclaradaError(parametroActual.posicion.getColumna(),parametroActual.posicion.getLinea(), parametroActual.tipo);
            }

            if (!entradaTipoParametroAcutal.buscarAncestro(st,parametroReferenciado.getTipo())) {
                throw new ParametroTipoError(posicion,entradaTipoParametroAcutal.getLexema());
            }


//            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
//                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
//                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
//            }
//

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

        //Seteamos para la gc
        clase = claseActual.getLexema();

        EntradaMetodo metodoReferenciado = claseActual.buscarMetodo(lexema);
        EntradaParametro parametroReferenciado;

        if (metodoReferenciado == null) {
            throw new MetodoNoDeclaradoError(posicion,lexema);
        }

        if (esEstatico) {
            if (!metodoReferenciado.esEstatico()) {
                throw new EstaticoMetodoError(posicion,lexema);
            }
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ParametrosCantidadError(posicion,lexema);
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
                throw new ParametrosCantidadError(posicion,lexema);            }

            EntradaClase entradaTipoParametroAcutal = st.buscarClase(parametroActual.tipo);

            if (entradaTipoParametroAcutal == null){
                throw new ClaseNoDeclaradaError(parametroActual.posicion.getColumna(),parametroActual.posicion.getLinea(), parametroActual.tipo);
            }

            if (!entradaTipoParametroAcutal.buscarAncestro(st,parametroReferenciado.getTipo())) {
                throw new ParametroTipoError(posicion, lexema);
            }

//            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
//                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
//                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
//            }



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
