package ast;

import analizadorLexico.TokenType;

public class NodoDouble extends NodoNum{
    double valor;

    public NodoDouble(Double literal, int linea, int columna) {
        super("DOUBLE", linea, columna);
        valor = literal;
    }
}
