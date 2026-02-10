package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo string en el AST */
public class NodoString extends NodoOperando{
    String valor;

    /** Constructor de la clase NodoString */
    public NodoString(String valor, int linea, int columna) {
        super("Str", linea, columna);
        this.valor = valor;
    }

    /** Método para realizar el chequeo de sentencias con encadenado
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param tipoEncadenadoPrev Tipo del encadenado previo
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(
            EntradaMetodo entradaMetodo,
            SymbolTable st,
            String tipoEncadenadoPrev,
            int profundidad)
    throws ErrorTiny
    {
        throw new EncadenadoInvalido(posicion,valor);
    }

    /** Método para realizar el chequeo de sentencias
     *
     * @param entradaMetodo Entrada del método actual en la tabla de símbolos
     * @param st Tabla de símbolos
     * @param profundidad Profundidad actual en el árbol
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    @Override
    public String chequeoDeSentencias(
            EntradaMetodo entradaMetodo,
            SymbolTable st,
            int profundidad)
            throws ErrorTiny
    {
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoString") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor")
                + valorJson(valor) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo")
                + valorJson("String") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion")
                + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea")
                + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna")
                + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        if (encadenado != null){
            salida += ",\n";
            salida += tabs(profundidad + 1) +
                    claveJson("encadenado") + "\n";
            salida += tabs(profundidad + 2) + "{\n";
            this.encadenado.chequeoDeSentencias(
                entradaMetodo,
                st,
                this.tipo,
                profundidad + 1
            );
            salida += tabs(profundidad + 2) + "}\n";
            this.tipo = encadenado.getTipo();
        }
        return salida;
    }
}
