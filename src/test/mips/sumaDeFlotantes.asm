.data
num1: .float 43.2
num2: .float 12.4

.text
main:

la $t0, num1
lwc1 $f2, 0($t0)

la $t0, num2
lwc1 $f4, 0($t0)

add.s $f12, $f2, $f4
li $v0, 2      # syscall para imprimir float
syscall

li $v0, 10     # syscall para salir
syscall