package ast;

import analizadorLexico.TokenType;

public class NodoExpBin extends NodoExpUn{
    NodoExp ladoIzquierdo;

    public NodoExpBin(NodoExp ladoIzquierdo, NodoExp ladoDerecho, TokenType operador, int linea, int columna){
        super(ladoDerecho,operador, linea, columna);
        this.ladoIzquierdo = ladoDerecho;
    }
}
