class Circle{
    pub Double radius; // Radio del circulo
}

impl Circle {

    /* Calcula el area de un circulo dado su radio
    *  Recibe: Double r - radio del circulo
    *  Retorna: Double - area del circulo
    */
    fn Double area ( Double r ) {
        ret 3.1416 * r * r;
    }

    .(Double r){
        radius=r;
    }

}

start {
    Circle c;
    Double r;
    c=new Circle(0.0);
    r=IO.in_double();
    c.radius=r;
    (IO.out_double(c.area(c.radius)));
}
