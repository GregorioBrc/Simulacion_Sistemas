package App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import Class.Arbol;

public class MotorJuego implements ActionListener {
    private Ventana Vt;
    private ArrayList<Arbol> Arbs;
    private int Arbol_Indx = 0;
    Timer Tiker;

    public MotorJuego() throws IOException {
        Arbs = new ArrayList<Arbol>();
        Arbs.add(new Arbol("Main"));
        Vt = new Ventana(Arbs.get(0));

        Tiker = new Timer(1000, this);
        Tiker.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Arbs.get(Arbol_Indx).Actualizar_Tokens();
        Vt.getPn_Gene().Actualizar(new double[] { Arbs.get(Arbol_Indx).getTotal_Tokens() },
                new double[] { Arbs.get(Arbol_Indx).getTotal_GeneraxTick() });
    }

}
