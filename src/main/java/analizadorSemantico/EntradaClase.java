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
                        throw new RedefinirMetodoError(metodo.getLinea(),metodo.getColumna(),lexema, metodo.lexema);
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
                    throw new RedefinirAtributoError( atributo.getLinea(), atributo.getColumna(), this.getLexema(), atributo.getLexema());
                }
                EntradaAtributos atributoSuperClase = superClase.atributos.get(nombreAtributo);
                this.atributos.put(nombreAtributo, atributoSuperClase);
            }
        }
    }


    public String consolidarClase() throws ErrorSemantico {

        if (!lexema.equals("Object") && !lexema.equals("IO") && !lexema.equals("Int") && !lexema.equals("Bool") && !lexema.equals("Str") && !lexema.equals("Double")) {
            if (!tieneConstructor()) {
                throw new ClaseSinConstructorError(this.getLinea(), this.getColumna(), this.getLexema());
            }
        }

        agregarMetodosDeSuperClase();
        agregarAtributosDeSuperClase();

        String salida = consolidar() + "\n" +
                "superClase: " + ((superClase != null) ? superClase.getLexema() : "null") + "\n" +
                "Atributos: { ";
        for (EntradaAtributos atributo : atributos.values()) {
            salida += atributo.consolidarAtributo();
        }
        salida += "} \n";
        if (constructor != null) {
            salida += constructor.consolidarMetodo() + "\n" +
                    "Metodos: {";
        }

        for (EntradaMetodo metodo : metodos.values()) {
            salida += metodo.consolidarMetodo();
        }

        salida += "}";

        return salida;

    }
}
