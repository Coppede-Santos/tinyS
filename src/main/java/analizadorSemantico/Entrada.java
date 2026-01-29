package analizadorSemantico;

/** Clase abstracta que representa una entrada en la tabla de símbolos */
public abstract class Entrada {

    String lexema;
    Posicion posicion;

    /** Constructor por defecto */
    public Entrada () {}

    /** Constructor de la clase Entrada
     *
     * @param lexema Lexema de la entrada
     * @param linea Linea donde se encuentra la entrada
     * @param columna Columna donde se encuentra la entrada
     */
    public Entrada(String lexema, int linea, int columna) {
        this.lexema = lexema;
        this.posicion = new Posicion(linea, columna);
    }

    /** Constructor de la clase Entrada
     *
     * @param lexema Lexema de la entrada
     */
    public Entrada (String lexema){
        this.lexema = lexema;
    }

    /** Getter del lexema de la entrada
     *
     * @return Lexema de la entrada
     */
    public String getLexema() {
        return lexema;
    }

    /** Getter de la línea y columna de la entrada
     *
     * @return Línea y columna de la entrada
     */
    public int getLinea() {
        return posicion.linea;
    }

    public int getColumna() {
        return posicion.columna;
    }

    /** Setter de la posición de la entrada
     *
     * @param linea Línea donde se encuentra la entrada
     * @param columna Columna donde se encuentra la entrada
     */
    public void setPosicion(int linea, int columna) {
        this.posicion = new Posicion(linea, columna);
    }

    /** Setter del lexema de la entrada
     *
     * @param lexema Lexema de la entrada
     */
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    /** Método para consolidar la información de la entrada en formato JSON
     *
     * @param profundidad Profundidad de la entrada en la tabla de símbolos
     * @return String con la información de la entrada en formato JSON
     */
    public String consolidar(int profundidad) {
        String tabs = "";
        String salida = "";
        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }
        salida +=
                tabs + "\"lexema\": \"" + lexema + "\",\n";

        if (posicion != null) {
            salida +=
                    tabs + "\"posicion\": {\n" +
                    tabs + "\t\"linea\": " + posicion.linea + ",\n" +
                    tabs + "\t\"columna\": " + posicion.columna + "\n" +
                    tabs + "},\n";
        }
        return salida;
    }
}
