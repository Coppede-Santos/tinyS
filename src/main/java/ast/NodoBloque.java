package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.LinkedList;

public class NodoBloque extends NodoSentencia{
    LinkedList<NodoSentencia> sentencias = new LinkedList<>();

    public NodoBloque(int linea, int columna) {
        super(linea, columna);
    }

    public void insertarSentencia(NodoSentencia nodoSentencia){
        sentencias.add(nodoSentencia);
    }



    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico{
        String salida = "";

        for (NodoSentencia nodoSentencia : sentencias){
            salida += nodoSentencia.chequeoDeSentencias(entradaMetodo, st);
        }

        return salida;
    }
}
