package ast;

import analizadorLexico.TokenType;

public class NodoExpUn extends NodoExp{
    NodoExp ladoDerecho;
    TokenType operador;

    public NodoExpUn(NodoExp nodoExp, TokenType type) {
        this.ladoDerecho = nodoExp;
        this.operador = type;
    }
}
