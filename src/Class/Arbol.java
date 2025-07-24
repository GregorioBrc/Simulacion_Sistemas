package Class;

import java.awt.Color;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import Misc.Utils;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Arbol {
    public double Total_GeneraxTick;
    public JPanel Cuerpo;
    public ArrayList<Nodo> Nodos;

    public Arbol() throws IOException {
        Nodos = new ArrayList<Nodo>();
        Cargar_Panel();
        Cargar_Nodos();
        Cargar_Vertices();
        Cargar_Cuerpo_Nodos();
        Calcular_Total();
    }

    private void Cargar_Panel() {
        Cuerpo = new JPanel();
        Cuerpo.setSize(500, 500);
        Cuerpo.setLayout(null);
        Cuerpo.setBackground(Color.GREEN);
    }

    private void Cargar_Nodos() throws IOException {
        File Fl = new File("Nodos.dt");
        if (Fl.exists()) {
            BufferedReader Br = new BufferedReader(new FileReader(Fl));

            String Ax = "";
            String[] Spl;

            while ((Ax = Br.readLine()) != null && Ax.length() != 0) {
                Spl = Ax.split("\\|");
                Nodo Ax_Nd = new Nodo(Integer.parseInt(Spl[1]), Spl[2], Spl[3], Boolean.parseBoolean(Spl[4]));
                Ax_Nd.setId_Vertice(Utils.String_To_Array(Spl[5]));

                if (Spl[0].equals("Nd_Gene")) {
                    Nodos.add(new Generador(Ax_Nd, Utils.Parse_Dou(Spl[6])));

                } else if (Spl[0].equals("Nd_Click")) {
                    Nodos.add(new Modificador_Click(Ax_Nd, Utils.Parse_Dou(Spl[6]), Spl[7]));

                } else if (Spl[0].equals("Nd_Modi")) {
                    Modificador_Nodo a = new Modificador_Nodo(Ax_Nd, Utils.Parse_Dou(Spl[6]), Spl[7]);
                    a.setId_Nodos_Afect(Utils.String_To_Array(Spl[8]));
                    Nodos.add(a);
                }
            }

            Br.close();
        } else {
            Nodos = new ArrayList<>();
            System.out.println("Nodos no encontrado. Iniciando lista vacía.");
            Fl.createNewFile();

        }
    }

    public void Guardar_Nodos() {
        try {
            File Fl = new File("Nodos.dt");
            BufferedWriter Bw = new BufferedWriter(new FileWriter(Fl));
            String Nd = "";

            for (int i = 0; i < Nodos.size(); i++) {
                if (Nodos.get(i) instanceof Generador) {
                    Nd = ((Generador) Nodos.get(i)).toString();
                } else if (Nodos.get(i) instanceof Modificador_Nodo) {
                    Nd = ((Modificador_Nodo) Nodos.get(i)).toString();
                } else if (Nodos.get(i) instanceof Modificador_Click) {
                    Nd = ((Modificador_Click) Nodos.get(i)).toString();
                } else {
                    throw new Exception("No deberia pasar");
                }
                Bw.write(Nd);
                Bw.newLine();
            }

            Bw.close();

            System.out.println("Nodos Guardados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Cargar_Vertices() {
        ArrayList<Nodo> Ax;

        for (int i = 0; i < Nodos.size(); i++) {
            Ax = new ArrayList<Nodo>();
            for (int j = 0; j < Nodos.get(i).getId_Vertice().length; j++) {
                for (int j2 = 0; j2 < Nodos.size(); j2++) {
                    if (Nodos.get(i).getId_Vertice()[j] == Nodos.get(j2).getId()) {
                        Ax.add(Nodos.get(j2));
                    }
                }
            }
            Nodos.get(i).setVertice(Ax);
        }
    }

    public void Cargar_Cuerpo_Nodos() {
        int a = Cuerpo.getWidth() / 2, b = Cuerpo.getHeight() / 2, c = 1;

        for (int i = 0, j = 1; i < Nodos.size(); i++,j++) {
            JLabel Ax = Nodos.get(i).getCuerpo();
            Ax.setBackground(Color.BLACK);
            Ax.setOpaque(true);
            // Ax.setSize(10, 10);
            if (c % 2 == 0) {
                if (j < c / 2) {
                    Ax.setLocation(a - j * 25, b);
                } else {
                    Ax.setLocation(a + j * 25, b);
                }
            } else {
                if (j < Math.round((double) c / 2.0)) {
                    Ax.setLocation(a - j * 25, b);
                }
                if (j > Math.round((double) c / 2.0)) {
                    Ax.setLocation(a + j * 25, b);
                } else {
                    Ax.setLocation(a, b);
                }
            }

            //Ax.setLocation(a - Ax.getWidth() / 2, b - Ax.getHeight() - 10);

            if (j == c) {
                b -= 30;
                c++;
                j = 1;
            }
            System.out.println(Ax.getBounds());
            Cuerpo.add(Ax);
        }
    }

    private void Calcular_Total() {
        Total_GeneraxTick = 0;
        for (int i = 0; i < Nodos.size(); i++) {
            if (Nodos.get(i) instanceof Generador) {
                Total_GeneraxTick += ((Generador) Nodos.get(i)).getGeneradoxTick();
            }
        }
    }

    public void setNodos(ArrayList<Nodo> nodos) {
        Nodos = nodos;
    }

}
