package analizadorSemantico;

public abstract class Entrada {

    String lexema;
    Posicion posicion;

    public Entrada () {
    }

    public Entrada(String lexema, int linea, int columna) {
        this.lexema = lexema;
        this.posicion = new Posicion(linea, columna);
    }
    public Entrada (String lexema){
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return posicion.linea;
    }
    public int getColumna() {
        return posicion.columna;
    }

    public void setPosicion(int linea, int columna) {
        this.posicion = new Posicion(linea, columna);
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String consolidar() {
        String salida =
                "lexema='" + lexema + '\'';
               if (posicion != null){
                   salida += ", posicion=(" + posicion.linea + ", " + posicion.columna + ")";
               }
                return salida;

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
