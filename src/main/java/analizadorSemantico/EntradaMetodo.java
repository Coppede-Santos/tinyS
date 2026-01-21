package analizadorSemantico;

import java.util.*;

import generacionDeCodigo.TopVisitor;

import java.util.HashMap;

public class EntradaMetodo extends Entrada{
    EntradaClase tipoRetorno = null;
    EntradaClase subtipoRetorno = null;
    boolean esEstatico = false;
    HashMap <String, EntradaParametro> parametros = new HashMap<>();
    HashMap <String, EntradaVariables> variablesLocales = new HashMap<>();
    int posicionMetodo;

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

    public int getCantidadVariablesLocales() {
        return variablesLocales.size();
    }

    public EntradaParametro buscarParametro(String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    public EntradaParametro buscarParametroPorPosicion(int posicionParametro) {
        for (EntradaParametro parametro : parametros.values()) {
            if (parametro.posicionParametro == posicionParametro) {
                return parametro;
            }
        }
        return null;
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

    public boolean esEstatico() {
        return esEstatico;
    }

    public int getCantidadParametros() {
        return parametros.size();
    }

    public void setEsEstatico(boolean esEstatico) {
        this.esEstatico = esEstatico;
    }

    public int getPosicionMetodo() {
        return posicionMetodo;
    }

    public void setPosicionMetodo(int posicionMetodo) {
        this.posicionMetodo = posicionMetodo;
    }

    public Boolean compararFirma(EntradaMetodo metodo) {
        if (tipoRetorno != metodo.tipoRetorno) return false;
        if (subtipoRetorno != metodo.subtipoRetorno) return false;
        if (esEstatico != metodo.esEstatico) return false;


        for (EntradaParametro currentParametro : this.parametros.values()) {
            EntradaParametro parametro = metodo.buscarParametro(currentParametro.lexema);
            if (parametro == null) return false;
            if (!Objects.equals(parametro.tipo, currentParametro.tipo)) return false;
            if (!Objects.equals(parametro.subtipo, currentParametro.subtipo)) return false;
            if (parametro.posicionParametro != currentParametro.posicionParametro) return false;
        }
        return true;
    }

    public String consolidarMetodo(int profundidad, boolean metodoFinal) {
        String salida;

        String tabs = "";

        for (int i = 0; i < profundidad; i++) {
            tabs += "\t";
        }

        salida = tabs + "{\n";

        int posicionActualVariable = 1;

        salida += consolidar(profundidad+1) +
                tabs + "\t\"tipoRetorno\": " + ((tipoRetorno != null) ? ("\"" + tipoRetorno.getLexema() + "\"") : "null") + ",\n"+
                tabs + "\t\"subtipoRetorno\": " + ((subtipoRetorno != null) ? ("\"" + subtipoRetorno.getLexema() + "\"") : "null") + ",\n"+
                tabs + "\t\"esEstatico\": " + esEstatico + ",\n" +
                tabs + "\t\"variablesLocales\": [\n";
        for (EntradaVariables variable : variablesLocales.values()) {
            salida += tabs + "\t\t{\n" +  variable.consolidarVariable(5) + "\n";

            // Check if it is the last variable
            if (variable != variablesLocales.values().toArray()[variablesLocales.size() - 1]) {
                salida += tabs + "\t\t},\n";
            } else {
                salida += tabs + "\t\t}\n";
            }

            variable.setPosicionVariable(posicionActualVariable);
            posicionActualVariable++;

        }
        salida += tabs + "\t],\n"+
        tabs + "\t\"parametros\": [\n";

        for (EntradaParametro parametro : parametros.values()) {
            salida += tabs + "\t\t{\n" + parametro.consolidarParametro(profundidad+3) + "\n";

            if (parametro != parametros.values().toArray()[parametros.size() - 1]) {
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

    public void accept(TopVisitor topVisitor) {
        topVisitor.generarCodigo();
    }

    public HashMap<String, EntradaVariables> getVariablesLocales() {
        return variablesLocales;
    }
}
