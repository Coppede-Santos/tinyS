package analizadorSemantico;

import java.util.*;
import analizadorSemantico.Errores.ErrorSemantico;

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
        inicializarMetodosDeClaseIO();
        inicializarMetodosDeClaseArray();
        inicializarMetodosDeClaseString();


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
        EntradaClase obj = new EntradaClase("Object",0,0);
        insertarClase("Object", obj);

        EntradaClase intClase = new EntradaClase("Int",0,0, obj);
        insertarClase("Int", intClase);

        EntradaClase booleanClase = new EntradaClase("Bool",0,0, obj);
        insertarClase("Bool", booleanClase);

        EntradaClase stringClase = new EntradaClase("Str",0,0, obj);
        insertarClase("Str", stringClase);

        EntradaClase doubleClase = new EntradaClase("Double",0,0, obj);
        insertarClase("Double", doubleClase);

        EntradaClase arrayClase = new EntradaClase("Array",0,0, obj);
        insertarClase("Array", arrayClase);

        EntradaClase ioClase = new EntradaClase("IO",0,0, obj);
        insertarClase("IO", ioClase);

    }

    public void inicializarMetodosDeClaseIO() {
        // IO methods
        EntradaClase ioClase = buscarClase("IO");
        if (ioClase != null) {
            EntradaMetodo out_str = new EntradaMetodo("out_str", true, null);
            EntradaParametro parametroOutStr = new EntradaParametro("s", buscarClase("Str"), 0);
            out_str.insertarParametro("s", parametroOutStr);
            ioClase.insertarMetodo("out_str", out_str);

            EntradaMetodo out_int = new EntradaMetodo("out_int", true, null);
            EntradaParametro parametroOutInt = new EntradaParametro("i", buscarClase("Int"), 0);
            out_int.insertarParametro("i", parametroOutInt);
            ioClase.insertarMetodo("out_int", out_int);

            EntradaMetodo out_bool = new EntradaMetodo("out_bool", true, null);
            EntradaParametro parametroOutBool = new EntradaParametro("b", buscarClase("Bool"), 0);
            out_bool.insertarParametro("b", parametroOutBool);
            ioClase.insertarMetodo("out_bool", out_bool);

            // out_double
            EntradaMetodo out_double = new EntradaMetodo("out_double", true, null);
            EntradaParametro parametroOutDouble = new EntradaParametro("d", buscarClase("Double"), 0);
            out_double.insertarParametro("d", parametroOutDouble);
            ioClase.insertarMetodo("out_double", out_double);

            // out_array_int
            EntradaMetodo out_array_int = new EntradaMetodo("out_array_int", true, null);
            EntradaParametro parametroOutArrayInt = new EntradaParametro(
                    "a",
                    buscarClase("Array"),
                    buscarClase("Int"),
                    0);
            out_array_int.insertarParametro("a", parametroOutArrayInt);
            ioClase.insertarMetodo("out_array_int", out_array_int);

            // out_array_str
            EntradaMetodo out_array_str = new EntradaMetodo("out_array_str", true, null);
            EntradaParametro parametroOutArrayStr = new EntradaParametro(
                    "a",
                    buscarClase("Array"),
                    buscarClase("Str"),
                    0);
            out_array_str.insertarParametro("a", parametroOutArrayStr);
            ioClase.insertarMetodo("out_array_str", out_array_str);

            // out_array_bool
            EntradaMetodo out_array_bool = new EntradaMetodo("out_array_bool", true, null);
            EntradaParametro parametroOutArrayBool = new EntradaParametro(
                    "a",
                    buscarClase("Array"),
                    buscarClase("Bool"),
                    0);
            out_array_bool.insertarParametro("a", parametroOutArrayBool);
            ioClase.insertarMetodo("out_array_bool", out_array_bool);

            // out_array_double
            EntradaMetodo out_array_double = new EntradaMetodo("out_array_double", true, null);
            EntradaParametro parametroOutArrayDouble = new EntradaParametro(
                    "a",
                    buscarClase("Array"),
                    buscarClase("Double"),
                    0);
            out_array_double.insertarParametro("a", parametroOutArrayDouble);
            ioClase.insertarMetodo("out_array_double", out_array_double);

            // in_str
            EntradaMetodo in_str = new EntradaMetodo("in_str", true, buscarClase("Str"));
            ioClase.insertarMetodo("in_str", in_str);

            // in_int
            EntradaMetodo in_int = new EntradaMetodo("in_int", true, buscarClase("Int"));
            ioClase.insertarMetodo("in_int", in_int);

            // in_bool
            EntradaMetodo in_bool = new EntradaMetodo("in_bool", true, buscarClase("Bool"));
            ioClase.insertarMetodo("in_bool", in_bool);

            // in_double
            EntradaMetodo in_double = new EntradaMetodo("in_double", true, buscarClase("Double"));
            ioClase.insertarMetodo("in_double", in_double);
        }
    }

    public void inicializarMetodosDeClaseArray() {
        // Array methods
        EntradaClase arrayClase = buscarClase("Array");
        if (arrayClase != null) {
            EntradaMetodo length = new EntradaMetodo("length", true, buscarClase("Int"));
            arrayClase.insertarMetodo("length", length);
            EntradaMetodo constructor = new EntradaMetodo("Array", true, null);
            EntradaParametro parametroConstructor = new EntradaParametro("n", buscarClase("Int"), 0);
            constructor.insertarParametro("n", parametroConstructor);
            arrayClase.setConstructor(constructor);
        }

    }

    public void inicializarMetodosDeClaseString() {
        // String methods
        EntradaClase stringClase = buscarClase("Str");
        if (stringClase != null) {
            EntradaMetodo length = new EntradaMetodo("length", true, buscarClase("Int"));
            stringClase.insertarMetodo("length", length);

            EntradaMetodo concat = new EntradaMetodo("concat", true, buscarClase("Str"));
            EntradaParametro parametroConcat = new EntradaParametro("s", buscarClase("Str"), 0);
            concat.insertarParametro("s", parametroConcat);
            stringClase.insertarMetodo("concat", concat);
        }
    }


    public void ordenarClasesPorPosicion() {
        List<Map.Entry<String, EntradaClase>> listaEntradas = new ArrayList<>(clases.entrySet());

        // Ordenar por línea y columna de la posición de EntradaClase
        listaEntradas.sort((e1, e2) -> {
            int cmp = Integer.compare(e1.getValue().getLinea(), e2.getValue().getLinea());
            if (cmp == 0) {
                cmp = Integer.compare(e1.getValue().getColumna(), e2.getValue().getColumna());
            }
            return cmp;
        });

        LinkedHashMap<String, EntradaClase> clasesOrdenadas = new LinkedHashMap<>();
        for (Map.Entry<String, EntradaClase> entry : listaEntradas) {
            clasesOrdenadas.put(entry.getKey(), entry.getValue());
        }
        this.clases = clasesOrdenadas;
    }

    public String consolidarTS() throws ErrorSemantico {
        ordenarClasesPorPosicion();
        String salida = "{\n" +
                "\t\"clases\": [\n";
        for (EntradaClase clase : clases.values()) {
            // Check if it is the last class
            if (clase != clases.values().toArray()[clases.size() - 1]) {
                salida += clase.consolidarClase(false);
            } else {
                salida += clase.consolidarClase(true);
            }
        }
        salida += "\n \t ],\n" +
                "\t\"start\":\n";

        salida += startMethod.consolidarMetodo(2, true);
        salida += "\n}";
        return salida;
    }
}
