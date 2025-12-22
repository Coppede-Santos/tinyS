package analizadorSemantico;

import java.util.HashMap;

public class EntradaClase extends Entrada {
    EntradaClase superClase;
    HashMap<String, EntradaAtributos> atributos = new HashMap<>();
    HashMap<String, EntradaMetodo> metodos = new HashMap<>();
    EntradaMetodo constructor = null;

    public EntradaClase(String nombre) {
        super(nombre);
    }

    public EntradaClase(String nombre, EntradaClase superClase) {
        super(nombre);
        this.superClase = superClase;
    }

    public EntradaClase(String nombre, int linea, int columna, EntradaClase superClase) {
        super(nombre, linea, columna);
        this.superClase = superClase;
    }
    public EntradaClase(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    EntradaAtributos buscarAtributo(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    boolean insertarAtributo(String nombreAtributo, EntradaAtributos entradaAtributo) {
        if (atributos.containsKey(nombreAtributo))
            return false;
        atributos.put(nombreAtributo, entradaAtributo);
        return true;
    }

    EntradaMetodo buscarMetodo(String nombreMetodo) {
        return metodos.get(nombreMetodo);
    }

    boolean insertarMetodo(String nombreMetodo, EntradaMetodo entradaMetodo) {
        if (metodos.containsKey(nombreMetodo))
            return false;
        metodos.put(nombreMetodo, entradaMetodo);
        return true;
    }

    public EntradaClase getSuperClase() {
        return superClase;
    }

    public EntradaMetodo getConstructor() {
        return constructor;
    }

    public void setConstructor(EntradaMetodo constructor) {
        this.constructor = constructor;
    }

    public void setSuperClase(EntradaClase superClase) {
        this.superClase = superClase;
    }
}
