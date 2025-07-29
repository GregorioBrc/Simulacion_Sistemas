package model;

import java.awt.Dimension;

public class Generador extends Nodo {

    private double GeneradoxTick;

    public Generador(Nodo Nd, double gene) {
        super(Nd);
        GeneradoxTick = gene;
        setDm(new Dimension(50, 50));
    }

    public Generador(int id, String nombre, String descripcion, boolean is_Activ, double gene) {
        super(id, nombre, descripcion, is_Activ);
        GeneradoxTick = gene;
    }

    public void Modificar_Gene(Modificador_Nodo N) {
        GeneradoxTick *= (1 + N.getValor_Modif());
    }

    public double getGeneradoxTick() {
        return GeneradoxTick;
    }

    public void setGeneradoxTick(double generadoxTick) {
        GeneradoxTick = generadoxTick;
    }

    @Override
    public String toString() {
        return "Nd_Gene|" + super.toString() + "|" + GeneradoxTick;
    }
}
