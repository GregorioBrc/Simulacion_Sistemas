package model;

import java.awt.Dimension;

public class Generador extends Nodo {

    private static final double CONST_SUBIDA_COSTO = 1.15;

    private double generadoxTick;
    private int cant;
    private double costoBase; // Para recalcular desde el valor inicial

    public Generador(Nodo nd, double gene) {
        super(nd);
        this.generadoxTick = gene;
        this.cant = 0;
        this.costoBase = getCosto(); // heredado de Nodo
        setDm(new Dimension(50, 50));
    }

    // ================== GETTERS / SETTERS ==================
    public int getCant() { return cant; }
    public void setCant(int c) { 
        this.cant = c; 
        recalcCostoDesdeBase();
    }

    public double getGeneradoxTick() { 
        return generadoxTick * cant; 
    }
    public void setGeneradoxTick(double generadoxTick) {
        this.generadoxTick = generadoxTick;
    }

    // ================== LÓGICA ==================
    public void recalcCostoDesdeBase() {
        setCosto(costoBase * Math.pow(CONST_SUBIDA_COSTO, cant));
    }

    public void Modificar_Gene(Modificador_Nodo n) {
        generadoxTick *= (1 + n.getValor_Modif());
    }

    public void Comprar_Uni(){
        cant += 1;
        setCosto(getCosto() * CONST_SUBIDA_COSTO);
    }

    @Override
    public String toString() {
        return "Nd_Gene|" + super.toString() + "|" + generadoxTick;
    }
}