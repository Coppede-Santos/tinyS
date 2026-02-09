class A{}


class B : A{}

class C : A{}

class D : B{}

impl A{.(){}
    fn queSoy(){
    (IO.out_str("Soy A"));
    (IO.out_str("\n"));
   }
}

impl B{.(){}
     fn queSoy(){
        (IO.out_str("Soy B"));
        (IO.out_str("\n"));
     }
}

impl C{.(){}
 st fn A devuelvoHijos(Int a){
     if (a == 0) {
     ret new A();
     }
     if (a == 1) {
          ret new B();
     }
     if (a == 2) {
          ret new C();
     }
     else{
        ret new D();
     }
 }
  fn queSoy(){
     (IO.out_str("Soy C"));
     (IO.out_str("\n"));
    }
 }

impl D{.(){}
 fn queSoy(){
    (IO.out_str("Soy D"));
    (IO.out_str("\n"));
   }
}


start{
A a;
A b;

if(a == nil && b == nil){
    (IO.out_str("correcto 0"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("incorrecto 0"));
    (IO.out_str("\n"));
}

if(a == b){
    (IO.out_str("correcto 1"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("incorrecto 1"));
    (IO.out_str("\n"));
}

a = new C();
b = new C();

if(a != b){
    (IO.out_str("correcto 2"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("incorrecto 2"));
    (IO.out_str("\n"));
}

if(a == b){
    (IO.out_str("incorrecto 2"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("correcto 2"));
    (IO.out_str("\n"));
}

a = b;

if(a == b){
    (IO.out_str("correcto 3"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("incorrecto 3"));
    (IO.out_str("\n"));
}

if(a != b){
    (IO.out_str("incorrecto 4"));
    (IO.out_str("\n"));
}else{
    (IO.out_str("correcto 4"));
    (IO.out_str("\n"));
}

(IO.out_double(5 / 2 ));
(IO.out_str("\n"));
(IO.out_int(5 div 2));

}


