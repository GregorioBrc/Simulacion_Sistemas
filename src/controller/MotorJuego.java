package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import model.Arbol;
import view.*;

public class MotorJuego implements ActionListener {
    private Ventana Vt;
    private ArrayList<ArbolPanel> Arbs;
    private int Arbol_Indx = 0;
    Timer Tiker;

    public MotorJuego() throws IOException {
        Arbs = new ArrayList<ArbolPanel>();
        Arbs.add(new ArbolPanel("Main"));
        Vt = new Ventana(Arbs.get(0));

        Tiker = new Timer(1000, this);
        Tiker.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Arbol Ax =  Arbs.get(Arbol_Indx).getTree();
        Ax.Actualizar_Tokens();
        Vt.getPn_Gene().Actualizar(new double[] { Ax.getTotal_Tokens() },
                new double[] { Ax.getTotal_GeneraxTick() });
    }

}
