package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.SymbolTable;
import ast.Errores.MetodoNoDeclaradoError;

import java.util.HashMap;

import static ast.AstJsonBuilder.*;

public class NodoClass {
    String nombre;
    HashMap<String,NodoBloque> metodos = new HashMap<>();

    public NodoClass(String nombre) {
        this.nombre = nombre;
    }

    public void insertarMetodo(String lexema, NodoBloque bloque){
        metodos.put(lexema,bloque);
    }

    public NodoBloque getMetodo(String lexema){
        return metodos.get(lexema);
    }


    public String chequeoDeSentencias(SymbolTable st) throws ErrorTiny {

        EntradaMetodo entradaMetodo;
        NodoBloque bloque;
        EntradaClase entradaClase = st.buscarClase(nombre);
        int profundidad = 1;


        //No deberia ser llamado nunca
        if(entradaClase == null) throw new ClaseNoDeclaradaError(0,0,nombre);

        st.setClassActual(entradaClase);

        String salida = "";

        salida += tabs(3) + claveJson("nombre") + valorJson(nombre) + ",\n";
        salida += tabs(3) + claveJson("metodos") + "[\n";

        for (String metodoLex : metodos.keySet()){

            if (entradaClase.getConstructor().getLexema().equals(metodoLex)) {
                entradaMetodo = entradaClase.getConstructor();
            } else {
                entradaMetodo = entradaClase.getMetodo(metodoLex);

                if (entradaMetodo == null) throw new MetodoNoDeclaradoError(metodos.get(metodoLex).posicion, metodoLex);
            }

            bloque = metodos.get(metodoLex);
            salida += tabs(4) + "{\n";
            salida += tabs(5) + claveJson("nombre") + valorJson(entradaMetodo.getLexema()) + ",\n";
            // salida += tabs(5) + claveJson("posicion") + "{\n";
            // salida += tabs(6) + claveJson("linea") + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
            // salida += tabs(6) + claveJson("columna") + valorJson(String.valueOf(posicion.getColumna())) + "\n";
            // salida += tabs(5) + "},\n";
            salida += tabs(5) + claveJson("sentencias") + "\n";
            salida += bloque.chequeoDeSentencias(entradaMetodo, st, 6);
            if (metodoLex != metodos.keySet().toArray()[metodos.size()-1]){
                salida += tabs(4) + "},\n";
            } else {
                salida += tabs(4) + "}\n";
            }

        }

        salida += tabs(3) + "]\n";

        return salida;
    }
}
