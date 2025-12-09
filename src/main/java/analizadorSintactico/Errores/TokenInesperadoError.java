package analizadorSintactico.Errores;

public class TokenInesperadoError extends ErrorSintactico {
    public TokenInesperadoError(int line,int column,String esperado, String recibido) {
        super(line, column, "TOKEN INESPERADO - Se espera: " + esperado + " - Se obtuvo: " + recibido + " ");
    }
}
