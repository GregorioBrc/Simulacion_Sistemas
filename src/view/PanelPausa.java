// src/view/PanelPausa.java
package view;

import javax.swing.*;
import java.awt.*;

/** Diálogo de pausa con fondo translúcido y botones redondeados. */
public class PanelPausa extends JDialog {

    public interface PauseListener {
        void onContinue();
        void onSave();
        void onExitToMenu();
    }

    public PanelPausa(JFrame owner, controller.PauseListener l){
        super(owner, "Pausa", true);
        setUndecorated(true);
        setSize(320, 230);
        setLocationRelativeTo(owner);
        // Permite transparencia en el borde del diálogo
        setBackground(new Color(0, 0, 0, 0));

        // Contenedor con sombra/fondo
        JPanel root = new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // “tarjeta” oscura translúcida
                int arc = 24;
                g2.setColor(new Color(20, 35, 70, 230));
                g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, arc, arc);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        JPanel card = new JPanel();
        card.setOpaque(false);
        card.setLayout(new GridLayout(0,1,10,10));
        card.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        RoundButton cont = new RoundButton("Continuar");
        cont.addActionListener(e -> { l.onContinue(); dispose(); });

        RoundButton save = new RoundButton("Guardar partida");
        save.addActionListener(e -> { l.onSave(); }); // ya no pide ID: usa el actual

        RoundButton exit = new RoundButton("Salir al Menú");
        exit.addActionListener(e -> { l.onExitToMenu(); dispose(); });

        card.add(cont);
        card.add(save);
        card.add(exit);

        root.add(card, new GridBagConstraints());

        // ESC para cerrar/continuar
        getRootPane().registerKeyboardAction(
            e -> { l.onContinue(); dispose(); },
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    /** Botón redondeado a juego con “Entropía”/botones de mapas. */
    private static class RoundButton extends JButton {
        private final Color bg  = new Color(144, 224, 196);
        private final Color brd = Color.BLACK;
        private final int arc = 18;

        RoundButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.BLACK);
            setPreferredSize(new Dimension(240, 36));
            setFont(getFont().deriveFont(Font.BOLD, 13f));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            g2.setColor(brd);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}