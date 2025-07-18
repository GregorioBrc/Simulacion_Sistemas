package Class;

import java.util.ArrayList;
import java.util.Arrays;

public class Modificador_Nodo extends Modificador {

    private ArrayList<Nodo> Nodos_Afect;
    private int[] id_Nodos_Afect;

    public Modificador_Nodo(Nodo Nd, double Valor, String Descrp_Modif) {
        super(Nd, Valor, Descrp_Modif);
    }

    public Modificador_Nodo(int id, String nombre, String descripcion, boolean is_Activ, double Valor,
            String Descrp_Modif) {
        super(id, nombre, descripcion, is_Activ, Valor, Descrp_Modif);
        Nodos_Afect = new ArrayList<Nodo>();
    }

    public ArrayList<Nodo> getNodos_Afect() {
        return Nodos_Afect;
    }

    public void setNodos_Afect(ArrayList<Nodo> nodos_Afect) {
        Nodos_Afect = nodos_Afect;
    }

    public int[] getId_Nodos_Afect() {
        return id_Nodos_Afect;
    }

    public void setId_Nodos_Afect(int[] id_Nodos_Afect) {
        this.id_Nodos_Afect = id_Nodos_Afect;
    }

    @Override
    public String toString() {
        int[] Ax = new int[Nodos_Afect.size()];
        for (int i = 0; i < Ax.length; i++) {
            Ax[i] = Nodos_Afect.get(i).getId();
        }
        return "Nd_Modi|" + super.toString() + "|" + Arrays.toString(Ax);
    }
}
