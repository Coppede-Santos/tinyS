package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.Posicion;
import analizadorSemantico.SymbolTable;

public abstract class NodoSentencia {

    public Posicion posicion;

    public NodoSentencia(int linea, int columna){
        this.posicion = new Posicion(linea, columna);
    }

    public abstract String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws  ErrorTiny;

}
