// src/controller/MenuListener.java
package controller;

public interface MenuListener {
    void onStartNew();                 // Ir al formulario
    void onLoadGame();                 // Ir al listado de partidas
    void onExit();                     // Cerrar app
}