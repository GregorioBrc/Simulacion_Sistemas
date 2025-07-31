package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Generador;
import model.Modificador_Nodo;
import model.Nodo;

public class Panel_Info_Nodo extends JPanel {

    private JLabel[] Lbs;
    private String IniHtml = "<html><body>";
    private String FinHtml = "</body></html>";

    public Panel_Info_Nodo() {
        setSize(400, 150);
        setLayout(null);
        setBackground(Color.BLUE);

        CargarLabels();
    }

    private void CargarLabels() {
        Lbs = new JLabel[5];

        for (int i = 0; i < Lbs.length; i++) {
            Lbs[i] = new JLabel();
            Lbs[i].setOpaque(true);
            if (i == 0) {
                Lbs[i].setSize(50, 50);
                Lbs[i].setLocation(25, 10);
                Lbs[i].setBackground(Color.ORANGE);
            } else if (i == 1) {
                Lbs[i].setSize(175, 50);
                Lbs[i].setLocation(100, 10);
                Lbs[i].setBackground(Color.MAGENTA);
            } else if (i == 2) {
                Lbs[i].setSize(350, 65);
                Lbs[i].setLocation(25, 75);
                Lbs[i].setBackground(Color.cyan);
            } else if (i == 3) {
                Lbs[i].setSize(75, 25);
                Lbs[i].setLocation(300, 10);
                Lbs[i].setBackground(Color.orange);
            } else if (i == 4) {
                Lbs[i].setSize(Lbs[i - 1].getSize());
                Lbs[i].setLocation(Lbs[i - 1].getX(), Lbs[i - 1].getY() + Lbs[i - 1].getHeight() + 5);
                Lbs[i].setBackground(Color.red);
            }

            add(Lbs[i]);
        }

    }

    public void Cargar_Nodo(Nodo Nd) {
        Lbs[1].setText(Nd.getNombre());
        Lbs[2].setText(Refinar_Descripcion(Nd));
        Lbs[3].setText("Costo: " + Nd.getCosto());
        Lbs[4].setText("" + Nd.getToken());
    }

    private String Refinar_Descripcion(Nodo Nd) {
        String Ax = Nd.getDescripcion();
        if (Nd instanceof Generador) {
            Ax += "<br><u>Genera: " + ((Generador) (Nd)).getGeneradoxTick() + " " + Nd.getToken() + "</u>";
        } else if (Nd instanceof Modificador_Nodo) {
            Ax += "<br><u>" + ((Modificador_Nodo) Nd).getDescripcion_Modif() + "</u>";
            //Ax += "<br><u>Mejora: " + ((Modificador_Nodo) Nd).getValor_Modif() + "</u>";
        }
        return IniHtml + Ax + FinHtml;
    }

}
