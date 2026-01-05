package ast;

public class NodoBool extends NodoOperando{

    boolean valor;

    public NodoBool(boolean valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }

}
