package ast;

import java.util.LinkedList;

public class NodoLlamadaEncadenado extends NodoVar{

    LinkedList<NodoExp> parametros = new LinkedList<NodoExp>();

    public NodoLlamadaEncadenado (String lex, int linea, int columna){
        super(lex, linea, columna);
    }

    public void agregarParametro(NodoExp nodoExp){
        parametros.add(nodoExp);

    }

}
