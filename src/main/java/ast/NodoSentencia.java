package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Posicion;
import analizadorSemantico.SymbolTable;

/** Clase abstracta que representa un nodo de sentencia en el AST */
public abstract class NodoSentencia {

    public Posicion posicion;

    /** Constructor de la clase NodoSentencia */
    public NodoSentencia(int linea, int columna){
        this.posicion = new Posicion(linea, columna);
    }

    /** Método abstracto para realizar el chequeo de sentencias
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    public abstract String chequeoDeSentencias(
            EntradaMetodo entradaMetodo,
            SymbolTable st,
            int profundidad)
    throws  ErrorTiny;

}
