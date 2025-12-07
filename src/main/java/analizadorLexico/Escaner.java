package analizadorLexico;


import analizadorLexico.Errores.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static analizadorLexico.TokenType.*;

/**
 * La clase {@code Escaner} realiza el análisis léxico de un código fuente.
 * Divide el código en tokens, que son las unidades léxicas básicas del lenguaje.
 */

public class Escaner {
    private String buffer;
    private LectorCF lectorCF;

    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 0;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("fn", FN);
        keywords.put("class", CLASS);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("while", WHILE);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("new", NEW);
        keywords.put("ret", RET);
        keywords.put("self", SELF);
        keywords.put("pub", PUB);
        keywords.put("nil", NIL);
        keywords.put("impl", IMPL);
        keywords.put("st", ST);
        keywords.put("div", DIV);
        keywords.put("Array", ARRAY);
        keywords.put("Int", INT);
        keywords.put("Double", DOUBLE);
        keywords.put("Str", STR);
        keywords.put("start", START);
        keywords.put("void", VOID);

    }
    /**
     * Constructor para la clase {@code Escaner}.
     *
     * @param buffer El buffer que contiene el código fuente a escanear.
     */

    Escaner(String buffer) {
        this.buffer = buffer;
    }

    /**
     * Constructor para la clase {@code Escaner} que recibe un buffer y un lector de caracteres.
     *
     * @param buffer   El buffer que contiene el código fuente.
     * @param lectorCF El lector de caracteres para recargar el buffer.
     */

    Escaner(String buffer, LectorCF lectorCF) {
        this.buffer = buffer;
        this.lectorCF = lectorCF;
    }

    /**
     * Constructor para la clase {@code Escaner} que recibe un lector de caracteres.
     *
     * @param lectorCF El lector de caracteres para recargar el buffer.
     */

    Escaner(LectorCF lectorCF) {
        this.lectorCF = lectorCF;
    }

    /**
     * Constructor vacío para la clase {@code Escaner}.
     */

    public Escaner() {}

    /**
     * Establece el lector de caracteres para el escáner.
     *
     * @param lectorCF El lector de caracteres a establecer.
     */

    public void setEscaner(LectorCF lectorCF) {
        this.lectorCF = lectorCF;
    }

    /**
     * Establece el buffer para el escáner.
     *
     * @param buffer El buffer a establecer.
     */

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    /**
     * Verifica si se ha llegado al final del buffer.
     *
     * @return {@code true} si se ha llegado al final, {@code false} de lo contrario.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private boolean isAtEnd() throws ErrorLex, IOException {
        //return current >= source.length();
        if (current >= buffer.length()) {
            if (lectorCF != null && !lectorCF.isReachedEOF()) {
                buffer = lectorCF.rechargeBuffer();
            } else {
                return true;
            }
        }
        return current >= buffer.length();
    }

    /**
     * Obtiene el siguiente token del buffer.
     *
     * @return El siguiente token encontrado.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    public Token nextToken() throws ErrorLex, IOException {
        char c = advance();
        switch (c) {
            case '\0':
                start = current;
                return addToken(EOF);

            case '(':
                start = current - 1;
                return addToken(LEFT_PAREN);

            case ')':
                start = current - 1;
                return addToken(RIGHT_PAREN);

            case ',':
                start = current - 1;
                return addToken(COMMA);

            case '.':
                start = current - 1;
                return addToken(DOT);

            case ':':
                start = current - 1;
                return addToken(DOBLE_DOT);

            case '*':
                start = current - 1;
                return addToken(MULT);

            case '%':
                start = current - 1;
                return addToken(PERCENTAGE);

            case ';':
                start = current - 1;
                return addToken(SEMICOLON);

            case '{':
                start = current - 1;
                return addToken(LEFT_BRACE);

            case '}':
                start = current - 1;
                return addToken(RIGHT_BRACE);

            case '[':
                start = current - 1;
                return addToken(LEFT_BRACKET);

            case ']':
                start = current - 1;
                return addToken(RIGHT_BRACKET);

            case '+':
                start = current - 1;
                return addToken(nextMatch('+') ? PLUS_PLUS : PLUS);

            case '-':
                start = current - 1;
                return addToken(nextMatch('-') ? MINUS_MINUS : MINUS);

            case '!':
                start = current - 1;
                return addToken(nextMatch('=') ? NOT_EQUAL : NOT);

            case '=':
                start = current - 1;
                return addToken(nextMatch('=') ? EQUAL_EQUAL : EQUAL);

            case '>':
                start = current - 1;
                return addToken(nextMatch('=') ? GREATER_EQUAL : GREATER);

            case '<':
                start = current - 1;
                return addToken(nextMatch('=') ? LESS_EQUAL : LESS);

            case '/':
                //Puede ser un comentario:
                if (nextMatch('/')) {
                    //Pasamos de largo el comentario
                    while (look() != '\n' && !isAtEnd()) advance();
                    column = 0;

                    return nextToken();
                } else {
                    if (nextMatch('*')) {
                        //Pasamos de largo el comentario
                        char caracter;

                        while (true) {
                            caracter = advance();

                            if (caracter == '*') {
                                caracter = advance();


                                if (caracter == '/') {

                                    break;
                                }
                            }
                            column++;
                            if (caracter == '\n') {
                                line++;
                                column = 0;
                            }
                            if (isAtEnd()) {

                                throw new ComentarioSinCerrarError(line, column, String.valueOf(c));
                                //throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);
                            }

                        }
                        return nextToken();
                    } else {

                        start = current - 1;
                        return addToken(SLASH);
                    }
                }

            case '|':
                //Puede ser un comentario:
                if (nextMatch('|')) {
                    //Pasamos de largo el comentario
                    return addToken(OR);
                } else {
                    throw new CaracterInvalidoError(line, column, String.valueOf(c));
                    //throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);
                }

            case '&':
                //Puede ser un comentario:
                if (nextMatch('&')) {
                    //Pasamos de largo el comentario
                    return addToken(AND);
                } else {
                    throw new CaracterInvalidoError(line, column, String.valueOf(c));
                }
                //literales cadenas
            case '"':
                start = current - 1;
                return string();


            case ' ', '\t', '\r':
                return nextToken();
            case '\n':
                column = 0;
                line++;
                return nextToken();
            //case '\v': No lo toma
            //
        }
        if (isDigit(c)) {
            return number();
        } else {
            if (isAlpha(c)) {
                return identifier(c);
            } else {
                throw new CaracterInvalidoError(line, column, String.valueOf(c));
                //throw new IOException("CARACTER INVALIDO en línea " + line + ", columna " + column);
            }

        }

    }

    /**
     * Avanza al próximo caracter en el buffer.
     *
     * @return El caracter al que se avanzó, o '\0' si se alcanzó el final del archivo.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private char advance() throws ErrorLex, IOException {

        if (isAtEnd()) return '\0';
        column++;
        return buffer.charAt(current++);

    }

    /**
     * Observa el siguiente caracter en el buffer sin consumirlo.
     *
     * @return El siguiente caracter en el buffer, o '\0' si se alcanzó el final del archivo.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private char look() throws ErrorLex, IOException {
        if (isAtEnd()) return '\0';
        return buffer.charAt(current);
    }
    /**
    * Agrega un token a la lista de tokens.
    *
    * @param type El tipo de token a agregar.
    * @return el token creado
     */
    private Token addToken(TokenType type){
        String text = buffer.substring(start,current);
        //tokens.add(token);
        return new Token(type,text,column,line);
    }

    /**
     * Verifica si el siguiente caracter coincide con el esperado y lo consume.
     *
     * @param expected El caracter esperado.
     * @return {@code true} si coincide, {@code false} de lo contrario.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private boolean nextMatch(char expected) throws ErrorLex, IOException {
        if (isAtEnd()) return false;
        if (buffer.charAt(current) != expected) return false;
        column++;
        current++;
        return true;
    }

    /**
     * Procesa un literal de cadena entre comillas dobles del código fuente.
     * El metodo identifica el inicio y el final de un literal de cadena y maneja
     * posibles errores relacionados con definiciones de cadena no válidas o no cerradas.
     *
     * <p>Comportamiento:
     * - Itera a través de los caracteres dentro de un literal de cadena, asegurándose de que termine
     *   con una comilla doble de cierre.
     * - Realiza un seguimiento de los números de línea y los incrementa si se encuentra un carácter de nueva línea
     *   dentro del literal de cadena.
     * - Si se alcanza un final inesperado del código fuente antes de cerrar el
     *   cadena, se informa de un error.
     * - Al cierre adecuado, extrae el valor de la cadena, elimina las comillas circundantes,
     *   y crea un token de cadena correspondiente.
     *
     * <p>Condiciones previas:
     * - Se llama al encontrar un carácter '"' del código fuente durante
     *   escaneo.
     *
     * <p>Condiciones posteriores:
     * - Si se analiza correctamente, agrega un token `STRING` con su valor extraído
     *   a la colección de tokens.
     * - Si el análisis falla (por ejemplo, debido a una cadena sin cerrar), registra un error y
     *   no genera un token.
     * @return el token creado
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private Token string() throws ErrorLex, IOException {
        while (look() != '"' && !isAtEnd()) {
            if (look() == '\n') {
                line++;
                column = 0;
            }
            advance();
        }
        if (isAtEnd()) {
            throw new StringSinCerrarError(line, column, buffer.substring(start, current));
        }

        advance();

        return addToken(STRING_LITERAL);
    }

    /**
     * Verifica si un caracter es un dígito.
     *
     * @param c El caracter a verificar.
     * @return {@code true} si es un dígito, {@code false} de lo contrario.
     */

    private  boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    /**
     * Procesa un literal numérico (entero o decimal) del código fuente.
     * Identifica la secuencia de dígitos y el punto decimal (si existe) para
     * construir el token numérico apropiado.
     *
     * <p>Comportamiento:
     * - Avanza a través de los caracteres mientras sean dígitos.
     * - Si encuentra un punto decimal '.', verifica que le siga otro dígito.
     *   Si no es así, lanza un error indicando un formato de número decimal inválido.
     * - Si encuentra caracteres alfabéticos después de los dígitos (o del punto decimal),
     *   lanza un error indicando un carácter inválido.
     * - Determina si el número es entero o decimal basándose en la presencia del punto decimal
     *   y crea el token correspondiente (INTEGER_LITERAL o DOUBLE_LITERAL).
     *
     * <p>Condiciones previas:
     * - El metodo se llama al encontrar un dígito durante el análisis léxico.
     *
     * <p>Condiciones posteriores:
     * - Si se analiza correctamente, agrega un token INTEGER_LITERAL o DOUBLE_LITERAL
     *   con el valor numérico extraído.
     * - Si el análisis falla debido a un formato incorrecto, lanza una excepción ErrorLex.
     * @return el token creado
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private Token number() throws ErrorLex, IOException {
        start = current-1;
        boolean isDouble = false;
        while (isDigit(look())) advance();

        //Buscar la parte decimal

        if(look() == '.'){
            if (isDigit(lookNext())) {
                advance();
                isDouble = true;
            }else {
                throw new DoubleInvalidoError(line, column, buffer.substring(start + 1, current - 1));
            }
        }
        if(isAlpha(look())) {

            throw new CaracterInvalidoError(line, column, buffer.substring(start + 1, current - 1));
        }

        while (isDigit(look())) advance();
        if (isDouble){
            return addToken(DOUBLE_LITERAL);
        }
        else{
            return addToken(INTEGER_LITERAL);
        }

    }

    /**
     * Observa el siguiente caracter en el buffer después del actual sin consumirlo.
     *
     * @return El siguiente caracter en el buffer después del actual, o '\0' si se alcanzó el final del archivo.
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private char lookNext() throws ErrorLex, IOException {
        if (current + 1 >= buffer.length()) {
            if (lectorCF != null && !lectorCF.isReachedEOF()) {
                return '\0';
            }
            return '\0';
        }
        return buffer.charAt(current+1);
    }
    /**
     * Verifica si un caracter es una letra mayúscula.
     *
     * @param c El caracter a verificar.
     * @return {@code true} si es una letra mayúscula, {@code false} de lo contrario.
     */


    private boolean isAlphaCapital(char c){
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Verifica si un caracter es una letra minúscula.
     *
     * @param c El caracter a verificar.
     * @return {@code true} si es una letra minúscula, {@code false} de lo contrario.
     */

    private boolean isAlphaLower(char c){
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Verifica si un caracter es una letra (mayúscula o minúscula).
     *
     * @param c El caracter a verificar.
     * @return {@code true} si es una letra, {@code false} de lo contrario.
     */

    private boolean isAlpha(char c){
        return isAlphaCapital(c) ||
                isAlphaLower(c);
    }

    /**
     * Verifica si un caracter es alfanumérico (letra o dígito) o un guión bajo.
     *
     * @param c El caracter a verificar.
     * @return {@code true} si es alfanumérico o guión bajo, {@code false} de lo contrario.
     */

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) ||
                isDigit(c) ||
                c == '_';
    }

    /**
     * Procesa un identificador (palabra clave o nombre de variable/función) del código fuente.
     * Determina si el identificador es una palabra clave reservada o un identificador definido por el usuario
     * (IDCLASS para nombres de clases, IDOBJETS para nombres de objetos/variables).
     *
     * <p>Comportamiento:
     * - Avanza a través de los caracteres mientras sean alfanuméricos o guiones bajos.
     * - Verifica si el identificador coincide con alguna de las palabras clave reservadas
     *   definidas en el mapa `keywords`. Si es así, crea un token con el tipo de token correspondiente
     *   a la palabra clave.
     * - Si no es una palabra clave reservada, determina si es un IDCLASS (comienza con mayúscula)
     *   o un IDOBJETS (comienza con minúscula) y crea el token correspondiente.
     *
     * <p>Condiciones previas:
     * - El metodo se llama al encontrar un caracter alfabético durante el análisis léxico.
     *
     * <p>Condiciones posteriores:
     * - Agrega un token con el tipo de token apropiado (palabra clave, IDCLASS o IDOBJETS).
     * @param c el caracter actual
     * @return el token creado
     * @throws ErrorLex Si ocurre un error léxico.
     * @throws IOException Si ocurre un error de entrada/salida.
     */

    private Token identifier(char c) throws ErrorLex, IOException {
        start=current-1;
        boolean identificadorTipo = isAlphaCapital(c);

        while (isAlphaNumeric(look())){
            advance();
        }

        String text = buffer.substring(start,current);

        if (keywords.containsKey(text)){
            return addToken(keywords.get(text));
        }else{
            if (identificadorTipo){
                if (!isAlpha(text.charAt(text.length() -1))){
                    throw new IdentificadorInvalidoError(line, column, text);
                }
                return addToken(IDCLASS);
            }else{
                return addToken(IDOBJETS);
            }
        }
    }



}

