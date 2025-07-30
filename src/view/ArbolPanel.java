package view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Arbol_Listener;
import controller.Compra_Listener;
import model.Arbol;
import model.Nodo;

public class ArbolPanel extends JPanel implements MouseListener {

    private Arbol Tree;
    private final int Const_Separacion = 100;
    private Arbol_Listener Ar_List;
    private JLabel Node_Select_Jlabel;
    private Nodo Node_Select;
    private Compra_Listener Com_Listener;

    public ArbolPanel(String Nm) {
        try {
            Tree = new Arbol(Nm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IniciarComponent();
        Cargar_Cuerpo_Nodos();
    }

    public void setAr_List(Arbol_Listener ar_List) {
        Ar_List = ar_List;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Nodo Ax_Nd = null;
        for (Nodo nodo : Tree.getNodos()) {
            if (new Rectangle(nodo.getLocation(), nodo.getDm()).contains(e.getPoint()) && nodo.isIs_Activ()) {
                Ax_Nd = nodo;
            }
        }

        if (Ax_Nd != null) {

            if (Node_Select == Ax_Nd) {
                Com_Listener.Prerequisitos(Node_Select);
            } else {
                Node_Select_Jlabel = (JLabel) (getComponentAt(Ax_Nd.getLocation()));
                Node_Select = Ax_Nd;
                Ar_List.onNodoSeleccionado(Ax_Nd);
            }

        } else {
            Node_Select_Jlabel = null;
            Node_Select = null;
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

    public void setCom_Listener(Compra_Listener com_Listener) {
        Com_Listener = com_Listener;
    }

    private void IniciarComponent() {
        setSize(1000, 1000);
        setLayout(null);
        setBackground(Color.GREEN);
        addMouseListener(this);
    }

    private void Cargar_Cuerpo_Nodos() {
        int a = getWidth() / 2, b = (int) (getHeight() - Tree.getNodos().get(0).getDm().getHeight() * 2), c = 1;

        for (int i = 0, j = 1, k = 0; i < Tree.getNodos().size(); i++, j++) {
            Nodo Nd_ax = Tree.getNodos().get(i);

            JLabel Ax = new JLabel();
            Ax.setSize(Nd_ax.getDm());
            Ax.setBackground(Color.BLACK);
            Ax.setOpaque(true);
            if (c % 2 == 0) {
                if (j <= c / 2) {
                    Nd_ax.setLocation(
                            a - j * Const_Separacion + Const_Separacion / 2, b - Ax.getHeight() - 10);
                } else {
                    Nd_ax.setLocation(a + k * Const_Separacion - Const_Separacion / 2,
                            b - Ax.getHeight() - 10);
                    k++;
                }
            } else {
                if (j < Math.round((double) c / 2.0)) {
                    Nd_ax.setLocation(a - j * Const_Separacion, b - Ax.getHeight() - 10);
                } else if (j > Math.round((double) c / 2.0)) {
                    Nd_ax.setLocation(a + k * Const_Separacion, b - Ax.getHeight() - 10);
                    k++;
                } else {
                    Nd_ax.setLocation(a, b - Ax.getHeight() - 10);
                }
            }
            Ax.setVisible(Nd_ax.isIs_Activ());
            Ax.setLocation(Nd_ax.getLocation());
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

    public void Activar_Nodo(Nodo Nd) {
        JLabel ax;
        ax = ((JLabel) getComponentAt(Nd.getLocation()));
        ax.setBackground(Color.BLACK);
        for (Nodo N : Nd.getVertice()) {
            ax = ((JLabel) getComponentAt(N.getLocation()));
            ax.setVisible(true);
            ax.setBackground(Color.gray);
            N.setIs_Activ(true);
        }

        Tree.Calcular_Total();
    }

}
