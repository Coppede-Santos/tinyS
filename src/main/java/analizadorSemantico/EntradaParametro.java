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

        String tabs = "";
        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        return consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo.getLexema() +
                "\",\n" + tabs + "\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo.getLexema() + "\"") : "null")+
                ",\n" + tabs + "\"posicionParametro\": " + posicionParametro;
    }
}
