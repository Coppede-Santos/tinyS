package analizadorSemantico;

/** Clase que representa una entrada de variable en la tabla de símbolos */
public class EntradaVariables extends Entrada {
    String tipo; // Array
    String subtipo; // Int

    /** Constructor de la clase EntradaVariables
     *
     * @param nombre Nombre de la variable
     * @param linea Linea donde se encuentra la variable
     * @param columna Columna donde se encuentra la variable
     * @param tipo Tipo de la variable
     */
    public EntradaVariables(String nombre, int linea, int columna, String tipo) {
        super(nombre, linea, columna);
        this.tipo = tipo;
    }

    /** Constructor de la clase EntradaVariables
     *
     * @param nombre Nombre de la variable
     * @param tipo Tipo de la variable
     */
    public EntradaVariables(String nombre, String tipo) {
        super(nombre);
        this.tipo = tipo;
    }

    /** Constructor de la clase EntradaVariables
     *
     * @param nombre Nombre de la variable
     * @param tipo Tipo de la variable
     * @param subtipo Subtipo de la variable, si es Array
     */
    public EntradaVariables(String nombre, String tipo, String subtipo) {
        super(nombre);
        this.tipo = tipo;
        this.subtipo = subtipo;
    }

    /** Getter del tipo de la variable
     *
     * @return Tipo de la variable
     */
    public String getTipo() {
        return tipo;
    }

    /** Setter del tipo de la variable
     *
     * @param subtipo Tipo de la variable
     */
    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }

    /** Getter del subtipo de la variable
     *
     * @return Subtipo de la variable
     */
    public String getSubtipo() {
        return subtipo;
    }

    /** Método para consolidar la información de la variable en formato JSON
     *
     * @param profundidad Profundidad de la variable en la tabla de símbolos
     * @return String con la información de la variable en formato JSON
     */
    public String consolidarVariable(int profundidad) {
        String tabs = "";
        String salida;

        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        salida = consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo +
                "\",\n" + tabs + "\"subtipo\": " +
                ((subtipo != null) ? ("\"" + subtipo + "\"") : "null");
        return salida;
    }
}
