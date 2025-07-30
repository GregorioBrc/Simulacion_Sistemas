package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import model.Arbol;
import model.Billetera;
import model.Nodo;
import view.*;

public class MotorJuego implements ActionListener, Compra_Listener {
    private Ventana Vt;
    private Billetera Bill;
    private ArrayList<ArbolPanel> Arbs;
    private int Arbol_Indx = 0;
    Timer Tiker;

    public MotorJuego() throws IOException {
        Arbs = new ArrayList<ArbolPanel>();
        Bill = new Billetera();
        Arbs.add(new ArbolPanel("Main"));
        Vt = new Ventana(Arbs.get(0));

        Cargar_Listeners();
        Registrar_Tokens();

        Tiker = new Timer(1000, this);
        Tiker.start();
    }

    private void Cargar_Listeners() {
        for (ArbolPanel Arb : Arbs) {
            Arb.setCom_Listener(this);
        }
    }

    private void Registrar_Tokens() {
        for (int i = 0; i < Arbs.size(); i++) {
            for (String Nom : Arbs.get(i).getTree().getToken_a_Generar()) {
                Bill.registrarToken(Nom, 0);
                Bill.Sumar_Tokens_Generado(Nom, Arbs.get(i).getTree().Get_Generado_Token(Nom));
            }
        }
    }

    private void Actualizar_Tokens() {
        Arbol Ax = Arbs.get(Arbol_Indx).getTree();
        for (String Nm : Ax.getToken_a_Generar()) {
            Bill.actualizarGeneracion(Nm, Ax.Get_Generado_Token(Nm));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Bill.tick();
        ArrayList<String> Ax_Noms = Arbs.get(Arbol_Indx).getTree().getToken_a_Generar();
        Vt.getPn_Gene().Actualizar(Bill.getCantidades(Ax_Noms), Bill.getGeneracions(Ax_Noms));
    }

    @Override
    public void Prerequisitos(Nodo Nd) {
        if (Nd.getCosto() < Bill.getCantidad(Nd.getToken())) {
            Bill.Restar(Nd.getToken(), Nd.getCosto());
            Arbs.get(Arbol_Indx).Activar_Nodo(Nd);
        }
    }

}
