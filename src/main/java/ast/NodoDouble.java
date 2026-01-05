package ast;

public class NodoDouble extends NodoNum{
    double valor;

    public NodoDouble(Double literal, int linea, int columna) {
        super(linea, columna);
        valor = literal;
    }
}
