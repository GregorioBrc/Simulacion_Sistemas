package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Arbol_Listener;
import model.Arbol;
import model.Nodo;

public class ArbolPanel extends JPanel implements MouseListener {

    private Arbol Tree;
    private final int Const_Separacion = 100;
    private Arbol_Listener Ar_List;

    public ArbolPanel(String Nm) {
        try {
            Tree = new Arbol(Nm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IniciarComponent();
        Cargar_Cuerpo_Nodos();
    }

    private void IniciarComponent() {
        setSize(1000, 1000);
        setLayout(null);
        setBackground(Color.GREEN);
        addMouseListener(this);
    }

    private  void Cargar_Cuerpo_Nodos() {
        int a = getWidth() / 2, b = (int) (getHeight() - Tree.getNodos().get(0).getDm().getHeight() * 2), c = 1;

        for (int i = 0, j = 1, k = 0; i < Tree.getNodos().size(); i++, j++) {
            JLabel Ax = new JLabel();
            Ax.setSize(Tree.getNodos().get(i).getDm());
            Ax.setBackground(Color.BLACK);
            Ax.setOpaque(true);
            if (c % 2 == 0) {
                if (j <= c / 2) {
                    Tree.getNodos().get(i).setLocation(
                            a - j * Const_Separacion + Const_Separacion / 2, b - Ax.getHeight() - 10);
                } else {
                    Tree.getNodos().get(i).setLocation(a + k * Const_Separacion - Const_Separacion / 2,
                            b - Ax.getHeight() - 10);
                    k++;
                }
            } else {
                if (j < Math.round((double) c / 2.0)) {
                    Tree.getNodos().get(i).setLocation(a - j * Const_Separacion, b - Ax.getHeight() - 10);
                } else if (j > Math.round((double) c / 2.0)) {
                    Tree.getNodos().get(i).setLocation(a + k * Const_Separacion, b - Ax.getHeight() - 10);
                    k++;
                } else {
                    Tree.getNodos().get(i).setLocation(a, b - Ax.getHeight() - 10);
                }
            }
            Ax.setLocation(Tree.getNodos().get(i).getLocation());
            if (j == c) {
                b -= Const_Separacion;
                c++;
                j = 0;
                k = 1;
            }
            System.out.println(Ax.getBounds());
            add(Ax);
        }
    }

    public void setAr_List(Arbol_Listener ar_List) {
        Ar_List = ar_List;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Nodo Ax_Nd = null;
        for (Nodo nodo : Tree.getNodos()) {
            if (new Rectangle(nodo.getLocation(), nodo.getDm()).contains(e.getPoint())) {
                Ax_Nd = nodo;
            }
        }

        if (Ax_Nd != null) {
            Ar_List.onNodoSeleccionado(Ax_Nd);
        } else {
            Ar_List.onFondoDeArbolClickeado();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public Arbol getTree() {
        return Tree;
    }

    public int getConst_Separacion() {
        return Const_Separacion;
    }

    public Arbol_Listener getAr_List() {
        return Ar_List;
    }
}
