package ast;

import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

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
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";

        if (exp == null) throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");

        exp.chequeoDeSentencias(entradaMetodo,st);

        if(exp.getTipo() == "NIL") exp.setTipo(null);

        if(exp.getTipo() != entradaMetodo.getTipoRetorno().getLexema()) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");

        return salida;
    }
}
