package analizadorSintactico;

import ErrorManage.ErrorTiny;

public class ErrorSintactico extends ErrorTiny {
  ErrorSintactico(int line, int column, String descripcion){
    super (line, column, descripcion,"");
  }
}
