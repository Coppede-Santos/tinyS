.data

.text
main:

	sw $fp 0($sp)

	addiu $sp $sp -4

	jal m_start_1_5

	lw $fp 0($sp)

	addiu $sp $sp 4

	b exit

	m_start_1_5: # Label del metodo

	move $fp $sp #El frame apunta al enlace dinamico

	sw $ra 0($sp) #guardamos en la pila el return address

	addiu $sp $sp -4 #restamos 4 bytes para guardar el return address

	addi $sp $sp -12 #restamos 4 bytes para cada variable local

	li $v0, 9  # Solicitar espacio en memoria

	li $a0, 8  # 4 bytes y su vtable

	syscall 

	la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR

	li $t0, 0 # Guardamos el valor en la CIR en un temporal

	sw $t0, 4($v0) #Guardamos el valor en la CIR

	move $a0, $v0 # La dirección del objeto Int queda en $a0

	sw $a0 -4($fp)

	li $v0, 9  # Solicitar espacio en memoria

	li $a0, 8  # 4 bytes y su vtable

	syscall 

	la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR

	li $t0, 0 # Guardamos el valor en la CIR en un temporal

	sw $t0, 4($v0) #Guardamos el valor en la CIR

	move $a0, $v0 # La dirección del objeto Int queda en $a0

	sw $a0 -8($fp)

	li $v0, 9  # Solicitar espacio en memoria

	li $a0, 8  # 4 bytes y su vtable

	syscall 

	la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR

	li $t0, 0 # Guardamos el valor en la CIR en un temporal

	sw $t0, 4($v0) #Guardamos el valor en la CIR

	move $a0, $v0 # La dirección del objeto Int queda en $a0

	sw $a0 -12($fp)

	addiu $a0 $fp , -4 #Devolvemos la direccion de la variable en la pila a_1

	sw $a0, 0($sp)

	addi $sp, $sp, -4

	li $v0, 9  # Solicitar espacio en memoria

	li $a0, 8  # 4 bytes y su vtable

	syscall 

	la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR

	li $t0, 10 # Guardamos el valor en la CIR en un temporal

	sw $t0, 4($v0) #Guardamos el valor en la CIR

	move $a0, $v0 # La dirección del objeto Int queda en $a0

	lw $t0, 4($sp)

	addi $sp, $sp, 4

	sw $a0, 0($t0)

	addiu $a0 $fp , -8 #Devolvemos la direccion de la variable en la pila b_2

	sw $a0, 0($sp)

	addi $sp, $sp, -4

	li $v0, 9  # Solicitar espacio en memoria

	li $a0, 8  # 4 bytes y su vtable

	syscall 

	la $t0, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t0, 0($v0) #guardamos la dirección de la vtableInt en la CIR

	li $t0, 0 # Guardamos el valor en la CIR en un temporal

	sw $t0, 4($v0) #Guardamos el valor en la CIR

	move $a0, $v0 # La dirección del objeto Int queda en $a0

	lw $t0, 4($sp)

	addi $sp, $sp, 4

	sw $a0, 0($t0)

	addiu $a0 $fp , -12 #Devolvemos la direccion de la variable en la pila c_3

	sw $a0, 0($sp)

	addi $sp, $sp, -4

	#Empieza la expresion binaria

	#Empieza codigo para expBin

	lw $a0 ,-4($fp) #Buscamos la variable en la pila

	sw $a0, 0($sp) #Guarda la CIR de la expresión izq en la pila

	addi $sp, $sp, -4 #movemos el puntero de la pila

	lw $a0 ,-8($fp) #Buscamos la variable en la pila

	lw $t0, 4($sp) # cargamos la CIR de la exp izquierda en t0

	addi $sp, $sp, 4 # sacamos de la pila la exp izquierda

	move $t1, $a0 #guardamos la direccon de la CIR de exp derecha en t1

	lw $t0 4($t0) #Cargar el valor del int izquierdo

	lw $t1 4($t1) #Cargar el valor del int derecho

	beqz $t1, DivisionByZeroException #Si el lado derecho es igual a cero saltamos un error

	div $t0, $t0, $t1 #dividimos los dos int

	li $a0, 8  # 4 bytes y su vtable

	li $v0, 9  # Solicitar espacio en memoria

	syscall 

	move $a0 $v0 #La dirección del objeto Int queda en $a0

	la $t1, VTABLE_Int # Cargar la dirección de la vtable de Int en un temporal

	sw $t1, 0($a0) #guardamos la dirección de la vtableInt en la CIR

	sw $t0, 4($a0) #guardar el valor del int

	#Termina codigo para expBin

	lw $t0, 4($sp)

	addi $sp, $sp, 4

	sw $a0, 0($t0)

	li $a0, 0 # Valor de retorno por defecto

	m_start_1_5_end: # Label para el return del metodo

	lw $ra 0($fp) #cargamos el return address

	addiu $sp $sp 12 #limpiamos la pila de las variables locales

	addiu $sp $sp 4 #limpiamos la pila del return address

	jr $ra #salimos del metodo

	exit:

	li $v0, 10  # syscall para exit

	syscall  # salir del programa

	
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
        new_line: .asciiz "\n"
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
                bne $a0 $zero loop_m_out_str # Si llego a \0, salgo

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
    	
    .data
    
    VTABLE_Array:
        .word array
    	.word array_length
    
    .text
    array:
    array_length:
    	move $fp $sp
    	sw $ra 0($sp)
    	addiu $sp $sp -4
    
    	# 1. Recupero CIR Array
    	lw $t0, 4($fp)
    
    	# 2. Recupero longitud del array
    	lw $t0, 4($t0)
    
    	# 3. Creo nuevo CIR Int
    	li $v0, 9
    	li $a0, 8
    	syscall
    
    	lw $t1, VTABLE_Int
    	sw $t1, 0($v0)
    	sw $t0, 4($v0)
    
    	move $a0, $v0
    
    	# --
    	lw $ra 4($sp)
    	addiu $sp $sp 4
    	jr $ra
    	
    .data
        VTABLE_Str:
            .word str
        	.word length
        	.word concat
    
    .text
    
    str:
    
    length:
    	# Comienzo de start
    	move $fp $sp
    	sw $ra 0($sp)
    	addiu $sp $sp -4
    
    	# Cuerpo de length
    	lw $a0 4($fp) # Cargo el objeto
    	addiu $a0 $a0 4
    	li $t1 -1 # Guardo un contador
    
    	loop:
    		lb $t0 ($a0) # t0 = char actual
    		addi $a0 $a0 1 # Sumo un byte al valor str
    		addi $t1 $t1 1 # Sumo en uno el contador
    		bne $t0 $zero loop # Si llego a \0, salgo
    
    	# Creo CIR Int
    	#addiu $t2, $t1, 4 # Sumo len + 4 VT
    	li $a0, 8 # reservo 4 bytes para VT y 4 bytes para el int
    	li $v0, 9 # Reservo bytes
    	syscall
    
    	lw $t0, VTABLE_Int
    	sw $t0, 0($v0)
    	sw $t1, 4($v0)
    
    	move $a0, $v0
    
    	# Final de start
    	lw $ra 4($sp)
    	addiu $sp $sp 4
    	lw $fp 0($sp)
    	jr $ra
    
    concat: #----------------------------------------------------------
    	move $fp $sp
    	sw $ra 0($sp)
    	addiu $sp $sp -4
    
    	# Cuerpo de concat
    
    	## Paso1: Calcular longitud de la primer cadena
    	sw $fp 0($sp)
    	addiu $sp $sp -4
    
    	lw $a0 4($fp) # Buscamos el primer param de concat
    	sw $a0 0($sp) # Lo guardamos en la pila
    	addiu $sp $sp -4
    	jal length

    	addiu $sp $sp 4
    	lw $fp 4($sp)
    	addiu $sp $sp 4
    
    	sw $a0, 0($sp) # Guardamos l1 en la pila
    	addiu $sp $sp -4
    
    	## Paso2: Calcular longitud de la segunda cadena
    	sw $fp 0($sp)
    	addiu $sp $sp -4
    
    	lw $a0 8($fp) # Buscamos el segundo param de concat
    	sw $a0 0($sp) # Lo guardamos en la pila
    	addiu $sp $sp -4
    	jal length

    	addiu $sp $sp 4
    	lw $fp 4($sp)
    	addiu $sp $sp 4
    
    	sw $a0, 0($sp) # Guardamos l2 en la pila
    	addiu $sp $sp -4
    
    	## Paso3: Reservar memoria len1 + len2
    	## - Guardar la dirección de v0 en un registro para no perderlo
    	## - Crear CIR de Str nuevo, incluye guardar la VT
    
    	lw $t0, 4($sp) # Obtengo el CIR de L1
    	lw $t0, 4($t0) # Obtengo el len de L1
    	lw $t1, 8($sp) # Obtengo L2
    	lw $t1, 4($t1) # Obtengo el len de l2
    
    	#move $a0, $zero
    	add $a0, $t0, $t1 # a0 = l1 + l2
    	addi $a0, $a0, 5 # a0 = vt + len + padding
    
    	li $v0, 9
    	syscall
    
    	move $a0, $v0 # Guardo la CIR
    
    	la $t0, VTABLE_Str
    	sw $t0, 0($v0) # Guardo la VT en el heap
    
    	## Paso4: Escribir cadena 1 en v0
    
    	lw $t0 4($fp) # Obtengo CIR de s1
    	addiu $t0 $t0 4 # Apunto t0 a s1.val
    
    	move $t2 $v0 # Apunto t2 al CIR de s1
    	addiu $t2 $t2 4 # Apunto t2 a s2.val
    
    	concat_loop_1:
    		lb $t1 ($t0) # Catga byte de s1
    		beq $t1 $zero exit_concat_loop_1# Si es \0, salgo del loop para no escribirlo
    		sb $t1 ($t2) # Escribe byte en rta
    		addiu $t0 $t0 1
    		addiu $t2 $t2 1
    		b concat_loop_1
    
    	exit_concat_loop_1:
    
    	## Paso5: Escribir cadena 2 en v0 + len1
    
    	lw $t0 8($fp) # Obtengo CIR de s2
    	addiu $t0 $t0 4 # Apunto t0 a s2.val
    
    	concat_loop_2:
    		lb $t1 ($t0) # Catga byte de s2
    		sb $t1 ($t2) # Escribe byte en rta
    		addiu $t0 $t0 1
    		addiu $t2 $t2 1
    		bne $t1 $zero concat_loop_2 # Si es \0, termino
    
    	# desalojo variables locales
    	addiu $sp $sp 8
    
    	# Final del concat
    	lw $ra 4($sp)
    	addiu $sp $sp 4
    	jr $ra
    
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
    
    eq_str: # Compara dos strings, devuelve un CIR Bool
    	lw $t0 4($sp) # Cargo CIR1
    	lw $t1 8($sp) # Cargo CIR2
    
    	addiu $t0 $t0 4 # Obtengo str1
    	addiu $t1 $t1 4 # Obtengo str2
    
    	move $t2 $zero
    	addiu $t2 $t2 1 # areEqual = true
    
    	li $v0 9
    	li $a0 8
    	syscall
    
    	la $t4 VTABLE_Bool
    	sw $t4 0($v0)
    
    	eq_str_loop:
    		lb $t3 ($t0)
    		lb $t4 ($t1)
    
    		bne $t3 $t4 eq_str_false
    
    		addiu $t0 $t0 1
    		addiu $t1 $t1 1
    
    		beq $t3 $zero eq_str_true
    		b eq_str_loop
    
    	eq_str_false:
    		move $t2 $zero
    		sw $t2 4($v0)
    
    		b eq_str_return
    
    	eq_str_true:
    		sw $t2 4($v0)
    
    	eq_str_return:
    		move $a0 $v0
    		jr $ra
    		
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
    
    db_one: .double 1.0
    db_cero: .double 0.0
    
    .data
    DivisionByZeroExceptionMessage:
    	.asciiz "ERROR: DIVISION POR CERO"
    ArrayIndexOutOfBoundsExceptionMessage:
    	.asciiz "ERROR: INDICE DE ARRAY FUERA DE RANGO"
    NegativeArraySizeExceptionMessage:
    	.asciiz "ERROR: LONGITUD DE ARRAY NEGATIVO"
    nullPointerExceptionMessage:
        .asciiz "ERROR: OBJETO NULO"
    
    .text
    DivisionByZeroException:
    	la $a0 DivisionByZeroExceptionMessage
    	li $v0, 4
    	syscall
    
    	li $v0, 17
    	li $a0, 1
    	syscall
    
    ArrayIndexOutOfBoundsException:
    	la $a0 ArrayIndexOutOfBoundsExceptionMessage
    	li $v0, 4
    	syscall
    
    	li $v0, 17
    	li $a0, 1
    	syscall
    
    NegativeArraySizeException:
    	la $a0 NegativeArraySizeExceptionMessage
    	li $v0, 4
    	syscall
    
    	li $v0, 17
    	li $a0, 1
    	syscall

    nullPointerException:
        	la $a0 nullPointerExceptionMessage
        	li $v0, 4
        	syscall
        
        	li $v0, 17
        	li $a0, 1
        	syscall
        
    
