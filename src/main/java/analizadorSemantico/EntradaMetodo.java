package analizadorSemantico;

import analizadorSemantico.Errores.ErrorSemantico;

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

    public EntradaParametro buscarParametro(String nombreParametro) {
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

    public Boolean compararFirma(EntradaMetodo metodo) {
        if (tipoRetorno != metodo.tipoRetorno) return false;
        if (subtipoRetorno != metodo.subtipoRetorno) return false;
        if (esEstatico != metodo.esEstatico) return false;


        for (EntradaParametro currentParametro : this.parametros.values()) {
            EntradaParametro parametro = metodo.buscarParametro(currentParametro.lexema);
            if (parametro == null) return false;
            if (parametro.tipo != currentParametro.tipo) return false;
            if (parametro.subtipo != currentParametro.subtipo) return false;
            if (parametro.posicionParametro != currentParametro.posicionParametro) return false;
        }
        return true;
    }

    public String consolidarMetodo(){

        String salida = consolidar() + "\n" +
                "Tipo de retorno: " + ((subtipoRetorno != null) ? subtipoRetorno.getLexema() : "void") + "\n"+
                "Subtipo de retorno: " + ((subtipoRetorno != null) ? subtipoRetorno.getLexema() : "null") + "\n"+
                "Es Estatico: " + esEstatico + "\n"
                + "Variables Locales: {";
        for (EntradaVariables variable : variablesLocales.values()) {
            salida += variable.consolidarVariable() + "\n";
        }
        salida += "}\n"+
        "Parametros: {";

        for (EntradaParametro parametro : parametros.values()) {
            salida += "\tParametro: " + parametro.consolidarParametro() + "\n";
        }
        salida += "}";

        return salida;
    }
}
