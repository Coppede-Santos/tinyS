package ast;

import analizadorLexico.TokenType;


public class NodoConstructorArray extends NodoVar{
    TokenType subtipo;
    NodoExp dimension;

    public NodoConstructorArray(TokenType subtipo, int linea, int columna) {
        super("Array", linea, columna);
        this.subtipo=subtipo;

    }

    public void setDimension(NodoExp nodoExp){
        this.dimension = nodoExp;
    }

}
