package ast;

import analizadorLexico.TokenType;


public class NodoConstructorArray extends NodoVar{
    String subtipo;
    NodoExp dimension;

    public NodoConstructorArray(String subtipo, int linea, int columna) {
        super("ARRAY", linea, columna);
        this.subtipo=subtipo;

    }

    public void setDimension(NodoExp nodoExp){
        this.dimension = nodoExp;
    }

}
