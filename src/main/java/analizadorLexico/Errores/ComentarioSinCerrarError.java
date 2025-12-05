package analizadorLexico.Errores;

public class ComentarioSinCerrarError extends ErrorLex {
    public ComentarioSinCerrarError(int line, int column, String lexema){
        super (line, column, "COMENTARIO SIN TERMINAR ", lexema);}
}
