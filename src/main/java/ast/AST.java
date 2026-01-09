package ast;

import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.HashMap;

public class AST {
    HashMap<String,NodoClass> clases = new HashMap<>();
    NodoBloque start;

    public void insertarClass(String lexema, NodoClass clase) {
        clases.put(lexema,clase);
    }

    public NodoClass getClass(String lex){
        return clases.get(lex);
    }

    public void setStart(NodoBloque start) {
        this.start = start;
    }

    public String chequeoDeSentencias(SymbolTable st) throws ErrorSemantico{
        int profundidad = 1;
        String salida = "";
        salida += "{\n";
        salida += "\t\"clases\": [\n";

        for (NodoClass clase : clases.values()){
            salida += clase.chequeoDeSentencias(st);
        }

        salida += start.chequeoDeSentencias(st.getStartMethod(), st, profundidad + 1);
        salida += "}\n";

        return salida;
    }

}
