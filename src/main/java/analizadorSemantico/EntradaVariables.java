package analizadorSemantico;

public class EntradaVariables extends Entrada {
    EntradaClase tipo;

    public EntradaVariables(String nombre, int linea, int columna, EntradaClase tipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
    }

    public EntradaClase getTipo() {
        return tipo;
    }
}
