package analizadorSemantico.Errores;

/** Clase que representa un error semántico de parámetro redefinido */
public class ParametroRedefinidoError extends ErrorSemantico {
    public ParametroRedefinidoError(int line,int column,String lexema) {
        super(line,column, "El parametro ya ha sido declarado anteriormente en este contexto: ",lexema);}
}
