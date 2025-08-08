package controller;

public interface NewGameListener {
    void onSubmitNewGame(String username, String gameName); // Crear/arrancar
    void onBackToMenu();                                    // Volver
}