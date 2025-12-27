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

    public String consolidarAtributo() {
        String salida = consolidar() + "\n tipo:" + tipo.getLexema() +
                "\n subtipo:" + ((subtipo != null) ? subtipo.getLexema() : "null")+
                "\n esPrivado:" + esPrivado;

        return salida;
    }
}
