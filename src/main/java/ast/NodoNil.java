package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoNil extends NodoOperando{

    public NodoNil(int linea, int columna){
        super("nil", linea, columna);
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        if (encadenado != null){
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"prohibido encadenar una expresion","");
        }
        return "null";
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev) throws ErrorSemantico {
        throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "prohibido encadenar una expresion","");
    }
}
