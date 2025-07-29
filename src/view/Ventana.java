package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import controller.Arbol_Listener;
import model.Nodo;

public class Ventana extends JFrame implements KeyListener, Arbol_Listener {

    private JLayeredPane Cont = new JLayeredPane();
    private ArbolPanel Tree;
    private Panel_Info_Nodo Pn_Info;
    private Panel_Info_Generado Pn_Gene;

    public Ventana(ArbolPanel Ab) {
        setTitle("Juego de la Vida");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setContentPane(Cont);
        setVisible(true);

        addKeyListener(this);

        show();

        Tree = Ab;
        Ini_Components();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

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
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNodoSeleccionado(Nodo nodo) {
        Pn_Info.setVisible(true);
        Pn_Info.Cargar_Nodo(nodo);
    }

    @Override
    public void onFondoDeArbolClickeado() {
        Pn_Info.setVisible(false);
    }

    private void Ini_Components() {
        Pn_Info = new Panel_Info_Nodo();
        Pn_Info.setLocation(getWidth() / 2 - Pn_Info.getWidth() / 2, getHeight() - Pn_Info.getHeight() - 30);
        Cont.add(Pn_Info, 0);

        Pn_Gene = new Panel_Info_Generado(new String[] { "Token1"});
        Pn_Gene.setLocation(getWidth() / 2 - Pn_Gene.getWidth() / 2, 10);
        Cont.add(Pn_Gene, 1);

        // Tree.setSize(1000, 1000);
        Tree.setAr_List(this);

        Tree.setLocation(this.getWidth() - Tree.getWidth() - 50, this.getHeight() - Tree.getHeight() - 50);
        // System.out.println(this.getWidth() + ":" + this.getHeight());
        // Tree.setBackground(Color.GREEN);
        Cont.add(Tree, 2);
    }

}
