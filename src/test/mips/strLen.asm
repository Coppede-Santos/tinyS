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
	
	# Str s;
	li $v0 9
	la $a0 8
	syscall
	
	la $a0, VTABLE_Str
	sw $a0, 0($v0)
	la $a0, def_Str
	sw $a0, 4($v0)
	
	move $a0, $v0
	
	sw $a0 0($sp)
	addiu $sp $sp -4
	
	# s = "Hello world"
	## s
	lw $a0 -4($fp)
	
	## "Hello world"
	la $t0, hello_world_len
	lw $t0, 0($t0)
	addi $a0, $t0, 4
	
	li $v0 9
	syscall
	
	la $t0, VTABLE_Str
	sw $t0, 0($v0)
	la $a0, hello_world
	jal save_str
	
	move $a0, $v0
	
	lw $t0, -4($fp)
	
	move $t0, $a0
	sw $t0, -4($fp)
	
	# s.len();
	sw $fp 0($sp)
	addiu $sp $sp -4
	
	lw $a0 -4($fp)
	sw $a0 0($sp)
	addiu $sp $sp -4
	
	lw $a0 -4($fp) # Accedo al obj
	lw $t0 0($a0) # Accedo a la vtable
	lw $t0 0($t0) # Accedo a length
	
	jalr $t0
	
	lw $a0, 4($a0)
	li $v0, 1
	syscall
	
	# desalojo variables locales
	addiu $sp $sp 4
	
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
concat:

exit:
	li $v0, 10
	syscall
	
.data 
def_Str: .asciiz "0"

hello_world: .asciiz "Hello World"
hello_world_len: .word 11

VTABLE_Str:
	.word length
	.word concat
	
VTABLE_Int:
	
	
	
