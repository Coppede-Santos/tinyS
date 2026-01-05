package ast;

import analizadorLexico.TokenType;

public class NodoInt extends NodoNum{
    int valor;

    public NodoInt(int valor, int linea, int columna){
        super(TokenType.INTEGER_LITERAL, linea, columna);
        this.valor = valor;
    }


}
