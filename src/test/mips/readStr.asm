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
	
	sw $fp 0($sp)
	addiu $sp $sp -4
	jal IO_in_str
	
	sw $fp 0($sp)
	addiu $sp $sp -4
	jal IO_in_str
	
	# final de cuerpo de start
	lw $ra 4($sp)
	addiu $sp $sp 8
	lw $fp 0($sp)
	
	# exit
	jr $ra
	

length:
	# Comienzo de start
	move $fp $sp
	sw $ra 0($sp)
	addiu $sp $sp -4
	
	# Cuerpo de length
	lw $a0 4($fp) # Cargo el objeto
	lw $a0 4($a0) # Cargo el valor del objeto str
	li $t1 -1 # Guardo un contador
	
	loop:
		lb $t0 ($a0) # t0 = char actual
		addi $a0 $a0 1 # Sumo un byte al valor str
		addi $t1 $t1 1 # Sumo en uno el contador
		bne $t0 $zero loop # Si llego a \0, salgo
	
	move $a0, $t1 # Muevo el resultado en a0
	li $v0, 1 # Imprimo str
	syscall
	
	# Final de start
	lw $ra 4($sp)
	addiu $sp $sp 12
	lw $fp 0($sp)
	jr $ra
	
save_str:
	
	
concat:

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

exit:
	li $v0, 10
	syscall
	
.data 
def_Str: .asciiz "0"

hello_world: .asciiz "Hello World"

VTABLE_Str:
	.word length
	.word concat
	
	
	
