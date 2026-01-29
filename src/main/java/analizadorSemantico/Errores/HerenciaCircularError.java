package analizadorSemantico.Errores;

public class HerenciaCircularError extends ErrorSemantico
{
    public HerenciaCircularError(int line,int column,String lexema) {
        super(line,column, "No se permite herencia circular: ",lexema);
    }
}
