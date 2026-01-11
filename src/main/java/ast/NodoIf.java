package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import static ast.AstJsonBuilder.*;

public class NodoIf extends NodoSentencia{
    NodoExp condicion;
    NodoSentencia sentenciaIf;
    NodoSentencia sentenciaElse;

    public NodoIf(NodoExp condicion, NodoSentencia sentenciaIf, NodoSentencia sentenciaElse, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.sentenciaIf = sentenciaIf;
        this.sentenciaElse = sentenciaElse;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";
        if (condicion == null || sentenciaIf == null) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), ",","");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoIf") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("condicion") + "{\n";
        salida += condicion.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (!condicion.tipo.equals("Bool")){
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "La condicion de un if debe ser de tipo Bool","");
        }

        salida += tabs(profundidad + 1) + claveJson("sentenciaIf");
        if (!esBloque(sentenciaIf)) salida += "{\n";
        salida += sentenciaIf.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        if (!esBloque(sentenciaIf)) salida += tabs(profundidad + 1) + "}\n";


        if (sentenciaElse != null) {
            salida += tabs(profundidad + 1) + claveJson("sentenciaElse");
            if (!esBloque(sentenciaElse)) salida += "{\n";
            salida += sentenciaElse.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
            if (!esBloque(sentenciaElse)) salida += tabs(profundidad + 1) + "}\n";
        }

        return salida;
    }
}
