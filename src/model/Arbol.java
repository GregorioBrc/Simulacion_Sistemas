package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import Misc.Tokens;
import Misc.Utils;

public class Arbol {
    private ArrayList<Double> Total_GeneraxTick;
    private ArrayList<String> Token_a_Generar;
    private String Nombre;

    private JPanel Cuerpo;

    private ArrayList<Nodo> Nodos;

    public Arbol(String Nm) throws IOException {
        Nodos = new ArrayList<Nodo>();
        Token_a_Generar = new ArrayList<String>();
        Total_GeneraxTick = new ArrayList<Double>();
        Nombre = Nm;
        Cargar_Nodos();
        Cargar_Tokens();
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

    public void setNodos(ArrayList<Nodo> nodos) {
        Nodos = nodos;
    }

    public ArrayList<Double> getTotal_GeneraxTick() {
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

    public ArrayList<String> getToken_a_Generar() {
        return Token_a_Generar;
    }

    public void Cargar_Tokens() {
        boolean Enc;
        for (int i = 0; i < Nodos.size(); i++) {
            Enc = false;
            for (int j = 0; j < Token_a_Generar.size(); j++) {
                if (Token_a_Generar.get(j).equals(Nodos.get(i).getToken())) {
                    Enc = true;
                    break;
                }
            }
            if (!Enc) {
                Token_a_Generar.add(Nodos.get(i).getToken());
            }
        }
    }

    private void Cargar_Nodos() throws IOException {
        File Fl = new File("Nd_" + Nombre + ".dt"); 
        if (Fl.exists()) {
            Scanner Sc = new Scanner(Fl);
            String Ax;
            String[] Spl;
            while (Sc.hasNextLine()) {
                Ax = Sc.nextLine();

                if (Ax.startsWith("//") || Ax.trim().isEmpty()) {
                    continue;
                }

                Spl = Ax.split("\\|");
                // Asegura que hay suficientes elementos
                if (Spl.length < 9) {
                    System.err.println("Línea inválida: " + Ax);
                    continue;
                }

                // Crea el Nodo base con el nuevo constructor
                Nodo Ax_Nd = new Nodo(
                    Integer.parseInt(Spl[1]),
                    Tokens.tokens[Integer.parseInt(Spl[2])],
                    Spl[4],
                    Spl[5],
                    Boolean.parseBoolean(Spl[6]),
                    Double.parseDouble(Spl[3]),
                    Spl[Spl.length - 1] // Último campo = nombreImagen
                );

                Ax_Nd.setId_Vertice(Utils.String_To_Array(Spl[7]));

                switch (Spl[0]) {
                    case "Nd_Gene":
                        // |...|valorGenerado|nombreImagen
                        Nodos.add(new Generador(Ax_Nd, Utils.Parse_Dou(Spl[8])));
                        break;

                    case "Nd_Clic":
                        // |...|valorMod|descMod|nombreImagen
                        Nodos.add(new Modificador_Click(Ax_Nd, Utils.Parse_Dou(Spl[8]), Spl[9]));
                        break;

                    case "Nd_Modi":
                        // |...|valorMod|descMod|nodosAfectados|nombreImagen
                        Modificador_Nodo modi = new Modificador_Nodo(Ax_Nd, Utils.Parse_Dou(Spl[8]), Spl[9]);
                        modi.setId_Nodos_Afect(Utils.String_To_Array(Spl[10]));
                        Nodos.add(modi);
                        break;

                    default:
                        System.err.println("Tipo de nodo desconocido: " + Spl[0]);
                        break;
                }
            }
            Sc.close();
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
                        break;
                    }
                }
            }
            Nodos.get(i).setVertice(Ax);

            if (Nodos.get(i) instanceof Modificador_Nodo) {
                Modificador_Nodo Ax_mo = (Modificador_Nodo) Nodos.get(i);
                
                for (int In : Ax_mo.getId_Nodos_Afect()) {
                    Ax = new ArrayList<Nodo>();
                    for (Nodo nodo : Nodos) {
                        if (In == nodo.getId()) {
                            Ax.add(nodo);
                            break;
                        }
                    }
                    Ax_mo.setNodos_Afect(Ax);
                }
            }
        }
    }

    public void Calcular_Total() {

        Total_GeneraxTick.clear();
        for (int i = 0; i < Token_a_Generar.size(); i++) {
            Total_GeneraxTick.add(0.0);
        }

        for (int i = 0; i < Nodos.size(); i++) {
            for (int j = 0; j < Token_a_Generar.size(); j++) {
                if (Nodos.get(i) instanceof Generador && Nodos.get(i).getToken() == Token_a_Generar.get(j)
                        && Nodos.get(i).isIs_Activ()) {
                    Double Ax = Total_GeneraxTick.get(j);
                    Ax += ((Generador) Nodos.get(i)).getGeneradoxTick();
                    Total_GeneraxTick.set(j, Ax);
                }
            }
        }
    }

    public double Get_Generado_Token(String ax) {
        for (int i = 0; i < Token_a_Generar.size(); i++) {
            if (Token_a_Generar.get(i).equals(ax)) {
                return Total_GeneraxTick.get(i);
            }
        }
        return 0;
    }

}
