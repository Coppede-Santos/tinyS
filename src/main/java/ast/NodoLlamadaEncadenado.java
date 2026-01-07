package ast;

import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.EntradaParametro;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.LinkedList;
import java.util.Objects;

public class NodoLlamadaEncadenado extends NodoVar{

    LinkedList<NodoExp> parametros = new LinkedList<>();

    public NodoLlamadaEncadenado (String lex, int linea, int columna){
        super(lex, linea, columna);
    }

    // getCompany().getAddress().getStreet().getNumber();

    public void agregarParametro(NodoExp nodoExp){
        parametros.add(nodoExp);

    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        StringBuilder salida = new StringBuilder();

        EntradaClase claseActual = st.getClassActual();
        EntradaMetodo metodoReferenciado = claseActual.buscarMetodo(lexema);
        EntradaParametro parametroReferenciado;

        if (metodoReferenciado == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "el metodo " + lexema + " no existe en la clase " + claseActual.getLexema(), "");
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "la cantidad de parametros en la llamada al metodo " + lexema + " no coincide con la cantidad esperada", "");
        }

        for (int i = 0; i < parametros.size(); i++) {

            NodoExp parametroActual = parametros.get(i);

            salida.append(parametroActual.chequeoDeSentencias(entradaMetodo, st));

            parametroReferenciado = metodoReferenciado.buscarParametro(lexema);

            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
            }

            if (i != parametroReferenciado.getPosicionParametro()){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el orden de los parametros no coincide con el orden esperado en el metodo " + lexema, "");
            }

        }

        this.tipo = metodoReferenciado.getTipoRetorno().getLexema();

        if (encadenado != null){
            salida.append(this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo
            ));
        }

        return salida.toString();
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev) throws ErrorSemantico {
        StringBuilder salida = new StringBuilder();

        EntradaClase claseActual = st.buscarClase(tipoEncadenadoPrev);
        EntradaMetodo metodoReferenciado = claseActual.buscarMetodo(lexema);
        EntradaParametro parametroReferenciado;

        if (metodoReferenciado == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "el metodo " + lexema + " no existe en la clase " + claseActual.getLexema(), "");
        }

        if (parametros.size() != metodoReferenciado.getCantidadParametros()) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                    "la cantidad de parametros en la llamada al metodo " + lexema + " no coincide con la cantidad esperada", "");
        }

        for (int i = 0; i < parametros.size(); i++) {

            NodoExp parametroActual = parametros.get(i);

            salida.append(parametroActual.chequeoDeSentencias(entradaMetodo, st));

            parametroReferenciado = metodoReferenciado.buscarParametro(lexema);

            if (!Objects.equals(parametroActual.tipo, parametroReferenciado.getTipo().getLexema())) {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el tipo del parametro " + (i+1) + " no coincide con el tipo esperado en el metodo " + lexema, "");
            }

            if (i != parametroReferenciado.getPosicionParametro()){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),
                        "el orden de los parametros no coincide con el orden esperado en el metodo " + lexema, "");
            }

        }

        this.tipo = metodoReferenciado.getTipoRetorno().getLexema();

        if (encadenado != null){
            salida.append(this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo
            ));
        }

        return salida.toString();
    }

}
