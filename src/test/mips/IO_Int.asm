.text

main:
	sw $fp 0($sp)
	addiu $sp $sp -4
	jal start
	b exit
	
start:
	move $fp $sp
	sw $ra 0($sp)
	addiu $sp $sp -4
	
	# Int a;
	li $v0 9
	li $a0 8
	syscall
	
	lw $t0, VTABLE_Int
	sw $t0, 0($v0)
	li $t0, 0
	sw $t0, 4($v0)
	
	move $a0, $v0
	
	sw $a0, 0($sp)
	addiu $sp $sp -4
	
	# IO.in_int()
	sw $fp 0($sp)
	addiu $sp $sp -4
	
	jal IO_in_int
	
	# IO.out_int(IO.in_int())
	sw $fp 0($sp)
	addiu $sp $sp -4
	
	sw $a0 0($sp)
	addiu $sp $sp -4
	
	jal IO_out_int
	
	# desalojo variables locales
	addiu $sp $sp 4
	
	# final de cuerpo de start
	lw $ra 4($sp)
	addiu $sp $sp 8
	lw $fp 0($sp)
	
	# exit
	jr $ra
	
# --------------------------------------------
# --------------------------------------------
# --------------------------------------------

save_str:
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

length:
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
		
	# Creo CIR
	addiu $t2, $t1, 4 # Sumo len + 4 VT
	move $a0, $t1 # Muevo el resultado en a0
	li $v0, 9 # Reservo bytes
	syscall
	
	lw $t0, VTABLE_Int
	sw $t0, 0($v0)
	sw $t1, 4($v0)
	
	move $a0, $v0 
	
	# Final de start
	lw $ra 4($sp)
	addiu $sp $sp 12
	lw $fp 0($sp)
	jr $ra
		
concat:
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
	
	sw $a0, 0($sp) # Guardamos l1 en la pila
	addiu $sp $sp -4
	
	## Paso2: Calcular longitud de la segunda cadena
	sw $fp 0($sp)
	addiu $sp $sp -4
	
	lw $a0 8($fp) # Buscamos el segundo param de concat
	sw $a0 0($sp) # Lo guardamos en la pila
	addiu $sp $sp -4
	jal length
	
	sw $a0, 0($sp) # Guardamos l2 en la pila
	addiu $sp $sp -4
	
	## Paso3: Reservar memoria len1 + len2
	## - Guardar la dirección de v0 en un registro para no perderlo
	## - Crear CIR de Str nuevo, incluye guardar la VT
	
	lw $t0, -4($fp) # Obtengo el CIR de l1
	lw $t0, 4($t0) # Obtengo el len de l1
	lw $t1, -8($fp) # Obtengo l2
	lw $t1, 4($t1) # Obtengo el len de l2
	
	#move $a0, $zero
	add $a0, $t0, $t1 # a0 = l1 + l2
	addi $a0, $a0, 4 # a0 = vt + len
	
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
	addiu $sp $sp 16
	lw $fp 0($sp)
	jr $ra
	

IO_in_str:
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
	
	loop_IO_in_str:
		lb $t3 ($a0)
		addi $a0 $a0 1
		addi $t2 $t2 1
		bne $t3 $zero loop_IO_in_str
	
	li $v0 9
	move $a0 $t2
	syscall #Reservo espacio
	
	la $a0, VTABLE_Str
	sw $a0, 0($v0)
	
	move $a0, $v0
	
	addiu $v0 $v0 4
	
	move $t1 $v0 # Guardo puntero destino
	move $t3 $sp # Guardo puntero origen
	
	loop_IO_in_str_2:
		lb $t2 ($t3)
		sb $t2 ($t1)
		addi $t3 $t3 1
		addi $t1 $t1 1
		bne $t2 $zero loop_IO_in_str_2
		
	addiu $sp $sp 1024
	
	# --------------------------------------


	# Recuperar el valor del return address
	lw $ra 4($sp)

	# Actualizamos el valor el fp para que apunte al fp del llamador,
	addiu $sp $sp 8 # el valor a aumentar depende del tamano del registro de activacion
	lw $fp 0($sp)

	jr $ra
	
IO_out_str:
	move $fp $sp
	sw $ra 0($sp)
	addiu $sp $sp -4
	
	# Cuerpo de length
	lw $t0 4($fp) # Cargo el objeto
	addiu $t0 $t0 4
	li $t1 -1 # Guardo un contador
	
	loop_IO_out_str:
		lb $a0 ($t0) # t0 = char actual
		addi $t0 $t0 1 # Sumo un byte al valor str
		addi $t1 $t1 1 # Sumo en uno el contado
		li $v0 11
		syscall
		bne $a0 $zero loop_IO_out_str # Si llego a \0, salgo
	
	move $a0, $t1 # Muevo el resultado en a0
	
	# Final de start
	lw $ra 4($sp)
	addiu $sp $sp 12
	lw $fp 0($sp)
	jr $ra
	
IO_in_int:
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
	addiu $sp $sp 8
	lw $fp 0($sp)
	jr $ra
	

IO_out_int:
	move $fp $sp
	sw $ra 0($sp)
	addiu $sp $sp -4
	
	# 1. Recupero el int y lo imprimo
	lw $a0 4($a0)
	li $v0 1
	syscall
	
	# --
	lw $ra 4($sp)
	addiu $sp $sp 12
	lw $fp 0($sp)
	jr $ra
	
exit:
	li $v0, 10
	syscall
	
.data 
def_Str: .asciiz "0"

hello_world: .asciiz "Hello World"
,_Im_Trudy: .asciiz ", I'm Christopher"

hello_world_len: .word 11
,_Im_Trudy_len: .word 17

VTABLE_Str:
	.word length
	.word concat
	
VTABLE_IO:
    	.word IO_out_str
    	.word IO_out_int
    	.word IO_in_str
    	.word IO_in_int
    	#.word IO_out_bool
    	#.word IO_out_double
    	#.word IO_out_array_int
    	#.word IO_out_array_str
    	#.word IO_out_array_bool
    	#.word IO_out_array_double
    	#.word IO_in_bool
    	#.word IO_in_double
VTABLE_Int:
	
	
	
