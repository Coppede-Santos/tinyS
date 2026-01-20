package generacionDeCodigo;

import analizadorSemantico.SymbolTable;
import ast.NodoSentencia;

import java.util.LinkedList;

public class NodeVisitor {
    CodeGen codigo = new CodeGen();
    SymbolTable st;

    public NodeVisitor(){
    }

    public void generarCodigo(){
    }



    public visitChildren(NodoSentencia n) {
        for node : n.getChildren() {
            node.codeGenerator(this);

        }
    }



}
