package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.SymbolTable;
import ast.Errores.MetodoNoDeclaradoError;

import java.util.HashMap;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de clase en el AST */
public class NodoClass {
    String nombre;
    HashMap<String,NodoBloque> metodos = new HashMap<>();

    /** Constructor de la clase NodoClass */
    public NodoClass(String nombre) {
        this.nombre = nombre;
    }

    /** Método para insertar un método en la clase
     *
     * @param lexema del método
     * @param bloque del método
     */
    public void insertarMetodo(String lexema, NodoBloque bloque){
        metodos.put(lexema,bloque);
    }

    /** Método para realizar el chequeo de sentencias en la clase
     *
     * @param st Tabla de símbolos
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    public String chequeoDeSentencias(SymbolTable st) throws ErrorTiny {

        EntradaMetodo entradaMetodo;
        NodoBloque bloque;
        EntradaClase entradaClase = st.buscarClase(nombre);
        String salida = "";

        //No deberia ser llamado nunca
        if(entradaClase == null) throw new ClaseNoDeclaradaError(
                0,0,nombre
        );

        st.setClassActual(entradaClase);

        salida += tabs(3) + claveJson("nombre") +
                valorJson(nombre) + ",\n";
        salida += tabs(3) + claveJson("metodos") + "[\n";

        for (String metodoLex : metodos.keySet()){
            if (entradaClase.getConstructor().getLexema().equals(metodoLex)) {
                entradaMetodo = entradaClase.getConstructor();
            } else {
                entradaMetodo = entradaClase.getMetodo(metodoLex);

                if (entradaMetodo == null) {
                    throw new MetodoNoDeclaradoError(
                            metodos.get(metodoLex).posicion, metodoLex
                    );
                }
            }

            bloque = metodos.get(metodoLex);
            salida += tabs(4) + "{\n";
            salida += tabs(5) + claveJson("nombre")
                    + valorJson(entradaMetodo.getLexema()) + ",\n";
            salida += tabs(5) + claveJson("sentencias") + "\n";
            salida += bloque.chequeoDeSentencias(
                    entradaMetodo, st, 6
            );

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
