package ast;

import java.util.LinkedList;

public class NodoBloque extends NodoSentencia{
    LinkedList<NodoSentencia> sentencias = new LinkedList<>();


    public void insertarSentencia(NodoSentencia nodoSentencia){
        sentencias.add(nodoSentencia);
    }
}
