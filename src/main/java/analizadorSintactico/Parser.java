package analizadorSintactico;

import ErrorManage.ErrorTiny;
import analizadorLexico.Escaner;
import analizadorLexico.Token;
import analizadorLexico.TokenType;
import analizadorSemantico.*;
import analizadorSemantico.Errores.*;
import analizadorSintactico.Errores.MacheoIncorrectoError;
import analizadorSintactico.Errores.TokenInesperadoError;
import ast.*;

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
    private SymbolTable symbolTable;
    private AST ast;

    public Parser() {
        this.symbolTable = new SymbolTable();
        this.ast = new AST();
    }

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
            currentToken = escaner.nextToken();
        }else{
            throw new MacheoIncorrectoError(currentToken.getLine(),currentToken.getColumn(),tokenType.toString(), currentToken.getLexema());
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
            EntradaMetodo metodoStart = new EntradaMetodo("start", currentToken.getLine(), currentToken.getColumn());
            symbolTable.setMetodoActual(metodoStart);
            NodoBloque bloqueStart = new NodoBloque(currentToken.getLine(), currentToken.getColumn());
            macheo(START);

            bloque_metodo(bloqueStart);
            ast.setStart(bloqueStart);
            symbolTable.setStartMethod(metodoStart);

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

            if( symbolTable.buscarClase(currentToken.getLexema())!= null){
                throw new ClaseRedeclaradaError(currentToken.getLine(),currentToken.getColumn(),currentToken.getLexema());
            }
            EntradaClase e = new EntradaClase(currentToken.getLexema(),currentToken.getLine(),currentToken.getColumn());
            symbolTable.setClassActual(e);

            macheo(IDCLASS);

            String superClaseEntrada = clas_factorizado();

            EntradaClase claseActual = symbolTable.getClassActual();
            claseActual.setSuperClase(superClaseEntrada);
            symbolTable.insertarClase(claseActual.getLexema(),claseActual);
            ast.insertarClass(claseActual.getLexema(),new NodoClass(claseActual.getLexema()));

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

    private String clas_factorizado() throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE){

            macheo(LEFT_BRACE);
            atributo_class_recursivo();
            macheo(RIGHT_BRACE);
            return "Object";
        }else{
            if(type == DOBLE_DOT ){

                Token superClaseToken = herencia();
                String superClaseEntrada = superClaseToken.getLexema();
//                if(superClaseEntrada == null){
//                    throw new ClaseNoDeclaradaError(superClaseToken.getLine(),superClaseToken.getColumn(),superClaseToken.getLexema());
//                }

                macheo(LEFT_BRACE);
                atributo_class_recursivo();
                macheo(RIGHT_BRACE);

                return superClaseEntrada;
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

            EntradaClase claseActual = symbolTable.buscarClase(currentToken.getLexema());
            if(claseActual == null){
                throw new ClaseNoDeclaradaError(currentToken.getLine(),currentToken.getColumn(),currentToken.getLexema());
            }

            symbolTable.setClassActual(claseActual);

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

    private Token herencia() throws IOException, ErrorTiny {
        if(currentToken.getType() == DOBLE_DOT){
            macheo(DOBLE_DOT);

            Token superClase = currentToken;

            if (superClase.getLexema().equals("Int") || superClase.getLexema().equals("Double") ||
                superClase.getLexema().equals("Bool") || superClase.getLexema().equals("Str") ||
                superClase.getLexema().equals("Array")) {
                throw new HerenciaInvalidaError(superClase.getLine(),superClase.getColumn(),superClase.getLexema());
            }

            tipo();

            return(superClase);

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
                if (symbolTable.getClassActual().tieneConstructor()){
                    throw new MetodoRedeclaradoError(currentToken.getLine(),currentToken.getColumn(),symbolTable.getClassActual().getLexema());
                }
                constructor();
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un metodo o constructor", currentToken. getLexema());
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

            EntradaClase claseActual = symbolTable.getClassActual();
            NodoClass nodoClaseActual = ast.getClass(claseActual.getLexema());

            EntradaMetodo constructor = new EntradaMetodo(
                    claseActual.getLexema(),
                    currentToken.getLine(),
                    currentToken.getColumn());

            symbolTable.setMetodoActual(constructor);
            NodoBloque bloqueConstructor = new NodoBloque(currentToken.getLine(), currentToken.getColumn());

            macheo(DOT);
            argumentos_formales();
            bloque_metodo(bloqueConstructor);

            claseActual.setConstructor(constructor);
            nodoClaseActual.insertarMetodo(claseActual.getLexema(), bloqueConstructor);
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
            String tipoClase = currentToken.getLexema();
            String subtipo = tipo();

            if (type == ARRAY) {
                if (!subtipo.isEmpty()){
                    lista_declaraciones_variables(tipoClase, subtipo, true);
                }
            } else {
                lista_declaraciones_variables(tipoClase, null, true);
            }

            macheo(SEMICOLON);
        }else{
            if(type == PUB){
                visibilidad();

                type = currentToken.getType();

                String tipoClase = currentToken.getLexema();
                String subtipo = tipo();

                if (type == ARRAY) {
                    if (!subtipo.isEmpty()){
                        lista_declaraciones_variables(tipoClase, subtipo, false);
                    }
                } else {
                    lista_declaraciones_variables(tipoClase, null, false);
                }
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
        EntradaMetodo metodoActual = new EntradaMetodo();
        symbolTable.setMetodoActual(metodoActual);
        if(type == FN){

            macheo(FN);
            tipo_metodo_factorizacion();

            metodoActual.setPosicion(currentToken.getLine(), currentToken.getColumn());
            metodoActual.setLexema(currentToken.getLexema());

            macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos

            if(symbolTable.getClassActual().buscarMetodo(metodoActual.getLexema()) != null){
                throw new MetodoRedeclaradoError(currentToken.getLine(),currentToken.getColumn(),symbolTable.getMetodoActual().getLexema());
            }

            argumentos_formales();

            NodoClass nodoClaseActual = ast.getClass(symbolTable.getClassActual().getLexema());

            symbolTable.getClassActual().insertarMetodo(metodoActual.getLexema(), metodoActual);

            NodoBloque nodoBloque = new NodoBloque(currentToken.getLine(), currentToken.getColumn());
            bloque_metodo(nodoBloque);

            nodoClaseActual.insertarMetodo(symbolTable.getMetodoActual().getLexema(), nodoBloque);

        }else{
            if(type == ST ){
                metodoActual.setEsEstatico(true);
                forma_metodo();
                macheo(FN); // Circulo r; r.setRadio();
                tipo_metodo_factorizacion();

                metodoActual.setPosicion(currentToken.getLine(), currentToken.getColumn());
                metodoActual.setLexema(currentToken.getLexema());

                macheo(IDOBJETS);///// Corroborar eso, el identificador de metodo atributo es el mismo que el de objetos

                if(symbolTable.getClassActual().buscarMetodo(metodoActual.getLexema()) != null){
                    throw new MetodoRedeclaradoError(currentToken.getLine(),currentToken.getColumn(),symbolTable.getMetodoActual().getLexema());
                }

                argumentos_formales();

                NodoClass nodoClaseActual = ast.getClass(symbolTable.getClassActual().getLexema());

                symbolTable.getClassActual().insertarMetodo(metodoActual.getLexema(), metodoActual);

                NodoBloque nodoBloque = new NodoBloque(currentToken.getLine(), currentToken.getColumn());
                bloque_metodo(nodoBloque);

                nodoClaseActual.insertarMetodo(symbolTable.getMetodoActual().getLexema(), nodoBloque);
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
        EntradaMetodo metodoActual = symbolTable.getMetodoActual();
        if(type == IDCLASS || type == VOID || type == STR || type == BOOL || type == INT || type == DOUBLE || type == ARRAY){

            String tipoMetodo = currentToken.getLexema();
            String subtipoMetodo = tipo_metodo();

            if (type == VOID) {
                return;
            }
            if(type == ARRAY) {
                if (!subtipoMetodo.isEmpty()){
                    metodoActual.setSubtipoRetorno(symbolTable.buscarClase(subtipoMetodo));
                }
            }
            metodoActual.setTipoRetorno(symbolTable.buscarClase(tipoMetodo));

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

    private void bloque_metodo(NodoBloque bloque ) throws IOException, ErrorTiny {
        if(currentToken.getType()==LEFT_BRACE){
            macheo(LEFT_BRACE);
            decl_var_loc_bloque_recursivo();
            sentencia_bloque_recursivo(bloque);
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
    private void sentencia_bloque_recursivo(NodoBloque bloque) throws IOException, ErrorTiny {
      
        TokenType type = currentToken.getType();
        if(type == LEFT_BRACE || type == SEMICOLON || type == LEFT_PAREN || type== IF || type == WHILE || type == RET || type == IDOBJETS || type == SELF){
            NodoSentencia nodoSen =  sentencia();
            bloque.insertarSentencia(nodoSen);
            sentencia_bloque_recursivo(bloque);
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
            String tipoClase = currentToken.getLexema();
            String subtipo = tipo();

            if (type == ARRAY) {
                if (!subtipo.isEmpty()){
                    lista_declaraciones_variables(tipoClase, subtipo);
                }
            } else {
                lista_declaraciones_variables(tipoClase, null);
            }

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

    private void lista_declaraciones_variables(String tipo, String subtipo) throws IOException, ErrorTiny {
        if(currentToken.getType()==IDOBJETS){

            if (symbolTable.getMetodoActual().buscarVariableLocal(currentToken.getLexema()) != null) {
                throw new VariableRedefinidaError(currentToken.getLine(),currentToken.getColumn(),currentToken.getLexema());
            }

            EntradaVariable variableLocal = new EntradaVariable(
                    currentToken.getLexema(),
                    currentToken.getLine(),
                    currentToken.getColumn(),
                    tipo);

            if (subtipo != null) {
                variableLocal.setSubtipo(subtipo);
            }

            symbolTable.getMetodoActual().insertarVariableLocal(variableLocal.getLexema(), variableLocal);

            macheo(IDOBJETS);
            lista_declaraciones_variables_prima(tipo, subtipo);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un nombre de variable", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'lista_declaraciones_variables' para atributos de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private void lista_declaraciones_variables(String tipo, String subtipo, boolean esPrivado) throws IOException, ErrorTiny {
        if(currentToken.getType()==IDOBJETS){
            if (symbolTable.getClassActual().buscarAtributo(currentToken.getLexema()) != null) {
                throw new VariableRedefinidaError(currentToken.getLine(),currentToken.getColumn(),currentToken.getLexema());
            }

            EntradaAtributo atributo = new EntradaAtributo(
                    currentToken.getLexema(),
                    currentToken.getLine(),
                    currentToken.getColumn(),
                    tipo,
                    esPrivado,
                    symbolTable.getClassActual().getLexema());

            if (subtipo != null) {
                atributo.setSubtipo(subtipo);
            }


            symbolTable.getClassActual().insertarAtributo(atributo.getLexema(), atributo);

            macheo(IDOBJETS);
            lista_declaraciones_variables_prima(tipo, subtipo, esPrivado);
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

    private void lista_declaraciones_variables_prima(String tipo, String subtipo) throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == COMMA){
            macheo(COMMA);
            lista_declaraciones_variables(tipo, subtipo);
        }else{
            if(type == SEMICOLON){
                return;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"mas declaraciones de variables locales o un punto y coma", currentToken. getLexema());
            }
        }
    }

    private void lista_declaraciones_variables_prima(String tipo, String subtipo, boolean esPrivado) throws IOException, ErrorTiny {
        TokenType type = currentToken.getType();
        if (type == COMMA){
            macheo(COMMA);
            lista_declaraciones_variables(tipo, subtipo, esPrivado);
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
        EntradaMetodo metodoActual = symbolTable.getMetodoActual();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY ){
            TokenType tipoArgumento = type;

            String claseTipo = currentToken.getLexema();

            String subtipoArgumento = tipo();

            EntradaParametro parametro = new EntradaParametro(
                    currentToken.getLexema(),
                    currentToken.getLine(),
                    currentToken.getColumn(),
                    claseTipo,
                    metodoActual.getCantidadParametros());

            if(tipoArgumento == ARRAY) {
                if (!subtipoArgumento.isEmpty()){
                    parametro.setSubtipo(subtipoArgumento);
                }
            }
            if (metodoActual.buscarParametro(currentToken.getLexema()) != null) {
                throw new ParametroRedefinidoError(currentToken.getLine(),currentToken.getColumn(),currentToken.getLexema());
            }

            metodoActual.insertarParametro(currentToken.getLexema(), parametro);

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


    private String tipo_metodo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==IDCLASS || type==STR || type==BOOL || type==INT || type==DOUBLE || type==ARRAY){
            return tipo();
        }else{
            if (type == VOID){
                macheo(VOID);
                return null;
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


    private String tipo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == STR || type == BOOL || type == INT || type == DOUBLE){
            tipo_primitivo();
        }else{
            if(type == IDCLASS){
                tipo_referencia();
            }else{
                if(type == ARRAY){
                    String subtipo = tipo_arreglo();
                    return subtipo;
                }else{
                    throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un tipo o clase", currentToken. getLexema());
                }
            }
        }
        return null;
    }


    /**
     * Implementa la regla de producción para 'tipo_primitivo' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */


    private String tipo_primitivo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==STR){
            macheo(STR);
            return "Str";
        }else{
            if(type==BOOL){
                macheo(BOOL);
                return "Bool";
            }else{
                if(type==INT){
                    macheo(INT);
                    return "Int";
                }else{
                    if(type==DOUBLE){
                        macheo(DOUBLE);
                        return "Double";
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

    private String tipo_arreglo() throws IOException, ErrorTiny{
        if(currentToken.getType() == ARRAY){
            macheo(ARRAY);
            String subtipo = tipo_primitivo();
            return subtipo;
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

    private NodoSentencia sentencia() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type == IF){

            int linea = currentToken.getLine();
            int columna = currentToken.getColumn();
            macheo(IF);
            macheo(LEFT_PAREN);
            NodoExp nodoExp =  expOr();
            macheo(RIGHT_PAREN);
            NodoSentencia nodoIf =  sentencia();
            NodoSentencia nodoElse = sentencia_else();
            return new NodoIf(nodoExp, nodoIf, nodoElse, linea, columna);
        }else{
            if(type==WHILE){
                int linea = currentToken.getLine();
                int columna = currentToken.getColumn();

                macheo(WHILE);
                macheo(LEFT_PAREN);
                NodoExp nodoExp =  expOr();
                macheo(RIGHT_PAREN);
                NodoSentencia nodoSentencia = sentencia();
                return new NodoWhile(nodoExp,nodoSentencia, linea, columna);
            }else{
                if(type==RET){
                    int linea = currentToken.getLine();
                    int columna = currentToken.getColumn();

                    macheo(RET);
                    NodoExp nodoExp = ExpOr_factorizado();
                    macheo(SEMICOLON);
                    return new NodoRet(nodoExp, linea, columna);
                }else{
                    if(type==IDOBJETS || type==SELF){
                        NodoAsignacion nodoAsignacion= asignacion();
                        macheo(SEMICOLON);
                        return nodoAsignacion;
                    }else{
                        if(type==LEFT_PAREN){
                           NodoExp nodoExp = sentencia_simple();
                           macheo(SEMICOLON);
                           return nodoExp;
                        }else{
                            if(type==LEFT_BRACE){
                                return bloque();
                            }else{
                                if(type==SEMICOLON){
                                    macheo(SEMICOLON);
                                    return null;
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

    private NodoExp ExpOr_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type==IDCLASS || type==IDOBJETS || type==PLUS || type==MINUS || type==NOT || type==PLUS_PLUS || type==MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == STRING_LITERAL || type==SELF || type == NEW || type == LEFT_PAREN){
            return expOr();
        }else{
            if(type == SEMICOLON){
                return null;
            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion o punto y coma", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'sentencia_else' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoSentencia sentencia_else()throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == ELSE){
            macheo(ELSE);
            return sentencia();
        }else{
            if( type == RIGHT_BRACE || type == SEMICOLON || type == IDOBJETS || type== SELF || type == LEFT_PAREN
                    || type == IF || type == WHILE || type == LEFT_BRACE || type == RET){
                return null;
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

    private NodoBloque bloque() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == LEFT_BRACE){
            macheo(LEFT_BRACE);
            NodoBloque bloque = new NodoBloque(currentToken.getLine(), currentToken.getColumn());
            sentencia_bloque_recursivo(bloque);
            macheo(RIGHT_BRACE);
            return bloque;
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

    private NodoAsignacion asignacion() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS ){

            NodoVar nodoVar = accesoVar_simple();
            macheo(EQUAL);
            NodoExp nodoExp = expOr();
            return new NodoAsignacion(nodoVar, nodoExp, currentToken.getLine(), currentToken.getColumn());
        }else{
            if (type == SELF){
                NodoVar nodoVar = accesoSelf_simple();
                macheo(EQUAL);
                NodoExp nodoExp = expOr();
                return new NodoAsignacion(nodoVar, nodoExp, currentToken.getLine(), currentToken.getColumn());
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

    private NodoVar accesoVar_simple() throws IOException, ErrorTiny{

        TokenType type = currentToken.getType();
        if(type == IDOBJETS){
            Token token = currentToken;
            macheo(IDOBJETS);
            return accesoVar_simple_prima(token);
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

    private NodoVar accesoVar_simple_prima(Token token) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == EQUAL){
            NodoVar nodoVar = new NodoVar(token.getLexema(), token.getLine(), token.getColumn());
            nodoVar.setEncadenado(encadenado_simple_recursivo());
            return nodoVar;
        }else{
            if(type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                NodoArrayAcceso arreglo = new NodoArrayAcceso(token.getLexema(), currentToken.getLine(), currentToken.getColumn());
                arreglo.setIndice(expOr());
                macheo(RIGHT_BRACKET);
                return arreglo;
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

    private NodoVar encadenado_simple_recursivo() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT){
            NodoVar nodoVar = encadeado_simple();
            nodoVar.setEncadenado(encadenado_simple_recursivo());
            return nodoVar;
        }else{
            if(type == EQUAL){
                return null;
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

    private NodoVar accesoSelf_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF){
            macheo(SELF);
            NodoVar nodoVar = new NodoVar("self", currentToken.getLine(), currentToken.getColumn());
            nodoVar.setEncadenado(encadenado_simple_recursivo());
            return nodoVar;
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

    private NodoVar encadeado_simple() throws IOException, ErrorTiny{
        if(currentToken.getType() == DOT){
            macheo(DOT);
            NodoVar nodoVar = new NodoVar(currentToken.getLexema(), currentToken.getLine(), currentToken.getColumn());
            macheo(IDOBJETS);
            return nodoVar;
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

    private NodoExp sentencia_simple() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN){
            macheo(LEFT_PAREN);
            NodoExp nodoExp = expOr();
            macheo(RIGHT_PAREN);
            return nodoExp;
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

    private NodoExp expOr() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp =  expAnd();
            return expOrPrima(nodoExp);
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

    private NodoExp expOrPrima(NodoExp nodoLadoIzq) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == OR) {
            macheo(OR);
            NodoExp nodoLadoDer = expAnd();
            NodoExpBin nodoResul = new NodoExpBin(nodoLadoIzq,nodoLadoDer,OR, currentToken.getLine(), currentToken.getColumn());
            return expOrPrima(nodoResul);

        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET) {
                return nodoLadoIzq;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expAnd' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expAnd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp = expIgual();
            return expAndPrima(nodoExp);
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

    private NodoExp expAndPrima(NodoExp nodoLadoIzq) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == AND) {
            macheo(AND);

            NodoExp nodoLadoDer =  expIgual();
            return new NodoExpBin(nodoLadoIzq,nodoLadoDer,AND,currentToken.getLine(), currentToken.getColumn());

        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR) {
                return nodoLadoIzq;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),
                        "una expresion, una operacion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expIgual' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expIgual() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp= expCompuesta();
            return expIgualPrima(nodoExp);
        }else{
            throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion, una operacion o cerrar expresion", currentToken. getLexema());
        }
    }

    /**
     * Implementa la regla de producción para 'expIgualPrima' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expIgualPrima(NodoExp nodoLadoIzq) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == EQUAL_EQUAL || type == NOT_EQUAL) {

            opIgual();

            NodoExp nodoLadoDerecho = expCompuesta();
            NodoExpBin nodoExpBin = new NodoExpBin(nodoLadoIzq,nodoLadoDerecho,type,currentToken.getLine(), currentToken.getColumn());
            return expIgualPrima(nodoExpBin);
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND) {
                return nodoLadoIzq ;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion, una operacion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expCompuesta' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expCompuesta() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp = expAd();
            return expCompuestaPrima(nodoExp);
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

    private NodoExp expCompuestaPrima(NodoExp nodoLadoIzq) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
            opCompuesta();
            NodoExp nodoLadoDer =  expAd();
            NodoExpBin nodoExpBin = new NodoExpBin(nodoLadoIzq,nodoLadoDer,type,currentToken.getLine(), currentToken.getColumn());
            return nodoExpBin;

        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL) {
                return nodoLadoIzq;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion, una operacion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expAd' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expAd() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp = expMul();
            return expAdPrima(nodoExp);
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

    private NodoExp expAdPrima(NodoExp nodoLadoIzq) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == PLUS || type == MINUS) {
            opAd();
            NodoExp nodoLadoDerecho =  expMul();
            NodoExpBin nodoExpBin = new NodoExpBin(nodoLadoIzq,nodoLadoDerecho,type,currentToken.getLine(), currentToken.getColumn());
            return expAdPrima(nodoExpBin);
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL) {
                return nodoLadoIzq;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion, una operacion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expMul' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expMul() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == LEFT_PAREN || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == SELF || type == NEW){
            NodoExp nodoExp = expUn();
            return expMulPrima(nodoExp);
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

    private NodoExp expMulPrima(NodoExp nodoLadoIzquierdo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == MULT || type == SLASH || type == DIV || type == PERCENTAGE) {
            opMul();
            NodoExp nodoExpLadoDerecho = expUn();
            NodoExpBin nodoExpBin = new NodoExpBin(nodoLadoIzquierdo,nodoExpLadoDerecho,type,
                    currentToken.getLine(), currentToken.getColumn());
            return expMulPrima(nodoExpBin);


        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS) {
                return nodoLadoIzquierdo;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion, una operacion o cerrar expresion", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'expUn' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp expUn() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == LEFT_PAREN) {
            macheo(LEFT_PAREN);
            return parentesis_factorizado();
        } else {
            if (type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS) {
                macheo(type);
                NodoExp nodoExp = expUn();
                return new NodoExpUn(nodoExp, type, currentToken.getLine(), currentToken.getColumn());
            } else {
                if (type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL) {
                    return literal();
                } else {
                    if (type == IDCLASS || type == IDOBJETS || type == SELF || type == NEW) {

                        NodoExp nodoExp = primario_sin_parentesis();
                        //NodoExp nodoEncadenado = encadenado_factorizado();
                        //nodoExp.setEncadenado(nodoEncadenado);
                        return nodoExp;
                    }else{
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion o una operación", currentToken. getLexema());
                    }
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



    private NodoExp parentesis_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if (type == INT){
            macheo(INT);
            macheo(RIGHT_PAREN);
            NodoExp nodoExp = expUn();
            return new NodoExpUn(nodoExp, INT, currentToken.getLine(), currentToken.getColumn());
        }else{
            if(type==IDCLASS || type==IDOBJETS || type==PLUS || type==MINUS || type==NOT || type==PLUS_PLUS || type==MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == DOUBLE_LITERAL || type == STRING_LITERAL || type==SELF || type == NEW || type == LEFT_PAREN){

                NodoExp nodoExp = expOr();
                macheo(RIGHT_PAREN);
                NodoExp nodoEncadenado = encadenado_factorizado();
                nodoExp.setEncadenado(nodoEncadenado);
                return nodoExp;


            }else{
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresion o un tipo entero", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'encadenado_factorizado' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoExp encadenado_factorizado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            return encadenado();
        }else {
            if (type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == OR || type == AND || type == NOT_EQUAL || type == EQUAL_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == RIGHT_BRACKET || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV) {
                return null;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo, una operación o cerrar una expresión", currentToken. getLexema());
            }
        }
    }

    /**
     * Implementa la regla de producción para 'literal' de la gramática.
     *
     * @throws IOException Si ocurre un error de E/S.
     * @throws ErrorTiny Si se encuentra un error léxico.
     */

    private NodoOperando literal() throws IOException, ErrorTiny{
        if (currentToken.getType() == NIL){
            macheo(NIL);
            return new NodoNil(currentToken.getLine(), currentToken.getColumn());
        }else {
            if (currentToken.getType() == TRUE) {
                macheo(TRUE);
                return new NodoBool( true,currentToken.getLine(), currentToken.getColumn());
            }else{
                if (currentToken.getType() == FALSE) {
                    macheo(FALSE);
                    return new NodoBool(false,currentToken.getLine(), currentToken.getColumn());
                }else {
                    if (currentToken.getType() == INTEGER_LITERAL) {
                        Integer literal = Integer.parseInt(currentToken.getLexema());
                        macheo(INTEGER_LITERAL);
                        return new NodoInt(literal,currentToken.getLine(), currentToken.getColumn());
                    } else {
                        if (currentToken.getType() == STRING_LITERAL) {
                            String literal = currentToken.getLexema().substring(
                                    1, currentToken.getLexema().length() - 1
                            );
                            macheo(STRING_LITERAL);
                            return new NodoString(literal,currentToken.getLine(), currentToken.getColumn());
                        } else {
                            if (currentToken.getType() == DOUBLE_LITERAL) {
                                Double literal = Double.parseDouble(currentToken.getLexema());
                                macheo(DOUBLE_LITERAL);
                                return new NodoDouble(literal,currentToken.getLine(), currentToken.getColumn());
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
            if(type==SELF || type==NEW || type==IDCLASS || type==IDOBJETS) {
                primario_sin_parentesis();
            }else{
                    throw new TokenInesperadoError(currentToken.getLine(), currentToken.getColumn(), "una expresión primaria (expresión entre paréntesis, acceso a variable, 'self', llamada a método o constructor)", currentToken.getLexema());

            }
        }
    }

    private NodoExp primario_sin_parentesis() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type==SELF){
                return accesoSelf();
        }else{
            if(type==IDOBJETS){
                Token idObject = currentToken;
                macheo(IDOBJETS);
                return id_factor(idObject);
            }else{
                if(type==IDCLASS){
                    return llamada_metodo_estatico();
                }else{
                    if(type==NEW){
                        return llamada_conclasor();
                    }else{
                        throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"una expresión primaria (expresión acceso a variable, 'self', llamada a método o constructor)", currentToken. getLexema());
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

    private NodoVar accesoSelf() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == SELF ) {
            macheo(SELF);
            NodoVar nodoVar = new NodoVar(symbolTable.getClassActual().getLexema(),currentToken.getLine(), currentToken.getColumn());
            NodoExp nodoExp= encadenado_factorizado();
            nodoVar.setEncadenado(nodoExp);
            return nodoVar;
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

    private NodoVar accesoVar_prima(Token token) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA  || type == RIGHT_PAREN || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == GREATER || type == LESS || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == DIV || type == PERCENTAGE){
            NodoVar nodoVar = new NodoVar(token.getLexema(), token.getLine(), token.getColumn());
            nodoVar.setEncadenado(encadenado_factorizado());
            return nodoVar;
        }else{
            if (type == LEFT_BRACKET){
                macheo(LEFT_BRACKET);
                NodoArrayAcceso nodoArray = new NodoArrayAcceso(token.getLexema(), currentToken.getLine(), currentToken.getColumn());
                nodoArray.setIndice(expOr());
                macheo(RIGHT_BRACKET);
                nodoArray.setEncadenado(encadenado_factorizado());
                return nodoArray;
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

    private void llamada_metodo(NodoLlamadaMetodo nodoLlamadaMetodo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN ) {
            argumentos_actuales(nodoLlamadaMetodo);
            NodoExp nodoExp = encadenado_factorizado();
            nodoLlamadaMetodo.setEncadenado(nodoExp);
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

    private NodoVar llamada_metodo_estatico() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS ) {

            //Aca se crea el nodo para el llamado a una clase estatica
            NodoVar nodoVarStatic = new NodoVar(currentToken.getLexema(),currentToken.getLine(), currentToken.getColumn());
            nodoVarStatic.setEsEstatico(true);
            macheo(IDCLASS);
            macheo(DOT);
            //Se crea el nodo del metodo que se llama con la clase estatica
            NodoLlamadaMetodo nodoLlamadaMetodo = new NodoLlamadaMetodo(currentToken.getLexema(),
                    currentToken.getLine(), currentToken.getColumn());
            //encadenamos el metodo a la clase static
            nodoLlamadaMetodo.setEsEstatico(true);
            nodoVarStatic.setEncadenado(nodoLlamadaMetodo);
            macheo(IDOBJETS);
            //agregamos al nodo metodo todos sus parametros
            llamada_metodo(nodoLlamadaMetodo);
            //seguimos encadenando si es necesario
            NodoExp nodoEncadenado = encadenado_factorizado();
            nodoLlamadaMetodo.setEncadenado(nodoEncadenado);
            return nodoVarStatic;
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

    private NodoOperando llamada_conclasor() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == NEW ) {
            macheo(NEW);
            return llamada_conclasor_prima();
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

    private NodoOperando llamada_conclasor_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS){

            NodoLlamadaMetodo nodoLlamadaMetodo = new NodoLlamadaMetodo(
                    currentToken.getLexema(),
                    currentToken.getLine(),
                    currentToken.getColumn());
            macheo(IDCLASS);

            argumentos_actuales(nodoLlamadaMetodo);
            NodoExp nodoExp = encadenado_factorizado();
            nodoLlamadaMetodo.setEncadenado(nodoExp);
            return nodoLlamadaMetodo;
        }else{
            if (type == STR || type == DOUBLE || type == INT || type == BOOL){

                tipo_primitivo();
                NodoConstructorArray arreglo = new NodoConstructorArray(type.toString(),
                        currentToken.getLine(),
                        currentToken.getColumn()
                );
                arreglo.setTipo("Array");
                macheo(LEFT_BRACKET);
                NodoExp nodoExp = expOr();
                arreglo.setDimension(nodoExp);
                macheo(RIGHT_BRACKET);
                return arreglo;
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

    private void argumentos_actuales(NodoLlamadaMetodo nodoLlamadaMetodo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == LEFT_PAREN) {
            macheo(LEFT_PAREN);
            lista_expresiones_factorizado(nodoLlamadaMetodo);
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

    private void lista_expresiones_factorizado(NodoLlamadaMetodo nodoLlamadaMetodo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            lista_expresiones(nodoLlamadaMetodo);
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

    private void lista_expresiones(NodoLlamadaMetodo nodoLlamadaMetodo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDCLASS || type == IDOBJETS || type == PLUS || type == MINUS || type == NOT || type == PLUS_PLUS || type == MINUS_MINUS || type == NIL || type == TRUE || type == FALSE || type == INTEGER_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == LEFT_PAREN || type == SELF || type == NEW) {
            NodoExp nodoExp = expOr();
            nodoLlamadaMetodo.agregarParametro(nodoExp);
            lista_expresiones_prima(nodoLlamadaMetodo);
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

    private void lista_expresiones_prima(NodoLlamadaMetodo nodoLlamadaMetodo) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == COMMA) {
            macheo(COMMA);
            lista_expresiones(nodoLlamadaMetodo);
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

    private NodoVar encadenado() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT) {
            macheo(DOT);
            return encadenado_prima();
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

    private NodoVar encadenado_prima() throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == IDOBJETS) {
            Token idObject = currentToken;
            macheo(IDOBJETS);
            return id_factor(idObject);
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

    private NodoVar id_factor(Token token) throws IOException, ErrorTiny{
        TokenType type = currentToken.getType();
        if(type == DOT || type == SEMICOLON || type == COMMA || type == RIGHT_PAREN || type == LEFT_BRACKET || type == RIGHT_BRACKET || type == OR || type == AND || type == EQUAL_EQUAL || type == NOT_EQUAL || type == LESS || type == GREATER || type == GREATER_EQUAL || type == LESS_EQUAL || type == PLUS || type == MINUS || type == MULT || type == SLASH || type == PERCENTAGE || type == DIV ) {
            return accesoVar_prima(token);
        }else {
            if (type == LEFT_PAREN) {
                NodoLlamadaMetodo nodoLlamadaMetodo = new NodoLlamadaMetodo(token.getLexema(), currentToken.getLine(), currentToken.getColumn());
                llamada_metodo(nodoLlamadaMetodo);
                return nodoLlamadaMetodo;
            } else {
                throw new TokenInesperadoError(currentToken.getLine(),currentToken.getColumn(),"un acceso a variable o llamada a metodo, una operación o cerrar una expresión", currentToken. getLexema());
            }
        }
    }

    public SymbolTable getTablaSimbolos() {
        return symbolTable;
    }

    public AST getAST() {
        return ast;
    }
}























