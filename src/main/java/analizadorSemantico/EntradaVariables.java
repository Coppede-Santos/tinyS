package analizadorSemantico;

public class EntradaVariables extends Entrada {
    EntradaClase tipo; // Array
    EntradaClase subtipo; // Int

    public EntradaVariables(String nombre, int linea, int columna, EntradaClase tipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
    }

    public EntradaVariables(String nombre, EntradaClase tipo) {
        super(nombre);
        this.tipo = tipo;
    }

    public EntradaVariables(String nombre, EntradaClase tipo, EntradaClase subtipo) {
        super(nombre);
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    public EntradaVariables(String nombre, int linea, int columna, EntradaClase tipo, EntradaClase subtipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    public EntradaClase getTipo() {
        return tipo;
    }

    public void setSubtipo(EntradaClase subtipo) {
        this.subtipo = subtipo;
    }
}
