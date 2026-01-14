package ast;

import analizadorLexico.TokenType;

public abstract class NodoNum extends NodoOperando{

    public NodoNum(String type, int linea, int columna){
        super(type, linea, columna);

    }
}
