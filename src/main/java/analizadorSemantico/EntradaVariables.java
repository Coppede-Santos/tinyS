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

    public String consolidarVariable(int profundidad) {

        String tabs = "";

        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        String salida = consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo.getLexema() +
                "\",\n" + tabs + "\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo.getLexema() + "\"") : "null");
        return salida;
    }
}
