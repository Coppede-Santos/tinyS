class Clasificador {
}

impl Clasificador {
.(){}

fn Int tipo(Double x){
if (x < 0){
ret -1;
}
else
if (x == 0){
ret 0;
}
else{
ret 1;
}
}

}

start{
Clasificador c;
Double n;

c = new Clasificador();
n = IO.in_double();

(IO.out_int(c.tipo(n)));
(IO.out_str("\n"));
}