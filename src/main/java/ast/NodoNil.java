package ast;

import analizadorLexico.TokenType;

public class NodoNil extends NodoOperando{

    public NodoNil(int linea, int columna){
        super(TokenType.NIL, linea, columna);
    }
}
