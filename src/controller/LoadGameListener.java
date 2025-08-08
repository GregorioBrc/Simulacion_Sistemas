// src/controller/LoadGameListener.java
package controller;

public interface LoadGameListener {
    void onResumeSelected(String saveId); // Cargar partida seleccionada
    void onBackToMenu();
}