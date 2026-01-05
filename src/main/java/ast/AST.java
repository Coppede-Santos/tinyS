package ast;

import analizadorSemantico.Errores.ErrorSemantico;

import java.util.HashMap;

public class AST {
    HashMap<String,NodoClass> clases = new HashMap<>();
    NodoBloque start;

    public void insertarClass(String lexema, NodoClass clase) {
        clases.put(lexema,clase);
    }

    public NodoClass getClass(String lex){
        return clases.get(lex);
    }

    public void setStart(NodoBloque start) {
        this.start = start;
    }
}
