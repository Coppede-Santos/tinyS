package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import generacionDeCodigo.MethodBodyVisitor;

/** Clase que representa un nodo de expresión en el AST */
public abstract class NodoExp extends NodoSentencia {

    String tipo;
    NodoExp encadenado;

    /** Constructor de la clase NodoExp */
    public NodoExp(String tipo, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
    }

    /** Constructor de la clase NodoExp sin tipo inicial */
    public NodoExp(int linea, int columna) {
        super(linea, columna);
    }

//    @Override
//    public void accept(MethodBodyVisitor methodBodyVisitor) {
//        methodBodyVisitor.generarCodigo(this);
//    }

    /** Método para establecer el encadenado */
    public void setEncadenado(NodoExp encadenado) {
        this.encadenado = encadenado;
    }

    /** Método para establecer el tipo */
    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    /** Método para obtener el tipo */
    public String getTipo() {
        return tipo;
    }

    public NodoExp getEncadenado() {
        return encadenado;
    }

    /** Método abstracto para realizar el chequeo de sentencias con encadenado
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param tipoEncadenadoPrev Tipo del encadenado previo
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    public abstract String chequeoDeSentencias(
            EntradaMetodo entradaMetodo,
            SymbolTable st,
            String tipoEncadenadoPrev,
            int profundidad
    ) throws  ErrorTiny;
}
