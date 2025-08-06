package view;

import java.awt.*;
import javax.swing.*;

import Misc.Utils;
import model.Generador;
import model.Modificador_Nodo;
import model.Nodo;

public class Panel_Info_Nodo extends JPanel {

    private JLabel[] Lbs;
    private final String IniHtml = "<html><body>";
    private final String FinHtml = "</body></html>";
    private static final java.util.Map<String, ImageIcon> cacheImagenes = new java.util.HashMap<>();

    public Panel_Info_Nodo() {
        setSize(450, 200);
        setLayout(null);
        setOpaque(false); // Para permitir fondo ovalado personalizado
        CargarLabels();
    }

    private void CargarLabels() {
        Lbs = new JLabel[5];

        for (int i = 0; i < Lbs.length; i++) {
            labelBorde lb = new labelBorde("", 20, 20, new Color(144, 224, 196), Color.BLACK);

            if (i == 0) {
                // Icono o representación, centrado verticalmente a la izquierda
                lb.setBounds(25, 60, 60, 60);
            } else if (i == 1) {
                // Nombre, centrado arriba
                lb.setBounds(100, 25, 200, 40);
            } else if (i == 2) {
                // Descripción, centrado en el panel
                lb.setBounds(100, 90, 320, 70);
            } else if (i == 3) {
                // Costo, arriba a la derecha
                lb.setBounds(320, 25, 100, 25);
            } else if (i == 4) {
                // Token, debajo del costo
                lb.setBounds(320, 55, 100, 25);
            }

            lb.setHorizontalAlignment(SwingConstants.CENTER);
            lb.setVerticalAlignment(SwingConstants.CENTER);
            Lbs[i] = lb;
            add(lb);
        }
    }

    public void Cargar_Nodo(Nodo Nd) {
        ImageIcon icono = obtenerImagenEscalada(Nd.getNombreImagen(), 50, 50);
        if (icono != null) {
            Lbs[0].setIcon(icono);
            Lbs[0].setText("");
        }

        Lbs[1].setText(IniHtml + "<div style='color:#000000; font-size:14px; font-weight:bold;'>" + Nd.getNombre()
                + "</div>" + FinHtml);
        Lbs[2].setText(Refinar_Descripcion(Nd));
        Lbs[3].setText(IniHtml + "<div style='color:#000000; font-size:08px;'>Costo:"
                + Utils.Formatear_Num(Nd.getCosto()) + "</div>" + FinHtml);
        Lbs[4].setText(IniHtml + "<div style='color:#000000; font-size:12px;'>" + Nd.getToken() + "</div>" + FinHtml);
    }

    private String Refinar_Descripcion(Nodo Nd) {
        String Ax = Nd.getDescripcion();
        if (Nd instanceof Generador) {
            Ax += "<br><u>Genera: " + ((Generador) Nd).getGeneradoxTick() + " " + Nd.getToken() + "</u>";
        } else if (Nd instanceof Modificador_Nodo) {
            Ax += "<br><u>" + ((Modificador_Nodo) Nd).getDescripcion_Modif() + "</u>";
        }
        return IniHtml + "<div style='color:#000000; padding:2px font-size:12px;'>" + Ax + "</div>" + FinHtml;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(144, 224, 196)); // Fondo ovalado
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        g2.dispose();
    }

    private ImageIcon obtenerImagenEscalada(String nombreImagen, int w, int h) {
        String clave = nombreImagen + "_" + w + "x" + h;

        if (cacheImagenes.containsKey(clave)) {
            return cacheImagenes.get(clave);
        }

        String ruta = "/img/" + nombreImagen + ".png";
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
            Image escalada = icono.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon resultado = new ImageIcon(escalada);
            cacheImagenes.put(clave, resultado);
            return resultado;
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + ruta);
            return null;
        }
    }

}
