package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.SymbolTable;
import ast.Errores.ExpresionInvalidaError;
import ast.Errores.TipoInvalidoError;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de sentencia 'if' en el AST */
public class NodoIf extends NodoSentencia{
    NodoExp condicion;
    NodoSentencia sentenciaIf;
    NodoSentencia sentenciaElse;

    /** Constructor de la clase NodoIf */
    public NodoIf(NodoExp condicion, NodoSentencia sentenciaIf, NodoSentencia sentenciaElse, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentenciaIf = sentenciaIf;
        this.sentenciaElse = sentenciaElse;
    }

    /** Método para realizar el chequeo de sentencias en el nodo 'if'
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

        //N0 debería llegar nunca
        if (condicion == null || sentenciaIf == null) {
            throw new ExpresionInvalidaError(posicion,"if");
        }

        salida += tabs(profundidad + 1) + claveJson("tipoNodo")
                + valorJson("NodoIf") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("condicion")
                + "{\n";
        salida += condicion.chequeoDeSentencias(
                entradaMetodo,st, profundidad + 1
        );
        salida += tabs(profundidad + 1) + "},\n";

        if (!condicion.tipo.equals("Bool")){
            throw new TipoInvalidoError(posicion, condicion.tipo, "Bool" );
        }

        salida += tabs(profundidad + 1) + claveJson("sentenciaIf");
        if (!esBloque(sentenciaIf)) {
            salida += "{\n";
        }

        salida += sentenciaIf.chequeoDeSentencias(
                entradaMetodo,st, profundidad + 1
        );
        if (!esBloque(sentenciaIf)) {
            salida += tabs(profundidad + 1) + "}\n";
        }

        if (sentenciaElse != null) {
            salida += tabs(profundidad + 1) +
                    claveJson("sentenciaElse");
            if (!esBloque(sentenciaElse)) {
                salida += "{\n";
            }
            salida += sentenciaElse.chequeoDeSentencias(
                    entradaMetodo,st, profundidad + 1
            );
            if (!esBloque(sentenciaElse)) {
                salida += tabs(profundidad + 1) + "}\n";
            }
        }

        return salida;
    }
}
