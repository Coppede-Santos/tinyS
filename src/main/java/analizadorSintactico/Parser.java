package analizadorSintactico;

import ErrorManage.ErrorTiny;
import analizadorLexico.Escaner;
import analizadorLexico.Token;
import analizadorLexico.TokenType;
import analizadorSintactico.Errores.MacheoIncorrectoError;
import analizadorSintactico.Errores.TokenInesperadoError;
import analizadorSintactico.Errores.TokenInesperadoError;

import java.io.IOException;

import static analizadorLexico.TokenType.*;

/**
 * Clase que implementa el analizador sintáctico para el lenguaje TinyS.
 *
 * El analizador sintáctico (Parser) toma como entrada el flujo de tokens
 * producido por el analizador léxico (Scanner) y verifica si la secuencia
 * de tokens se ajusta a la gramática del lenguaje TinyS.
 */

public class Parser {

    private Token currentToken;
    private Escaner escaner;
    private Token lookaheadToken;
    private boolean hasLookahead = false;

    /**
     * Establece el escáner que proporcionará los tokens al parser.
     *
     * @param escaner El escáner a utilizar.
     */

    public void setEscaner(Escaner escaner){
        this.escaner = escaner;
    }

    /**
     * Establece el token actual.
     *
     * @param currentToken El token actual.
     */
    public void setCurrentToken (Token currentToken) {
        this.currentToken = currentToken;
    }
    /**
     * Comprueba si el token actual coincide con el tipo de token esperado y avanza al siguiente token.
     *
     * @param tokenType El tipo de token esperado.
     * @throws IOException  Si ocurre un error de E/S durante la lectura del siguiente token.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    public void macheo(TokenType tokenType) throws IOException, ErrorTiny {
        if (currentToken.getType() == tokenType){
            if (hasLookahead) {
                currentToken = lookaheadToken;
                hasLookahead = false;
            } else {
                currentToken = escaner.nextToken();
            }
        }else{
            throw new MacheoIncorrectoError(currentToken.getLine(),currentToken.getColumn(),tokenType.toString(), currentToken.getLexema());
        }
    }

    /**
     * Lee el siguiente token sin consumirlo.
     *
     * @throws IOException Si ocurre un error de E/S durante la lectura del siguiente token.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void peekToken() throws IOException, ErrorTiny {

        if (!hasLookahead) {
            lookaheadToken = escaner.nextToken();
            hasLookahead = true;
        }
    }

    /**
     * Implementa la regla de producción para el símbolo inicial 's' de la gramática.
     *
     * @return true si la regla se aplica con éxito, false en caso contrario.
     * @throws ErrorTiny Si se encuentra un error léxico.
     * @throws IOException Si ocurre un error de E/S.
     */

    public boolean s() throws ErrorTiny, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL || type == START){
            program();
            macheo(EOF);
            return true;
        }
        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una clase o una implementación", currentToken. getLexema());
    }

    /**
     * Implementa la regla de producción para 'program' de la gramática.
     *
     * @throws ErrorTiny Si se encuentra un error léxico.
     * @throws IOException Si ocurre un error de E/S.
     */

    private void program() throws ErrorTiny, IOException {
        TokenType type = currentToken.getType();
        if (type == CLASS || type == IMPL || type == START){
            lista_definiciones();
            start();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definicion de una clase o implementación", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'start' de la gramática.
     *
     * @throws ErrorTiny Si se encuentra un error léxico.
     * @throws IOException Si ocurre un error de E/S.
     */

    private void start() throws ErrorTiny, IOException {
        if (currentToken.getType() == START){
            macheo(START);
            bloque_metodo();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un metodo start", currentToken. getLexema());
        }
    }


    /**
     * Implementa la regla de producción para 'lista_definiciones' de la gramática.
     *
     * @throws ErrorTiny Si se encuentra un error léxico.
     * @throws IOException Si ocurre un error de E/S.
     */

    private void lista_definiciones() throws ErrorTiny, IOException {
        TokenType type = currentToken.getType();
        if (type == START){
            return;
        }else{
            if (type == CLASS) {
                class_lista_recursivo();
            }else{
                    if (type == IMPL){
                        impl_lista_recursivo();
                    }else{
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una clase o una implementación", currentToken. getLexema());
                    }
            }
        }

    }

    /**
     * Implementa la regla de producción recursiva para 'class_lista_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void class_lista_recursivo() throws IOException, ErrorTiny {
        if (currentToken.getType() == CLASS){
            clas();
            lista_factorizacion();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una clase", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción recursiva para 'impl_lista_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void impl_lista_recursivo() throws IOException, ErrorTiny {
        if(currentToken.getType()==IMPL){
            impl();
            lista_factorizacion();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una implementación", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_factorizacion' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_factorizacion() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == CLASS ){
            class_lista_recursivo();
        }else{
            if(type == IMPL){
                impl_lista_recursivo();
            }else{
                if(type == START){
                    return;
                }else{
                    throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una clase o una implementación", currentToken. getLexema());
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'clas' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void clas() throws IOException, ErrorTiny {
        if(currentToken.getType()==CLASS){
            macheo(CLASS);
            macheo(IDCLASS);
            clas_factorizado();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una clase", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'clas_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void clas_factorizado() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            atributo_class_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            if(type == DOBLE_DOT ){
                herencia();
                macheo(LEFT_BRACE);
                atributo_class_recursivo();
                macheo(RIGHT_BRACE);
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un bloque o herencia", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción recursiva para 'atributo_class_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void atributo_class_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();

        if(type == IDCLASS || type == PUB || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            atributo();
            atributo_class_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la asignacion de un tipo valido o un bloque", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'impl' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void impl() throws IOException, ErrorTiny {
        if(currentToken.getType() == IMPL){
            macheo(IMPL);
            macheo(IDCLASS);
            macheo(LEFT_BRACE);
            miembro();
            miembro_impl_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"la definición de una implementación", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción recursiva para 'miembro_impl_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void miembro_impl_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == FN || type == ST || type == DOT){
            miembro();
            miembro_impl_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una firma para metodo o constructor, o cerrar implementación", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'herencia' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void herencia() throws IOException, ErrorTiny {
        if(currentToken.getType() == DOBLE_DOT){
            macheo(DOBLE_DOT);
            tipo();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una herencia", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'miembro' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void miembro() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == FN|| type == ST){
            metodo();
        }else{
            if(type == DOT){
                constructor();
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"else", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'constructor' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void constructor() throws IOException, ErrorTiny {
        if (currentToken.getType()==DOT){
            macheo(DOT);
            argumentos_formales();
            bloque_metodo();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un constructor", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'atributo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void atributo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo();
            lista_declaraciones_variables();
            macheo(SEMICOLON);
        }else{
            if(type == PUB){
                visibilidad();
                tipo();
                lista_declaraciones_variables();
                macheo(SEMICOLON);
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un atributo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'metodo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void metodo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == FN){
            macheo(FN);
            tipo_metodo_factorizacion();
            macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos
            argumentos_formales();
            bloque_metodo();
        }else{
            if(type == ST ){
                forma_metodo();
                macheo(FN);
                tipo_metodo_factorizacion();
                macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos
                argumentos_formales();
                bloque_metodo();
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un metodo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'tipo_metodo_factorizacion' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void tipo_metodo_factorizacion() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == VOID || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){
            tipo_metodo();
        }else{
            if(type == IDOBJETS){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo de metodo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'visibilidad' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void visibilidad() throws IOException, ErrorTiny {
        if (currentToken.getType() == PUB){
            macheo(PUB);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"tipo de visibilidad", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'forma_metodo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void forma_metodo() throws IOException, ErrorTiny {
        if (currentToken.getType() == ST){
            macheo(ST);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una forma de metodo", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'bloque_metodo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void bloque_metodo() throws IOException, ErrorTiny {
        if(currentToken.getType()==LEFT_BRACE){
            macheo(LEFT_BRACE);
            decl_var_loc_bloque_recursivo();
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un bloque", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción recursiva para 'decl_var_loc_bloque_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void decl_var_loc_bloque_recursivo() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();

        if(type == IDCLASS  || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY ){
            decl_var_locales();
            decl_var_loc_bloque_recursivo();
        }else{
            if(type == LEFT_BRACE || type == RIGHT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type == IF || type == WHILE || type == RET || type == IDOBJETS || type == PUB || type == NEW || type == FN || type == ST || type == DOT || type == SELF){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una declaracion de variables locales o un bloque", currentToken. getLexema());
            }
        }
    }


    /**
     * Implementa la regla de producción recursiva para 'sentencia_bloque_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */
//corroborar id
    private void sentencia_bloque_recursivo() throws IOException, ErrorTiny {
      
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type== IF || type == WHILE || type == RET || type == IDOBJETS || type == SELF){
            sentencia();
            sentencia_bloque_recursivo();
        }else{
            if(type == RIGHT_BRACE){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una sentencia o cierre de bloque", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'decl_var_locales' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void decl_var_locales() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            tipo();
            lista_declaraciones_variables();
            macheo(SEMICOLON);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una declaracion de variables locales", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_declaraciones_variables' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_declaraciones_variables() throws IOException, ErrorTiny {
        if(currentToken.getType()==IDOBJETS){
            macheo(IDOBJETS);
            lista_declaraciones_variables_prima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un nombre de variable", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_declaraciones_variables_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_declaraciones_variables_prima() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == COMMA){
            macheo(COMMA);
            lista_declaraciones_variables();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"mas declaraciones de variables locales o un punto y coma", currentToken. getLexema());
            }
        }
    }

    private void argumentos_formales() throws IOException, ErrorTiny {
        if(currentToken.getType()==LEFT_PAREN){
            macheo(LEFT_PAREN);
            lista_argumentos_formales_factorizado();
            macheo(RIGHT_PAREN);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de argumentos formales", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_argumentos_formales_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_argumentos_formales_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            lista_argumentos_formales();
        }else{
            if(type == RIGHT_PAREN){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de argumentos formales", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'lista_argumentos_formales' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_argumentos_formales() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            argumento_formal();
            lista_argumentos_formales_prima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"el tipo de un argumento formal", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_argumentos_formales_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_argumentos_formales_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==COMMA){
            macheo(COMMA);
            lista_argumentos_formales();
        }else{
            if(type==RIGHT_PAREN){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de argumentos formales", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'argumento_formal' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void argumento_formal() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            tipo();
            macheo(IDOBJETS);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"el tipo de un argumento formal", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'tipo_metodo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */


    private void tipo_metodo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            tipo();
        }else{
            if (type == VOID){
                macheo(VOID);
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo de metodo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'tipo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */


    private void tipo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == STR || type == BOOL || type == INT || type == DOUBLE){
            tipo_primitivo();
        }else{
            if(type == IDCLASS){
                tipo_referencia();
            }else{
                if(type == ARRAY){
                    tipo_arreglo();
                }else{
                    throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo", currentToken. getLexema());
                }
            }
        }
    }


    /**
     * Implementa la regla de producción para 'tipo_primitivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */


    private void tipo_primitivo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==STR){
            macheo(STR);
        }else{
            if(type==BOOL){
                macheo(BOOL);
            }else{
                if(type==INT){
                    macheo(INT);
                }else{
                    if(type==DOUBLE){
                        macheo(DOUBLE);
                    }else{
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo de primitivo", currentToken. getLexema());
                    }
                }
            }
        }
    }
    /**
     * Implementa la regla de producción para 'tipo_referencia' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void tipo_referencia() throws IOException, ErrorTiny{
        if(currentToken.getType()==IDCLASS){
            macheo(IDCLASS);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo de referencia", currentToken. getLexema());
        }
    }

    private void tipo_arreglo() throws IOException, ErrorTiny{
        if(currentToken.getType() == ARRAY){
            macheo(ARRAY);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo arreglo", currentToken. getLexema());
        }
    }
// corroborar asignacion ID
    /**
     * Implementa la regla de producción para 'sentencia' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void sentencia() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type == IF){
            macheo(IF);
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
            sentencia();
            sentencia_else();
        }else{
            if(type==WHILE){
                macheo(WHILE);
                macheo(LEFT_PAREN);
                expOr();
                macheo(RIGHT_PAREN);
                sentencia();
            }else{
                if(type==RET){
                    macheo(RET);
                    ExpOr_factorizado();
                    macheo(SEMICOLON);
                }else{
                    if(type==IDOBJETS || type==SELF){
                        asignacion();
                        macheo(SEMICOLON);
                    }else{
                        if(type==LEFT_PAREN){
                           sentencia_simple();
                           macheo(SEMICOLON);
                        }else{
                            if(type==LEFT_BRACE){
                                bloque();
                            }else{
                                if(type==SEMICOLON){
                                    macheo(SEMICOLON);
                                }else{
                                    throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una sentencia", currentToken. getLexema());
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Implementa la regla de producción para 'ExpOr_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void ExpOr_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type==IDCLASS || type==IDOBJETS || type==PLUS || type==MINUS || type==NOT || type==PLUS_PLUS || type==MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == STRING_LITERAL || type==SELF || type == NEW || type == LEFT_PAREN){
            expOr();
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'sentencia_else' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void sentencia_else()throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == ELSE){
            macheo(ELSE);
            sentencia();
        }else{
            if( type == RIGHT_BRACE || type == SEMICOLON || type == IDOBJETS || type== SELF || type == LEFT_PAREN
                    || type == IF || type == WHILE || type == LEFT_BRACE || type == RET){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una sentencia else, una nueva sentencia o cerrar el bloque", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'bloque' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void bloque() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            sentencia_bloque_recursivo();
            macheo(RIGHT_BRACE);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un bloque", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'asignacion' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void asignacion() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS ){
            accesoVar_simple();
            macheo(EQUAL);
            expOr();
        }else{
            if (type == SELF){
                accesoSelf_simple();
                macheo(EQUAL);
                expOr();
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una asignacion", currentToken. getLexema());
            }
        }
    }


    /**
     * Implementa la regla de producción para 'accesoVar_simple' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void accesoVar_simple() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type == IDOBJETS){
            macheo(IDOBJETS);
            accesoVar_simple_prima();

        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'accesoVar_simple_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void accesoVar_simple_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == EQUAL){
            encadenado_simple_recursivo();
        }else{
            if(type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción recursiva para 'encadenado_simple_recursivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void encadenado_simple_recursivo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT){
            encadeado_simple();
            encadenado_simple_recursivo();
        }else{
            if(type == EQUAL){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'accesoSelf_simple' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void accesoSelf_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF){
            macheo(SELF);
            encadenado_simple_recursivo();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable self", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'encadeado_simple' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void encadeado_simple() throws IOException, ErrorTiny{
        if(currentToken.getType() == DOT){
            macheo(DOT);
            macheo(IDOBJETS);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'sentencia_simple' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void sentencia_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una sentencia simple", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expOr' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expOr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAnd();
            expOrPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expOrPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expOrPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == OR) {
            macheo(OR);
            expAnd();
            expOrPrima();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expAnd' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expAnd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expIgual();
            expAndPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expAndPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expAndPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == AND) {
            macheo(AND);
            expIgual();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),
                        "una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expIgual' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expIgual() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expCompuesta();
            expIgualPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expIgualPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expIgualPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == EQUAL_EQUAL || type == NOT_EQUAL) {
            opIgual();
            expCompuesta();
            expIgualPrima();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expCompuesta' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expCompuesta() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expAd();
            expCompuestaPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expCompuestaPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expCompuestaPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
            opCompuesta();
            expAd();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expAd' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expAd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expMul();
            expAdPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expAdPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expAdPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == PLUS || type == MINUS) {
            opAd();
            expMul();
            expAdPrima();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expMul' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expMul() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            expUn();
            expMulPrima();
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expMulPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expMulPrima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == MULT || type == SLASH || type == DIV || type == PERCENTAGE) {
            opMul();
            expUn();
            expMulPrima();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expUn' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expUn() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == LEFT_PAREN) {
            peekToken();
            if (lookaheadToken.getType() == INT){
                opUnario();
                expUn();
            }else {
                operando();
            }
        } else {
            if (type == IDCLASS || type == IDOBJETS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW) {
                operando();
            } else {
                if (type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS) {
                    opUnario();
                    expUn();
                } else {
                    throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion", currentToken. getLexema());
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'opIgual' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void opIgual() throws IOException, ErrorTiny{
        if (currentToken.getType() == EQUAL_EQUAL){
            macheo(EQUAL_EQUAL);
        }else {
            if (currentToken.getType() == NOT_EQUAL) {
                macheo(NOT_EQUAL);
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operador de igualdad", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'opAd' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void opAd() throws IOException, ErrorTiny{
        if (currentToken.getType() == PLUS){
            macheo(PLUS);
        }else {
            if (currentToken.getType() == MINUS) {
                macheo(MINUS);
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operador aditivo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'opCompuesta' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void opCompuesta() throws IOException, ErrorTiny{
        if (currentToken.getType() == GREATER){
            macheo(GREATER);
        }else {
            if (currentToken.getType() == GREATER_EQUAL) {
                macheo(GREATER_EQUAL);
            }else{
                if (currentToken.getType() == LESS) {
                    macheo(LESS);
                }else {
                    if (currentToken.getType() == LESS_EQUAL) {
                        macheo(LESS_EQUAL);
                    } else {
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operador relacional", currentToken. getLexema());
                    }
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'opUnario' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void opUnario() throws IOException, ErrorTiny{
        if (currentToken.getType() == PLUS){
            macheo(PLUS);
        }else {
            if (currentToken.getType() == MINUS) {
                macheo(MINUS);
            }else{
                if (currentToken.getType() == NOT) {
                    macheo(NOT);
                }else {
                    if (currentToken.getType() == PLUS_PLUS) {
                        macheo(PLUS_PLUS);
                    } else {
                        if (currentToken.getType() == MINUS_MINUS) {
                            macheo(MINUS_MINUS);
                        } else {
                            if (currentToken.getType() == LEFT_BRACE) {
                                macheo(LEFT_BRACE);
                                macheo(INT);
                                macheo(RIGHT_PAREN);
                            } else {
                                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operador unario", currentToken. getLexema());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'opMul' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void opMul() throws IOException, ErrorTiny{
        if (currentToken.getType() == MULT){
            macheo(MULT);
        }else {
            if (currentToken.getType() == SLASH) {
                macheo(SLASH);
            }else{
                if (currentToken.getType() == PERCENTAGE) {
                    macheo(PERCENTAGE);
                }else {
                    if (currentToken.getType() == DIV) {
                        macheo(DIV);
                    } else {
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operador multiplicativo", currentToken. getLexema());
                    }
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'operando' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void operando() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == LEFT_PAREN  || type == SELF || type == NEW){
            primario();
            encadenado_factorizado();
        }else{
            if (type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL){
                literal();
            }else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un operando", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'encadenado_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void encadenado_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            encadenado();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == RIGHT_BRACKET || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'literal' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void literal() throws IOException, ErrorTiny{
        if (currentToken.getType() == NIL){
            macheo(NIL);
        }else {
            if (currentToken.getType() == TRUE) {
                macheo(TRUE);
            }else{
                if (currentToken.getType() == FALSE) {
                    macheo(FALSE);
                }else {
                    if (currentToken.getType() == INTEGER_LITERAL) {
                        macheo(INTEGER_LITERAL);
                    } else {
                        if (currentToken.getType() == STRING_LITERAL) {
                            macheo(STRING_LITERAL);
                        } else {
                            if (currentToken.getType() == DOUBLE_LITERAL) {
                                macheo(DOUBLE_LITERAL);
                            } else {
                                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un literal", currentToken. getLexema());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'primario' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void primario() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            expresionParentizada();
        }else{
            if(type==SELF){
                accesoSelf();
            }else{
                if(type==IDOBJETS){
                    macheo(IDOBJETS);
                    id_factor();
                }else{
                    if(type==IDCLASS){
                        llamada_metodo_estatico();
                    }else{
                        if(type==NEW){
                            llamada_conclasor();
                        }else{
                            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresión primaria (expresión entre paréntesis, acceso a variable, 'self', llamada a método o constructor)", currentToken. getLexema());
                        }
                    }
                }
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expresionParentizada' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void expresionParentizada() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            macheo(LEFT_PAREN);
            expOr();
            macheo(RIGHT_PAREN);
            encadenado_factorizado();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion parentizada", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'accesoSelf' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void accesoSelf() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF ) {
            macheo(SELF);
            encadenado_factorizado();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a self", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'accesoVar_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void accesoVar_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA  || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == DIV || type == PERCENTAGE){
            encadenado_factorizado();
        }else{
            if (type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
                encadenado_factorizado();
            }else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'llamada_metodo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void llamada_metodo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            argumentos_actuales();
            encadenado_factorizado();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una llamada a metodo", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'llamada_metodo_estatico' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void llamada_metodo_estatico() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS ) {
            macheo(IDCLASS);
            macheo(DOT);
            macheo(IDOBJETS);
            llamada_metodo();
            encadenado_factorizado();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una llamada a metodo estatico", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'llamada_conclasor' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void llamada_conclasor() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == NEW ) {
            macheo(NEW);
            llamada_conclasor_prima();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una llamada a constructor", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'llamada_conclasor_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void llamada_conclasor_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS){
            macheo(IDCLASS);
            argumentos_actuales();
            encadenado_factorizado();
        }else{
            if (type == STR || type == DOUBLE || type == INT || type == BOOL){
                tipo_primitivo();
                macheo(LEFT_BRACKET);
                expOr();
                macheo(RIGHT_BRACKET);
            }else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una llamada a constructor", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'argumentos_actuales' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void argumentos_actuales() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN) {
            macheo(LEFT_PAREN);
            lista_expresiones_factorizado();
            macheo(RIGHT_PAREN);
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de argumentos", currentToken.getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_expresiones_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_expresiones_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            lista_expresiones();
        }else {
            if (type == RIGHT_PAREN) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de argumentos", currentToken. getLexema());
            }
        }
    }
    /**
     * Implementa la regla de producción para 'lista_expresiones' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    /**
     * Implementa la regla de producción para 'lista_expresiones' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_expresiones() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            expOr();
            lista_expresiones_prima();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de expresiones", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_expresiones_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_expresiones_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == COMMA) {
            macheo(COMMA);
            lista_expresiones();
        }else {
            if (type == RIGHT_PAREN) {
                return;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una lista de expresiones", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'encadenado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void encadenado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            macheo(DOT);
            encadenado_prima();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo", currentToken. getLexema());
        }
    }


    /**
     * Implementa la regla de producción para 'encadenado_prima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void encadenado_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS) {
            macheo(IDOBJETS);
            id_factor();
        }else {
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo", currentToken. getLexema());
        }
    }


    /**
     * Implementa la regla de producción para 'id_factor' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void id_factor() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == LEFT_BRACKET || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == LESS || type == GREATER || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV ) {
            accesoVar_prima();
        }else {
            if (type == LEFT_PAREN) {
                llamada_metodo();
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo", currentToken. getLexema());
            }
        }
    }
}























