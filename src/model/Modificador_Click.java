package model;

public class Modificador_Click extends Modificador {

    public Modificador_Click(Nodo Nd, double Valor, String Descrp_Modif) {
        super(Nd, Valor, Descrp_Modif);
    }

    @Override
    public String toString() {
        return "Nd_Click|" + super.toString();
    }
}
