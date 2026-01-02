package ast;

import analizadorLexico.TokenType;

public class NodoNil extends NodoOperando{

    public NodoNil(){
        super(TokenType.NIL);
    }
}
