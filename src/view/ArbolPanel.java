package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.Arbol_Listener;
import controller.Click_Listener;
import controller.Compra_Listener;
import model.Arbol;
import model.Generador;
import model.Modificador_Click;
import model.Modificador_Nodo;
import model.Nodo;

public class ArbolPanel extends JPanel implements MouseListener {

    private final int Const_Separacion = 100;
    private final double Const_Grand = 1.25;

    private Arbol Tree;
    private Arbol_Listener Ar_List;
    private Nodo_Vista Node_Select_Jlabel;
    private Nodo Node_Select;
    private Compra_Listener Com_Listener;
    private Click_Listener Cli_List;
    private Image imagenFondo = null;


    public ArbolPanel(String Nm, String Fondo) {
        try {
            Tree = new Arbol(Nm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IniciarComponent();
        cargarImagenFondo("src/img/"+Fondo); // Ruta relativa| src/img/fondo1.png
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
                    Com_Listener.Prerequisitos(Node_Select, 0);
                } else if (Ax_Nd instanceof Generador) {
                    Com_Listener.Prerequisitos(Node_Select, 1);
                }
            } else {
                if (Node_Select_Jlabel != null) {
                    DeSelecionar_Nodo(Node_Select_Jlabel);
                }
                Node_Select_Jlabel = (Nodo_Vista) (getComponentAt(Ax_Nd.getLocation()));
                Node_Select = Ax_Nd;
                Selecionar_Nodo(Node_Select_Jlabel);
                Ar_List.onNodoSeleccionado(Ax_Nd);
            }

        } else {
            if (Node_Select == null) {
                Cli_List.Click_Token();
            } else {
                DeSelecionar_Nodo(Node_Select_Jlabel);
            }

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

    public void setCli_List(Click_Listener cli_List) {
        Cli_List = cli_List;
    }

    public void Activar_Nodo(Nodo Nd) {
        Nodo_Vista ax = (Nodo_Vista) getComponentAt(Nd.getLocation());
        ax.setNombreImagen(Nd.getNombreImagen());
        ax.setIsActiv(true); // Activa visualmente (por ejemplo cambia color a verde)
        Nd.setIs_Activ(true);

        if (Nd instanceof Generador) {
            ((Generador) Nd).Comprar_Uni();
            ax.setTexto(((Generador) Nd).getCant() + ""); // Mostramos el valor numérico si quieres
        }

        if (Nd instanceof Modificador_Nodo) {
            Asignar_Modificador((Modificador_Nodo) Nd);
        }

        if (Nd instanceof Modificador_Click) {
            Cli_List.Modi_Click_Token((Modificador_Click) Nd);
            return;
        }

        Tree.Calcular_Total();

        for (Nodo N : Nd.getVertice()) {
            Nodo_Vista vecino = (Nodo_Vista) getComponentAt(N.getLocation());
            vecino.setVisible(true);
            vecino.setColor(Color.GRAY); // Visualmente "habilitado"
        }
    }


    public void Comprar_Generador(Generador Nd) {
        Nd.Comprar_Uni();

        Nodo_Vista ax = ((Nodo_Vista) getComponentAt(Nd.getLocation()));
        ax.setForeground(Color.white);
        ax.setTexto(Nd.getCant() + "");

        Tree.Calcular_Total();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo != null) {
            g.drawImage(imagenFondo,0, 0, getWidth(), getHeight(), this);
        }

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

    private void Selecionar_Nodo(Nodo_Vista Nd_Jla) {
        Nd_Jla.setSize((int) (Nd_Jla.getWidth() * Const_Grand), (int) (Nd_Jla.getHeight() * Const_Grand));
    }

    private void DeSelecionar_Nodo(Nodo_Vista Nd_Jla) {
        Nd_Jla.setSize((int) (Nd_Jla.getWidth() / Const_Grand), (int) (Nd_Jla.getHeight() / Const_Grand));
    }

    private void cargarImagenFondo(String path) {
        try {
            imagenFondo = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error cargando imagen de fondo: " + path);
        }
    }

    private void IniciarComponent() {
        setSize(1200, 800);
        setLayout(null);
        addMouseListener(this);
    }

private void Cargar_Cuerpo_Nodos() {
    int centerX = getWidth() / 2;
    int offsetX = -20; // ⇨ Ajusta este valor para ir más a la derecha
    int startY = getHeight() - 150;
    int level = 0;
    int nodeIndex = 0;

    int nodesInLevel = 1;

        while (nodeIndex < Tree.getNodos().size()) {
            int spacing = Const_Separacion;
            int totalWidth = (nodesInLevel - 1) * spacing;
            int startX = centerX - totalWidth / 2 + offsetX;

            for (int i = 0; i < nodesInLevel && nodeIndex < Tree.getNodos().size(); i++, nodeIndex++) {
                Nodo nodo = Tree.getNodos().get(nodeIndex);
                Nodo_Vista vista = new Nodo_Vista();
                vista.setSize(nodo.getDm());
                vista.setColor(Color.GRAY);

                // Posición del nodo
                int x = startX + i * spacing;
                int y = startY - level * Const_Separacion;

                nodo.setLocation(x, y);
                vista.setLocation(x, y);

                if (nodeIndex != 0) {
                    vista.setVisible(nodo.isIs_Activ());
                }

                add(vista);
            }

            level++;
            nodesInLevel++;
        }
    }



    private void Asignar_Modificador(Modificador_Nodo nd) {
        for (Nodo N : nd.getNodos_Afect()) {
            if (N instanceof Generador) {
                ((Generador) N).Modificar_Gene(nd);
            }
        }
    }

}
