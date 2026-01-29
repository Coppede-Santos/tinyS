package analizadorSemantico;

import generacionDeCodigo.TopVisitor;

public class EntradaAtributo extends EntradaVariable {
    boolean esPrivado = true;
    String clasePropietaria;
    int posicionAtributo;

    public EntradaAtributo(String nombre, int linea, int columna, String tipo, boolean esPrivado, String clasePropietaria) {
        super(nombre, linea, columna, tipo);
        this.esPrivado = esPrivado;
        this.clasePropietaria = clasePropietaria;
    }

    public boolean esPrivado() {
        return esPrivado;
    }

    public int getPosicionAtributo() {
        return posicionAtributo;
    }

    public void setPosicionAtributo(int posicionAtributo) {
        this.posicionAtributo = posicionAtributo;
    }

    public String getClasePropietaria() {
        return clasePropietaria;
    }

    public String consolidarAtributo(int profundidad) {
        String salida = consolidar(profundidad) + "\t\t\t\t\t\"tipo\": \"" + tipo +
                "\",\n\t\t\t\t\t\"subtipo\": " + ((subtipo != null) ? ("\"" + subtipo + "\"") : "null")+
                ",\n\t\t\t\t\t\"esPrivado\": " + esPrivado;

        return salida;
    }

}