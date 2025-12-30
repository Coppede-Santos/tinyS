package ast;

import java.util.LinkedList;

public class NodoLlamadaEncadenado extends NodoVar{

    LinkedList<NodoExp> parametros;

    public NodoLlamadaEncadenado (String lex){
        super(lex);
    }

}
