package ast;

import analizadorLexico.TokenType;

public class NodoBool extends NodoOperando{

    boolean valor;

    public NodoBool(Boolean valor, int linea, int columna) {
        super("BOOL", linea, columna);
        this.valor = valor;
    }

}
