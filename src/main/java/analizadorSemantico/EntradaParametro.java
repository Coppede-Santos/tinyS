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

    public String consolidarParametro(int profundidad) {

        String salida = consolidar(profundidad) + "\t\t\t\t\t\t\"tipo\": \"" + tipo.getLexema() +
                "\",\n\t\t\t\t\t\t\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo.getLexema() + "\"") : "null")+
                ",\n\t\t\t\t\t\t\"posicionParametro\": " + posicionParametro;
        return salida;
    }
}
