package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.Posicion;
import analizadorSemantico.SymbolTable;

public abstract class NodoExp extends NodoSentencia {

    String tipo;
    NodoExp encadenado;

    public NodoExp(String tipo, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
    }

    public NodoExp(int linea, int columna) {
        super(linea, columna);
    }

    public void setEncadenado(NodoExp encadenado) {
        this.encadenado = encadenado;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
