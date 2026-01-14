package analizadorSemantico;

public class EntradaAtributos extends EntradaVariables{
    boolean esPrivado = true;

    public EntradaAtributos(String nombre, int linea, int columna, String tipo, boolean esPrivado) {
        super(nombre, linea, columna, tipo);
        this.esPrivado = esPrivado;
    }

    public boolean esPrivado() {
        return esPrivado;
    }

    public String consolidarAtributo(int profundidad) {
        String salida = consolidar(profundidad) + "\t\t\t\t\t\"tipo\": \"" + tipo +
                "\",\n\t\t\t\t\t\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo + "\"") : "null")+
                ",\n\t\t\t\t\t\"esPrivado\": " + esPrivado;

        return salida;
    }
}
