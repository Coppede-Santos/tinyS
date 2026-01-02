package ast;

import analizadorLexico.TokenType;

public abstract class NodoNum extends NodoOperando{

    public NodoNum(TokenType type){
        super(type);

    }
}
