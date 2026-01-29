package generacionDeCodigo;

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


}
