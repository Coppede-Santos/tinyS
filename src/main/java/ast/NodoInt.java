package ast;

import analizadorLexico.TokenType;

public class NodoInt extends NodoNum{
    int valor;

    public NodoInt(int valor, int linea, int columna){
        super("INT", linea, columna);
        this.valor = valor;
    }


}
