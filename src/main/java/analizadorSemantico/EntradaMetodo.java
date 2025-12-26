package analizadorSemantico;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;

public class EntradaMetodo extends Entrada{
    EntradaClase tipoRetorno = null;
    EntradaClase subtipoRetorno = null;
    boolean esEstatico = false;
    HashMap <String, EntradaParametro> parametros = new HashMap<>();
    HashMap <String, EntradaVariables> variablesLocales = new HashMap<>();

    public EntradaMetodo(){

    }

    public EntradaMetodo(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    public EntradaMetodo(String nombre, int linea, int columna, boolean esEstatico, EntradaClase tipoRetorno) {
        super(nombre, linea, columna);
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    public EntradaMetodo(String nombre, boolean esEstatico, EntradaClase tipoRetorno) {
        super(nombre);
        this.esEstatico = esEstatico;
        this.tipoRetorno = tipoRetorno;
    }

    public EntradaVariables buscarParametro(String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    public boolean insertarParametro(String nombreParametro, EntradaParametro entradaParametro) {
        if (parametros.containsKey(nombreParametro))
            return false;
        parametros.put(nombreParametro, entradaParametro);
        return true;
    }

    public EntradaVariables buscarVariableLocal(String nombreVariableLocal) {
        return variablesLocales.get(nombreVariableLocal);
    }

    public boolean insertarVariableLocal(String nombreVariableLocal, EntradaVariables entradaVariableLocal) {
        if (variablesLocales.containsKey(nombreVariableLocal))
            return false;
        variablesLocales.put(nombreVariableLocal, entradaVariableLocal);
        return true;
    }

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

    public boolean isEsEstatico() {
        return esEstatico;
    }

    public int getCantidadParametros() {
        return parametros.size();
    }
}
