package analizadorSintactico.Errores;

public class TokenInesperadoError extends ErrorSintactico {

    /**
     * Construye un nuevo error de token inesperado con la información proporcionada.
     *
     * @param line     La línea donde ocurrió el error.
     * @param column   La columna donde ocurrió el error.
     * @param esperado El token que se esperaba.
     * @param recibido El token que se recibió.
     */
    public TokenInesperadoError(int line,int column,String esperado, String recibido) {
        super(line, column, "TOKEN INESPERADO - Se espera: " + esperado + " - Se obtuvo: " + recibido + " ");
    }
}
