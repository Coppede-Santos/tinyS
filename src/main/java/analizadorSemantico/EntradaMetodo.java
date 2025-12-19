package analizadorSemantico;

import java.util.HashMap;

public class EntradaMetodo extends Entrada{
    EntradaClase tipoRetorno = null;
    boolean esEstatico = false;
    HashMap <String, EntradaVariables> parametros = new HashMap<>();
    HashMap <String, EntradaVariables> variablesLocales = new HashMap<>();

    public EntradaMetodo(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    public EntradaMetodo(String nombre, int linea, int columna, boolean esEstatico, EntradaClase tipoRetorno) {
        super(nombre, linea, columna);
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    EntradaVariables buscarParametro(String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    boolean insertarParametro(String nombreParametro, EntradaVariables entradaParametro) {
        if (parametros.containsKey(nombreParametro))
            return false;
        parametros.put(nombreParametro, entradaParametro);
        return true;
    }

    EntradaVariables buscarVariableLocal(String nombreVariableLocal) {
        return variablesLocales.get(nombreVariableLocal);
    }

    boolean insertarVariableLocal(String nombreVariableLocal, EntradaVariables entradaVariableLocal) {
        if (variablesLocales.containsKey(nombreVariableLocal))
            return false;
        variablesLocales.put(nombreVariableLocal, entradaVariableLocal);
        return true;
    }

    public EntradaClase getTipoRetorno() {
        return tipoRetorno;
    }

    public boolean isEsEstatico() {
        return esEstatico;
    }
}
