// src/view/MenuCargar.java
package view;

import controller.LoadGameListener;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class MenuCargar extends JPanel {
    private final Image bg = new ImageIcon(getClass().getResource("/img/background.png")).getImage();
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private final LoadGameListener listener;

    public MenuCargar(LoadGameListener l){
        this.listener = l;
        setLayout(new GridBagLayout());
        setOpaque(false);

        // --- Tarjeta redondeada ---
        RoundPanel card = new RoundPanel(
                new Color(20, 35, 70, 220),   // fondo
                new Color(0, 0, 0, 160),      // borde
                24                            // radio
        );
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("SELECT GAME", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        card.add(title, BorderLayout.NORTH);

        // --- Lista de partidas ---
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(list.getFont().deriveFont(16f));
        list.setBackground(new Color(255, 255, 255, 230));
        list.setForeground(Color.DARK_GRAY);

        JScrollPane sp = new JScrollPane(list);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(new LineBorder(new Color(255,255,255,200), 2, true));
        card.add(sp, BorderLayout.CENTER);

        // --- Botones ---
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);

        JButton back = new MenuButton("← Volver");
        back.addActionListener(e -> listener.onBackToMenu());

        JButton resume = new MenuButton("Resume Game");
        resume.addActionListener(e -> {
            String id = list.getSelectedValue();
            if (id != null) listener.onResumeSelected(id);
        });

        btns.add(back);
        btns.add(resume);
        card.add(btns, BorderLayout.SOUTH);

        add(card, new GridBagConstraints());
    }

    public void setSaves(List<String> ids){
        model.clear();
        ids.forEach(model::addElement);
    }

    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    // ====== Botón redondeado (mismo estilo del menú de inicio) ======
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