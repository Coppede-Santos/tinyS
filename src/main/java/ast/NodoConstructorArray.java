package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import analizadorSemantico.Errores.ErrorSemantico;


public class NodoConstructorArray extends NodoOperando{
    String subtipo;
    NodoExp dimension;

    public NodoConstructorArray(String subtipo, int linea, int columna) {
        super("Array", linea, columna);
        this.subtipo=subtipo;
    }

    public void setDimension(NodoExp nodoExp){
        this.dimension = nodoExp;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";

        salida += dimension.chequeoDeSentencias(entradaMetodo, st);

        if (!dimension.getTipo().equals("Int")){
            throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"Se esperaba tipo INT en la dimension del array","");
        }

        // a = (new Array Int[dim]).length();

        if (encadenado != null){
            salida += this.encadenado.chequeoDeSentencias(
                    entradaMetodo,
                    st,
                    this.tipo
            );
        }

        // Array Int

        return salida;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev) throws ErrorSemantico {
        throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"No se puede encadenar a un constructor de array","");
    }

}
