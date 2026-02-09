package ast;

import ErrorManage.ErrorTiny;
import analizadorLexico.TokenType;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;
import ast.Errores.EncadenadoInvalido;
import ast.Errores.ExpresionInvalidaError;
import generacionDeCodigo.MethodBodyVisitor;

import java.util.Objects;

import static ast.AstJsonBuilder.*;

public class NodoExpBin extends NodoExpUn{
    NodoExp ladoIzquierdo;

    public NodoExpBin(NodoExp ladoIzquierdo, NodoExp ladoDerecho, TokenType operador, int linea, int columna){
        super(ladoDerecho,operador, linea, columna);
        this.ladoIzquierdo = ladoIzquierdo;
    }

    public NodoExp getLadoIzquierdo() {
        return ladoIzquierdo;
    }

    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";

        if (ladoDerecho == null || operador == null || ladoIzquierdo == null)
            throw new ExpresionInvalidaError(posicion, String.valueOf(operador));

        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoExpBin") + ",\n";

        salida += tabs(profundidad + 1) + claveJson("ladoIzquierdo") + "{\n";
        salida += ladoIzquierdo.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        salida += tabs(profundidad + 1) + claveJson("operador") + valorJson(operador.toString()) + ",\n";

        salida += tabs(profundidad + 1) + claveJson("ladoDerecho") + "{\n";
        salida += ladoDerecho.chequeoDeSentencias(entradaMetodo, st, profundidad + 1);
        salida += tabs(profundidad + 1) + "},\n";

        if (ladoDerecho.getTipo() == null || ladoIzquierdo.getTipo() == null) throw new ExpresionInvalidaError(posicion, String.valueOf(operador));

        String tipoDer = ladoDerecho.getTipo();
        String tipoIz = ladoIzquierdo.getTipo();

        boolean algunTipoNoEsNumerico = (!Objects.equals(tipoIz, "Int") && !Objects.equals(tipoIz, "Double"))
                || (!Objects.equals(tipoDer, "Int") && !Objects.equals(tipoDer, "Double"));

        if(operador == TokenType.DIV){
            if (!Objects.equals(tipoDer, "Int") || !Objects.equals(tipoIz, "Int"))
                throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            tipo = "Int";
        }

        if  (operador == TokenType.MINUS || operador == TokenType.MULT || operador == TokenType.SLASH || operador == TokenType.PERCENTAGE){
            if (algunTipoNoEsNumerico)
                throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            if (tipoDer.equals("Double") || tipoIz.equals("Double")){
                tipo = "Double";
            }else tipo = "Int";
        }


        if (operador == TokenType.PLUS || operador == TokenType.MULT){

            if ((!Objects.equals(tipoDer, "Int") && !Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Str"))
                || (!Objects.equals(tipoIz, "Int") &&  !Objects.equals(tipoIz, "Double") && !Objects.equals(tipoIz, "Str"))){
                throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            }

            switch (tipoIz){
                case "Int":
                    switch (tipoDer){
                        case "Int": tipo = "Int"; break;
                        case "Double": tipo = "Double"; break;
                        default: throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
                    } break;
                case "Double":
                   if (!Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Int")) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                   tipo = "Double";
                   break;
                case "Str":
                    if (!Objects.equals(tipoDer, "Str") || operador == TokenType.MULT) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(),"","");
                    tipo = "Str";
                    break;
                default: throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            }
        }

        if(operador == TokenType.GREATER || operador == TokenType.GREATER_EQUAL || operador == TokenType.LESS || operador == TokenType.LESS_EQUAL){
            if (algunTipoNoEsNumerico) throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            tipo = "Bool";
        }

        if(operador == TokenType.EQUAL_EQUAL || operador == TokenType.NOT_EQUAL){
            if (!Objects.equals(tipoDer, tipoIz)){
                if(!((Objects.equals(tipoDer, "Int") && Objects.equals(tipoIz, "Double"))
                        || (Objects.equals(tipoDer, "Double") && Objects.equals(tipoIz, "Int")))){
                    if (! Objects.equals(tipoDer, "nil") && !Objects.equals(tipoIz, "nil")){
                        throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
                    }
                }
            }

            if(!Objects.equals(tipoDer, "Str") && !Objects.equals(tipoDer, "Int")
                    && !Objects.equals(tipoDer, "Double") && !Objects.equals(tipoDer, "Bool") && !Objects.equals(tipoDer, "nil") && !Objects.equals(tipoIz, "nil") ){
                throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            }
            tipo = "Bool";
        }
        
        if (operador == TokenType.AND || operador == TokenType.OR){
            if (!Objects.equals(tipoDer, "Bool") || !Objects.equals(tipoIz, "Bool"))
                throw new ExpresionInvalidaError (posicion, String.valueOf(operador));
            tipo = "Bool";
        }

        // ("A" + "B").length()
        if (encadenado != null) {
            salida += this.encadenado.chequeoDeSentencias(entradaMetodo, st, tipo, profundidad + 1);
            this.tipo = encadenado.getTipo();
        }

        salida += tabs(profundidad + 1) + claveJson("tipo") + valorJson(tipo) + "\n";

        return salida;
    }

    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, String tipoEncadenadoPrev, int profundidad) throws ErrorTiny {
        throw new EncadenadoInvalido(posicion, tipoEncadenadoPrev);
    }

    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
    }

}
