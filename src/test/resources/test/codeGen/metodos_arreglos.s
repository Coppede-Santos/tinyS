start{
Array Int arraio;
Array Bool arrboo;
Array Str arrstr;
Array Double arrdbl;

arraio = new Int[5];
arrboo = new Bool[5];
arrstr = new Str[5];
arrdbl = new Double[5];


arraio[0] = 1;
arraio[1] = 2;
arraio[2] = 3;
arraio[3] = 4;
arraio[4] = 5;
(IO.out_array_int(arraio));

arrboo[0] = true;
arrboo[1] = false;
arrboo[2] = true;
arrboo[3] = false;
arrboo[4] = true;
(IO.out_array_bool(arrboo));


arrdbl[0] = 1.0;
arrdbl[1] = 2.0;
arrdbl[2] = 3.0;
arrdbl[3] = 4.0;
arrdbl[4] = 5.0;
//(IO.out_array_double(arrdbl));

arrstr[0] = "hola";
arrstr[4] = "adios";
arrstr[2] = "que tal";
arrstr[3] = "como estas";
arrstr[1] = "que tal";
//(IO.out_array_str(arrstr));

}