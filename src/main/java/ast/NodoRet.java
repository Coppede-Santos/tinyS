package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import static ast.AstJsonBuilder.*;

public class NodoRet extends NodoSentencia{
    NodoExp exp;

    public NodoRet(NodoExp exp, int linea, int columna){
        super(linea, columna);
        this.exp = exp;
    }

    public NodoExp getExp(){
        return exp;
    }


    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorSemantico {
        String salida = "";

        if (exp == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoRet") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("expresion") + "{\n";
        salida += exp.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);
        salida += tabs(profundidad + 1) + "}\n";

        if(exp.getTipo() == "nil") exp.setTipo(null);

        if(exp.getTipo() != entradaMetodo.getTipoRetorno().getLexema()) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");

        return salida;
    }
}
