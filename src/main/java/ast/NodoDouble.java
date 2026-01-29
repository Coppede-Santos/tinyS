package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;
import generacionDeCodigo.MethodBodyVisitor;

import static ast.AstJsonBuilder.claveJson;
import static ast.AstJsonBuilder.tabs;
import static ast.AstJsonBuilder.valorJson;

/** Clase que representa un nodo double en el AST */
public class NodoDouble extends NodoNum{
    double valor;

    /** Constructor de la clase NodoDouble */
    public NodoDouble(Double literal, int linea, int columna) {
        super("Double", linea, columna);
        valor = literal;
    }

    public double getValor() {
        return valor;
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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        throw new EncadenadoInvalido(posicion,String.valueOf(valor));
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
        String salida = "";

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoDouble") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("valor")
                + valorJson(String.valueOf(valor)) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("tipo")
                + valorJson("Double") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("posicion")
                + "{\n";
        salida += tabs(profundidad + 2) + claveJson("linea")
                + valorJson(String.valueOf(posicion.getLinea())) + ",\n";
        salida += tabs(profundidad + 2) + claveJson("columna")
                + valorJson(String.valueOf(posicion.getColumna())) + "\n";
        salida += tabs(profundidad + 1) + "}\n";

        if (encadenado != null){
            throw new EncadenadoInvalido(posicion, String.valueOf(valor));
        }

        return salida;
    }

    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
    }
}
