package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

public class NodoExpUn extends NodoExp{
    NodoExp ladoDerecho;
    TokenType operador;

    public NodoExpUn(NodoExp nodoExp, TokenType type, int linea, int columna) {
        super(linea, columna);
        this.ladoDerecho = nodoExp;
        this.operador = type;
    }


    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico {
        String salida = "";


        if (ladoDerecho == null || operador == null)
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "", "");

        ladoDerecho.chequeoDeSentencias(entradaMetodo, st);

        tipo = ladoDerecho.getTipo();

        if (ladoDerecho.getTipo() == null) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "", "");

        if (operador == TokenType.PLUS_PLUS || operador == TokenType.MINUS_MINUS || operador == TokenType.PLUS || operador == TokenType.MINUS ){
            if(tipo != "INT" && tipo != "DOUBLE") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
        }

        if (operador == TokenType.NOT){ if (tipo != "BOOL") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");}

        if(operador == TokenType.LEFT_PAREN){
            if (tipo != "DOUBLE") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
            tipo = "INT";
        }

        if (encadenado != null){
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
        }


        return salida;
    }
}
