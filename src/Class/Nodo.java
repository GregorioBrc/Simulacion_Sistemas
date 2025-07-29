package Class;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;

public class Nodo {
    private int Id;
    private String Nombre;
    private String Descripcion;
    private boolean Is_Activ;
    private JLabel Cuerpo;
    private boolean Select;
    private ArrayList<Nodo> Vertice;
    private int[] id_Vertice;

    public int[] getId_Vertice() {
        return id_Vertice;
    }

    public Nodo(Nodo Nd) {
        Id = Nd.getId();
        Nombre = Nd.getNombre();
        Descripcion = Nd.getDescripcion();
        Is_Activ = Nd.isIs_Activ();
        Vertice = Nd.getVertice();
        id_Vertice = Nd.getId_Vertice();
        Cuerpo = Nd.Cuerpo;
        
    }

    public Nodo(int id, String nombre, String descripcion, boolean is_Activ) {
        Id = id;
        Nombre = nombre;
        Descripcion = descripcion;
        Is_Activ = is_Activ;
        Cuerpo = new JLabel();
        Vertice = new ArrayList<Nodo>();
    }

    @Override
    public String toString() {
        int[] Ax_Aris = new int[Vertice.size()];

        for (int i = 0; i < Vertice.size(); i++) {
            Ax_Aris[i] = Vertice.get(i).Id;
        }

        return String.format("%d|%s|%s|%b|%s", Id, Nombre, Descripcion, Is_Activ, Arrays.toString(Ax_Aris));
    }

    public int getId() {
        return Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public JLabel getCuerpo() {
        return Cuerpo;
    }

    public boolean isIs_Activ() {
        return Is_Activ;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setIs_Activ(boolean is_Activ) {
        Is_Activ = is_Activ;
    }

    public void setVertice(ArrayList<Nodo> vertice) {
        Vertice = vertice;
    }

    public ArrayList<Nodo> getVertice() {
        return Vertice;
    }

    public void setId_Vertice(int[] id_Vertice) {
        this.id_Vertice = id_Vertice;
    }

    public boolean isSelect() {
        return Select;
    }

    public void setSelect(boolean select) {
        Select = select;
    }

    public void setCuerpo(JLabel cuerpo) {
        Cuerpo = cuerpo;
    }

    
}
