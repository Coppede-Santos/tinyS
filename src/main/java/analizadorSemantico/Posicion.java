package analizadorSemantico;

public class Posicion {
    int linea;
    int columna;

    public Posicion(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
}
