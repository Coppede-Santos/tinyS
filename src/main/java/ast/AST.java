package ast;

import analizadorSemantico.Errores.ErrorSemantico;
import analizadorSemantico.SymbolTable;

import java.util.HashMap;

import static ast.AstJsonBuilder.borrarTrailingCommas;
import static ast.AstJsonBuilder.claveJson;

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
            salida += "\t\t{\n";
            salida += clase.chequeoDeSentencias(st);
            if (clase != clases.values().toArray()[clases.size()-1]){
                salida += "\t\t},\n";
            } else {
                salida += "\t\t}\n";
            }
        }

        salida += "\t],\n";

        salida += "\t" + claveJson("bloqueStart") + "{\n";

        salida += "\t\t" + claveJson("posicion") + "{\n";
        salida += "\t\t\t" + claveJson("linea") + st.getStartMethod().getLinea() + ",\n";
        salida += "\t\t\t" + claveJson("columna") + st.getStartMethod().getColumna() + "\n";
        salida += "\t\t},\n";

        salida += "\t\t" + claveJson("sentencias") + "\n";
        salida += start.chequeoDeSentencias(st.getStartMethod(), st, profundidad + 2);

        salida += "\t}\n";

        salida += "}\n";

        return borrarTrailingCommas(salida);
    }

}
