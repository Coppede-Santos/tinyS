package analizadorSintactico.Errores;

/**
 * La clase {@code MacheoIncorrectoError} representa un error de macheo incorrecto
 * detectado durante el análisis sintáctico de un programa en TinyS.
 * <p>
 * Esta clase extiende {@link ErrorSintactico} y se utiliza para encapsular
 * información sobre errores de macheo, incluyendo la línea y columna
 * donde ocurrió el error, así como los valores esperado y recibido.
 */
public class MacheoIncorrectoError extends ErrorSintactico {
    public MacheoIncorrectoError(int line,int column,String esperado, String recibido) {
        super(line, column, "Macheo incorrecto - Se espera: " + esperado + " - Se obtuvo: " + recibido);
    }
}
