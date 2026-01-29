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
if (i % 2 == 0){
v[i] = i;
}
else{
v[i] = -i;
}

acc = acc + v[i];
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