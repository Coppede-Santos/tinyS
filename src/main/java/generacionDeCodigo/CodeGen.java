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
                            .word m_out_array_int_0_0
                            .word m_in_str_0_0
                            .word m_in_double_0_0
                            .word m_out_array_str_0_0
                            .word m_in_int_0_0
                            .word m_out_int_0_0
                            .word m_in_bool_0_0
                            .word m_out_str_0_0
                            .word m_out_double_0_0
                            .word m_out_array_double_0_0
                            .word m_out_bool_0_0
                            .word m_out_array_bool_0_0
            
                    true: .asciiz "true"
                    false: .asciiz "false"
                    new_line: .asciiz "\\n"
                    left_bracket: .asciiz "["
                    right_bracket: .asciiz "]"
                    comma: .asciiz ","
            
                    .text
                    IO:
                        jr $ra
            
                    m_in_str_0_0:
                        # Actualizamos frame pointer al de este metodo
                        move $fp $sp
            
                        # Cargamos return address
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # --- Se genera el codigo del metodo ---
                        move $t0 $sp # Guardo el tope en t0
                        addiu $sp $sp -1024 # Reservo 1024 bytes para el str
            
                        move $a0 $sp
                        li $a1 1024
                        li $v0 8 # Leo el dato
                        syscall
            
                        move $a0 $sp
                        li $t2 -1
            
                        loop_m_in_str:
                            lb $t3 ($a0)
                            addi $a0 $a0 1
                            addi $t2 $t2 1
                            bne $t3 $zero loop_m_in_str
            
                        #li $v0 1
                        #move $a0 $t2
                        #syscall
            
                        addiu $t2 $t2 4
            
                        li $v0 9
                        move $a0 $t2
                        syscall #Reservo espacio
            
                        la $a0, VTABLE_Str
                        sw $a0, 0($v0)
            
                        move $a0, $v0
            
                        addiu $v0 $v0 4
            
                        move $t1 $v0 # Guardo puntero destino
                        move $t3 $sp # Guardo puntero origen
            
                        loop_m_in_str_2:
                            lb $t2 ($t3)
                            sb $t2 ($t1)
                            addi $t3 $t3 1
                            addi $t1 $t1 1
                            bne $t2 $zero loop_m_in_str_2
            
                        addiu $sp $sp 1024 # Desalojo espacio reservado para lectura
                        # --- Fin del codigo del metodo ---
            
                        # --------------------------------------
            
            
                        # Recuperar el valor del return address
                        lw $ra 4($sp)
                        addiu $sp $sp 4 # desalojamos el enlace dinamico
            
                        jr $ra
            
                    m_out_str_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # Cuerpo de length
                        lw $t0 8($fp) # Cargo el objeto
                        addiu $t0 $t0 4
                        li $t1 -1 # Guardo un contador
            
                        loop_m_out_str:
                            lb $a0 ($t0) # t0 = char actual
                            addi $t0 $t0 1 # Sumo un byte al valor str
                            addi $t1 $t1 1 # Sumo en uno el contado
                            li $v0 11
                            syscall
                            bne $a0 $zero loop_m_out_str # Si llego a \\0, salgo
            
                        move $a0, $t1 # Muevo el resultado en a0
            
                        # Final de start
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_in_int_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # 1. Creo el CIR
                        li $v0 9
                        li $a0 8
                        syscall
            
                        move $a0 $v0
            
                        la $t0 VTABLE_Int
                        sw $t0 0($a0)
            
                        # 2. Leo el int
                        li $v0 5
                        syscall
            
                        # 3. Guardo el int
                        sw $v0 4($a0)
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
            
                    m_out_int_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # 1. Recupero el int y lo imprimo
                        lw $a0 8($fp)
                        lw $a0 4($a0) # Obtengo el valor del CIR
                        li $v0 1
                        syscall
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_in_bool_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        move $t0 $zero # flag = 0
            
                        # 1. Leo el input del usuario
                        li $v0, 12
                        syscall
            
                        move $t3, $v0
            
                        li $v0 4
                        la $a0 new_line
                        syscall
            
                        move $v0, $t3
            
                        # 2. Verifico si es igual a "1" o a "t"
            
                        li $t1, '1'
                        li $t2, 't'
            
                        beq $t1, $v0, m_in_bool_true
                        beq $t2, $v0, m_in_bool_true
                        b m_in_bool_default
            
                        m_in_bool_true:
                        addiu $t0 $t0 1
            
                        m_in_bool_default:
            
                        li $v0 9
                        li $a0 8
                        syscall
            
                        move $a0, $v0
            
                        lw $t1, VTABLE_Bool
                        sw $t1, 0($v0)
                        sw $t0, 4($v0)
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_bool_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # 1. Recupero el bool
                        lw $a0 8($fp)
                        lw $a0 4($a0) # Obtengo el valor del CIR
                        move $t0 $zero
                        addiu $t0 $t0 1
            
                        beq $a0 $t0 m_out_bool_true
            
                        m_out_bool_false:
                            li $v0 4
                            la $a0 false
                            syscall
            
                            b m_out_bool_default
            
                        m_out_bool_true:
                            li $v0 4
                            la $a0 true
                            syscall
            
                        m_out_bool_default:
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_in_double_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        # 1. Creo el CIR
                        li $v0 9
                        li $a0 12
                        syscall
            
                        move $a0 $v0
            
                        la $t0 VTABLE_Double
                        sw $t0 0($a0)
            
                        # 2. Leo el double
                        li $v0 7
                        syscall
            
                        # 3. Guardo el double
                        swc1 $f0 4($a0)
                        swc1  $f1 8($a0)
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_double_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        sw $a0 8($fp)
            
                        # 1. Recupero el double y lo imprimo
                        lwc1 $f12 4($a0)
                        lwc1 $f13 8($a0)
                        li $v0 3
                        syscall
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_array_int_0_0:
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        lw $t1 8($fp)
            
                        # 1. Recupero la longitud del arreglo
                        lw $t0 4($t1)
            
                        # 2. Verifico si dim > 0
                        la $a0 left_bracket
                        li $v0 4
                        syscall
            
                        beq $t0 $zero m_out_array_int_exit
            
                        # 3. Si dim > 0, iteramos por cada CIR
            
            
                        addiu $t1 $t1 4 # Aumento index
            
                        m_out_array_int_loop:
                            sw $fp 0($sp)
                            addiu $sp $sp -4
            
            
                            addiu $t1 $t1 4 # Aumento index
                            lw $a0 0($t1) # Obtengo sig. pos
                            sw $a0 0($sp) # Guardo en pila
                            addiu $sp $sp -8
                            jal m_out_int_0_0 # Imprimo elem
            
            
            
                            addiu $sp $sp 8
                            lw $fp 0($sp)
                            addiu $sp $sp 4
            
                            subiu $t0 $t0 1 # Obtenemos elem restantes
            
                            beq $t0 $zero m_out_array_int_exit # Si ya no hay elem siguientes, salgo
            
                            la $a0 comma
                            li $v0 4
                            syscall
            
                            b m_out_array_int_loop
            
                        m_out_array_int_exit:
                            la $a0 right_bracket
                            li $v0 4
                            syscall
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_array_str_0_0:  #+++++++++++++++++++++++++++++++++++
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        lw $t1 8($fp)
            
                        # 1. Recupero la longitud del arreglo
                        lw $t0 4($t1)
            
                        
                        la $a0 left_bracket
                        li $v0 4
                        syscall
            
                        # 2. Verifico si dim > 0
                        beq $t0 $zero m_out_array_str_exit
            
                        # Guardamos variables intermedias para que no se pierdan
            
                        ## v1: Index
                        addiu $t1 $t1 4 # Aumento index
                        sw $t1, 0($sp)
                        addiu $sp $sp -4
            
                        ## v2: Contador
                        sw $t0, 0($sp)
                        addiu $sp $sp -4
            
                        # 3. Si dim > 0, iteramos por cada CIR
            
                        m_out_array_str_loop:
                            sw $fp 0($sp)
                            addiu $sp $sp -4
            
                            lw $t1, 12($sp) # Recupero index
                            addiu $t1 $t1 4 # Aumento index
                            sw $t1, 12($sp) # Recupero index
                            
            
                            lw $a0 0($t1) # Obtengo sig. pos
                            sw $a0 0($sp) # Guardo en pila
                            addiu $sp $sp -8
                            jal m_out_str_0_0 # Imprimo elem
                            
                            addiu $sp $sp 8
                            lw $fp 0($sp)
                            addiu $sp $sp 4
            
            
                            lw $t0, 4($sp)
                            subiu $t0 $t0 1 # Obtenemos elem restantes
                            sw $t0, 4($sp)
                            
                            beq $t0 $zero m_out_array_str_exit # Si ya no hay elem siguientes, salgo
            
                            la $a0 comma
                            li $v0 4
                            syscall
            
                            b m_out_array_str_loop
            
                        m_out_array_str_exit:
                            la $a0 right_bracket
                            li $v0 4
                            syscall
            
                        # Desalojo variables locales
                        addiu $sp $sp 8
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_array_bool_0_0: #------------------------------
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        lw $t1 8($fp)
            
                        # 1. Recupero la longitud del arreglo
                        lw $t0 4($t1)
            
                        la $a0 left_bracket
                        li $v0 4
                        syscall
                        
                        # 2. Verifico si dim > 0
                        beq $t0 $zero m_out_array_bool_exit
            
                        # Guardamos variables intermedias para que no se pierdan
            
                        ## v1: Index
                        addiu $t1 $t1 4 # Aumento index
                        sw $t1, 0($sp)
                        addiu $sp $sp -4
            
                        ## v2: Contador
                        sw $t0, 0($sp)
                        addiu $sp $sp -4
            
            
                        # 3. Si dim > 0, iteramos por cada CIR
                        
            
                        m_out_array_bool_loop:  
                            sw $fp 0($sp)
                            addiu $sp $sp -4
            
                            lw $t1, 12($sp) # Recupero index
                            addiu $t1 $t1 4 # Aumento index
                            sw $t1, 12($sp) # Recupero index
                            
                            lw $a0 0($t1) # Obtengo sig. pos
                            sw $a0 0($sp) # Guardo en pila
                            addiu $sp $sp -8
                            jal m_out_bool_0_0 # Imprimo elem
            
                            addiu $sp $sp 8
                            
                            lw $fp 0($sp)
                            addiu $sp $sp 4
            
                            #cargamos el contador
                            lw $t0, 4($sp)
                            subiu $t0 $t0 1 # Obtenemos elem restantes
                            sw $t0, 4($sp)
            
                            beq $t0 $zero m_out_array_bool_exit # Si ya no hay elem siguientes, salgo
            
                            la $a0 comma
                            li $v0 4
                            syscall
            
                            b m_out_array_bool_loop
            
                        m_out_array_bool_exit:
                            la $a0 right_bracket
                            li $v0 4
                            syscall
            
                        # Desalojo variables locales
                        addiu $sp $sp 8
            
                        # --
                        lw $ra 4($sp)
                        addiu $sp $sp 4
                        jr $ra
            
                    m_out_array_double_0_0: #++++++++++++++++++++++++++++++++++
                        move $fp $sp
                        sw $ra 0($sp)
                        addiu $sp $sp -4
            
                        lw $t1 8($fp)
            
                        # 1. Recupero la longitud del arreglo
                        lw $t0 4($t1)
            
                        
                        la $a0 left_bracket
                        li $v0 4
                        syscall
                        
                        # 2. Verifico si dim > 0
                        beq $t0 $zero m_out_array_double_exit
            
                        # Guardamos variables intermedias para que no se pierdan
            
                        ## v1: Index
                        addiu $t1 $t1 4 # Aumento index
                        sw $t1, 0($sp)
                        addiu $sp $sp -4
            
                        ## v2: Contador
                        sw $t0, 0($sp)
                        addiu $sp $sp -4
            
                        # 3. Si dim > 0, iteramos por cada CIR
            
                        m_out_array_double_loop:
                            sw $fp 0($sp)
                            addiu $sp $sp -4
            
                            lw $t1, 12($sp) # Recupero index
                            addiu $t1 $t1 4 # Aumento index
                            sw $t1, 12($sp) # Recupero index
            
                            lw $a0 0($t1) # Obtengo sig. pos
                            sw $a0 0($sp) # Guardo en pila
                            addiu $sp $sp -8
                            jal m_out_double_0_0 # Imprimo elem
                            
                            addiu $sp $sp 8
                            lw $fp 0($sp)
                            addiu $sp $sp 4
            
            
            
            
                            #cargamos el contador
                            lw $t0, 4($sp)
                            subiu $t0 $t0 1 # Obtenemos elem restantes
                            sw $t0, 4($sp)
                            
                            beq $t0 $zero m_out_array_double_exit # Si ya no hay elem siguientes, salgo
            
                            la $a0 comma
                            li $v0 4
                            syscall
            
                            b m_out_array_double_loop
            
                        m_out_array_double_exit:
                            la $a0 right_bracket
                            li $v0 4
                            syscall
            
                        # Desalojo variables locales
                        addiu $sp $sp 8
            
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
                	#addiu $t2, $t1, 4 # Sumo len + 4 VT
                	li $a0, 8 # reservo 4 bytes para VT y 4 bytes para el int
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
                	addiu $sp $sp 4
                	lw $fp 0($sp)
                	jr $ra
               \s
                concat: #----------------------------------------------------------
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
                	
                	addiu $sp $sp 4
                	lw $fp 4($sp)
                	addiu $sp $sp 4
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
                	
                	addiu $sp $sp 4
                	lw $fp 4($sp)
                	addiu $sp $sp 4
               \s
                	sw $a0, 0($sp) # Guardamos l2 en la pila
                	addiu $sp $sp -4
               \s
                	## Paso3: Reservar memoria len1 + len2
                	## - Guardar la dirección de v0 en un registro para no perderlo
                	## - Crear CIR de Str nuevo, incluye guardar la VT
               \s
                	lw $t0, 4($sp) # Obtengo el CIR de L1
                	lw $t0, 4($t0) # Obtengo el len de L1
                	lw $t1, 8($sp) # Obtengo L2
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
                	addiu $sp $sp 4
                	jr $ra
               \s
                save_str: # Escribe el contenido de $a0 en la direccion apuntada por v0 + 4 #----------------------------------------------------------
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
                    .word Object
                    
                .text
                Object:
                	jr $ra
                    
                .data
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
                nullPointerExceptionMessage:
                    .asciiz "ERROR: OBJETO NULO"
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
                	
                nullPointerException:
                    	la $a0 nullPointerExceptionMessage
                    	li $v0, 4
                    	syscall
                   \s
                    	li $v0, 17
                    	li $a0, 1
                    	syscall
                   \s
               \s""";

        return s;
    }
}
