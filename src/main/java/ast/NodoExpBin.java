package ast;

import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.Objects;

public class NodoExpBin extends NodoExpUn{
    NodoExp ladoIzquierdo;

    public NodoExpBin(NodoExp ladoIzquierdo, NodoExp ladoDerecho, TokenType operador, int linea, int columna){
        super(ladoDerecho,operador, linea, columna);
        this.ladoIzquierdo = ladoIzquierdo;
    }

    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st) throws ErrorSemantico{
        String salida = "";

        if (ladoDerecho == null || operador == null || ladoIzquierdo == null)
            throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "", "");

        ladoDerecho.chequeoDeSentencias(entradaMetodo, st);
        ladoIzquierdo.chequeoDeSentencias(entradaMetodo, st);


        if (ladoDerecho.getTipo() == null || ladoIzquierdo.getTipo() == null) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "", "");

        String tipoDer = ladoDerecho.getTipo();
        String tipoIz = ladoIzquierdo.getTipo();

        boolean algunTipoNoEsNumerico = (!Objects.equals(tipoIz, "Int") && !Objects.equals(tipoIz, "Double"))
                || (!Objects.equals(tipoDer, "Int") && !Objects.equals(tipoDer, "Double"));

        if(operador == TokenType.DIV){
            if (!Objects.equals(tipoDer, "Int") && !Objects.equals(tipoIz, "Int"))
                throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");
            tipo = "Int";
        }

        if  (operador == TokenType.MINUS || operador == TokenType.MULT || operador == TokenType.SLASH || operador == TokenType.PERCENTAGE){
            if (algunTipoNoEsNumerico)
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            if (tipoDer.equals("Double") || tipoIz.equals("Double")){
                tipo = "Double";
            }else tipo = "Int";
        }


        if (operador == TokenType.PLUS || operador == TokenType.MULT){

            if ((!Objects.equals(tipoDer, "Int") && !Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Str"))
                || (!Objects.equals(tipoIz, "Int") &&  !Objects.equals(tipoIz, "Double") && !Objects.equals(tipoIz, "Str"))){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
            }

            switch (tipoIz){
                case "Int":
                    switch (tipoDer){
                        case "Int": tipo = "Int"; break;
                        case "Double": tipo = "Double"; break;
                        default: throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                    } break;
                case "Double":
                   if (!Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Int")) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                   tipo = "Double";
                   break;
                case "Str":
                    if (!Objects.equals(tipoDer, "Str") || operador == TokenType.MULT) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                    tipo = "Str";
                    break;
                default: throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
            }
        }

        if(operador == TokenType.GREATER || operador == TokenType.GREATER_EQUAL || operador == TokenType.LESS || operador == TokenType.LESS_EQUAL){
            if (algunTipoNoEsNumerico) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            tipo = "Bool";
        }

        if(operador == TokenType.EQUAL_EQUAL || operador == TokenType.NOT_EQUAL){
            if (!Objects.equals(tipoDer, tipoIz)){
                if((Objects.equals(tipoDer, "Int") && Objects.equals(tipoIz, "Double"))
                        || (Objects.equals(tipoDer, "Double") && Objects.equals(tipoIz, "Int")))
                    tipo = "Bool";
                else{
                    throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
                }
            }

            if(!Objects.equals(tipoDer, "Str") && !Objects.equals(tipoDer, "Int")
                    && !Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Bool")){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            }
            tipo = "Bool";
        }
        
        if (operador == TokenType.AND || operador == TokenType.OR){
            if (!Objects.equals(tipoDer, "Bool") || !Objects.equals(tipoIz, "Bool"))
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            tipo = "Bool";
        }

        // ("A" + "B").length()
        if (encadenado != null) {
            salida += this.encadenado.chequeoDeSentencias(entradaMetodo, st, tipo);
            this.tipo = encadenado.getTipo();
        }

        return salida;
    }

    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev) throws ErrorSemantico {
        throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "prohibido encadenar una expresion binaria","");
    }

}
