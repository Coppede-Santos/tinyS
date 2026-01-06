package ast;

import analizadorLexico.TokenType;

public class NodoString extends NodoOperando{
    String valor;

    public NodoString(String valor, int linea, int columna) {
        super("STRING", linea, columna);
        this.valor = valor;
    }

}
