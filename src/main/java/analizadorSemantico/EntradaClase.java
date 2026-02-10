package analizadorSemantico;

import analizadorSemantico.Errores.*;

import java.util.HashMap;

public class EntradaClase extends Entrada {
    String superClase;
    HashMap<String, EntradaAtributos> atributos = new HashMap<>();
    HashMap<String, EntradaMetodo> metodos = new HashMap<>();
    EntradaMetodo constructor;
    boolean estaConsolidada = false;

    /** Constructor de la clase EntradaClase
     *
     * @param nombre Nombre de la clase
     */
    public EntradaClase(String nombre) {
        super(nombre);
    }

    /** Constructor de la clase EntradaClase
     *
     * @param nombre Nombre de la clase
     * @param superClase Nombre de la superclase
     */
    public EntradaClase(String nombre, String superClase) {
        super(nombre);
        this.superClase = superClase;
    }

    /** Constructor de la clase EntradaClase
     *
     * @param nombre Nombre de la clase
     * @param linea Linea donde se encuentra la clase
     * @param columna Columna donde se encuentra la clase
     * @param superClase Nombre de la superclase
     */
    public EntradaClase(String nombre, int linea, int columna,
                        String superClase) {
        super(nombre, linea, columna);
        this.superClase = superClase;
    }

    /** Constructor de la clase EntradaClase
     *
     * @param nombre Nombre de la clase
     * @param linea Linea donde se encuentra la clase
     * @param columna Columna donde se encuentra la clase
     */
    public EntradaClase(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    /** Busca un atributo en la clase
     *
     * @param nombreAtributo Nombre del atributo a buscar
     * @return EntradaAtributos del atributo buscado, o null si no existe
     */
    public EntradaAtributos buscarAtributo(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    /** Inserta un atributo en la clase
     *
     * @param nombreAtributo Nombre del atributo a insertar
     * @param entradaAtributo EntradaAtributos del atributo a insertar
     */
    public void insertarAtributo(String nombreAtributo,
                                 EntradaAtributos entradaAtributo) {
        if (!atributos.containsKey(nombreAtributo)) {
            atributos.put(nombreAtributo, entradaAtributo);
        }
    }

    /** Busca un metodo en la clase
     *
     * @param nombreMetodo Nombre del metodo a buscar
     * @return EntradaMetodo del metodo buscado, o null si no existe
     */
    public EntradaMetodo buscarMetodo(String nombreMetodo) {
        return metodos.get(nombreMetodo);
    }

    /** Inserta un metodo en la clase
     *
     * @param nombreMetodo Nombre del metodo a insertar
     * @param entradaMetodo EntradaMetodo del metodo a insertar
     */
    public void insertarMetodo(String nombreMetodo, EntradaMetodo entradaMetodo) {
        if (!metodos.containsKey(nombreMetodo)) {
            metodos.put(nombreMetodo, entradaMetodo);
        }
    }

    /** Obtiene un metodo de la clase
     *
     * @param lexema Nombre del metodo a obtener
     * @return EntradaMetodo del metodo obtenido
     */
    public EntradaMetodo getMetodo(String lexema) {
        return metodos.get(lexema);
    }

    /** Obtiene el nombre de la superclase
     *
     * @return Nombre de la superclase
     */
    public String getSuperClase() {
        return superClase;
    }

    /** Verifica si la clase tiene un constructor
     *
     * @return true si tiene constructor, false si no
     */
    public boolean tieneConstructor() {
        return (constructor != null);
    }

    public EntradaMetodo getConstructor() {
        return constructor;
    }

    /** Establece el constructor de la clase
     *
     * @param constructor EntradaMetodo del constructor
     */
    public void setConstructor(EntradaMetodo constructor) {
        this.constructor = constructor;
    }

    /** Establece el nombre de la superclase
     *
     * @param superClase Nombre de la superclase
     */
    public void setSuperClase(String superClase) {
        this.superClase = superClase;
    }

    /** Busca si un nombre de clase es ancestro de la clase actual
     *
     * @param st Tabla de simbolos
     * @param nombre Nombre de la clase a buscar
     * @return true si es ancestro, false si no
     */
    public boolean buscarAncestro(SymbolTable st,String nombre){
        if (nombre.equals(this.lexema)) return true;
        if (superClase == null) return false;
        EntradaClase entradaSuperClase = st.buscarClase(superClase);
        if (entradaSuperClase == null) return false;
        return entradaSuperClase.buscarAncestro(st,nombre);
    }

    /** Agrega los metodos de la superclase a la clase actual
     *
     * @param superClase EntradaClase de la superclase
     * @throws ErrorSemantico Si hay un error de redefinicion de metodo
     */
    private void agregarMetodosDeSuperClase(EntradaClase superClase) throws ErrorSemantico{
        if (superClase != null) {
            for (EntradaMetodo metodoSuperClase : superClase.metodos.values()) {

                EntradaMetodo metodo= this.metodos.get(metodoSuperClase.lexema);

                if (metodo != null){
                    if (!metodo.compararFirma(metodoSuperClase)){
                        throw new RedefinirMetodoError(
                                metodo.getLinea(),
                                metodo.getColumna(),
                                metodo.lexema,
                                lexema
                        );
                    }
                }else{
                    this.metodos.put(metodoSuperClase.lexema, metodoSuperClase);
                }
            }
        }
    }

    /** Agrega los atributos de la superclase a la clase actual
     *
     * @param superClase EntradaClase de la superclase
     * @throws ErrorSemantico Si hay un error de redefinicion de atributo
     */
    private void agregarAtributosDeSuperClase(EntradaClase superClase)
            throws ErrorSemantico{
        if (superClase != null) {
            for (String nombreAtributo : superClase.atributos.keySet()) {
                EntradaAtributos atributo = this.buscarAtributo(nombreAtributo);
                if ( atributo != null){
                    throw new RedefinirAtributoError(
                            atributo.getLinea(),
                            atributo.getColumna(),
                            atributo.getLexema(),
                            this.getLexema()
                    );
                }
                EntradaAtributos atributoSuperClase = superClase.atributos.get(
                        nombreAtributo
                );
                this.atributos.put(nombreAtributo, atributoSuperClase);
            }
        }
    }

    /** Consolida la clase en formato JSON
     *
     * @param st Tabla de simbolos
     * @param claseFinal Indica si es la ultima clase a consolidar
     * @return String con la clase consolidada en formato JSON
     * @throws ErrorSemantico Si hay un error semantico durante la consolidacion
     */
    public String consolidarClase(SymbolTable st,
                                  boolean claseFinal)
            throws ErrorSemantico {
        String salida = "";
        EntradaClase entradaSuperClase = st.buscarClase(superClase);

        // Si la clase no es Base o Predefinida, y no tiene constructor,
        // entonces se tira un error
        if (!lexema.equals("Object") && !lexema.equals("IO")
                && !lexema.equals("Int") && !lexema.equals("Bool")
                && !lexema.equals("Str") && !lexema.equals("Double")
                && !tieneConstructor()) {
                throw new ClaseSinConstructorError(
                        this.getLinea(),
                        this.getColumna(),
                        this.getLexema()
                );
        }

        //Chequea que la clase no se encuentre en la linea de ancestros de su super clase
        if (superClase != null) {
            if (entradaSuperClase == null) {
                throw new HerenciaInvalidaError(
                        posicion.linea,
                        posicion.columna,
                        superClase
                );
            }
            if (entradaSuperClase.buscarAncestro(st,lexema)) {
                throw new HerenciaCircularError(posicion.linea,
                        posicion.columna,
                        lexema
                );
            }
            if (!entradaSuperClase.estaConsolidada){
                salida += entradaSuperClase.consolidarClase(st,false);
                entradaSuperClase.estaConsolidada = true;
            }
        }

        agregarMetodosDeSuperClase(entradaSuperClase);
        agregarAtributosDeSuperClase(entradaSuperClase);

        salida += "\t\t{\n" + consolidar(3) +
                "\t\t\t\"superClase\": " +
                ((superClase != null) ? ("\"" + superClase + "\"") : "null")
                + ",\n" +
                "\t\t\t\"atributos\": [\n";

        for (EntradaAtributos atributo : atributos.values()) {
            salida += "\t\t\t\t{\n" + atributo.consolidarAtributo(5);

            if (atributo != atributos.values().toArray()[atributos.size() - 1]) {
                salida += "\n\t\t\t\t},\n";
            } else {
                salida += "\n\t\t\t\t}\n";
            }
        }

        salida += "\t\t\t], \n";
        if (constructor != null) {
            salida += "\t\t\t\"constructor\": [\n" +
                    constructor.consolidarMetodo(4,true) +
                    "\t\t\t],\n";
        }

        salida += "\t\t\t\"metodos\": [\n";
        for (EntradaMetodo metodo : metodos.values()) {
            if (metodo != metodos.values().toArray()[metodos.size() - 1]) {
                salida += metodo.consolidarMetodo(4, false);
            } else {
                salida += metodo.consolidarMetodo(4, true);
            }
        }
        salida += "\t\t\t]\n";

        if (claseFinal) {
            salida += "\t\t}";
        } else {
            salida += "\t\t},\n";
        }

        this.estaConsolidada = true;

        return salida;
    }

    public boolean esTipoPrimitivo() {
        return lexema.equals("Int") ||
                lexema.equals("Double") ||
                lexema.equals("Bool") ||
                lexema.equals("Str")
                ;
    }
}
