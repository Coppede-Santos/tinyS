package ast;

import ErrorManage.ErrorTiny;
import analizadorSemantico.SymbolTable;

import java.util.HashMap;

import static ast.AstJsonBuilder.borrarTrailingCommas;
import static ast.AstJsonBuilder.claveJson;

/** Clase que representa el árbol sintáctico abstracto (AST) */
public class AST {
    HashMap<String,NodoClass> clases = new HashMap<>();
    NodoBloque start;

    /** Inserta una clase en la tabla de clases del AST
     * @param lexema Nombre de la clase
     * @param clase NodoClass que representa la clase
     */
    public void insertarClass(String lexema, NodoClass clase) {
        clases.put(lexema,clase);
    }

    /** Obtiene una clase de la tabla de clases del AST
     * @param lex Nombre de la clase
     * @return NodoClass que representa la clase
     */
    public NodoClass getClass(String lex){
        return clases.get(lex);
    }

    /** Setter del bloque start del AST
     */
    public void setStart(NodoBloque start) {
        this.start = start;
    }

    /** Realiza el chequeo de sentencias del AST
     * @param st Tabla de símbolos
     * @return String con el resultado del chequeo en formato JSON
     * @throws ErrorTiny Si ocurre un error durante el chequeo
     */
    public String chequeoDeSentencias(SymbolTable st) throws ErrorTiny {
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
        salida += "\t\t\t" + claveJson("linea") +
                st.getStartMethod().getLinea() + ",\n";
        salida += "\t\t\t" + claveJson("columna") +
                st.getStartMethod().getColumna() + "\n";
        salida += "\t\t},\n";

        salida += "\t\t" + claveJson("sentencias") + "\n";
        salida += start.chequeoDeSentencias(
                st.getStartMethod(),
                st,
                profundidad + 2
        );

        salida += "\t}\n";

        salida += "}\n";

        return borrarTrailingCommas(salida);
    }

}
