package ast;

import java.util.LinkedList;

public class NodoBloque extends NodoSentencia{
    LinkedList<NodoSentencia> sentencias = new LinkedList<>();

    public NodoBloque(int linea, int columna) {
        super(linea, columna);
    }

    public void insertarSentencia(NodoSentencia nodoSentencia){
        sentencias.add(nodoSentencia);
    }
}
