// Prueba de atributo público
// Salida esperada:
// 25
// 26
// 26

class Persona {
pub Int edad;
pub Int dni;
}

impl Persona {

fn Int cumplir_anios(){
edad = edad + 1;
ret edad;
}

.(){
edad = 0;
dni = 0;
}

}

start{
Persona p;

p = new Persona();

p.edad = 25;
p.dni = 12345678;

(IO.out_int(p.edad));
(IO.out_str("\n"));

(IO.out_int(p.cumplir_anios()));
(IO.out_str("\n"));

(IO.out_int(p.edad));
}