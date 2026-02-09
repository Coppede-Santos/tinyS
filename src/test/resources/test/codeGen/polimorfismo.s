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

a = C.devuelvoHijos(0);
(a.queSoy());

a = C.devuelvoHijos(1);
(a.queSoy());

a = C.devuelvoHijos(2);
(a.queSoy());

a = C.devuelvoHijos(3);
(a.queSoy());


}


