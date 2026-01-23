.data
Num1: .double 43.2
num2: .double 12.4

.text
main:

	la $t0, Num1
	ldc1 $f2, 0($t0)

	# Guardar E1 (double) en stack: reservar 8 bytes y almacenar
	addiu $sp, $sp, -8
	sdc1  $f2, 0($sp)

	la $t0, num2
	ldc1 $f4, 0($t0)

	# Recuperar E1 en $f6 y sumar con E2 ($f4)
	ldc1  $f6, 0($sp)
	add.d $f0, $f6, $f4    # resultado final en $f0 (double)
	add.d $f12, $f6, $f4   # también en $f12 para syscall imprimir double

	# limpiar stack
	addiu $sp, $sp, 8

	li $v0, 3      # syscall para imprimir double (usa $f12)
	syscall

	li $v0, 10     # syscall para salir
	syscall