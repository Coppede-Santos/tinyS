package ast;

public class NodoWhile extends NodoSentencia {
    NodoExp condicion;
    NodoSentencia sentencia;

    public NodoWhile(NodoExp condicion, NodoSentencia sentencia, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }
}
