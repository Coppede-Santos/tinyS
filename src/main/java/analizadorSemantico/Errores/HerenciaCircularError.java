package analizadorSemantico.Errores;

/** Clase que representa un error semántico de herencia circular */
public class HerenciaCircularError extends ErrorSemantico
{
    public HerenciaCircularError(int line,int column,String lexema) {
        super(line,column, "No se permite herencia circular: ",lexema);
    }
}
