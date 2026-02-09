package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.EntradaClase;
import analizadorSemantico.EntradaMetodo;
import analizadorSemantico.Errores.ClaseNoDeclaradaError;
import analizadorSemantico.SymbolTable;
import ast.Errores.RetInvalidoError;
import ast.Errores.TipoInvalidoError;
import generacionDeCodigo.MethodBodyVisitor;

import static ast.AstJsonBuilder.*;

public class NodoRet extends NodoSentencia{
    NodoExp exp;

    public NodoRet(NodoExp exp, int linea, int columna){
        super(linea, columna);
        this.exp = exp;
    }

    public NodoExp getExp() {
        return exp;
    }

    @Override
    public String chequeoDeSentencias(EntradaMetodo entradaMetodo, SymbolTable st, int profundidad) throws ErrorTiny {
        String salida = "";
        EntradaClase retornoDeclarado = entradaMetodo.getTipoRetorno();

        if (retornoDeclarado == null){
            throw new RetInvalidoError(posicion, entradaMetodo.getLexema());
        }


        salida += tabs(profundidad + 1) + claveJson("tipoNodo") + valorJson("NodoRet") + ",\n";
        salida += tabs(profundidad + 1) + claveJson("expresion") + "{\n";
        if (exp == null){
            salida += "";
            if (!retornoDeclarado.esTipoPrimitivo()){
                return salida;
            }else throw new TipoInvalidoError(posicion, "ret","nil");

        }else{
            salida += exp.chequeoDeSentencias(entradaMetodo,st, profundidad + 1);

        }
        salida += tabs(profundidad + 1) + "}\n";



        //if(exp.getTipo() == "nil") exp.setTipo(null);


        EntradaClase tipoRetorno = st.buscarClase(exp.getTipo());

        if (tipoRetorno == null){
            if(retornoDeclarado.esClasePrimitiva()) {
                throw new ClaseNoDeclaradaError(posicion.getLinea(), posicion.getColumna(), exp.getTipo());
            }
        }


        if (!tipoRetorno.buscarAncestro(st, retornoDeclarado.getLexema())){
            throw new TipoInvalidoError(posicion, "ret", exp.getTipo());
        }



        //if(exp.getTipo() != entradaMetodo.getTipoRetorno().getLexema()) throw new ErrorSemantico(posicion.getLinea(), posicion.getColumna(), "El tipo de retorno ("+exp.getTipo()+") no coincide con el tipo de retorno del metodo ("+entradaMetodo.getTipoRetorno().getLexema()+")","");

        return salida;
    }

    @Override
    public void accept(MethodBodyVisitor methodBodyVisitor) {
        methodBodyVisitor.generarCodigo(this);
    }

}
