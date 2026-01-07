package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.*;
import analizadorSemantico.Errores.ErrorSemantico;

import java.util.Objects;

public  class NodoVar extends NodoOperando{
    String lexema;
    Boolean esEstatico = false;

    public NodoVar(String lexema, int linea, int columna) {
        super(linea, columna);
        this.lexema = lexema;
    }

    public void setEsEstatico(Boolean esEstatico){
        this.esEstatico = esEstatico;

    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";

        EntradaVariables variable = entradaMetodo.buscarVariableLocal(lexema);
        if (variable == null) {
            variable = entradaMetodo.buscarParametro(lexema);
        }

        if (variable == null) {
            variable = st.getClassActual().buscarAtributo(lexema);
        }

        if (variable ==  null) {
            if  (esEstatico) {
                EntradaClase claseActual = st.buscarClase(lexema);
                if (claseActual == null) {
                    throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la clase " + lexema + " no existe", "");
                }
                this.tipo = claseActual.getLexema();
            }else {
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la variable " + lexema + " no existe en el metodo " + entradaMetodo.getLexema(), "");
            }
        } else {
            this.tipo = variable.getTipo().getLexema();
        }

        if (encadenado != null){
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo
            );
        }

        return salida;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev) throws ErrorSemantico {
        String salida = "";

        EntradaClase entradaClase = st.buscarClase(tipoEncadenadoPrev);
        if (entradaClase == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "la clase " + tipoEncadenadoPrev + " no existe","");
        }

        EntradaAtributos atributo = entradaClase.buscarAtributo(lexema);
        if (atributo == null) {
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "el atributo " + lexema + " no existe en la clase " + tipoEncadenadoPrev,"");
        }

        this.tipo = atributo.getTipo().getLexema();

        if (encadenado != null){
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo
            );
        }

        return salida;
    }
}
