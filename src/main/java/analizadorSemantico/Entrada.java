package analizadorSemantico;

public abstract class Entrada {

    String lexema;
    Posicion posicion;

    public Entrada(String lexema, int linea, int columna) {
        this.lexema = lexema;
        this.posicion = new Posicion(linea, columna);
    }
}

class Posicion {
    int linea;
    int columna;

    public Posicion(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }
}
