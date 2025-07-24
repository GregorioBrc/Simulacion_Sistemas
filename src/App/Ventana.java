package App;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Ventana extends JFrame implements KeyListener {

    private Container Cont = new Container();
    private JPanel Tree;

    public Ventana(JPanel Arb) {
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

        Tree = Arb;
        Ini_Tree();
    }

    private void Ini_Tree() {
        //Tree.setSize(1000, 1000);
        Tree.setLocation(this.getWidth()-Tree.getWidth()-50, this.getHeight()-Tree.getHeight()-50);
        System.out.println(this.getWidth() + ":" +this.getHeight());
        //Tree.setBackground(Color.GREEN);
        Cont.add(Tree);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
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
        }
        else if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

}
