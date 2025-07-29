// En una nueva carpeta, por ejemplo "GameLogic/" o "Core/"
package model;

import java.util.HashMap;
import java.util.Map;

public class Billetera {
    // Usamos un Mapa para poder tener múltiples tipos de tokens en el futuro
    // La clave es el nombre del token ("Energia", "Ciencia", etc.) y el valor es la cantidad.
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

    public void agregarTokens(String nombre, double cantidad) {
        tokens.put(nombre, tokens.getOrDefault(nombre, 0.0) + cantidad);
    }

    public void actualizarGeneracion(String nombre, double nuevaGeneracionTotal) {
        generacion_x_Tick.put(nombre, nuevaGeneracionTotal);
    }
    
    // Este método se llamaría en cada tick del MotorJuego
    public void tick() {
        for (String nombre : tokens.keySet()) {
            agregarTokens(nombre, generacion_x_Tick.getOrDefault(nombre, 0.0));
        }
    }

    public double getCantidad(String nombre) {
        return tokens.getOrDefault(nombre, 0.0);
    }

    public double getGeneracion(String nombre) {
        return generacion_x_Tick.getOrDefault(nombre, 0.0);
    }

    public String[] getNombresDeTokens() {
        return tokens.keySet().toArray(new String[0]);
    }
}