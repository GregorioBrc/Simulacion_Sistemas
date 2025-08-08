package model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Nodo {
    private int Id;

    private String Nombre;
    private String Descripcion;
    private String Token;

    private boolean Is_Activ;

    protected double Costo;

    private ArrayList<Nodo> Vertice;

    private int[] id_Vertice;
    private Point Location;

    private Dimension Dm;

    private String NombreImagen; // Nueva propiedad

    public Nodo(Nodo Nd) {
        Id = Nd.getId();
        Nombre = Nd.getNombre();
        Descripcion = Nd.getDescripcion();
        Is_Activ = Nd.isIs_Activ();
        Vertice = Nd.getVertice();
        id_Vertice = Nd.getId_Vertice();
        Token = Nd.getToken();
        Costo = Nd.getCosto();
        Location = Nd.Location;
        Dm = Nd.Dm;
        NombreImagen = Nd.getNombreImagen();
    }
    
    public void setCosto(double c) { Costo = c; }

    public Nodo(int id, String token, String nombre, String descripcion, boolean is_Activ, double costo, String nombreImagen) {
        Id = id;
        Nombre = nombre;
        Token = token;
        Descripcion = descripcion;
        Is_Activ = is_Activ;
        Costo = costo;
        Vertice = new ArrayList<Nodo>();
        Location = new Point();
        Dm = new Dimension();
        NombreImagen = nombreImagen;
    }

    public double getCosto() {
        return Costo;
    }

    @Override
    public String toString() {
        int[] Ax_Aris = new int[Vertice.size()];

        for (int i = 0; i < Vertice.size(); i++) {
            Ax_Aris[i] = Vertice.get(i).Id;
        }

        return String.format("%d|%s|%s|%s|%b|%s|%s", Id, Token, Nombre, Descripcion, Is_Activ, Arrays.toString(Ax_Aris), NombreImagen);
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

    public Point getLocation() {
        return Location;
    }

    public void setLocation(Point location) {
        Location = location;
    }

    public void setLocation(int x, int y) {
        Location = new Point(x, y);
    }

    public int[] getId_Vertice() {
        return id_Vertice;
    }

    public Dimension getDm() {
        return Dm;
    }

    public void setDm(Dimension dm) {
        Dm = dm;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getNombreImagen() {
        return NombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        NombreImagen = nombreImagen;
    }
}
