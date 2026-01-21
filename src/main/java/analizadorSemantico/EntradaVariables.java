package analizadorSemantico;

public class EntradaVariables extends Entrada {
    String tipo; // Array
    String subtipo; // Int
    int posicionVariable;

    public EntradaVariables(String nombre, int linea, int columna, String tipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
    }

    public EntradaVariables(String nombre, String tipo) {
        super(nombre);
        this.tipo = tipo;
    }

    public EntradaVariables(String nombre, String tipo, String subtipo) {
        super(nombre);
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    public EntradaVariables(String nombre, int linea, int columna, String tipo, String subtipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public int getPosicionVariable() {
        return posicionVariable;
    }

    public void setPosicionVariable(int posicionVariable) {
        this.posicionVariable = posicionVariable;
    }

    public String consolidarVariable(int profundidad) {

        String tabs = "";

        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        String salida = consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo +
                "\",\n" + tabs + "\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo + "\"") : "null");
        return salida;
    }
}
