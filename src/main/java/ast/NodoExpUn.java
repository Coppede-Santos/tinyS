package ast;

import analizadorLexico.TokenType;

public class NodoExpUn extends NodoExp{
    NodoExp ladoDerecho;
    TokenType operador;

    public NodoExpUn(NodoExp nodoExp, TokenType type, int linea, int columna) {
        super(linea, columna);
        this.ladoDerecho = nodoExp;
        this.operador = type;
    }

    public NodoExpUn(int linea, int columna) {
        super(linea, columna);
    }
}
