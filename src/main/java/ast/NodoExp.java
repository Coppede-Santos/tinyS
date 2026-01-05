package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.Posicion;

public class NodoExp extends NodoSentencia {

    TokenType tipo;
    NodoExp encadenado;

    public NodoExp(TokenType tipo, int linea, int columna) {
        super(linea, columna);

        this.tipo = tipo;
    }

    public NodoExp(int linea, int columna) {
        super(linea, columna);
    }

    public void setEncadenado(NodoExp encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo(TokenType tipo){
        this.tipo = tipo;
    }
}
