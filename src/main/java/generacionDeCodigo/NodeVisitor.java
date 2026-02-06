package generacionDeCodigo;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.AST;
import ast.NodoSentencia;


/**
 * Clase abstracta para los visitantes de los nodos del AST.
 */
public abstract class NodeVisitor {
    /**
     * Generador de codigo
     */
    CodeGen codigo = new CodeGen();
    /**
     * Tabla de simbolos
     */
    SymbolTable st;
    /**
     * Arbol semantico abstracto
     */
    AST ast;

    /**
     * Genera el label para un metodo.
     * @param metodo metodo para el cual se generara el label
     * @return label generado
     */
    public String getLabel(EntradaMetodo metodo) {
        String label = "m_" + metodo.getLexema() + "_" +metodo.getLinea() + "_" + metodo.getColumna();
        return label;
    }


}
