package ast;

public class NodoArray extends NodoOperando{
    NodoExp indice;

    public NodoArray(NodoExp indice, int linea, int columna) {
        super(linea, columna);
        this.indice = indice;
    }

    public NodoArray(int linea, int columna) {
        super(linea, columna);
    }

    public void setIndice(NodoExp indice) {
        this.indice = indice;
    }
}
