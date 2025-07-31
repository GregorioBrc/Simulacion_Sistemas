package model;

import java.awt.Dimension;

public class Generador extends Nodo {

    private final double Const_Subida_Costo = 1.15;

    private double GeneradoxTick;
    private int Cant;

    public Generador(Nodo Nd, double gene) {
        super(Nd);
        GeneradoxTick = gene;
        Cant = 0;
        setDm(new Dimension(50, 50));
    }

    public int getCant() {
        return Cant;
    }

    public void Modificar_Gene(Modificador_Nodo N) {
        GeneradoxTick *= (1 + N.getValor_Modif());
    }

    public void Comprar_Uni(){
        Cant += 1;
        Costo *= Const_Subida_Costo;
    }

    public double getGeneradoxTick() {
        return GeneradoxTick * Cant;
    }

    public void setGeneradoxTick(double generadoxTick) {
        GeneradoxTick = generadoxTick;
    }

    @Override
    public String toString() {
        return "Nd_Gene|" + super.toString() + "|" + GeneradoxTick;
    }
}
