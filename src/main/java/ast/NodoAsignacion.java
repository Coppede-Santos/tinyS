package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoAsignacion extends NodoSentencia{
    NodoVar izquierda;
    NodoExp derecha;

    public NodoAsignacion(NodoVar izquierda, NodoExp derecha, int linea, int columna) {
        super(linea, columna);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }


    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";
        if (izquierda == null || derecha == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");

        izquierda.chequeoDeSentencias(entradaMetodo,st);
        derecha.chequeoDeSentencias(entradaMetodo,st);

        if (izquierda.getTipo() != derecha.getTipo()) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");




        return salida;
    }
}
