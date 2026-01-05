package ast;

import analizadorLexico.TokenType;

public abstract class NodoOperando extends NodoExp {

    public NodoOperando(TokenType type, int linea, int columna){
        super(type, linea, columna);
    }
    public NodoOperando(int linea, int columna){
        super(linea, columna);
    }
}
