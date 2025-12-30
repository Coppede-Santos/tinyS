package ast;

public class NodoWhile extends NodoSentencia {
    NodoExp condicion;
    NodoSentencia sentencia;

    public NodoWhile(NodoExp condicion, NodoSentencia sentencia) {
        this.condicion = condicion;
        this.sentencia = sentencia;
    }
}
