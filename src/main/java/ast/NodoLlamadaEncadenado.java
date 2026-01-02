package ast;

import java.util.LinkedList;

public class NodoLlamadaEncadenado extends NodoVar{

    LinkedList<NodoExp> parametros = new LinkedList<NodoExp>();

    public NodoLlamadaEncadenado (String lex){
        super(lex);
    }

    public void agregarParametro(NodoExp nodoExp){
        parametros.add(nodoExp);

    }

}
