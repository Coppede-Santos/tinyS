package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;
import generacionDeCodigo.MethodBodyVisitor;

import static ast.AstJsonBuilder.claveJson;
import static ast.AstJsonBuilder.tabs;
import static ast.AstJsonBuilder.valorJson;

/** Clase que representa un nodo nil en el AST */
public class NodoNil extends NodoOperando{

    /** Constructor de la clase NodoNil */
    public NodoNil(int linea, int columna){
        super("nil", linea, columna);
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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        if (encadenado != null){
            throw new EncadenadoInvalido(posicion, tipo);
        }

        String salida = "";
        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoNil") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor")
                + "null" + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo")
                + valorJson("nil") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion")
                + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea")
                + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna")
                + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        return salida;
    }


    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo,
                                      SymbolTable st,
                                      String tipoEncadenadoPrev,
                                      int profundidad) throws ErrorTiny
    {
        throw new EncadenadoInvalido(posicion,"nil");
    }
}
