package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import ast.Errores.TipoInvalidoError;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de sentencia 'while' en el AST */
public class NodoWhile extends NodoSentencia {
    NodoExp condicion;
    NodoSentencia sentencia;

    /** Constructor de la clase NodoWhile */
    public NodoWhile(NodoExp condicion, NodoSentencia sentencia, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }

    /** Método para realizar el chequeo de sentencias en el nodo 'while'
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

        if(condicion == null || sentencia == null) {
            throw new ErrorSemantico(
                    posicion.getLinea(),
                    posicion.getColumna(),
                    "",""
            );
        }

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoWhile") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("condicion")
                + "{\n";
        salida += condicion.chequeoDeSentencias
                (entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!condicion.tipo.equals("Bool")){
            throw new TipoInvalidoError(
                    condicion.posicion, condicion.tipo, "While"
            );
        }

        salida += tabs(profundidad + 1) +
                claveJson("sentencia") + "\n";
        salida += sentencia.chequeoDeSentencias(
                entradaMetodo,st, profundidad + 2
        );

        return salida;
    }
}
