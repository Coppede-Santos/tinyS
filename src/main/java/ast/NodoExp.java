package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import generacionDeCodigo.MethodBodyVisitor;

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

//    @Override
//    public void accept(MethodBodyVisitor methodBodyVisitor) {
//        methodBodyVisitor.generarCodigo(this);
//    }

    /** Metodo para obtener el ultimo encadenado */
    public NodoExp getUltimoEncadenado() {
        NodoExp actual = this;
        while (actual.getEncadenado() != null) {
            actual = actual.getEncadenado();
        }
        return actual;
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

    public NodoExp getEncadenado() {
        return encadenado;
    }

    public abstract String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws  ErrorTiny;
}
