package analizadorSemantico;

import java.util.HashMap;

public class EntradaClase extends Entrada {
    EntradaClase superClase;
    HashMap<String, EntradaAtributos> atributos = new HashMap<>();
    HashMap<String, EntradaMetodo> metodos = new HashMap<>();
    boolean tieneConstructor = false;

    public EntradaClase(String nombre) {
        super(nombre);
    }

    public EntradaClase(String nombre, EntradaClase superClase) {
        super(nombre);
        this.superClase = superClase;
        agregarMetodosDeSuperClase();
    }

    public EntradaClase(String nombre, int linea, int columna, EntradaClase superClase) {
        super(nombre, linea, columna);
        this.superClase = superClase;
        agregarMetodosDeSuperClase();
    }
    public EntradaClase(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    public EntradaAtributos buscarAtributo(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    public boolean insertarAtributo(String nombreAtributo, EntradaAtributos entradaAtributo) {
        if (atributos.containsKey(nombreAtributo))
            return false;
        atributos.put(nombreAtributo, entradaAtributo);
        return true;
    }

    public EntradaMetodo buscarMetodo(String nombreMetodo) {
        return metodos.get(nombreMetodo);
    }

    public boolean insertarMetodo(String nombreMetodo, EntradaMetodo entradaMetodo) {
        if (metodos.containsKey(nombreMetodo))
            return false;
        metodos.put(nombreMetodo, entradaMetodo);
        return true;
    }

    public EntradaClase getSuperClase() {
        return superClase;
    }

    public boolean tieneConstructor() {
        return tieneConstructor;
    }

    public void setTieneConstructor(boolean tieneConstructor) {
        this.tieneConstructor = tieneConstructor;
    }

    public void setSuperClase(EntradaClase superClase) {
        this.superClase = superClase;
        agregarMetodosDeSuperClase();
    }

    private void agregarMetodosDeSuperClase() {
        if (superClase != null) {
            for (String nombreMetodo : superClase.metodos.keySet()) {
                EntradaMetodo metodoSuperClase = superClase.metodos.get(nombreMetodo);
                this.metodos.put(nombreMetodo, metodoSuperClase);
            }
        }
    }
}
