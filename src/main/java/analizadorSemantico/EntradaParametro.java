package analizadorSemantico;

/** Clase que representa una entrada de parámetro en la tabla de símbolos */
public class EntradaParametro extends EntradaVariables{

    int posicionParametro;

    /** Constructor de la clase EntradaParametro
     *
     * @param nombre Nombre del parámetro
     * @param linea Linea donde se encuentra el parámetro
     * @param columna Columna donde se encuentra el parámetro
     * @param tipo Tipo del parámetro
     * @param posicionParametro Posición del parámetro en la lista de parámetros
     */
    public EntradaParametro(String nombre,
                            int linea,
                            int columna,
                            String tipo,
                            int posicionParametro) {
        super(nombre, linea, columna, tipo);
        this.posicionParametro = posicionParametro;
    }

    /** Constructor de la clase EntradaParametro
     *
     * @param nombre Nombre del parámetro
     * @param tipo Tipo del parámetro
     * @param posicionParametro Posición del parámetro en la lista de parámetros
     */
    public EntradaParametro(String nombre,
                            String tipo,
                            int posicionParametro) {
        super(nombre, tipo);
        this.posicionParametro = posicionParametro;
    }

    /** Constructor de la clase EntradaParametro
     *
     * @param nombre Nombre del parámetro
     * @param tipo Tipo del parámetro
     * @param subtipo Subtipo del parámetro
     * @param posicionParametro Posición del parámetro en la lista de parámetros
     */
    public EntradaParametro(String nombre, String tipo, String subtipo,
                            int posicionParametro) {
        super(nombre, tipo, subtipo);
        this.posicionParametro = posicionParametro;
    }

    /** Método para consolidar la información del parámetro en formato JSON
     *
     * @param profundidad Profundidad de la entrada en la tabla de símbolos
     * @return String con la información del parámetro en formato JSON
     */
    public String consolidarParametro(int profundidad) {
        String tabs = "";
        String salida = "";
        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        salida += consolidar(profundidad) + tabs + "\"tipo\": \"" + tipo +
                "\",\n" + tabs + "\"subtipo\": " +
                ((subtipo != null) ? ("\"" + subtipo + "\"") : "null")+
                ",\n" + tabs + "\"posicionParametro\": " + posicionParametro;
        return salida;
    }

    public int getPosicionParametro() {
        return posicionParametro;
    }
}
