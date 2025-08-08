// src/controller/PauseListener.java
package controller;

public interface PauseListener {
    void onContinue();
    void onSave();      // Guardar progreso actual
    void onExitToMenu();
}