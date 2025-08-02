package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Nodo_Vista extends JComponent {

    private boolean isActiv = false;
    private String texto = "";
    private Color color = Color.BLACK;
    private Image imagen = null;


    public Nodo_Vista() {
        setPreferredSize(new Dimension(60, 60));
        setSize(60, 60);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isActiv) {
            g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        } else {
            // círculo como antes
            Color fillColor = isActiv ? Color.BLACK : Color.GRAY;
            g2d.setColor(fillColor);
            g2d.fillOval(0, 0, getWidth(), getHeight());
        }

        // Texto centrado
        if (!texto.isEmpty()) {
            g2d.setColor(Color.WHITE);
            Font font = getFont().deriveFont(Font.BOLD, 14f);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(texto);
            int y = (getHeight() - 2);
            g2d.drawString(texto, (getWidth() - textWidth) / 2, y);
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

    public void setImagen(String nombreImagen) {
        try {
            // Asume que la carpeta "img" está en el mismo nivel que "view" dentro de "src"
            imagen = ImageIO.read(getClass().getResource("/img/" + nombreImagen + ".png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar la imagen: " + nombreImagen + ".png");
            imagen = null;
        }
        repaint();
    }


    public boolean isActiv() {
        return isActiv;
    }
}
