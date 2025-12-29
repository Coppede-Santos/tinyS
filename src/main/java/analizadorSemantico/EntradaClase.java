package analizadorSemantico;

import analizadorSemantico.Errores.ClaseSinConstructorError;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.Errores.RedefinirAtributoError;
import analizadorSemantico.Errores.RedefinirMetodoError;

import java.util.HashMap;

public class EntradaClase extends Entrada {
    EntradaClase superClase;
    HashMap<String, EntradaAtributos> atributos = new HashMap<>();
    HashMap<String, EntradaMetodo> metodos = new HashMap<>();
    EntradaMetodo constructor;

    public EntradaClase(String nombre) {
        super(nombre);
    }

    public EntradaClase(String nombre, EntradaClase superClase) {
        super(nombre);
        this.superClase = superClase;
        //agregarMetodosDeSuperClase();
    }

    public EntradaClase(String nombre, int linea, int columna, EntradaClase superClase) {
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

    public EntradaClase getSuperClase() {
        return superClase;
    }

    public boolean tieneConstructor() {
        return (constructor != null);
    }


    public void setConstructor(EntradaMetodo constructor) {
        this.constructor = constructor;
    }

    public void setSuperClase(EntradaClase superClase) {
        this.superClase = superClase;
        //agregarMetodosDeSuperClase();
    }

    private void agregarMetodosDeSuperClase() throws ErrorSemantico{
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

    private void agregarAtributosDeSuperClase() throws ErrorSemantico{
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


    public String consolidarClase(boolean claseFinal) throws ErrorSemantico {

        if (!lexema.equals("Object") && !lexema.equals("IO") && !lexema.equals("Int") && !lexema.equals("Bool") && !lexema.equals("Str") && !lexema.equals("Double")) {
            if (!tieneConstructor()) {
                throw new ClaseSinConstructorError(this.getLinea(), this.getColumna(), this.getLexema());
            }
        }

        agregarMetodosDeSuperClase();
        agregarAtributosDeSuperClase();

        String salida = "\t\t{\n" + consolidar(3) +
                "\t\t\t\"superClase\": " + ((superClase != null) ? ("\"" + superClase.getLexema() + "\"") : "null") + ",\n" +
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
            // Check if it is the last method
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
