package ast;

import analizadorLexico.TokenType;

public class NodoExpBin extends NodoExpUn{
    NodoExp ladoIzquierdo;

    public NodoExpBin(NodoExp ladoIzquierdo, NodoExp ladoDerecho, TokenType operador){
        super(ladoDerecho,operador);
        this.ladoIzquierdo = ladoDerecho;
    }
}
