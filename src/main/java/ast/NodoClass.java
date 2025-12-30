package ast;

import java.util.HashMap;

public class NodoClass {
    String nombre;
    HashMap<String,NodoBloque> metodos = new HashMap<>();

    public NodoClass(String nombre) {
        this.nombre = nombre;
    }

    public void insertarMetodo(String lexema, NodoBloque bloque){
        metodos.put(lexema,bloque);
    }
}
