package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import static ast.AstJsonBuilder.*;

public class NodoWhile extends NodoSentencia {
    NodoExp condicion;
    NodoSentencia sentencia;

    public NodoWhile(NodoExp condicion, NodoSentencia sentencia, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";

        if(condicion == null || sentencia == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoWhile") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("condicion") + "{\n";
        salida += condicion.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!condicion.tipo.equals("Bool")){
            throw new ErrorSemantico(condicion.posicion.getLinea(), condicion.posicion.getColumna(),
                    "La condicion de un while debe ser de tipo Bool","");
        }

        salida += tabs(profundidad + 1) + claveJson("sentencia") + "\n";
        salida += sentencia.chequeoDeSentencias(entradaMetodo,st, profundidad + 2);

        return salida;
    }
}
