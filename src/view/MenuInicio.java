// src/view/MenuInicio.java
package view;

import controller.MenuListener;
import javax.swing.*;
import java.awt.*;

public class MenuInicio extends JPanel {
    private final Image bg = new ImageIcon(getClass().getResource("/img/background.png")).getImage();
    private final MenuListener listener;

    public MenuInicio(MenuListener listener) {
        this.listener = listener;
        setOpaque(false);
        setLayout(new GridBagLayout());  // un solo layout

        // Columna central (logo + botones)
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        // ==== LOGO escalado ====
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
        int logoW = 480, logoH = 180;
        Image logoImg = logoIcon.getImage().getScaledInstance(logoW, logoH, Image.SCALE_SMOOTH);
        JLabel logoLbl = new JLabel(new ImageIcon(logoImg));
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 24, 10));

        // ==== Botones ====
        JButton play = btn("INICIAR PARTIDA");
        play.addActionListener(e -> this.listener.onStartNew());

        JButton load = btn("CARGAR PARTIDA");
        load.addActionListener(e -> this.listener.onLoadGame());

        JButton exit = btn("SALIR");
        exit.addActionListener(e -> this.listener.onExit());

        // Ensamblar columna
        col.add(logoLbl);
        col.add(play);
        col.add(Box.createVerticalStrut(12));
        col.add(load);
        col.add(Box.createVerticalStrut(12));
        col.add(exit);

        // Centrar la columna en el panel
        add(col, new GridBagConstraints());
    }

    // Botón estilo redondeado para menú
    private JButton btn(String text) {
        JButton b = new MenuButton(text);
        b.setPreferredSize(new Dimension(260, 44));
        b.setMaximumSize(new Dimension(260, 44));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fondo escalado a todo el panel
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    // Clase interna para el estilo redondeado
    private static class MenuButton extends JButton {
        private final Color bg  = new Color(240, 240, 240); // gris claro
        private final Color brd = Color.BLACK;
        private final int arcW = 20, arcH = 20;

        MenuButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.BLACK);
            setFont(getFont().deriveFont(java.awt.Font.BOLD, 16f));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcW, arcH);

            // Borde
            g2.setColor(brd);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, arcW, arcH);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}