package generacionDeCodigo;

import java.io.*;
import java.util.LinkedList;


/**
 * Clase encargada de generar el codigo final en MIPS
 */
public class CodeGen {
    /**
     * Listas para almacenar el codigo por debajo de .data
     */
    LinkedList<String> data = new LinkedList<>();


    /**
     * Lista para almacenar el codigo por debajo de .text
     */
    LinkedList<String> codigo = new LinkedList<>();


    /**
     * Agrega una linea al codigo final
     * @param linea linea a agregar
     */
    public void agregarLinea(String linea) {
        codigo.add(linea);
    }

    /**
     * Agrega una linea al data final
     * @param data linea a agregar
     */
    public void agregarData(String data) {
        this.data.add(data);
    }


    /**
     * Una vez finalizado la etapa de generación de codigo se escribe el archivo final.
     */
    public String consolidar(){
        StringBuilder salida = new StringBuilder();

        salida.append(".data\n");
        for (String dato : data){
            salida.append(dato).append("\n");
        }

        salida.append("\n.text\n");

        for (String linea : codigo){
            salida.append(linea).append("\n\n\t");
        }

        salida.append("\n");

        String vtables = getCodigoVTABLES();

        for (String linea : vtables.split("\n")){
            salida.append(linea).append("\n");
        }

        return salida.toString();
    }

    public String getCodigoVTABLES(){
        String s;
        s = """
                .data
                VTABLE_IO:
                        .word IO
                    	.word IO_out_array_int
                    	.word IO_in_str
                    	.word IO_in_double
                    	.word IO_out_array_str
                    	.word IO_in_int
                    	.word IO_out_int
                    	.word IO_in_bool
                   	    .word IO_out_str
                    	.word IO_out_double
                    	.word IO_out_array_double
                    	.word IO_out_bool
                    	.word IO_out_array_bool
               \s
                true: .asciiz "true"
                false: .asciiz "false"
                new_line: .asciiz "\\n"
                left_bracket: .asciiz "["
                right_bracket: .asciiz "]"
                comma: .asciiz ","
               \s
                .text
                IO:
               \s
                IO_in_str:
                	# Actualizamos frame pointer al de este metodo
                	move $fp $sp
               \s
                	# Cargamos return address
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# --- Se genera el codigo del metodo ---
                	move $t0 $sp # Guardo el tope en t0
                	addiu $sp $sp -1024 # Reservo 1024 bytes para el str
               \s
                	move $a0 $sp
                	li $a1 1024
                	li $v0 8 # Leo el dato
                	syscall
               \s
                	move $a0 $sp
                	li $t2 -1
               \s
                	loop_IO_in_str:
                		lb $t3 ($a0)
                		addi $a0 $a0 1
                		addi $t2 $t2 1
                		bne $t3 $zero loop_IO_in_str
               \s
                	#li $v0 1
                	#move $a0 $t2
                	#syscall
               \s
                	addiu $t2 $t2 4
               \s
                	li $v0 9
                	move $a0 $t2
                	syscall #Reservo espacio
               \s
                	la $a0, VTABLE_Str
                	sw $a0, 0($v0)
               \s
                	move $a0, $v0
               \s
                	addiu $v0 $v0 4
               \s
                	move $t1 $v0 # Guardo puntero destino
                	move $t3 $sp # Guardo puntero origen
               \s
                	loop_IO_in_str_2:
                		lb $t2 ($t3)
                		sb $t2 ($t1)
                		addi $t3 $t3 1
                		addi $t1 $t1 1
                		bne $t2 $zero loop_IO_in_str_2
               \s
                	addiu $sp $sp 1024 # Desalojo espacio reservado para lectura
                	# --- Fin del codigo del metodo ---
               \s
                	# --------------------------------------
               \s
               \s
                	# Recuperar el valor del return address
                	lw $ra 4($sp)
                	addiu $sp $sp 4 # desalojamos el enlace dinamico
               \s
                	jr $ra
               \s
                IO_out_str:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# Cuerpo de length
                	lw $t0 8($fp) # Cargo el objeto
                	addiu $t0 $t0 4
                	li $t1 -1 # Guardo un contador
               \s
                	loop_IO_out_str:
                		lb $a0 ($t0) # t0 = char actual
                		addi $t0 $t0 1 # Sumo un byte al valor str
                		addi $t1 $t1 1 # Sumo en uno el contado
                		li $v0 11
                		syscall
                		bne $a0 $zero loop_IO_out_str # Si llego a \\0, salgo
               \s
                	move $a0, $t1 # Muevo el resultado en a0
               \s
                	# Final de start
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_in_int:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# 1. Creo el CIR
                	li $v0 9
                	li $a0 8
                	syscall
               \s
                	move $a0 $v0
               \s
                	la $t0 VTABLE_Int
                	sw $t0 0($a0)
               \s
                	# 2. Leo el int
                	li $v0 5
                	syscall
               \s
                	# 3. Guardo el int
                	sw $v0 4($a0)
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
               \s
                IO_out_int:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# 1. Recupero el int y lo imprimo
                	lw $a0 8($fp)
                	lw $a0 4($a0) # Obtengo el valor del CIR
                	li $v0 1
                	syscall
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_in_bool:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	move $t0 $zero # flag = 0
               \s
                	# 1. Leo el input del usuario
                	li $v0, 12
                	syscall
               \s
                	move $t3, $v0
               \s
                	li $v0 4
                	la $a0 new_line
                	syscall
               \s
                	move $v0, $t3
               \s
                	# 2. Verifico si es igual a "1" o a "t"
               \s
                	li $t1, '1'
                	li $t2, 't'
               \s
                	beq $t1, $v0, IO_in_bool_true
                	beq $t2, $v0, IO_in_bool_true
                	b IO_in_bool_default
               \s
                	IO_in_bool_true:
                	addiu $t0 $t0 1
               \s
                	IO_in_bool_default:
               \s
                	li $v0 9
                	li $a0 8
                	syscall
               \s
                	move $a0, $v0
               \s
                	lw $t1, VTABLE_Bool
                	sw $t1, 0($v0)
                	sw $t0, 4($v0)
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_bool:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# 1. Recupero el bool
                	lw $a0 8($fp)
                	lw $a0 4($a0) # Obtengo el valor del CIR
                	move $t0 $zero
                	addiu $t0 $t0 1
               \s
                	beq $a0 $t0 IO_out_bool_true
               \s
                	IO_out_bool_false:
                		li $v0 4
                		la $a0 false
                		syscall
               \s
                		b IO_out_bool_default
               \s
                	IO_out_bool_true:
                		li $v0 4
                		la $a0 true
                		syscall
               \s
                	IO_out_bool_default:
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_in_double:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# 1. Creo el CIR
                	li $v0 9
                	li $a0 12
                	syscall
               \s
                	move $a0 $v0
               \s
                	la $t0 VTABLE_Double
                	sw $t0 0($a0)
               \s
                	# 2. Leo el double
                	li $v0 7
                	syscall
               \s
                	# 3. Guardo el double
                	swc1 $f0 4($a0)
                	swc1  $f1 8($a0)
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_double:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	sw $a0 8($fp)
               \s
                	# 1. Recupero el double y lo imprimo
                	lwc1 $f12 4($a0)
                	lwc1 $f13 8($a0)
                	li $v0 3
                	syscall
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_array_int:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $t1 8($fp)
               \s
                	# 1. Recupero la longitud del arreglo
                	lw $t0 4($t1)
               \s
                	# 2. Verifico si dim > 0
                	la $a0 left_bracket
                	li $v0 4
                	syscall
               \s
                	beq $t0 $zero IO_out_array_int_exit
               \s
                	# 3. Si dim > 0, iteramos por cada CIR
               \s
               \s
                    addiu $t1 $t1 4 # Aumento index
               \s
                	IO_out_array_int_loop:
                		sw $fp 0($sp)
                		addiu $sp $sp -4
               \s
               \s
                        addiu $t1 $t1 4 # Aumento index
                		lw $a0 0($t1) # Obtengo sig. pos
                		sw $a0 0($sp) # Guardo en pila
                		addiu $sp $sp -8
                		jal IO_out_int # Imprimo elem
               \s
               \s
               \s
                		addiu $sp $sp 8
                		lw $fp 0($sp)
                		addiu $sp $sp 4
               \s
                		subiu $t0 $t0 1 # Obtenemos elem restantes
               \s
                		beq $t0 $zero IO_out_array_int_exit # Si ya no hay elem siguientes, salgo
               \s
                		la $a0 comma
                		li $v0 4
                		syscall
               \s
                		b IO_out_array_int_loop
               \s
                	IO_out_array_int_exit:
                		la $a0 right_bracket
                		li $v0 4
                		syscall
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_array_str:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $t1 4($fp)
               \s
                	# 1. Recupero la longitud del arreglo
                	lw $t0 4($t1)
               \s
                	# 2. Verifico si dim > 0
                	la $a0 left_bracket
                	li $v0 4
                	syscall
               \s
                	beq $t0 $zero IO_out_array_str_exit
               \s
                	# Guardamos variables intermedias para que no se pierdan
               \s
                	## v1: Index
                	sw $t1, 0($sp)
                	addiu $sp $sp -4
               \s
                	## v2: Contador
                	sw $t0, 0($sp)
                	addiu $sp $sp -4
               \s
                	# 3. Si dim > 0, iteramos por cada CIR
               \s
                	IO_out_array_str_loop:
                		sw $fp 0($sp)
                		addiu $sp $sp -4
               \s
                		lw $t1, -4($fp) # Recupero index
                		lw $t0, -8($fp) # Recupero contador
               \s
                		addiu $t1 $t1 4 # Aumento index
                		subiu $t0 $t0 1 # Obtenemos elem restantes
               \s
                		sw $t1, -4($fp) # Guardo index
                		sw $t0, -8($fp) # Guardo contador
               \s
                		lw $a0 4($t1) # Obtengo sig. pos
                		sw $a0 0($sp) # Guardo en pila
                		addiu $sp $sp -4
                		jal IO_out_str # Imprimo elem
                		addiu $sp $sp 4
                        lw $fp 0($sp)
                        addiu $sp $sp 4
               \s
               \s
                		lw $t0, -8($fp)
                		beq $t0 $zero IO_out_array_str_exit # Si ya no hay elem siguientes, salgo
               \s
                		la $a0 comma
                		li $v0 4
                		syscall
               \s
                		b IO_out_array_str_loop
               \s
                	IO_out_array_str_exit:
                		la $a0 right_bracket
                		li $v0 4
                		syscall
               \s
                	# Desalojo variables locales
                	addiu $sp $sp 8
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_array_bool:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $t1 4($fp)
               \s
                	# 1. Recupero la longitud del arreglo
                	lw $t0 4($t1)
               \s
                	# 2. Verifico si dim > 0
                	la $a0 left_bracket
                	li $v0 4
                	syscall
               \s
                	beq $t0 $zero IO_out_array_bool_exit
               \s
                	# Guardamos variables intermedias para que no se pierdan
               \s
                	## v1: Index
                	sw $t1, 0($sp)
                	addiu $sp $sp -4
               \s
                	## v2: Contador
                	sw $t0, 0($sp)
                	addiu $sp $sp -4
               \s
                	# 3. Si dim > 0, iteramos por cada CIR
                	addiu $t1 $t1 4 # Aumento index
               \s
                	IO_out_array_bool_loop:
                		sw $fp 0($sp)
                		addiu $sp $sp -4
               \s
               \s
                        addiu $t1 $t1 4 # Aumento index
                		lw $a0 0($t1) # Obtengo sig. pos
                		sw $a0 0($sp) # Guardo en pila
                		addiu $sp $sp -8
                		jal IO_out_bool # Imprimo elem
               \s
               \s
               \s
                		addiu $sp $sp 8
                		lw $fp 0($sp)
                		addiu $sp $sp 4
               \s
                		subiu $t0 $t0 1 # Obtenemos elem restantes
               \s
                		beq $t0 $zero IO_out_array_bool_exit # Si ya no hay elem siguientes, salgo
               \s
                		la $a0 comma
                		li $v0 4
                		syscall
               \s
                		b IO_out_array_bool_loop
               \s
                	IO_out_array_bool_exit:
                		la $a0 right_bracket
                		li $v0 4
                		syscall
               \s
                	# Desalojo variables locales
                	addiu $sp $sp 8
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
               \s
                IO_out_array_double:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $t1 4($fp)
               \s
                	# 1. Recupero la longitud del arreglo
                	lw $t0 4($t1)
               \s
                	# 2. Verifico si dim > 0
                	la $a0 left_bracket
                	li $v0 4
                	syscall
               \s
                	beq $t0 $zero IO_out_array_double_exit
               \s
                	# Guardamos variables intermedias para que no se pierdan
               \s
                	## v1: Index
                	sw $t1, 0($sp)
                	addiu $sp $sp -4
               \s
                	## v2: Contador
                	sw $t0, 0($sp)
                	addiu $sp $sp -4
               \s
                	# 3. Si dim > 0, iteramos por cada CIR
               \s
                	IO_out_array_double_loop:
                		sw $fp 0($sp)
                		addiu $sp $sp -4
               \s
                		lw $t1, -4($fp) # Recupero index
                		lw $t0, -8($fp) # Recupero contador
               \s
                		addiu $t1 $t1 4 # Aumento index
                		subiu $t0 $t0 1 # Obtenemos elem restantes
               \s
                		sw $t1, -4($fp) # Guardo index
                		sw $t0, -8($fp) # Guardo contador
               \s
                		lw $a0 4($t1) # Obtengo sig. pos
                		sw $a0 0($sp) # Guardo en pila
                		addiu $sp $sp -4
                		jal IO_out_double # Imprimo elem
                		addiu $sp $sp 4
                        lw $fp 0($sp)
                        addiu $sp $sp 4
               \s
               \s
                		lw $t0, -8($fp)
                		beq $t0 $zero IO_out_array_double_exit # Si ya no hay elem siguientes, salgo
               \s
                		la $a0 comma
                		li $v0 4
                		syscall
               \s
                		b IO_out_array_double_loop
               \s
                	IO_out_array_double_exit:
                		la $a0 right_bracket
                		li $v0 4
                		syscall
               \s
                	# Desalojo variables locales
                	addiu $sp $sp 8
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
                \t
                .data
               \s
                VTABLE_Array:
                    .word array
                	.word array_length
               \s
                .text
                array:
                array_length:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# 1. Recupero CIR Array
                	lw $t0, 4($fp)
               \s
                	# 2. Recupero longitud del array
                	lw $t0, 4($t0)
               \s
                	# 3. Creo nuevo CIR Int
                	li $v0, 9
                	li $a0, 8
                	syscall
               \s
                	lw $t1, VTABLE_Int
                	sw $t1, 0($v0)
                	sw $t0, 4($v0)
               \s
                	move $a0, $v0
               \s
                	# --
                	lw $ra 4($sp)
                	addiu $sp $sp 4
                	jr $ra
                \t
                .data
                    VTABLE_Str:
                        .word str
                    	.word length
                    	.word concat
               \s
                .text
               \s
                str:
               \s
                length:
                	# Comienzo de start
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# Cuerpo de length
                	lw $a0 4($fp) # Cargo el objeto
                	addiu $a0 $a0 4
                	li $t1 -1 # Guardo un contador
               \s
                	loop:
                		lb $t0 ($a0) # t0 = char actual
                		addi $a0 $a0 1 # Sumo un byte al valor str
                		addi $t1 $t1 1 # Sumo en uno el contador
                		bne $t0 $zero loop # Si llego a \\0, salgo
               \s
                	# Creo CIR Int
                	addiu $t2, $t1, 4 # Sumo len + 4 VT
                	move $a0, $t1 # Muevo el resultado en a0
                	li $v0, 9 # Reservo bytes
                	syscall
               \s
                	lw $t0, VTABLE_Int
                	sw $t0, 0($v0)
                	sw $t1, 4($v0)
               \s
                	move $a0, $v0
               \s
                	# Final de start
                	lw $ra 4($sp)
                	addiu $sp $sp 12
                	lw $fp 0($sp)
                	jr $ra
               \s
                concat:
                	move $fp $sp
                	sw $ra 0($sp)
                	addiu $sp $sp -4
               \s
                	# Cuerpo de concat
               \s
                	## Paso1: Calcular longitud de la primer cadena
                	sw $fp 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $a0 4($fp) # Buscamos el primer param de concat
                	sw $a0 0($sp) # Lo guardamos en la pila
                	addiu $sp $sp -4
                	jal length
               \s
                	sw $a0, 0($sp) # Guardamos l1 en la pila
                	addiu $sp $sp -4
               \s
                	## Paso2: Calcular longitud de la segunda cadena
                	sw $fp 0($sp)
                	addiu $sp $sp -4
               \s
                	lw $a0 8($fp) # Buscamos el segundo param de concat
                	sw $a0 0($sp) # Lo guardamos en la pila
                	addiu $sp $sp -4
                	jal length
               \s
                	sw $a0, 0($sp) # Guardamos l2 en la pila
                	addiu $sp $sp -4
               \s
                	## Paso3: Reservar memoria len1 + len2
                	## - Guardar la dirección de v0 en un registro para no perderlo
                	## - Crear CIR de Str nuevo, incluye guardar la VT
               \s
                	lw $t0, -4($fp) # Obtengo el CIR de l1
                	lw $t0, 4($t0) # Obtengo el len de l1
                	lw $t1, -8($fp) # Obtengo l2
                	lw $t1, 4($t1) # Obtengo el len de l2
               \s
                	#move $a0, $zero
                	add $a0, $t0, $t1 # a0 = l1 + l2
                	addi $a0, $a0, 4 # a0 = vt + len
               \s
                	li $v0, 9
                	syscall
               \s
                	move $a0, $v0 # Guardo la CIR
               \s
                	la $t0, VTABLE_Str
                	sw $t0, 0($v0) # Guardo la VT en el heap
               \s
                	## Paso4: Escribir cadena 1 en v0
               \s
                	lw $t0 4($fp) # Obtengo CIR de s1
                	addiu $t0 $t0 4 # Apunto t0 a s1.val
               \s
                	move $t2 $v0 # Apunto t2 al CIR de s1
                	addiu $t2 $t2 4 # Apunto t2 a s2.val
               \s
                	concat_loop_1:
                		lb $t1 ($t0) # Catga byte de s1
                		beq $t1 $zero exit_concat_loop_1# Si es \\0, salgo del loop para no escribirlo
                		sb $t1 ($t2) # Escribe byte en rta
                		addiu $t0 $t0 1
                		addiu $t2 $t2 1
                		b concat_loop_1
               \s
                	exit_concat_loop_1:
               \s
                	## Paso5: Escribir cadena 2 en v0 + len1
               \s
                	lw $t0 8($fp) # Obtengo CIR de s2
                	addiu $t0 $t0 4 # Apunto t0 a s2.val
               \s
                	concat_loop_2:
                		lb $t1 ($t0) # Catga byte de s2
                		sb $t1 ($t2) # Escribe byte en rta
                		addiu $t0 $t0 1
                		addiu $t2 $t2 1
                		bne $t1 $zero concat_loop_2 # Si es \\0, termino
               \s
                	# desalojo variables locales
                	addiu $sp $sp 8
               \s
                	# Final del concat
                	lw $ra 4($sp)
                	addiu $sp $sp 16
                	lw $fp 0($sp)
                	jr $ra
               \s
                save_str: # Escribe el contenido de $a0 en la direccion apuntada por v0 + 4
                	move $t0 $a0
                	move $t2 $v0
                	addiu $t2 $t2 4
                	save_str_loop:
                		lb $t1 ($t0)
                		sb $t1 ($t2)
                		addiu $t0 $t0 1
                		addiu $t2 $t2 1
                		bne $t1 $zero save_str_loop
                	jr $ra
               \s
                eq_str: # Compara dos strings, devuelve un CIR Bool
                	lw $t0 4($sp) # Cargo CIR1
                	lw $t1 8($sp) # Cargo CIR2
               \s
                	addiu $t0 $t0 4 # Obtengo str1
                	addiu $t1 $t1 4 # Obtengo str2
               \s
                	move $t2 $zero
                	addiu $t2 $t2 1 # areEqual = true
               \s
                	li $v0 9
                	li $a0 8
                	syscall
               \s
                	la $t4 VTABLE_Bool
                	sw $t4 0($v0)
               \s
                	eq_str_loop:
                		lb $t3 ($t0)
                		lb $t4 ($t1)
               \s
                		bne $t3 $t4 eq_str_false
               \s
                		addiu $t0 $t0 1
                		addiu $t1 $t1 1
               \s
                		beq $t3 $zero eq_str_true
                		b eq_str_loop
               \s
                	eq_str_false:
                		move $t2 $zero
                		sw $t2 4($v0)
               \s
                		b eq_str_return
               \s
                	eq_str_true:
                		sw $t2 4($v0)
               \s
                	eq_str_return:
                		move $a0 $v0
                		jr $ra
                	\t
                .data
                VTABLE_Int:
                VTABLE_Double:
                VTABLE_Bool:
                VTABLE_Object:
               \s
                db_one: .double 1.0
                db_cero: .double 0.0
               \s
                .data
                DivisionByZeroExceptionMessage:
                	.asciiz "ERROR: DIVISION POR CERO"
                ArrayIndexOutOfBoundsExceptionMessage:
                	.asciiz "ERROR: INDICE DE ARRAY FUERA DE RANGO"
                NegativeArraySizeExceptionMessage:
                	.asciiz "ERROR: LONGITUD DE ARRAY NEGATIVO"
               \s
                .text
                DivisionByZeroException:
                	la $a0 DivisionByZeroExceptionMessage
                	li $v0, 4
                	syscall
               \s
                	li $v0, 17
                	li $a0, 1
                	syscall
               \s
                ArrayIndexOutOfBoundsException:
                	la $a0 ArrayIndexOutOfBoundsExceptionMessage
                	li $v0, 4
                	syscall
               \s
                	li $v0, 17
                	li $a0, 1
                	syscall
               \s
                NegativeArraySizeException:
                	la $a0 NegativeArraySizeExceptionMessage
                	li $v0, 4
                	syscall
               \s
                	li $v0, 17
                	li $a0, 1
                	syscall
               \s""";

        return s;
    }
}
