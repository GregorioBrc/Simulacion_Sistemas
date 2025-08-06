package view;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import controller.Arbol_Listener;
import controller.Camb_Arbol;
import model.Nodo;

public class Ventana extends JFrame implements KeyListener, Arbol_Listener, ActionListener {

    private JLayeredPane Cont = new JLayeredPane();
    private ArbolPanel Tree;
    private Panel_Info_Nodo Pn_Info;
    private Panel_Info_Generado Pn_Gene;

    private JButton [] Btn_Cambio_Mapa;
    private int Cant_Botones = 0;
    private Camb_Arbol Camb_Arb_List;

    public Ventana(ArbolPanel Ab, int Cant_Trees) {
        setTitle("Juego de la Vida");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setContentPane(Cont);
        setVisible(true);

        addKeyListener(this);

        Cant_Botones = Cant_Trees;

        show();

        Tree = Ab;
        Ini_Components();
    }

    public void setCamb_Arb_List(Camb_Arbol camb_Arb_List) {
        Camb_Arb_List = camb_Arb_List;
    }

    public void setCant_Botones(int cant_Botones) {
        Cant_Botones = cant_Botones;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            Tree.setLocation((int) Tree.getLocation().getX(), (int) Tree.getLocation().getY() - 5);

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Tree.setLocation((int) Tree.getLocation().getX() + 5, (int) Tree.getLocation().getY());

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Tree.setLocation((int) Tree.getLocation().getX(), (int) Tree.getLocation().getY() + 5);

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Tree.setLocation((int) Tree.getLocation().getX() - 5, (int) Tree.getLocation().getY());
        } else if (e.getKeyChar() == KeyEvent.VK_SPACE) {

        }
    }

    public Panel_Info_Nodo getPn_Info() {
        return Pn_Info;
    }

    public Panel_Info_Generado getPn_Gene() {
        return Pn_Gene;
    }


    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void onNodoSeleccionado(Nodo nodo) {
        Pn_Info.setVisible(true);
        Pn_Info.Cargar_Nodo(nodo);
    }

    @Override
    public void onFondoDeArbolClickeado() {
        Pn_Info.setVisible(false);
    }

    public void CambiArb(ArbolPanel Arb) {
        Cont.remove(Tree);
        Tree = Arb;
        Tree.setAr_List(this);
        Tree.setLocation(this.getWidth() - Tree.getWidth(), this.getHeight() - Tree.getHeight());
        Cont.add(Tree, 100);
        Cont.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton Ax_b = (JButton) e.getSource();
        Camb_Arb_List.Cambiar_Arbol(Integer.parseInt(Ax_b.getClientProperty("Id_Arbol").toString()));
    }

    private void Ini_Components() {
        Pn_Info = new Panel_Info_Nodo();
        Pn_Info.setVisible(false);
        Pn_Info.setLocation(getWidth() - Pn_Info.getWidth()  , getHeight() - Pn_Info.getHeight() - 30);
        Cont.add(Pn_Info, 0);

        Btn_Cambio_Mapa = new JButton[Cant_Botones];
        for (int i = 0; i < Btn_Cambio_Mapa.length; i++) {
            Btn_Cambio_Mapa[i] = new JButton("Mapa " + (i + 1));
            Btn_Cambio_Mapa[i].setSize(50, 30);
            Btn_Cambio_Mapa[i].setLocation(getWidth()-Btn_Cambio_Mapa[i].getWidth() - 100, 30 + (i * 35));
            Btn_Cambio_Mapa[i].addActionListener(this);
            Btn_Cambio_Mapa[i].putClientProperty("Id_Arbol", i);
            Cont.add(Btn_Cambio_Mapa[i], 1);
        }

        Pn_Gene = new Panel_Info_Generado((Tree.getTree().getToken_a_Generar()));
        Pn_Gene.setLocation(getWidth() / 2 - Pn_Gene.getWidth() / 2, 10);
        Cont.add(Pn_Gene, 2);

        // Tree.setSize(1000, 1000);
        Tree.setAr_List(this);

        Tree.setLocation(this.getWidth() - Tree.getWidth(), this.getHeight() - Tree.getHeight());
        // System.out.println(this.getWidth() + ":" + this.getHeight());
        // Tree.setBackground(Color.GREEN);
        Cont.add(Tree, 100);
    }

}
