package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import Misc.Utils;

public class Arbol {
    private double Total_GeneraxTick;
    private double Total_Tokens;

    private String Nombre;

    private JPanel Cuerpo;
    private ArrayList<Nodo> Nodos;

    public Arbol(String Nm) throws IOException {
        Nodos = new ArrayList<Nodo>();
        Nombre = Nm;
        Cargar_Nodos();
        Cargar_Vertices();
        Calcular_Total();
    }

    public void Guardar_Nodos() {
        try {
            File Fl = new File("Nd_" + Nombre + ".dt");
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

    public void Actualizar_Tokens(){
        Total_Tokens += Total_GeneraxTick;
    }

    public void setNodos(ArrayList<Nodo> nodos) {
        Nodos = nodos;
    }

    public double getTotal_Tokens() {
        return Total_Tokens;
    }

    public void setTotal_Tokens(double total_Tokens) {
        Total_Tokens = total_Tokens;
    }

    public double getTotal_GeneraxTick() {
        return Total_GeneraxTick;
    }

    public JPanel getCuerpo() {
        return Cuerpo;
    }

    public ArrayList<Nodo> getNodos() {
        return Nodos;
    }

    public String getNombre() {
        return Nombre;
    }

    private void Cargar_Nodos() throws IOException {
        File Fl = new File("Nd_" + Nombre + ".dt");
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

    private void Calcular_Total() {
        Total_GeneraxTick = 0;
        for (int i = 0; i < Nodos.size(); i++) {
            if (Nodos.get(i) instanceof Generador) {
                Total_GeneraxTick += ((Generador) Nodos.get(i)).getGeneradoxTick();
            }
        }
    }

}
