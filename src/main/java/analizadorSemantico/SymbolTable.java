package analizadorSemantico;

import analizadorSemantico.Errores.ErrorSemantico;
import java.util.HashMap;


/**
 * Tabla de símbolos que almacena las clases, el contexto actual de clase y método,
 * y proporciona métodos para inicializar tipos primitivos y métodos estándar.
 */
public class SymbolTable
{
    HashMap<String, EntradaClase> clases = new HashMap<>();
    EntradaClase classActual = null;
    EntradaMetodo metodoActual = null;
    EntradaMetodo startMethod = null;

    /** Constructor de la tabla de símbolos que inicializa los tipos primitivos
     * y los métodos estándar de las clases IO, Array y String.
     */
    public SymbolTable()
    {
        inicializarTiposPrimitivos();
        inicializarMetodosDeClaseIO();
        inicializarMetodosDeClaseArray();
        inicializarMetodosDeClaseString();
    }

    /** Buscar una clase en la tabla de símbolos por su nombre.
     *
     * @param nombreClase El nombre de la clase a buscar.
     * @return La entrada de la clase si se encuentra, o null si no existe.
     */
    public EntradaClase buscarClase(String nombreClase) {
        return clases.get(nombreClase);
    }

    /** Insertar una nueva clase en la tabla de símbolos.
     *
     * @param nombreClase El nombre de la clase a insertar.
     * @param entradaClase La entrada de la clase a insertar.
     */
    public void insertarClase(String nombreClase, EntradaClase entradaClase) {
        if (!clases.containsKey(nombreClase)) {
            clases.put(nombreClase, entradaClase);
        }
    }

    /** Obtener la clase actual en el contexto.
     *
     * @return La entrada de la clase actual.
     */
    public EntradaClase getClassActual() {
        return classActual;
    }

    /** Establecer la clase actual en el contexto.
     *
     * @param classActual La entrada de la clase a establecer como actual.
     */
    public void setClassActual(EntradaClase classActual) {
        this.classActual = classActual;
    }

    /** Obtener el método actual en el contexto.
     *
     * @return La entrada del método actual.
     */
    public EntradaMetodo getMetodoActual() {
        return metodoActual;
    }

    /** Establecer el método actual en el contexto.
     *
     * @param metodoActual La entrada del método a establecer como actual.
     */
    public void setMetodoActual(EntradaMetodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    /** Establecer el método de inicio (start) de la tabla de símbolos.
     *
     * @param startMethod La entrada del método de inicio.
     */
    public void setStartMethod(EntradaMetodo startMethod) {
        this.startMethod = startMethod;
    }

    /** Inicializar los tipos primitivos en la tabla de símbolos.
     * Crea las entradas para las clases Object, Int, Bool, Str, Double, Array e IO.
     */
    public void inicializarTiposPrimitivos() {
        EntradaClase obj = new EntradaClase(
                "Object",0,0);
        insertarClase("Object", obj);

        EntradaClase intClase = new EntradaClase(
                "Int",0,0,"Object");
        insertarClase("Int", intClase);

        EntradaClase booleanClase = new EntradaClase(
                "Bool",0,0, "Object");
        insertarClase("Bool", booleanClase);

        EntradaClase stringClase = new EntradaClase(
                "Str",0,0, "Object");
        insertarClase("Str", stringClase);

        EntradaClase doubleClase = new EntradaClase(
                "Double",0,0, "Object");
        insertarClase("Double", doubleClase);

        EntradaClase arrayClase = new EntradaClase(
                "Array",0,0, "Object");
        insertarClase("Array", arrayClase);

        EntradaClase ioClase = new EntradaClase(
                "IO",0,0, "Object");
        insertarClase("IO", ioClase);

    }

    /** Inicializar los métodos estándar de la clase IO.
     * Agrega métodos como out_str, out_int, in_str, in_int, etc. a la clase IO.
     */
    public void inicializarMetodosDeClaseIO() {
        // IO methods
        EntradaClase ioClase = buscarClase("IO");
        if (ioClase != null) {
            EntradaMetodo out_str = new EntradaMetodo(
                    "out_str", true, null);
            EntradaParametro parametroOutStr = new EntradaParametro(
                    "s", "Str", 0);
            out_str.insertarParametro("s", parametroOutStr);
            ioClase.insertarMetodo("out_str", out_str);

            EntradaMetodo out_int = new EntradaMetodo(
                    "out_int", true, null);
            EntradaParametro parametroOutInt = new EntradaParametro(
                    "i", "Int", 0);
            out_int.insertarParametro("i", parametroOutInt);
            ioClase.insertarMetodo("out_int", out_int);

            EntradaMetodo out_bool = new EntradaMetodo(
                    "out_bool", true, null);
            EntradaParametro parametroOutBool = new EntradaParametro(
                    "b", "Bool", 0);
            out_bool.insertarParametro("b", parametroOutBool);
            ioClase.insertarMetodo("out_bool", out_bool);

            // out_double
            EntradaMetodo out_double = new EntradaMetodo(
                    "out_double", true, null);
            EntradaParametro parametroOutDouble = new EntradaParametro(
                    "d", "Double", 0);
            out_double.insertarParametro("d", parametroOutDouble);
            ioClase.insertarMetodo("out_double", out_double);

            // out_array_int
            EntradaMetodo out_array_int = new EntradaMetodo(
                    "out_array_int", true, null);
            EntradaParametro parametroOutArrayInt = new EntradaParametro(
                    "a",
                    "Array",
                    "Int",
                    0);
            out_array_int.insertarParametro(
                    "a", parametroOutArrayInt);
            ioClase.insertarMetodo("out_array_int", out_array_int);

            // out_array_str
            EntradaMetodo out_array_str = new EntradaMetodo(
                    "out_array_str", true, null);
            EntradaParametro parametroOutArrayStr = new EntradaParametro(
                    "a",
                    "Array",
                    "Str",
                    0);
            out_array_str.insertarParametro(
                    "a", parametroOutArrayStr);
            ioClase.insertarMetodo("out_array_str", out_array_str);

            // out_array_bool
            EntradaMetodo out_array_bool = new EntradaMetodo(
                    "out_array_bool", true, null);
            EntradaParametro parametroOutArrayBool = new EntradaParametro(
                    "a",
                    "Array",
                    "Bool",
                    0);
            out_array_bool.insertarParametro(
                    "a", parametroOutArrayBool);
            ioClase.insertarMetodo(
                    "out_array_bool", out_array_bool);

            // out_array_double
            EntradaMetodo out_array_double = new EntradaMetodo(
                    "out_array_double", true, null);
            EntradaParametro parametroOutArrayDouble = new EntradaParametro(
                    "a",
                    "Array",
                    "Double",
                    0);
            out_array_double.insertarParametro(
                    "a", parametroOutArrayDouble);
            ioClase.insertarMetodo(
                    "out_array_double", out_array_double);

            // in_str
            EntradaMetodo in_str = new EntradaMetodo(
                    "in_str",
                    true,
                    buscarClase("Str")
            );
            ioClase.insertarMetodo("in_str", in_str);

            // in_int
            EntradaMetodo in_int = new EntradaMetodo(
                    "in_int",
                    true,
                    buscarClase("Int")
            );
            ioClase.insertarMetodo("in_int", in_int);

            // in_bool
            EntradaMetodo in_bool = new EntradaMetodo(
                    "in_bool",
                    true,
                    buscarClase("Bool")
            );
            ioClase.insertarMetodo("in_bool", in_bool);

            // in_double
            EntradaMetodo in_double = new EntradaMetodo(
                    "in_double",
                    true,
                    buscarClase("Double")
            );
            ioClase.insertarMetodo("in_double", in_double);
        }
    }

    /** Inicializar los métodos estándar de la clase Array.
     * Agrega el método length y el constructor a la clase Array.
     */
    public void inicializarMetodosDeClaseArray() {
        EntradaClase arrayClase = buscarClase("Array");
        if (arrayClase != null) {

            EntradaMetodo length = new EntradaMetodo(
                    "length",
                    true,
                    buscarClase("Int")
            );
            arrayClase.insertarMetodo("length", length);

            EntradaMetodo constructor = new EntradaMetodo(
                    "Array",
                    true,
                    null
            );

            EntradaParametro parametroConstructor = new EntradaParametro(
                    "n", "Int", 0
            );

            constructor.insertarParametro(
                    "n",
                    parametroConstructor
            );

            arrayClase.setConstructor(constructor);
        }
    }

    /** Inicializar los métodos estándar de la clase String.
     * Agrega métodos como length y concat a la clase String.
     */
    public void inicializarMetodosDeClaseString() {
        EntradaClase stringClase = buscarClase("Str");
        if (stringClase != null) {
            // String.length
            EntradaMetodo length = new EntradaMetodo(
                    "length",
                    true,
                    buscarClase("Int")
            );
            stringClase.insertarMetodo("length", length);

            // String.concat
            EntradaMetodo concat = new EntradaMetodo(
                    "concat",
                    true,
                    buscarClase("Str")
            );

            EntradaParametro parametroConcat = new EntradaParametro(
                    "s",
                    "Str",
                    0
            );
            concat.insertarParametro("s", parametroConcat);
            stringClase.insertarMetodo("concat", concat);
        }
    }

    /** Consolidar la tabla de símbolos en una representación JSON.
     *
     * @return Una cadena JSON que representa la tabla de símbolos.
     * @throws ErrorSemantico Si ocurre un error semántico durante la consolidación.
     */
    public String consolidarTS() throws ErrorSemantico {
        //ordenarClasesPorPosicion();
        String salida = """
                {
                \t"clases": [
                """;
        for (EntradaClase clase : clases.values()) {

            if (clase != clases.values().toArray()[clases.size() - 1]) {
                salida += clase.consolidarClase(this,false);
            } else {
                salida += clase.consolidarClase(this,true);
            }
        }
        salida += """
                
                 \t ],
                \t"start":
                """;

        salida += startMethod.consolidarMetodo(2, true);
        salida += "\n}";
        return salida;
    }
}
