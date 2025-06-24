package analizadorLexico;
import ErrorManage.ErrorTiny;
public class ErrorLex extends ErrorTiny {

    ErrorLex(int line, int column, String descripcion, String lexema){
        super (line, column, descripcion, lexema);
    }
}
