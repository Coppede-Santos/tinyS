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



        if(operador == TokenType.DIV){
            if (tipoDer != "INT" && tipoIz != "INT") throw new ErrorSemantico(posicion.getLinea(),posicion.getColumna(),"","");
            tipo = "INT";
        }

        if  (operador == TokenType.MINUS || operador == TokenType.MULT || operador == TokenType.SLASH || operador == TokenType.PERCENTAGE){
            if (tipoDer != "INT" && tipoIz != "INT" && tipoDer != "DOUBLE" && tipoIz != "DOUBLE") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            if (tipoDer == "DOUBLE" || tipoIz == "DOUBLE"){
                tipo = "DOUBLE";
            }else tipo = "INT";
        }


        if (operador == TokenType.PLUS || operador == TokenType.MULT){
            if (tipoDer != "INT" && tipoIz != "INT" && tipoDer != "DOUBLE" && tipoIz != "DOUBLE" && tipoDer != "STRING" && tipoIz != "STRING"){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
            }
            switch (tipoIz){
                case "INT":
                    switch (tipoDer){
                        case "INT": tipo = "INT"; break;
                        case "DOUBLE": tipo = "DOUBLE"; break;
                        default: throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                    } break;
                case "DOUBLE":
                   if (tipoDer != "DOUBLE" && tipoDer != "INT") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                   tipo = "Double";
                   break;
                case "STRING":
                    if (tipoDer != "STRING" || operador == TokenType.MULT) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                    tipo = "STRING";
                    break;
                default: throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
            }
        }



        if(operador == TokenType.GREATER || operador == TokenType.GREATER_EQUAL || operador == TokenType.LESS || operador == TokenType.LESS_EQUAL){
            if (tipoDer != "INT" && tipoIz != "INT" && tipoDer != "DOUBLE" && tipoIz != "DOUBLE") throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            tipo = "BOOL";
        }

        if(operador == TokenType.EQUAL_EQUAL || operador == TokenType.NOT_EQUAL){
            if (tipoDer != tipoIz){
                if((tipoDer == "Int" && tipoIz == "Double") || (tipoDer == "Double" && tipoIz == "Int"))
                    tipo = "Bool";
                else{
                    throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
                }
            }

            if(tipoDer != "String" && tipoDer != "Int" && tipoDer != "Double" && tipoDer != "Bool"){
                throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "","");
            }
            tipo = "Bool";
        }



        return salida;
    }

}
