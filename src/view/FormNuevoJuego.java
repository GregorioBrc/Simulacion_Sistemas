// src/view/FormNuevoJuego.java
package view;

import controller.NewGameListener;
import javax.swing.*;
import java.awt.*;

public class FormNuevoJuego extends JPanel {
    private final Image bg = new ImageIcon(getClass().getResource("/img/background.png")).getImage();
    private final JTextField tfUser = new JTextField(16);
    private final JTextField tfGame = new JTextField(16);
    private final NewGameListener listener;

    public FormNuevoJuego(NewGameListener l){
        this.listener = l;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // --- Tarjeta redondeada con borde ---
        RoundPanel card = new RoundPanel(
                new Color(20, 35, 70, 220),   // fondo
                new Color(0, 0, 0, 160),      // borde
                24                            // radio
        );
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel title = lbl("Start Game", 24f);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);
        c.gridwidth = 1;

        // Username
        c.gridy = 1; c.gridx = 0; card.add(lbl("Username", 16f), c);
        c.gridx = 1; card.add(tfUser, c);

        // Game Name
        c.gridy = 2; c.gridx = 0; card.add(lbl("Game Name", 16f), c);
        c.gridx = 1; card.add(tfGame, c);

        // Botones
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btns.setOpaque(false);

        JButton back  = new MenuButton("← Volver");
        back.addActionListener(e -> listener.onBackToMenu());

        JButton start = new MenuButton("Iniciar Juego");
        start.addActionListener(e -> {
            String u = tfUser.getText().trim();
            String g = tfGame.getText().trim();
            if (u.isEmpty() || g.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa usuario y nombre de partida.");
                return;
            }
            listener.onSubmitNewGame(u, g);
        });

        btns.add(back);
        btns.add(start);

        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        card.add(btns, c);

        add(card, new GridBagConstraints());
    }

    private JLabel lbl(String t, float sz){
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(l.getFont().deriveFont(Font.BOLD, sz));
        return l;
    }

    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    // ====== Botón redondeado estilo menú ======
    private static class MenuButton extends JButton {
        private final Color bg  = new Color(240, 240, 240); // gris claro
        private final Color brd = Color.BLACK;
        private final int arcW = 18, arcH = 18;

        MenuButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.BLACK);
            setFont(getFont().deriveFont(Font.BOLD, 16f));
            setPreferredSize(new Dimension(180, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcW, arcH);

            g2.setColor(brd);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, arcW, arcH);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ====== Panel redondeado con borde ======
    private static class RoundPanel extends JPanel {
        private final Color bg;
        private final Color border;
        private final int arc;

        RoundPanel(Color bg, Color border, int arc){
            this.bg = bg;
            this.border = border;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            g2.setColor(border);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
