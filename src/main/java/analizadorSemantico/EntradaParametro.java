package analizadorSemantico;

public class EntradaParametro extends EntradaVariables{

    int posicionParametro;

    public EntradaParametro(String nombre, int linea, int columna, EntradaClase tipo, int posicionParametro) {
        super(nombre, linea, columna, tipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, EntradaClase tipo, int posicionParametro) {
        super(nombre, tipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, EntradaClase tipo, EntradaClase subtipo, int posicionParametro) {
        super(nombre, tipo, subtipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, int linea, int columna, EntradaClase tipo, EntradaClase subtipo, int posicionParametro) {
        super(nombre, linea, columna, tipo, subtipo);
        this.posicionParametro = posicionParametro;
    }
}
