package analizadorSemantico.Errores;

public class SubtipoArregloError extends ErrorSemantico {
    public SubtipoArregloError(int line,int column,String lexema) {
        super(line,column, "El subtipo de arreglo es invalido: ",lexema);
    }
}
