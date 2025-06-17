package analizadorLexico;

/**
 * La clase {@code ErrorLex} representa una excepción que se lanza cuando se encuentra un error léxico
 * durante el proceso de análisis.
 */

public class ErrorLex extends Exception{

    /**
     * Constructor para la clase {@code ErrorLex}.
     *
     * @param line        El número de línea donde ocurrió el error.
     * @param column      El número de columna donde ocurrió el error.
     * @param descripcion Una descripción del error.
     * @param lexema      El lexema que causó el error.
     */

    ErrorLex(int line, int column, String descripcion, String lexema){
        super ("| LINEA " + line + "( COLUMNA: " + column + ") | " + descripcion + " " + lexema );
    }
}
