package view;

import javax.swing.*;
import java.awt.*;

public class labelBorde extends JLabel {
    private int arcWidth;
    private int arcHeight;
    private Color backgroundColor;
    private Color borderColor;

    public labelBorde(String text, int arcWidth, int arcHeight, Color backgroundColor, Color borderColor) {
        super(text);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;

        // Configuraciones importantes
        setOpaque(false); // Para que el fondo redondeado se pinte, no el cuadrado por defecto
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Bordes suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo redondeado
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // Borde
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        g2.dispose();

        // Ahora sí: dibujar texto por encima
        super.paintComponent(g);
    }

    // Setters
    public void setArc(int width, int height) {
        this.arcWidth = width;
        this.arcHeight = height;
        repaint();
    }

    public void setColors(Color background, Color border) {
        this.backgroundColor = background;
        this.borderColor = border;
        repaint();
    }

    public void setLabelText(String text) {
        setText(text);
        repaint();
    }
}
