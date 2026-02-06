class StressArray {
pub Array Int v;
pub Int i, acc;
}

impl StressArray {

fn Int run(Int n){
v = new Int[n];
i = 0;
acc = 0;

while (i < n){
(IO.out_str("i: "));
(IO.out_int(i));
(IO.out_str("\n"));

if (i % 2 == 0){
v[i] = i;
}
else{
v[i] = -i;
}

(IO.out_str("v[i]: "));
(IO.out_int(v[i]));
(IO.out_str("\n"));

acc = acc + v[i];

(IO.out_str("acc: "));
(IO.out_int(acc));
(IO.out_str("\n"));

(++i);
}

ret acc;
}

.(){
i = 0;
acc = 0;
}

}

start{
StressArray s;
Int n;

s = new StressArray();
n = IO.in_int();

(IO.out_int(s.run(n)));
(IO.out_str("\n"));
}