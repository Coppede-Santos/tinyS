package ast;

/** Clase que representa un nodo operando en el AST */
public abstract class NodoOperando extends NodoExp {

    /** Constructor de la clase NodoOperando */
    public NodoOperando(String type, int linea, int columna){
        super(type, linea, columna);
    }

    /** Constructor de la clase NodoOperando sin tipo inicial */
    public NodoOperando(int linea, int columna){
        super(linea, columna);
    }
}
