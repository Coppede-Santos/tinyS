package ast;

import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.HashMap;

public class NodoClass {
    String nombre;
    HashMap<String,NodoBloque> metodos = new HashMap<>();

    public NodoClass(String nombre) {
        this.nombre = nombre;
    }

    public void insertarMetodo(String lexema, NodoBloque bloque){
        metodos.put(lexema,bloque);
    }


    public String chequeoDeSentencias(SymbolTable st) throws ErrorSemantico{

        EntradaClase entradaClase = st.buscarClase(nombre);

        if(entradaClase == null) throw new ErrorSemantico(0,0,"no tiene una clase definida","");

        st.setClassActual(entradaClase);

        String salida = "";

        EntradaMetodo entradaMetodo;
        NodoBloque bloque;

        for (String metodoLex : metodos.keySet()){

            if (entradaClase.getConstructor().getLexema().equals(metodoLex)) {
                entradaMetodo = entradaClase.getConstructor();
            } else {
                entradaMetodo = entradaClase.getMetodo(metodoLex);

                if (entradaMetodo == null) throw new ErrorSemantico(0,0,"no tiene un metodo definido","");
            }

            bloque = metodos.get(metodoLex);
            salida += bloque.chequeoDeSentencias(entradaMetodo, st);

        }

        return salida;
    }
}
