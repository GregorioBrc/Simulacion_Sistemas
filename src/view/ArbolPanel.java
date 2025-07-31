package view;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Arbol_Listener;
import controller.Click_Listener;
import controller.Compra_Listener;
import model.Arbol;
import model.Generador;
import model.Modificador_Click;
import model.Modificador_Nodo;
import model.Nodo;

public class ArbolPanel extends JPanel implements MouseListener {

    private Arbol Tree;
    private final int Const_Separacion = 100;
    private Arbol_Listener Ar_List;
    private JLabel Node_Select_Jlabel;
    private Nodo Node_Select;
    private Compra_Listener Com_Listener;
    private Click_Listener Cli_List;

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
            if (new Rectangle(nodo.getLocation(), nodo.getDm()).contains(e.getPoint())) {
                Ax_Nd = nodo;
            }
        }

        if (Ax_Nd != null) {
            if (!getComponentAt(Ax_Nd.getLocation()).isVisible()) {
                Ax_Nd = null;
            }
        }

        if (Ax_Nd != null) {

            if (Node_Select == Ax_Nd) {
                if (!Node_Select.isIs_Activ()) {
                    Com_Listener.Prerequisitos(Node_Select);
                }
            } else {
                Node_Select_Jlabel = (JLabel) (getComponentAt(Ax_Nd.getLocation()));
                Node_Select = Ax_Nd;
                Ar_List.onNodoSeleccionado(Ax_Nd);
            }

        } else {
            if (Node_Select == null) {
                Cli_List.Click_Token();
            }

            Node_Select_Jlabel = null;
            Node_Select = null;
            Ar_List.onFondoDeArbolClickeado();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

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

    public void setCli_List(Click_Listener cli_List) {
        Cli_List = cli_List;
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
            Ax.setBackground(Color.gray);
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

            if (i != 0) {
                Ax.setVisible(Nd_ax.isIs_Activ());
            }

            Ax.setLocation(Nd_ax.getLocation());
            if (j == c) {
                b -= Const_Separacion;
                c++;
                j = 0;
                k = 1;
            }
            add(Ax);
        }
    }

    public void Activar_Nodo(Nodo Nd) {
        JLabel ax;
        ax = ((JLabel) getComponentAt(Nd.getLocation()));
        ax.setBackground(Color.BLACK);
        Nd.setIs_Activ(true);
        for (Nodo N : Nd.getVertice()) {
            ax = ((JLabel) getComponentAt(N.getLocation()));
            ax.setVisible(true);
            ax.setBackground(Color.gray);
        }

        if (Nd instanceof Modificador_Nodo) {
            Asignar_Modificador((Modificador_Nodo) Nd);
        }

        if (Nd instanceof Modificador_Click) {
            Cli_List.Modi_Click_Token((Modificador_Click) Nd);
            return;
        }

        Tree.Calcular_Total();
    }

    private void Asignar_Modificador(Modificador_Nodo nd) {
        for (Nodo N : nd.getNodos_Afect()) {
            if (N instanceof Generador) {
                ((Generador) N).Modificar_Gene(nd);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Nodo nodoOrigen : Tree.getNodos()) {
            Point origen = nodoOrigen.getLocation();
            int x1 = origen.x + nodoOrigen.getDm().width / 2;
            int y1 = origen.y + nodoOrigen.getDm().height / 2;

            for (Nodo nodoDestino : nodoOrigen.getVertice()) {
                Point destino = nodoDestino.getLocation();
                int x2 = destino.x + nodoDestino.getDm().width / 2;
                int y2 = destino.y + nodoDestino.getDm().height / 2;

                if (!nodoDestino.isIs_Activ()) {
                    g2d.setColor(Color.GRAY);

                    BasicStroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                            new float[] { 9 }, 0);
                    g2d.setStroke(dashed);
                } else {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.setStroke(new BasicStroke(3));
                }

                if (nodoOrigen.isIs_Activ()) {
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }

}
