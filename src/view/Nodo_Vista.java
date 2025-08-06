package view;

import javax.swing.*;
import java.awt.*;

public class Nodo_Vista extends JComponent {

    private boolean isActiv = false;
    private String texto = "";
    private Color color = Color.BLACK;
    private String nombreImagen = ""; // Asegúrate de tener este atributo
    private static final java.util.Map<String, ImageIcon> cacheImagenes = new java.util.HashMap<>();

    public Nodo_Vista() {
        setPreferredSize(new Dimension(80, 80));
        setSize(80, 80);
        setOpaque(false);
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isActiv && nombreImagen != null && !nombreImagen.isEmpty()) {
                ImageIcon icono = obtenerImagenEscalada(this.nombreImagen, 50, 50);

                int x = (getWidth() - 50) / 2;
                int y = (getHeight() - 50) / 2;
                g2d.drawImage(icono.getImage(), x, y, 50, 50, this);
        } else {
            // Fondo circular si no está activa
            g2d.setColor(Color.GRAY);
            g2d.fillOval(0, 0, getWidth(), getHeight());
        }

        // Dibujar texto si existe
        if (!texto.isEmpty()) {
            g2d.setFont(getFont().deriveFont(Font.BOLD, 14f));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(texto);
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - 5);

            // Fondo redondeado y semitransparente detrás del texto
            int padding = 4;
            g2d.setColor(new Color(0, 0, 0, 150)); // Negro semitransparente
            g2d.fillRoundRect(x - padding, y - fm.getAscent() - padding / 2, textWidth + 2 * padding, textHeight + padding, 12, 12);

            // Texto
            g2d.setColor(Color.WHITE);
            g2d.drawString(texto, x, y);
        }
    }


    public void setIsActiv(boolean activ) {
        this.isActiv = activ;
        repaint();
    }

    public void setTexto(String texto) {
        this.texto = texto;
        repaint();
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public boolean isActiv() {
        return isActiv;
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
            if (!nombreImagen.equals("Null")) {
                return obtenerImagenEscalada("Null",10,10);
            }
            else{
                return null;
            }
        }
    }
    
}
