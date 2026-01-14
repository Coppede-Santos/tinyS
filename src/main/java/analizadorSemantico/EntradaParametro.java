package analizadorSemantico;

public class EntradaParametro extends EntradaVariables{

    int posicionParametro;

    public EntradaParametro(String nombre, int linea, int columna, String tipo, int posicionParametro) {
        super(nombre, linea, columna, tipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, String tipo, int posicionParametro) {
        super(nombre, tipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, String tipo, String subtipo, int posicionParametro) {
        super(nombre, tipo, subtipo);
        this.posicionParametro = posicionParametro;
    }

    public EntradaParametro(String nombre, int linea, int columna, String tipo, String subtipo, int posicionParametro) {
        super(nombre, linea, columna, tipo, subtipo);
        this.posicionParametro = posicionParametro;
    }

    public String consolidarParametro(int profundidad) {

        String tabs = "";
        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        return consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo +
                "\",\n" + tabs + "\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo + "\"") : "null")+
                ",\n" + tabs + "\"posicionParametro\": " + posicionParametro;
    }
}
