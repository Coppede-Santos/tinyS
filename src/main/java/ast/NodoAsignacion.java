package ast;

public class NodoAsignacion extends NodoSentencia{
    NodoVar izquierda;
    NodoExp derecha;

    public NodoAsignacion(NodoVar izquierda, NodoExp derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }
}
