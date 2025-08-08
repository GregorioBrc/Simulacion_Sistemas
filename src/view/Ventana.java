package view;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import controller.Arbol_Listener;
import controller.Camb_Arbol;
import controller.LoadGameListener;
import controller.MenuListener;
import controller.NewGameListener;
import model.Nodo;

/**
 * Ventana principal.
 * Modo 1 (original): new Ventana(arbolPanel, cantidadArboles) → muestra el juego.
 * Modo 2 (menús):    new Ventana() + initMenuFlows(...) + showMenu() → navega por pantallas y luego showJuego(arbol).
 */
public class Ventana extends JFrame implements KeyListener, Arbol_Listener, ActionListener {

    // ---------- EXISTENTE (modo juego) ----------
    private JLayeredPane Cont = new JLayeredPane();
    private ArbolPanel Tree;
    private Panel_Info_Nodo Pn_Info;
    private Panel_Info_Generado Pn_Gene;

    private JButton[] Btn_Cambio_Mapa;
    private int Cant_Botones = 0;
    private Camb_Arbol Camb_Arb_List;

    // ---------- NUEVO (menús y pausa) ----------
    private CardLayout cards;
    private JPanel root; // panel con CardLayout
    private MenuInicio pnMenu;
    private FormNuevoJuego pnNuevo;
    private MenuCargar pnCargar;
    private JButton btnPausa;
    private controller.PauseListener pauseListener;

    // ========= Constructores =========

    /** Constructor NUEVO: crea la ventana sin juego (para flujos con menú). */
    public Ventana() {
        configurarFrame();
        // En modo menú, initMenuFlows(...) definirá el contentPane.
        setVisible(true);
        addKeyListener(this);
    }

    /** Constructor ORIGINAL: crea y muestra directamente el juego. */
    public Ventana(ArbolPanel Ab, int Cant_Trees) {
        configurarFrame();

        setContentPane(Cont);
        setVisible(true);

        addKeyListener(this);

        Cant_Botones = Cant_Trees;
        Tree = Ab;

        Ini_Components(); // carga tu UI original de juego
    }

    // Config común de la ventana
    private void configurarFrame() {
        setTitle("Juego de la Vida");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

    // ========= Integración de MENÚS =========

    /**
     * Inicializa el flujo de pantallas de menú (inicio, nuevo, cargar).
     * Tras esto puedes llamar a showMenu(), showNuevo(), showCargar(...)
     */
    public void initMenuFlows(MenuListener menuL,
                              NewGameListener newL,
                              LoadGameListener loadL) {

        cards = new CardLayout();
        root = new JPanel(cards);

        pnMenu  = new MenuInicio(menuL);
        pnNuevo = new FormNuevoJuego(newL);
        pnCargar = new MenuCargar(loadL);

        root.add(pnMenu,  "MENU");
        root.add(pnNuevo, "NUEVO");
        root.add(pnCargar,"CARGAR");

        setContentPane(root);
        revalidate();
        repaint();
    }
    
    private void removePauseButton() {
    if (btnPausa != null) {
        getLayeredPane().remove(btnPausa);
        btnPausa = null;
        getLayeredPane().revalidate();
        getLayeredPane().repaint();
    }
}

    public void showMenu(){
    removePauseButton(); // <- lo quita si existe
    if (root == null || cards == null) return;
    setContentPane(root);
    revalidate();
    repaint();
    cards.show(root, "MENU");
}

    public void showNuevo() {
    removePauseButton(); // <- lo quita si existe
    if (cards != null) cards.show(root, "NUEVO");
}

    public void showCargar(java.util.List<String> saves) {
    removePauseButton(); // <- lo quita si existe
    if (pnCargar != null) {
        pnCargar.setSaves(saves);
    }
    if (cards != null) cards.show(root, "CARGAR");
}

    /**
     * Cambia de los menús al juego reutilizando tu UI original.
     * Llama a Ini_Components() y añade botón de Pausa.
     */
    public void showJuego(ArbolPanel juego) {
        this.Tree = juego;

        // Volvemos a usar tu contenedor de siempre:
        setContentPane(Cont);
        Ini_Components(); // crea Pn_Info, Pn_Gene, botones de mapas, añade Tree, etc.

        // Botón Pausa (superior izquierda)
        if (btnPausa == null) {
            btnPausa = new RoundButton("Pausa");
btnPausa.setSize(90, 32);
btnPausa.setLocation(10, 10);
btnPausa.addActionListener(e -> firePause());
getLayeredPane().add(btnPausa, Integer.valueOf(200));
        }

        revalidate();
        repaint();
    }

    private void firePause() {
        if (pauseListener != null) {
            new PanelPausa(this, pauseListener).setVisible(true);
        }
    }

    public void setPauseListener(controller.PauseListener l) {
        this.pauseListener = l;
    }

    // ========= Setters existentes =========

    public void setCamb_Arb_List(Camb_Arbol camb_Arb_List) {
        Camb_Arb_List = camb_Arb_List;
    }

    public void setCant_Botones(int cant_Botones) {
        Cant_Botones = cant_Botones;
    }

    // ========= KeyListener =========

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // NUEVO: ESC abre pausa
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            firePause();
            return;
        }

        // ORIGINAL: mover el árbol con flechas
        if (Tree != null) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                Tree.setLocation((int) Tree.getLocation().getX(), (int) Tree.getLocation().getY() - 5);

            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                Tree.setLocation((int) Tree.getLocation().getX() + 5, (int) Tree.getLocation().getY());

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                Tree.setLocation((int) Tree.getLocation().getX(), (int) Tree.getLocation().getY() + 5);

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                Tree.setLocation((int) Tree.getLocation().getX() - 5, (int) Tree.getLocation().getY());
            } else if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                // reservado
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // ========= Arbol_Listener =========

    @Override
    public void onNodoSeleccionado(Nodo nodo) {
        if (Pn_Info != null) {
            Pn_Info.setVisible(true);
            Pn_Info.Cargar_Nodo(nodo);
        }
    }

    @Override
    public void onFondoDeArbolClickeado() {
        if (Pn_Info != null) {
            Pn_Info.setVisible(false);
        }
    }

    // ========= Cambio de árbol (original) =========

    public void CambiArb(ArbolPanel Arb) {
        Cont.remove(Tree);
        Tree = Arb;
        Tree.setAr_List(this);
        Tree.setLocation(this.getWidth() - Tree.getWidth(), this.getHeight() - Tree.getHeight());
        Cont.add(Tree, 100);
        Cont.repaint();
    }

    // ========= ActionListener (original) =========

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton Ax_b = (JButton) e.getSource();
        Camb_Arb_List.Cambiar_Arbol(Integer.parseInt(Ax_b.getClientProperty("Id_Arbol").toString()));
    }

    // ========= UI original del juego =========

    private void Ini_Components() {
        Cont.removeAll();

        Pn_Info = new Panel_Info_Nodo();
        Pn_Info.setVisible(false);
        Pn_Info.setLocation(getWidth() - Pn_Info.getWidth(), getHeight() - Pn_Info.getHeight() - 30);
        Cont.add(Pn_Info, 0);

        // Botones de cambio de mapa (estilo "Entropía" y arriba a la derecha)
        Btn_Cambio_Mapa = new JButton[Cant_Botones];

        int btnW = 100, btnH = 30;
        int marginRight = 20;  // margen desde el borde derecho
        int top = 10;          // margen superior
        int gap = 10;          // separación vertical entre botones

        for (int i = 0; i < Btn_Cambio_Mapa.length; i++) {
            Btn_Cambio_Mapa[i] = new RoundButton("Mapa " + (i + 1));
            Btn_Cambio_Mapa[i].setSize(btnW, btnH);

            int x = getWidth() - btnW - marginRight;  // pegado a la derecha
            int y = top + i * (btnH + gap);           // en columna
            Btn_Cambio_Mapa[i].setLocation(x, y);

            Btn_Cambio_Mapa[i].addActionListener(this);
            Btn_Cambio_Mapa[i].putClientProperty("Id_Arbol", i);
            Cont.add(Btn_Cambio_Mapa[i], 1);
        }

        if (Tree != null) {
            Pn_Gene = new Panel_Info_Generado(Tree.getTree().getToken_a_Generar());
            Pn_Gene.setLocation(getWidth() / 2 - Pn_Gene.getWidth() / 2, 10);
            Cont.add(Pn_Gene, 2);

            Tree.setAr_List(this);
            Tree.setLocation(this.getWidth() - Tree.getWidth(), this.getHeight() - Tree.getHeight());
            Cont.add(Tree, 100);
        }

        Cont.revalidate();
        Cont.repaint();
    }

    // ========= Getters útiles =========

    public Panel_Info_Nodo getPn_Info() {
        return Pn_Info;
    }

    public Panel_Info_Generado getPn_Gene() {
        return Pn_Gene;
    }

    // ---------- Botón con estilo ovalado (igual al panel de "Entropía") ----------
    private static class RoundButton extends JButton {
        private final Color bg  = new Color(144, 224, 196);
        private final Color brd = Color.BLACK;
        private final int arcW = 20, arcH = 20;

        RoundButton(String text){
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.BLACK);
            setFont(getFont().deriveFont(java.awt.Font.BOLD, 12f));
        }

        @Override protected void paintComponent(Graphics g) {
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
