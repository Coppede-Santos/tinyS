package ast;

import ErrorManage.ErrorTiny;
import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;
import ast.Errores.ExpresionInvalidaError;

import static ast.AstJsonBuilder.*;

public class NodoExpUn extends NodoExp{
    NodoExp ladoDerecho;
    TokenType operador;

    public NodoExpUn(NodoExp nodoExp, TokenType type, int linea, int columna) {
        super(linea, columna);
        this.ladoDerecho = nodoExp;
        this.operador = type;
    }


    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";


        if (ladoDerecho == null || operador == null)
            throw new ExpresionInvalidaError(posicion, String.valueOf(operador));

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoExpUn") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("operador") + valorJson(operador.toString()) + ",\n";
        salida += tabs(profundidad + 1) + claveJson("ladoDerecho") + "{\n";
        salida += ladoDerecho.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        tipo = ladoDerecho.getTipo();

        if (ladoDerecho.getTipo() == null) throw new ExpresionInvalidaError (posicion, String.valueOf(operador));

        if (operador == TokenType.PLUS_PLUS || operador == TokenType.MINUS_MINUS || operador == TokenType.PLUS || operador == TokenType.MINUS ){
            if(tipo != "Int" && tipo != "Double") throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
        }

        if (operador == TokenType.NOT){ if (tipo != "Bool") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");}

        if(operador == TokenType.LEFT_PAREN){
            if (tipo != "Double") throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            tipo = "Int";
        }

        if (encadenado != null){
            throw new EncadenadoInvalido(posicion, String.valueOf(operador));
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(tipo) + "\n";

        return salida;
    }

    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        throw new ExpresionInvalidaError(posicion,tipoEncadenadoPrev);
    }
}
