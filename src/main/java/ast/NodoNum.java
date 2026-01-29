package ast;

/** Clase que representa un nodo numérico en el AST */
public abstract class NodoNum extends NodoOperando{

    /** Constructor de la clase NodoNum */
    public NodoNum(String type, int linea, int columna){
        super(type, linea, columna);
    }
}
