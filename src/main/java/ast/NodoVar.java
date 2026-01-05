package ast;

import analizadorLexico.TokenType;

public  class NodoVar extends NodoOperando{
    String lexema;
    Boolean esEstatico = false;

    public NodoVar(String lexema, int linea, int columna) {
        super(linea, columna);
        this.lexema = lexema;
    }

    public void setEsEstatico(Boolean esEstatico){
        this.esEstatico = esEstatico;

    }

}
