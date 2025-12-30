package ast;

public  class NodoVar extends NodoOperando{
    String lexema;
    NodoVar encadenado;

    public NodoVar(String lexema) {
        this.lexema = lexema;
    }

}
