package ast;

import analizadorLexico.TokenType;

public class NodoBool extends NodoOperando{

    boolean valor;

    public NodoBool(TokenType tipo, int linea, int columna) {
        super(tipo, linea, columna);
        if (tipo == TokenType.TRUE) {
            this.valor = true;
        } else {
            this.valor = false;
        }
    }

}
