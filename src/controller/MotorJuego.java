package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import javax.sound.sampled.*;
import java.net.URL;

import Misc.Tokens;
import model.Arbol;
import model.Billetera;
import model.Generador;
import model.Modificador_Click;
import model.Nodo;
import view.*;

public class MotorJuego implements ActionListener, Compra_Listener, Click_Listener, Camb_Arbol {
    private Ventana Vt;
    private Billetera Bill;
    private ArrayList<ArbolPanel> Arbs;
    private int Arbol_Indx = 0;
    private double Click_Token_Value = 1;
    private Clip musicaFondo;
    Timer Tiker;

    public MotorJuego() throws IOException {
        Arbs = new ArrayList<ArbolPanel>();
        Bill = new Billetera();
        Arbs.add(new ArbolPanel("Main", "fondo1.png", "background"));
        Arbs.add(new ArbolPanel("Dino", "Back_Dino.jpg", "dino"));
        Arbs.add(new ArbolPanel("Espacio", "Back_Space.png", "space"));
        Vt = new Ventana(Arbs.get(0), Arbs.size());

        Cargar_Listeners();
        Registrar_Tokens();
        CalcularGeneracionGlobal();
        reproducirMusicaFondo(Arbs.get(0).getMusic());

        Actualizar_Info_gene();
        Tiker = new Timer(10, this);
        Tiker.start();
        Vt.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(Tiker)) {
            Bill.tick();
            Actualizar_Info_gene();
        }
    }

    @Override
    public void Prerequisitos(Nodo Nd, int Modo) {
        if (Nd.getCosto() <= Bill.getCantidad(Nd.getToken())) {
            Bill.Restar(Nd.getToken(), Nd.getCosto());

            if (Modo == 0) {
                Arbs.get(Arbol_Indx).Activar_Nodo(Nd);
            } else if (Modo == 1) {
                Arbs.get(Arbol_Indx).Comprar_Generador(((Generador) Nd));
                Vt.getPn_Info().Cargar_Nodo(Nd);
            }

            CalcularGeneracionGlobal();
            Actualizar_Info_gene();
            Vt.repaint();
        }
    }

    @Override
    public void Click_Token() {
        Bill.Sumar_Tokens(Arbs.get(Arbol_Indx).getTree().getToken_a_Generar(), Click_Token_Value);
        Actualizar_Info_gene();
    }

    @Override
    public void Modi_Click_Token(Modificador_Click Nd) {
        Click_Token_Value *= Nd.getValor_Modif();
    }

    private void Cargar_Listeners() {
        for (ArbolPanel Arb : Arbs) {
            Arb.setCom_Listener(this);
            Arb.setCli_List(this);
        }

        Vt.setCamb_Arb_List(this);
    }

    private void Registrar_Tokens() {
        for (int i = 0; i < Arbs.size(); i++) {
            for (String Nom : Arbs.get(i).getTree().getToken_a_Generar()) {
                Bill.registrarToken(Nom, 0);
            }
        }
    }

    private void Actualizar_Info_gene() {
        ArrayList<String> Ax_Noms = Arbs.get(Arbol_Indx).getTree().getToken_a_Generar();
        Vt.getPn_Gene().Actualizar(Bill.getCantidades(Ax_Noms), Bill.getGeneracions(Ax_Noms));
    }

    private void reproducirMusicaFondo(String ruta) {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }

        try {
            URL url = getClass().getResource(ruta);
            System.out.println(url);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY); // Repetir indefinidamente
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la música de fondo: " + e.getMessage());
        }
    }

    private void CalcularGeneracionGlobal() {
        String[] todosLosTokens = Bill.getNombresDeTokens();

        for (String nombreToken : todosLosTokens) {
            double generacionTotal = 0.0;

            for (ArbolPanel arbolPanel : Arbs) {
                generacionTotal += arbolPanel.getTree().Get_Generado_Token(nombreToken);
            }

            Bill.actualizarGeneracion(nombreToken, generacionTotal);
        }
    }

    @Override
    public void Cambiar_Arbol(int Indx) {
        if (Indx == Arbol_Indx) {
            return;
        }
        if (Indx >= 0 && Indx < Arbs.size()) {
            Arbol_Indx = Indx;
            reproducirMusicaFondo(Arbs.get(Indx).getMusic());
            Vt.getPn_Gene().Reconstruir(Arbs.get(Indx).getTree().getToken_a_Generar());
            Actualizar_Info_gene();
            Vt.CambiArb(Arbs.get(Indx));
        }
    }

}
