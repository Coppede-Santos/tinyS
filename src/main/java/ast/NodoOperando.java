package ast;

import analizadorLexico.TokenType;

public abstract class NodoOperando extends NodoExp {

    public NodoOperando(TokenType type){
        super(type);
    }
    public NodoOperando(){
        super();
    }
}
