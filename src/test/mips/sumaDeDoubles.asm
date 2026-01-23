.data
num1: .double 43.2
num2: .double 12.4

.text
main:

la $t0, num1
ldc1 $f2, 0($t0)

la $t0, num2
ldc1 $f4, 0($t0)

add.d $f12, $f2, $f4

li $v0, 3      # syscall para imprimir double
syscall

li $v0, 10     # syscall para salir
syscall