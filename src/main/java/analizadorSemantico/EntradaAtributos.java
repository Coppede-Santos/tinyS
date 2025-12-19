package analizadorSemantico;

public class EntradaAtributos extends EntradaVariables{
    boolean esPrivado = true;

    public EntradaAtributos(String nombre, int linea, int columna, EntradaClase tipo) {
        super(nombre, linea, columna, tipo);
    }

    public EntradaAtributos(String nombre, int linea, int columna, EntradaClase tipo, boolean esPrivado) {
        super(nombre, linea, columna, tipo);
        this.esPrivado = esPrivado;
    }

    public boolean esPrivado() {
        return esPrivado;
    }
}
