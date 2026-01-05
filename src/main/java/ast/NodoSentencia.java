package ast;

import analizadorSemantico.Posicion;

public abstract class NodoSentencia {

    public Posicion posicion;

    public NodoSentencia(int linea, int columna){
        this.posicion = new Posicion(linea, columna);
    }

}
