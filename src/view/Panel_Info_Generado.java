package view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Panel_Info_Generado extends JPanel {

    private ArrayList<JLabel> J_Tokens;
    private ArrayList<JLabel> J_Tokens_x_Seg;
    private ArrayList<JLabel> J_Nombre_Token;

    public Panel_Info_Generado(String[] Nom) {
        J_Tokens = new ArrayList<JLabel>();
        J_Tokens_x_Seg = new ArrayList<JLabel>();
        J_Nombre_Token = new ArrayList<JLabel>();

        Iniciar(Nom);
    }

    private void Iniciar(String[] Nom_a) {
        int N = Nom_a.length;

        setSize(200, 100);
        setBackground(Color.RED);
        setLayout(null);

        JLabel Ax;
        for (int i = 0; i < N * 3; i++) {
            Ax = new JLabel();
            Ax.setSize(getWidth() / N - 10, getHeight() / 3 - 10);
            Ax.setHorizontalAlignment(SwingConstants.CENTER);
            Ax.setOpaque(true);

            if (i % 3 == 0) {
                Ax.setBackground(Color.orange);
                Ax.setLocation(i / 3 * (Ax.getWidth() + 10) + 5, 5);
                Ax.setText(Nom_a[i / 3]);
                J_Nombre_Token.add(Ax);

            } else if (i % 3 == 1) {
                Ax.setBackground(Color.MAGENTA);
                Ax.setLocation(i / 3 * (Ax.getWidth() + 10) + 5, Ax.getHeight() + 15);
                Ax.setText("0");
                J_Tokens.add(Ax);

            } else {
                Ax.setBackground(Color.yellow);
                Ax.setLocation(i / 3 * (Ax.getWidth() + 10) + 5, Ax.getHeight() * 2 + 15);
                Ax.setText("0");
                J_Tokens_x_Seg.add(Ax);
            }

            add(Ax);

        }
    }

    public void Actualizar(double[] Tokens, double[] Token_x_Seg) {
        for (int i = 0; i < Tokens.length; i++) {

            J_Tokens.get(i).setText(Double.toString(Tokens[i / 2]));

            if (Token_x_Seg != null && Token_x_Seg.length != 0) {
                J_Tokens_x_Seg.get(i).setText(Double.toString(Token_x_Seg[i / 2]));
            }

        }
    }
}
