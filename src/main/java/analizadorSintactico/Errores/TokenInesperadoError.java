package analizadorSintactico.Errores;

public class TokenInesperadoError extends ErrorSintactico {
    public TokenInesperadoError(int line,int column,String descripcion) {
        super(line, column, "TOKEN INESPERADO, Se espera: " + descripcion);
    }
}
