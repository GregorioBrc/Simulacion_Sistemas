package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Nodo;

public class Panel_Info_Nodo extends JPanel {

    private JLabel[] Lbs;

    public Panel_Info_Nodo() {
        setSize(400, 150);
        setLayout(null);
        setBackground(Color.BLUE);

        CargarLabels();
    }

    private void CargarLabels() {
        Lbs = new JLabel[3];

        for (int i = 0; i < Lbs.length; i++) {
            Lbs[i] = new JLabel();
            Lbs[i].setOpaque(true);
            if (i == 0) {
                Lbs[i].setSize(50, 50);
                Lbs[i].setLocation(25, 10);
                Lbs[i].setBackground(Color.ORANGE);
            } else if (i == 1) {
                Lbs[i].setSize(275, 50);
                Lbs[i].setLocation(100, 10);
                Lbs[i].setBackground(Color.MAGENTA);
            } else if (i == 2) {
                Lbs[i].setSize(350, 65);
                Lbs[i].setLocation(25, 75);
                Lbs[i].setBackground(Color.cyan);
            }

            add(Lbs[i]);
        }

    }

    public void Cargar_Nodo(Nodo Nd) {
        Lbs[1].setText(Nd.getNombre());
        Lbs[2].setText(Nd.getDescripcion());
    }

}
