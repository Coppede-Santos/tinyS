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

    public String consolidar(int profundidad) {
        String tabs = "";
        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }
        String salida =
                tabs + "\"lexema\": \"" + lexema + "\",\n";
        if (posicion != null) {
            salida += tabs + "\"posicion\": {\n" +
                    tabs + "\t\"linea\": " + posicion.linea + ",\n" +
                    tabs + "\t\"columna\": " + posicion.columna + "\n" +
                    tabs + "},\n";
        }
        return salida;
    }
}

