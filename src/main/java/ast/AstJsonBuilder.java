package ast;

/** Clase auxiliar para construir representaciones JSON del AST */
public class AstJsonBuilder {

    /** Método para generar una cadena de tabulaciones
     *
     * @param cantidad Cantidad de tabulaciones
     * @return String con la cantidad especificada de tabulaciones
     */
    public static String tabs(int cantidad){
        return "\t".repeat(Math.max(0, cantidad));
    }

    /** Método para generar una clave JSON
     *
     * @param key Clave a formatear
     * @return String con la clave en formato JSON
     */
    public static String claveJson(String key){
        return "\"" + key + "\": ";
    }

    /** Método para generar un valor JSON
     *
     * @param value Valor a formatear
     * @return String con el valor en formato JSON
     */
    public static String valorJson(String value){
        return "\"" + value + "\"";
    }

    /** Método para verificar si un nodo de sentencia es un bloque
     *
     * @param nodoSentencia Nodo de sentencia a verificar
     * @return true si el nodo es un bloque, false en caso contrario
     */
    public static boolean esBloque(NodoSentencia nodoSentencia) {
        return nodoSentencia.getClass().getSimpleName().equals("NodoBloque");
    }

    /** Método para eliminar comas finales en estructuras JSON
     *
     * @param json Cadena JSON a procesar
     * @return Cadena JSON sin comas finales
     */
    public static String borrarTrailingCommas(String json) {
        String[] lines = json.split("\n");
        for (int i = 0; i < lines.length - 1; i++) {
            String trimmedNext = lines[i + 1].trim();
            if (trimmedNext.startsWith("]") || trimmedNext.startsWith("}")) {
                lines[i] = lines[i].replaceAll(",\\s*$", "");
            }
        }
        return String.join("\n", lines);
    }
}
