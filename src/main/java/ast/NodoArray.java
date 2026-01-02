package ast;

import analizadorLexico.TokenType;


public class NodoArray extends NodoOperando{
    TokenType subtipo;
    NodoExp dimension;

    public NodoArray( TokenType subtipo){
        super(TokenType.ARRAY);
        this.subtipo=subtipo;

    }

    public void setDimension(NodoExp nodoExp){
        this.dimension = nodoExp;
    }

}
