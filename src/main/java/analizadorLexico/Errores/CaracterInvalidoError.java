package analizadorLexico.Errores;

public class CaracterInvalidoError extends ErrorLex {
    public CaracterInvalidoError(int line, int column, String lexema){
        super (line, column, "CARACTER INVALIDO ", lexema);
    }
}
