package analizadorSemantico;

import analizadorSemantico.Errores.*;

import java.util.HashMap;

public class EntradaClase extends Entrada {
    String superClase;
    HashMap<String, EntradaAtributos> atributos = new HashMap<>();
    HashMap<String, EntradaMetodo> metodos = new HashMap<>();
    EntradaMetodo constructor;
    boolean estaConsolidada = false;

    public EntradaClase(String nombre) {
        super(nombre);
    }

    public EntradaClase(String nombre, String superClase) {
        super(nombre);
        this.superClase = superClase;
        //agregarMetodosDeSuperClase();
    }

    public EntradaClase(String nombre, int linea, int columna, String superClase) {
        super(nombre, linea, columna);
        this.superClase = superClase;
        //agregarMetodosDeSuperClase();
    }
    public EntradaClase(String nombre, int linea, int columna) {
        super(nombre, linea, columna);
    }

    public EntradaAtributos buscarAtributo(String nombreAtributo) {
        return atributos.get(nombreAtributo);
    }

    public boolean insertarAtributo(String nombreAtributo, EntradaAtributos entradaAtributo) {
        if (atributos.containsKey(nombreAtributo))
            return false;
        atributos.put(nombreAtributo, entradaAtributo);
        return true;
    }

    public EntradaMetodo buscarMetodo(String nombreMetodo) {
        return metodos.get(nombreMetodo);
    }

    public boolean insertarMetodo(String nombreMetodo, EntradaMetodo entradaMetodo) {
        if (metodos.containsKey(nombreMetodo))
            return false;
        metodos.put(nombreMetodo, entradaMetodo);
        return true;
    }

    public String getSuperClase() {
        return superClase;
    }

    public boolean tieneConstructor() {
        return (constructor != null);
    }


    public void setConstructor(EntradaMetodo constructor) {
        this.constructor = constructor;
    }

    public void setSuperClase(String superClase) {
        this.superClase = superClase;
        //agregarMetodosDeSuperClase();
    }

    public boolean buscarAncestro(SymbolTable st,String nombre){
        if (nombre.equals(this.lexema)) return true;
        if (superClase == null) return false;
        EntradaClase entradaSuperClase = st.buscarClase(superClase);
        if (entradaSuperClase == null) return false;
        return entradaSuperClase.buscarAncestro(st,nombre);
    }

    private void agregarMetodosDeSuperClase(EntradaClase superClase) throws ErrorSemantico{
        if (superClase != null) {
            for (EntradaMetodo metodoSuperClase : superClase.metodos.values()) {

                EntradaMetodo metodo= this.metodos.get(metodoSuperClase.lexema);

                if (metodo != null){
                    if (!metodo.compararFirma(metodoSuperClase)){
                        throw new RedefinirMetodoError(metodo.getLinea(),metodo.getColumna(),metodo.lexema, lexema);
                    }
                }else{
                    this.metodos.put(metodoSuperClase.lexema, metodoSuperClase);
                }
            }
        }
    }

    private void agregarAtributosDeSuperClase(EntradaClase superClase) throws ErrorSemantico{
        if (superClase != null) {
            for (String nombreAtributo : superClase.atributos.keySet()) {
                EntradaAtributos atributo = this.buscarAtributo(nombreAtributo);
                if ( atributo != null){
                    throw new RedefinirAtributoError( atributo.getLinea(), atributo.getColumna(), atributo.getLexema(), this.getLexema());
                }
                EntradaAtributos atributoSuperClase = superClase.atributos.get(nombreAtributo);
                this.atributos.put(nombreAtributo, atributoSuperClase);
            }
        }
    }


    public String consolidarClase(SymbolTable st,boolean claseFinal) throws ErrorSemantico {

        if (!lexema.equals("Object") && !lexema.equals("IO") && !lexema.equals("Int") && !lexema.equals("Bool") && !lexema.equals("Str") && !lexema.equals("Double")) {
            if (!tieneConstructor()) {
                throw new ClaseSinConstructorError(this.getLinea(), this.getColumna(), this.getLexema());
            }
        }

        String salida = "";

        EntradaClase entradaSuperClase = st.buscarClase(superClase);

        //Chequea que la clase no se encuentre en la linea de ancestros de su super clase
        if (superClase != null) {
            if (entradaSuperClase == null) {
                throw new HerenciaInvalidaError(posicion.linea, posicion.columna, superClase);
            }
            if (entradaSuperClase.buscarAncestro(st,lexema)) {
                throw new HerenciaCircularError(posicion.linea, posicion.columna, lexema);
            }
            if (!entradaSuperClase.estaConsolidada){
                salida += entradaSuperClase.consolidarClase(st,false);
                entradaSuperClase.estaConsolidada = true;
            }
        }







        agregarMetodosDeSuperClase(entradaSuperClase);
        agregarAtributosDeSuperClase(entradaSuperClase);

        salida += "\t\t{\n" + consolidar(3) +
                "\t\t\t\"superClase\": " + ((superClase != null) ? ("\"" + superClase + "\"") : "null") + ",\n" +
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
            salida += "\t\t\t\"constructor\": [\n" + constructor.consolidarMetodo(4,true) +
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

        return salida;

    }
}
