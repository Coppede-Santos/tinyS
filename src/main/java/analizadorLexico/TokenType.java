package analizadorLexico;
/**
 * La enumeración {@code TokenType} define los diferentes tipos de tokens léxicos
 * que pueden ser identificados durante el análisis léxico.
 */
public enum TokenType {
    /**
     * Identificador de clase.
     */
    IDCLASS,
    /**
     * Identificador de objeto.
     */
    IDOBJETS,

    //Clases predefinidas
    /**
     * Tipo de dato array.
     */
    ARRAY,
    /**
     * Tipo de dato cadena (string).
     */
    STR,
    /**
     * Tipo de dato entero (integer).
     */
    INT,
    /**
     * Tipo de dato booleano (boolean).
     */
    DOUBLE,
    /**
     * Tipo de dato booleano (boolean).
     */
    BOOL,
    /**
     * Palabra clave "start".
     */
    START,
    //Literales
    /**
     * Literal entero.
     */
    INTEGER_LITERAL,
    /**
     * Literal de punto flotante (double).
     */
    DOUBLE_LITERAL,
    /**
     * Literal de cadena (string).
     */
    STRING_LITERAL,
    /**
     * palabra clave "nil".
     */
    //Palabras clave
    NIL,
    /**
     * Palabra clave "impl".
     */
    IMPL,
    /**
     * Palabra clave "else".
     */
    ELSE,
    /**
     * Palabra clave "false".
     */
    FALSE,
    /**
     * Palabra clave "if".
     */
    IF,
    /**
     * Palabra clave "ret".
     */
    RET,
    /**
     * Palabra clave "while".
     */
    WHILE,
    /**
     * Palabra clave "true".
     */
    TRUE,
    /**
     * Palabra clave "new".
     */
    NEW,
    /**
     * Palabra clave "fn".
     */
    FN,
    /**
     * Palabra clave "st".
     */
    ST,
    /**
     * Palabra clave "pub".
     */
    PUB,
    /**
     * Palabra clave "self".
     */
    SELF,
    /**
     * Palabra clave "div".
     */
    DIV,
    /**
     * Palabra clave "class".
     */
    CLASS,
    /**
     * Palabra clave "void".
     */
    VOID,
    //Tokens simbolos
    // (            )          ,     .    +      /     -      {              }            ;           [             ]           *       :
    /**
     * Paréntesis izquierdo.
     */
    LEFT_PAREN,
    /**
     * Paréntesis derecho.
     */
    RIGHT_PAREN,
    /**
     * Coma.
     */
    COMMA,
    /**
     * Punto.
     */
    DOT,
    /**
     * Signo de suma.
     */
    PLUS,
    /**
     * Barra (división).
     */
    SLASH,
    /**
     * Signo de resta.
     */
    MINUS,
    /**
     * Llave izquierda.
     */
    LEFT_BRACE,
    /**
     * Llave derecha.
     */
    RIGHT_BRACE,
    /**
     * Punto y coma.
     */
    SEMICOLON,
    /**
     * Corchete izquierdo.
     */
    LEFT_BRACKET,
    /**
     * Corchete derecho.
     */
    RIGHT_BRACKET,
    /**
     * Asterisco (multiplicación).
     */
    MULT,
    /**
     * Doble punto.
     */
    DOBLE_DOT,
    //Tokens operaciones
    // !   !=         =        ==         >           >=       <       <=        ++          --
    /**
     * Operador "not".
     */
    NOT,
    /**
     * Operador "not equal".
     */
    NOT_EQUAL,
    /**
     * Operador de asignación "equal".
     */
    EQUAL,
    /**
     * Operador "equal equal".
     */
    EQUAL_EQUAL,
    /**
     * Operador "greater than".
     */
    GREATER,
    /**
     * Operador "greater than or equal".
     */
    GREATER_EQUAL,
    /**
     * Operador "less than".
     */
    LESS,
    /**
     * Operador "less than or equal".
     */
    LESS_EQUAL,
    /**
     * Operador "plus plus" (incremento).
     */
    PLUS_PLUS,
    /**
     * Operador "minus minus" (decremento).
     */
    MINUS_MINUS,
    /**
     * Operador "and".
     */
    AND,
    /**
     * Operador "or".
     */
    OR,
    /**
     * Operador "percentage".
     */
    PERCENTAGE,
    //Finalizar
    /**
     * Fin de archivo.
     */
    EOF
}
