package analizadorSemantico;

import java.util.HashMap;

public class SymbolTable
{
    HashMap<String, EntradaClase> clases = new HashMap<>();
    EntradaClase classActual = null;
    EntradaMetodo metodoActual = null;
    EntradaMetodo startMethod = null;

    public boolean SymbolTable()
    {
        return true;
    }

    EntradaClase buscarClase(String nombreClase)
    {
        return clases.get(nombreClase);
    }

    boolean insertarClase(String nombreClase, EntradaClase entradaClase)
    {
        if (clases.containsKey(nombreClase))
            return false;
        clases.put(nombreClase, entradaClase);
        return true;
    }

    public EntradaClase getClassActual() {
        return classActual;
    }

    public void setClassActual(EntradaClase classActual) {
        this.classActual = classActual;
    }

    public EntradaMetodo getMetodoActual() {
        return metodoActual;
    }

    public void setMetodoActual(EntradaMetodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    public EntradaMetodo getStartMethod() {
        return startMethod;
    }

    public void setStartMethod(EntradaMetodo startMethod) {
        this.startMethod = startMethod;
    }
}
