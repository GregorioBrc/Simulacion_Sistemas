package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Billetera {
    // Usamos un Mapa para poder tener múltiples tipos de tokens en el futuro
    // La clave es el nombre del token ("Energia", "Ciencia", etc.) y el valor es la
    // cantidad.
    private Map<String, Double> tokens;
    private Map<String, Double> generacion_x_Tick;

    public Billetera() {
        this.tokens = new HashMap<>();
        this.generacion_x_Tick = new HashMap<>();
    }

    // Inicializa los tokens que usará el juego
    public void registrarToken(String nombre, double cantidadInicial) {
        tokens.put(nombre, cantidadInicial);
        generacion_x_Tick.put(nombre, 0.0);
    }

    public void Sumar_Tokens(String nombre, double cantidad) {
        tokens.put(nombre, tokens.getOrDefault(nombre, 0.0) + cantidad);
    }

    public void Sumar_Tokens(ArrayList<String> nombres, double cantidad) {
        for (String Nom : nombres) {
            tokens.put(Nom, tokens.getOrDefault(Nom, 0.0) + cantidad);
        }
    }

    public void Sumar_Tokens_Generado(String nombre, double cantidad) {
        generacion_x_Tick.put(nombre, generacion_x_Tick.getOrDefault(nombre, 0.0) + cantidad);
    }

    public void actualizarGeneracion(String nombre, double nuevaGeneracionTotal) {
        generacion_x_Tick.put(nombre, nuevaGeneracionTotal);
    }

    // Este método se llamaría en cada tick del MotorJuego
    public void tick() {
        for (String nombre : tokens.keySet()) {
            Sumar_Tokens(nombre, generacion_x_Tick.getOrDefault(nombre, 0.0));
        }
    }

    public void Restar(String Nm, double Val) {
        tokens.put(Nm, tokens.get(Nm) - Val);
    }

    public double getCantidad(String nombre) {
        return tokens.getOrDefault(nombre, 0.0);
    }

    public double[] getCantidades(ArrayList<String> NN) {
        double[] AxCant = new double[NN.size()];
        for (int i = 0; i < AxCant.length; i++) {
            AxCant[i] = tokens.getOrDefault(NN.get(i), 0.0);
        }
        return AxCant;
    }

    public double getGeneracion(String nombre) {
        return generacion_x_Tick.getOrDefault(nombre, 0.0);
    }

    public double[] getGeneracions(ArrayList<String> NN) {
        double[] AxGent = new double[NN.size()];
        for (int i = 0; i < AxGent.length; i++) {
            AxGent[i] = generacion_x_Tick.getOrDefault(NN.get(i), 0.0);
        }
        return AxGent;
    }

    public String[] getNombresDeTokens() {
        return tokens.keySet().toArray(new String[0]);
    }
}