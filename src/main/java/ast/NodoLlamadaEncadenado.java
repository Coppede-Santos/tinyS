package ast;

import java.util.LinkedList;

public class NodoLlamadaEncadenado extends NodoEncadenado{

    LinkedList<NodoExp> parametros;
    NodoEncadenado encadenado;

    public NodoLlamadaEncadenado (String lex){
        super(lex);
    }

}
