package ast;

public class NodoInt extends NodoNum{
    int valor;

    public NodoInt(int valor, int linea, int columna){
        super(linea, columna);
        this.valor = valor;
    }


}
