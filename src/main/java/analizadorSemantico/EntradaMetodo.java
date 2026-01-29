package analizadorSemantico;

import java.util.HashMap;
import java.util.Objects;

/** Clase que representa una entrada de método en la tabla de símbolos
 */
public class EntradaMetodo extends Entrada{
    EntradaClase tipoRetorno = null;
    EntradaClase subtipoRetorno = null;
    boolean esEstatico = false;
    HashMap <String, EntradaParametro> parametros = new HashMap<>();
    HashMap <String, EntradaVariables> variablesLocales = new HashMap<>();

    /** Constructor por defecto
     *
     */
    public EntradaMetodo(){}

    /** Constructor de la clase EntradaMetodo
     *
     * @param nombre Nombre del método
     * @param linea Linea donde se encuentra el método
     * @param columna Columna donde se encuentra el método
     */
    public EntradaMetodo(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    /** Constructor de la clase EntradaMetodo
     *
     * @param nombre Nombre del método
     * @param esEstatico Indica si el método es estático
     * @param tipoRetorno Tipo de retorno del método
     */
    public EntradaMetodo(String nombre, boolean esEstatico,
                         EntradaClase tipoRetorno) {
        super(nombre);
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    /** Método para buscar un parámetro en el método
     *
     * @param nombreParametro Nombre del parámetro a buscar
     * @return EntradaParametro del parámetro buscado, o null si no existe
     */
    public EntradaParametro buscarParametro(String nombreParametro) {
        return parametros.get(nombreParametro);
    }


    /** Método para buscar un parámetro por su posición en el método
     *
     * @param posicionParametro Posición del parámetro a buscar
     * @return EntradaParametro del parámetro buscado, o null si no existe
     */
    public EntradaParametro buscarParametroPorPosicion(int posicionParametro) {
        for (EntradaParametro parametro : parametros.values()) {
            if (parametro.posicionParametro == posicionParametro) {
                return parametro;
            }
        }
        return null;
    }

    /** Método para insertar un parámetro en el método
     *
     * @param nombreParametro Nombre del parámetro a insertar
     * @param entradaParametro EntradaParametro del parámetro a insertar
     */
    public void insertarParametro(String nombreParametro, EntradaParametro entradaParametro) {
        if (!parametros.containsKey(nombreParametro)) {
            parametros.put(nombreParametro, entradaParametro);
        }
    }

    /** Método para buscar una variable local en el método
     *
     * @param nombreVariableLocal Nombre de la variable local a buscar
     * @return EntradaVariables de la variable local buscada, o null si no existe
     */
    public EntradaVariables buscarVariableLocal(String nombreVariableLocal) {
        return variablesLocales.get(nombreVariableLocal);
    }

    /** Método para insertar una variable local en el método
     *
     * @param nombreVariableLocal Nombre de la variable local a insertar
     * @param entradaVariableLocal EntradaVariables de la variable local a insertar
     */
    public void insertarVariableLocal(String nombreVariableLocal, EntradaVariables entradaVariableLocal) {
        if (!variablesLocales.containsKey(nombreVariableLocal)) {
            variablesLocales.put(nombreVariableLocal, entradaVariableLocal);
        }
    }

    /** Getters y Setters */
    public EntradaClase getTipoRetorno() {
        return tipoRetorno;
    }

    public EntradaClase getSubtipoRetorno() {
        return subtipoRetorno;
    }

    public void setTipoRetorno(EntradaClase tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }

    public void setSubtipoRetorno(EntradaClase subtipoRetorno) {
        this.subtipoRetorno = subtipoRetorno;
    }

    public boolean esEstatico() {
        return esEstatico;
    }

    public int getCantidadParametros() {
        return parametros.size();
    }

    public void setEsEstatico(boolean esEstatico) {
        this.esEstatico = esEstatico;
    }

    /** Método para comparar la firma de dos métodos
     *
     * @param metodo Método a comparar
     * @return true si las firmas son iguales, false en caso contrario
     */
    public Boolean compararFirma(EntradaMetodo metodo) {
        if (tipoRetorno != metodo.tipoRetorno) {
            return false;
        }
        if (subtipoRetorno != metodo.subtipoRetorno) {
            return false;
        }
        if (esEstatico != metodo.esEstatico) {
            return false;
        }

        for (EntradaParametro currentParametro : this.parametros.values()) {
            EntradaParametro parametro = metodo.buscarParametro(
                    currentParametro.lexema
            );
            if (parametro == null) {
                return false;
            }
            if (!Objects.equals(parametro.tipo, currentParametro.tipo)) {
                return false;
            }
            if (!Objects.equals(parametro.subtipo, currentParametro.subtipo)) {
                return false;
            }
            if (parametro.posicionParametro != currentParametro.posicionParametro) {
                return false;
            }
        }
        return true;
    }

    /** Método para consolidar la información del método en formato JSON
     *
     * @param profundidad Profundidad de la estructura JSON
     * @param metodoFinal Indica si es el último método en la lista
     * @return String con la información del método en formato JSON
     */
    public String consolidarMetodo(int profundidad, boolean metodoFinal) {
        String salida;
        String tabs = "";

        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        salida = tabs + "{\n";

        salida += consolidar(profundidad+1) +
                tabs + "\t\"tipoRetorno\": " +
                ((tipoRetorno != null) ?
                        ("\"" + tipoRetorno.getLexema() + "\"")
                        : "null") + ",\n"+
                tabs + "\t\"subtipoRetorno\": " +
                ((subtipoRetorno != null) ?
                        ("\"" + subtipoRetorno.getLexema() + "\"") :
                        "null") + ",\n"+
                tabs + "\t\"esEstatico\": " + esEstatico + ",\n" +
                tabs + "\t\"variablesLocales\": [\n";

        for (EntradaVariables variable : variablesLocales.values()) {
            salida += tabs + "\t\t{\n" +
                    variable.consolidarVariable(5) + "\n";

            // Check if it is the last variable
            if (variable != variablesLocales.values().toArray()[
                    variablesLocales.size() - 1
                    ]) {
                salida += tabs + "\t\t},\n";
            } else {
                salida += tabs + "\t\t}\n";
            }

        }
        salida += tabs + "\t],\n"+
        tabs + "\t\"parametros\": [\n";

        for (EntradaParametro parametro : parametros.values()) {
            salida += tabs + "\t\t{\n" + parametro.consolidarParametro(
                    profundidad+3) + "\n";

            if (parametro != parametros.values().toArray()[
                    parametros.size() - 1]) {
                salida += tabs + "\t\t},\n";
            } else {
                salida += tabs + "\t\t}\n";
            }
        }

        salida += tabs + "\t]\n";

        if (metodoFinal) {
            salida += tabs + "}\n";
        } else {
            salida += tabs + "},\n";
        }

        return salida;
    }
}
