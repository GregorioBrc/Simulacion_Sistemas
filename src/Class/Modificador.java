package Class;

public class Modificador extends Nodo {

    private double Valor_Modif;
    private String Descripcion_Modif;

    public Modificador(Nodo Nd, double Valor, String Descrp_Modif) {
        super(Nd);
        Valor_Modif = Valor;
        Descripcion_Modif = Descrp_Modif;
        Nd.getCuerpo().setSize(15, 15);
    }

    public Modificador(int id, String nombre, String descripcion, boolean is_Activ, double Valor, String Descrp_Modif) {
        super(id, nombre, descripcion, is_Activ);
        Valor_Modif = Valor;
        Descripcion_Modif = Descrp_Modif;
    }

    public double getValor_Modif() {
        return Valor_Modif;
    }

    public String getDescripcion_Modif() {
        return Descripcion_Modif;
    }

    public void setValor_Modif(double valor_Modif) {
        Valor_Modif = valor_Modif;
    }

    public void setDescripcion_Modif(String descripcion_Modif) {
        Descripcion_Modif = descripcion_Modif;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("|%f|%s", Valor_Modif, Descripcion_Modif);
    }

}
