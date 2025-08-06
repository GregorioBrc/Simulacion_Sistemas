package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import Misc.Utils;

public class Panel_Info_Generado extends JPanel {

    private ArrayList<JLabel> J_Tokens;
    private ArrayList<JLabel> J_Tokens_x_Seg;
    private ArrayList<JLabel> J_Nombre_Token;

    public Panel_Info_Generado(ArrayList<String> Nom) {
        J_Tokens = new ArrayList<>();
        J_Tokens_x_Seg = new ArrayList<>();
        J_Nombre_Token = new ArrayList<>();

        setOpaque(false); // Importantísimo para permitir transparencia y formas personalizadas
        Iniciar(Nom);
    }

    private void Iniciar(ArrayList<String> Nom_a) {
        int N = Nom_a.size();

        setSize(420, 120);
        setLayout(null);

        for (int i = 0; i < N; i++) {
            int w = getWidth() / (N * 2) + 40;
            int h = getHeight() / 3 - 15;
            int totalWidth = N * (w + 10) - 10;
            int startX = (getWidth() - totalWidth) / 2;
            int x = startX + i * (w + 10);

            // Nombre del token
            labelBorde nombre = new labelBorde(
                    "<html><div style='color:#000000; font-size:12px; font-weight:bold; text-align:center;'>"
                            + Nom_a.get(i) + "</div></html>",
                    20, 20,
                    new Color(144, 224, 196),
                    Color.BLACK);
            nombre.setBounds(x, 18, w, h);
            J_Nombre_Token.add(nombre);
            add(nombre);

            // Cantidad de tokens
            labelBorde cantidad = new labelBorde(
                    "<html><div style='color:#000000; font-size:14px; font-weight:bold;'>0</div></html>",
                    20, 20,
                    new Color(144, 224, 196),
                    Color.BLACK);
            cantidad.setBounds(x, h + 23, w, h + 2);
            J_Tokens.add(cantidad);
            add(cantidad);

            // Tokens por segundo
            labelBorde porSegundo = new labelBorde(
                    "<html><div style='color:#000000; font-size:11px;'>0 T/s</div></html>",
                    20, 20,
                    new Color(144, 224, 196),
                    Color.BLACK);
            porSegundo.setBounds(x, 2 * h + 30, w, h);
            J_Tokens_x_Seg.add(porSegundo);
            add(porSegundo);
        }
    }

    public void Reconstruir(ArrayList<String> nuevosNombres) {
        removeAll();

        J_Tokens.clear();
        J_Tokens_x_Seg.clear();
        J_Nombre_Token.clear();

        Iniciar(nuevosNombres);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Permite bordes suaves
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Color de fondo ovalado
        g2.setColor(new Color(144, 224, 196));

        // Dibuja óvalo completo del tamaño del panel
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        g2.dispose();
    }

    public void Actualizar(double[] Tokens, double[] Token_x_Seg) {
        for (int i = 0; i < Tokens.length; i++) {
            String cantidad = "<html><div style='color:#000000; font-size:14px; font-weight:bold;'>" +
                    Utils.Formatear_Num(Tokens[i]) + "</div></html>";
            J_Tokens.get(i).setText(cantidad);

            if (Token_x_Seg != null && Token_x_Seg.length != 0) {
                String velocidad = "<html><div style='color:#000000; font-size:11px;'>" +
                        Utils.Formatear_Num(Token_x_Seg[i]) + " T/s</div></html>";
                J_Tokens_x_Seg.get(i).setText(velocidad);
            }
        }
    }

}
