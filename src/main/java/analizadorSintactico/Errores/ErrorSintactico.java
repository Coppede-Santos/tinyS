package analizadorSintactico.Errores;

import ErrorManage.ErrorTiny;

/**
 * La clase {@code ErrorSintactico} representa un error sintáctico
 * detectado durante el análisis sintáctico de un programa en TinyS.
 * <p>
 * Esta clase extiende {@link ErrorTiny} y se utiliza para encapsular
 * información sobre errores sintácticos, incluyendo la línea y columna
 * donde ocurrió el error, así como una descripción del mismo.
 * </p>
 */
public class ErrorSintactico extends ErrorTiny {
  ErrorSintactico(int line, int column, String descripcion){
    super (line, column, descripcion,"");
  }
}
