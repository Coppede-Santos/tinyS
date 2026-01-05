package ast;

public class NodoString extends NodoOperando{
    String valor;

    public NodoString(String valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }

}
