package Class;

public class Modificador_Click extends Modificador {

    public Modificador_Click(Nodo Nd, double Valor, String Descrp_Modif) {
        super(Nd, Valor, Descrp_Modif);
    }

    public Modificador_Click(int id, String nombre, String descripcion, boolean is_Activ, double Valor,
            String Descrp_Modif) {
        super(id, nombre, descripcion, is_Activ, Valor, Descrp_Modif);
    }

    @Override
    public String toString() {
        return "Nd_Click|" + super.toString();
    }
}
