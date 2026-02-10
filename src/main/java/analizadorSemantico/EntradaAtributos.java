package analizadorSemantico;

/** Clase que representa una entrada de atributo en la tabla de símbolos */
public class EntradaAtributos extends EntradaVariables{
    boolean esPrivado;
    String clasePropietaria;

    /** Constructor de la clase EntradaAtributos
     *
     * @param nombre Nombre del atributo
     * @param linea Linea donde se encuentra el atributo
     * @param columna Columna donde se encuentra el atributo
     * @param tipo Tipo del atributo
     * @param esPrivado Indica si el atributo es privado
     */    

    public EntradaAtributos(String nombre, int linea, int columna, String tipo, boolean esPrivado, String clasePropietaria) {
        super(nombre, linea, columna, tipo);
        this.esPrivado = esPrivado;
        this.clasePropietaria = clasePropietaria;
    }

    /** Método que indica si el atributo es privado
     *
     * @return true si el atributo es privado, false en caso contrario
     */
    public boolean esPrivado() {
        return esPrivado;
    }

    /** Método que obtiene la clase propietaria del atributo
     *
     * @return String con el nombre de la clase propietaria
     */
    public String getClasePropietaria() {
        return clasePropietaria;
    }

    /** Método para consolidar la información del atributo en formato JSON
     *
     * @param profundidad Profundidad de la entrada en la tabla de símbolos
     * @return String con la información del atributo en formato JSON
     */
    public String consolidarAtributo(int profundidad) {
        String salida;
        salida = consolidar(profundidad) + "\t\t\t\t\t\"tipo\": \"" + tipo +
                "\",\n\t\t\t\t\t\"subtipo\": "
                + ((subtipo != null) ? ("\"" + subtipo + "\"") : "null") +
                ",\n\t\t\t\t\t\"esPrivado\": " + esPrivado;

        return salida;
    }
}
