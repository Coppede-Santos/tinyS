package analizadorSemantico;

import java.util.HashMap;

public class SymbolTable
{
    HashMap<String, EntradaClase> clases = new HashMap<>();
    EntradaClase classActual = null;
    EntradaMetodo metodoActual = null;
    EntradaMetodo startMethod = null;

    public SymbolTable()
    {
        inicializarTiposPrimitivos();
    }

    public EntradaClase buscarClase(String nombreClase)
    {
        return clases.get(nombreClase);
    }

    public boolean insertarClase(String nombreClase, EntradaClase entradaClase)
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

    public boolean isStartMethod() {
        return startMethod != null;
    }

    public void inicializarTiposPrimitivos() {
        EntradaClase obj = new EntradaClase("Object");
        insertarClase("Object", obj);

        EntradaClase intClase = new EntradaClase("Int", obj);
        insertarClase("Int", intClase);

        EntradaClase booleanClase = new EntradaClase("Boolean", obj);
        insertarClase("Boolean", booleanClase);

        EntradaClase stringClase = new EntradaClase("String", obj);
        insertarClase("String", stringClase);

        EntradaClase doubleClase = new EntradaClase("Double", obj);
        insertarClase("Double", doubleClase);

        EntradaClase arrayClase = new EntradaClase("Array", obj);
        insertarClase("Array", arrayClase);

    }
}
