package ErrorManage;

public class ErrorTiny extends Exception {
    public ErrorTiny(int line, int column, String description, String lexema) {
        super(" \n| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: | \n"
                + "|LINEA " + line + " | COLUMNA " + column + " | " + description + lexema + "|");
    }
}
