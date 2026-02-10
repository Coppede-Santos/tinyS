package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.SymbolTable;
import ast.Errores.RetInvalidoError;
import ast.Errores.TipoInvalidoError;

import static ast.AstJsonBuilder.*;

/** Clase que representa un nodo de sentencia 'return' en el AST */
public class NodoRet extends NodoSentencia{
    NodoExp exp;

    /** Constructor de la clase NodoRet */
    public NodoRet(NodoExp exp, int linea, int columna){
        super(linea, columna);
        this.exp = exp;
    }

    /** Método para realizar el chequeo de sentencias en el nodo 'return'
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
        EntradaClase retornoDeclarado = entradaMetodo.getTipoRetorno();

        if (retornoDeclarado == null){
            throw new RetInvalidoError(posicion, entradaMetodo.getLexema());
        }

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") +
                valorJson("NodoRet") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("expresion") +
                "{\n";

        if (exp == null){
            salida += "";
            if (!retornoDeclarado.esTipoPrimitivo()){
                return salida;
            }
            else {
                throw new TipoInvalidoError(posicion, "ret","nil");
            }

        }else{
            salida += exp.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        }
        salida += tabs(profundidad + 1) + "}\n";


        EntradaClase tipoRetorno = st.buscarClase(exp.getTipo());

        if (tipoRetorno == null){
            throw new ClaseNoDeclaradaError(
                    posicion.getLinea(),
                    posicion.getColumna(),
                    exp.getTipo()
            );
        }

        if (!tipoRetorno.buscarAncestro(st, retornoDeclarado.getLexema())){
            throw new TipoInvalidoError(
                    posicion,
                    "ret",
                    exp.getTipo()
            );
        }


        return salida;
    }
}
