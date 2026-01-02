package ast;

import analizadorLexico.TokenType;

public class NodoExp extends NodoSentencia {

    TokenType tipo;
    NodoExp encadenado;

    public NodoExp( TokenType tipo) {
        this.tipo = tipo;
    }
    public NodoExp() {

    }

    public void setEncadenado(NodoExp encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo(TokenType tipo){
        this.tipo = tipo;
    }
}
