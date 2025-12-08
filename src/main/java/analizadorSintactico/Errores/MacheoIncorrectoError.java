package analizadorSintactico.Errores;

public class MacheoIncorrectoError extends ErrorSintactico {
    public MacheoIncorrectoError(int line,int column,String esperado, String recibido) {
        super(line, column, "Macheo incorrecto - Se espera: " + esperado + " - Se obtuvo: " + recibido);
    }
}
