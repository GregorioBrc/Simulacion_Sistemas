package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.Timer;

import model.Billetera;
import model.Generador;
import model.Modificador_Click;
import model.Nodo;
import view.*;

public class MotorJuego implements ActionListener, Compra_Listener, Click_Listener, Camb_Arbol {

    private Ventana Vt;
    private Billetera Bill;
    private ArrayList<ArbolPanel> Arbs;
    private int Arbol_Indx = 0;
    private double Click_Token_Value = 1;
    private Clip musicaFondo;
    private final String MUS_MENU = "/audio/background.wav";
    private String currentGameId;        // id = nombre de la partida
    Timer Tiker;

    // ================== CONSTRUCTOR ==================
    public MotorJuego() throws IOException {
        Arbs = new ArrayList<>();
        Bill = new Billetera();

        Vt = new Ventana();
        Vt.initMenuFlows(
            // MenuListener
            new MenuListener() {
                public void onStartNew() {
                    reproducirMusicaFondo(MUS_MENU);
                    Vt.showNuevo();
                }
                public void onLoadGame() {
                    reproducirMusicaFondo(MUS_MENU);
                    Vt.showCargar(Misc.SaveManager.list());
                }
                public void onExit() { System.exit(0); }
            },
            // NewGameListener
            new NewGameListener() {
                public void onSubmitNewGame(String user, String game) {
                    try {
                        currentGameId = game.trim();                // SOLO nombre de la partida
                        prepararJuego();
                        reproducirMusicaFondo(Arbs.get(0).getMusic());
                        Vt.showJuego(Arbs.get(0));                  // aquí se crean los paneles
                        Actualizar_Info_gene();                     // ya existe Pn_Gene
                        arrancarTick();
                        // Guardar estado inicial (opcional pero útil)
                        Misc.SaveManager.save(currentGameId, snapshotState());
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
                public void onBackToMenu() { Vt.showMenu(); }
            },
            // LoadGameListener
            new LoadGameListener() {
                public void onResumeSelected(String saveId) {
                    try {
                        currentGameId = saveId;                     // elegido en la lista
                        prepararJuego();
                        reproducirMusicaFondo(Arbs.get(0).getMusic());
                        Vt.showJuego(Arbs.get(0));
                        // aplicar estado real desde el JSON
                        Misc.SaveManager.load(currentGameId).ifPresent(s -> {
                            applyState(s);
                        });
                        arrancarTick();
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
                public void onBackToMenu() { Vt.showMenu(); }
            }
        ); // <-- cierra initMenuFlows

        // Pausa
        Vt.setPauseListener(new PauseListener() {
            public void onContinue(){ /* nada */ }
            public void onSave(){
                if (currentGameId != null && !currentGameId.isBlank()) {
                    Misc.SaveManager.save(currentGameId, snapshotState());
                }
            }
            public void onExitToMenu(){
                if (Tiker != null) Tiker.stop();
                reproducirMusicaFondo(MUS_MENU);
                Vt.showMenu();
            }
        });

        reproducirMusicaFondo(MUS_MENU);
        Vt.showMenu();
    }

    // ================== PREPARAR JUEGO ==================
    private void prepararJuego() throws IOException {
        Arbs.clear();
        Arbs.add(new ArbolPanel("Main", "fondo1.png", "background"));
        Arbs.add(new ArbolPanel("Dino", "Back_Dino.jpg", "dino"));
        Arbs.add(new ArbolPanel("Espacio", "Back_Space.png", "space"));

        Vt.setCant_Botones(Arbs.size());
        Cargar_Listeners();
        Registrar_Tokens();
        CalcularGeneracionGlobal();
        // No llamar a Actualizar_Info_gene() aquí: Pn_Gene aún no existe
    }

    private void arrancarTick() {
        if (Tiker != null) Tiker.stop();
        Tiker = new Timer(100, this);
        Tiker.start();
    }

    // ================== LOOP ==================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(Tiker)) {
            Bill.tick();
            Actualizar_Info_gene();
        }
    }

    // ================== INTERACCIONES ==================
    @Override
    public void Prerequisitos(Nodo Nd, int Modo) {
        if (Nd.getCosto() <= Bill.getCantidad(Nd.getToken())) {
            Bill.Restar(Nd.getToken(), Nd.getCosto());

            if (Modo == 0) {
                Arbs.get(Arbol_Indx).Activar_Nodo(Nd);
            } else if (Modo == 1) {
                Arbs.get(Arbol_Indx).Comprar_Generador(((Generador) Nd));
                Vt.getPn_Info().Cargar_Nodo(Nd);
            }

            CalcularGeneracionGlobal();
            Actualizar_Info_gene();
            Vt.repaint();
        }
    }

    @Override
    public void Click_Token() {
        Bill.Sumar_Tokens(Arbs.get(Arbol_Indx).getTree().getToken_a_Generar(), Click_Token_Value);
        Actualizar_Info_gene();
    }

    @Override
    public void Modi_Click_Token(Modificador_Click Nd) {
        Click_Token_Value *= Nd.getValor_Modif();
    }

    private void Cargar_Listeners() {
        for (ArbolPanel Arb : Arbs) {
            Arb.setCom_Listener(this);
            Arb.setCli_List(this);
        }
        Vt.setCamb_Arb_List(this);
    }

    private void Registrar_Tokens() {
        for (ArbolPanel arb : Arbs) {
            for (String nom : arb.getTree().getToken_a_Generar()) {
                Bill.registrarToken(nom, 0);
            }
        }
    }

    private void Actualizar_Info_gene() {
        if (Vt == null || Vt.getPn_Gene() == null) return; // blindaje
        ArrayList<String> Ax_Noms = Arbs.get(Arbol_Indx).getTree().getToken_a_Generar();
        Vt.getPn_Gene().Actualizar(Bill.getCantidades(Ax_Noms), Bill.getGeneracions(Ax_Noms));
    }

    // ================== MÚSICA ==================
    private void reproducirMusicaFondo(String ruta) {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
        try {
            URL url = getClass().getResource(ruta);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la música de fondo: " + e.getMessage());
        }
    }

    // ================== GENERACIÓN GLOBAL ==================
    private void CalcularGeneracionGlobal() {
        String[] todosLosTokens = Bill.getNombresDeTokens();
        for (String nombreToken : todosLosTokens) {
            double generacionTotal = 0.0;
            for (ArbolPanel arbolPanel : Arbs) {
                generacionTotal += arbolPanel.getTree().Get_Generado_Token(nombreToken);
            }
            Bill.actualizarGeneracion(nombreToken, generacionTotal);
        }
    }

    // ================== CAMBIO DE ÁRBOL ==================
    @Override
    public void Cambiar_Arbol(int Indx) {
        if (Indx == Arbol_Indx) return;
        if (Indx >= 0 && Indx < Arbs.size()) {
            Arbol_Indx = Indx;
            reproducirMusicaFondo(Arbs.get(Indx).getMusic());
            if (Vt.getPn_Gene() != null) {
                Vt.getPn_Gene().Reconstruir(Arbs.get(Indx).getTree().getToken_a_Generar());
                Actualizar_Info_gene();
            }
            Vt.CambiArb(Arbs.get(Indx));
        }
    }

    // ================== SNAPSHOT / RESTORE ==================

    // Serializa todo el estado importante en un JSON simple (sin librerías)
    private String snapshotState() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\"ver\":1,");
        sb.append("\"arb\":").append(Arbol_Indx).append(',');
        sb.append("\"click\":").append(Click_Token_Value).append(',');

        // tokens
        String[] names = Bill.getNombresDeTokens();
        sb.append("\"tokens\":{");
        for (int i = 0; i < names.length; i++) {
            String nm = names[i];
            sb.append('"').append(nm).append('"').append(':').append(Bill.getCantidad(nm));
            if (i < names.length - 1) sb.append(',');
        }
        sb.append("},");

        // árboles + nodos
        sb.append("\"trees\":[");
        for (int i = 0; i < Arbs.size(); i++) {
            var nodos = Arbs.get(i).getTree().getNodos();
            sb.append("{\"idx\":").append(i).append(",\"nodes\":[");
            for (int j = 0; j < nodos.size(); j++) {
                var n = nodos.get(j);
                sb.append("{\"id\":").append(n.getId())
                  .append(",\"act\":").append(n.isIs_Activ());
                if (n instanceof Generador) {
                    sb.append(",\"cant\":").append(((Generador) n).getCant());
                }
                sb.append("}");
                if (j < nodos.size() - 1) sb.append(',');
            }
            sb.append("]}");
            if (i < Arbs.size() - 1) sb.append(',');
        }
        sb.append("]}");
        return sb.toString();
    }

    // Reconstruye el estado. No cobra tokens al reactivar/poner cantidades.
    private void applyState(String raw) {
        try {
            // 1) árbol actual
            var mArb = java.util.regex.Pattern.compile("\"arb\"\\s*:\\s*(\\d+)").matcher(raw);
            if (mArb.find()) Arbol_Indx = Integer.parseInt(mArb.group(1));

            // 2) click
            var mClick = java.util.regex.Pattern.compile("\"click\"\\s*:\\s*([0-9eE+\\-.]+)").matcher(raw);
            if (mClick.find()) Click_Token_Value = Double.parseDouble(mClick.group(1));

            // 3) tokens (set exacto: restar actual y sumar guardado)
            var mTok = java.util.regex.Pattern.compile("\"tokens\"\\s*:\\s*\\{([^}]*)\\}").matcher(raw);
            if (mTok.find()) {
                String body = mTok.group(1).trim();
                if (!body.isEmpty()) {
                    for (String kvp : splitByComma(body)) {
                        String[] kv = kvp.split(":");
                        if (kv.length == 2) {
                            String name = kv[0].trim().replaceAll("^\"|\"$", "");
                            double val = Double.parseDouble(kv[1].trim());
                            double curr = Bill.getCantidad(name);
                            Bill.Restar(name, curr); // a cero
                            Bill.Sumar_Tokens(name, val);
                        }
                    }
                }
            }

            // 4) nodos por árbol (activar y poner cantidades)
            var mTrees = java.util.regex.Pattern.compile("\"trees\"\\s*:\\s*\\[(.*)\\]\\s*\\}$",
                    java.util.regex.Pattern.DOTALL).matcher(raw);
            if (mTrees.find()) {
                String treesArr = mTrees.group(1);
                var objMatcher = java.util.regex.Pattern.compile(
                    "\\{\\s*\"idx\"\\s*:\\s*(\\d+)\\s*,\\s*\"nodes\"\\s*:\\s*\\[([^\\]]*)\\]\\s*\\}"
                ).matcher(treesArr);

                while (objMatcher.find()) {
                    int idxTree = Integer.parseInt(objMatcher.group(1));
                    if (idxTree < 0 || idxTree >= Arbs.size()) continue;
                    String nodesArr = objMatcher.group(2).trim();
                    if (nodesArr.isEmpty()) continue;

                    for (String nodeObj : splitByObjects(nodesArr)) {
                        int id = extractInt(nodeObj, "\"id\"\\s*:\\s*(\\d+)", -1);
                        boolean act = extractBool(nodeObj, "\"act\"\\s*:\\s*(true|false)", false);
                        int cant = extractInt(nodeObj, "\"cant\"\\s*:\\s*(\\d+)", 0);

                        Nodo n = getNodoById(Arbs.get(idxTree), id);
                        if (n == null) continue;

                        // Si debe estar activo y no lo está, activarlo visualmente (no cobra)
                        if (act && !n.isIs_Activ()) {
                            Arbs.get(idxTree).Activar_Nodo(n); // esto pone cant=1 si es generador
                        }

                        // Si es generador, fijar cantidad = cant (compras sin cobrar)
                        if (n instanceof Generador) {
                            Generador g = (Generador) n;
                            int actual = g.getCant();
                            // si quedó 0 y debe estar activo, activación ya puso 1.
                            // Queremos llevar a 'cant':
                            for (int k = actual; k < cant; k++) {
                                Arbs.get(idxTree).Comprar_Generador(g); // incrementa cant y UI
                            }
                        }
                    }
                }
            }

            // Recalcular totales y refrescar UI
            for (ArbolPanel ap : Arbs) {
                ap.getTree().Calcular_Total();
            }
            if (Vt.getPn_Gene() != null) {
                Vt.getPn_Gene().Reconstruir(Arbs.get(Arbol_Indx).getTree().getToken_a_Generar());
                Actualizar_Info_gene();
            }
            Vt.CambiArb(Arbs.get(Arbol_Indx));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===== Helpers de parse/lookup que no requieren tocar otras clases =====
    private static int extractInt(String s, String regex, int def) {
        var m = java.util.regex.Pattern.compile(regex).matcher(s);
        return m.find() ? Integer.parseInt(m.group(1)) : def;
    }
    private static boolean extractBool(String s, String regex, boolean def) {
        var m = java.util.regex.Pattern.compile(regex).matcher(s);
        return m.find() ? Boolean.parseBoolean(m.group(1)) : def;
    }
    private static List<String> splitByComma(String body) {
        // separa por comas de nivel 0 (no hay anidaciones complejas en nuestro JSON)
        List<String> out = new ArrayList<>();
        int last = 0, depth = 0;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '{' || c == '[') depth++;
            else if (c == '}' || c == ']') depth--;
            else if (c == ',' && depth == 0) {
                out.add(body.substring(last, i).trim());
                last = i + 1;
            }
        }
        if (last < body.length()) out.add(body.substring(last).trim());
        return out;
    }
    private static List<String> splitByObjects(String arr) {
        // separa objetos {...} dentro de un array, sin JSON parser
        List<String> out = new ArrayList<>();
        int start = 0, depth = 0;
        boolean inObj = false;
        for (int i = 0; i < arr.length(); i++) {
            char c = arr.charAt(i);
            if (c == '{') {
                if (!inObj) { start = i; inObj = true; }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && inObj) {
                    out.add(arr.substring(start, i + 1));
                    inObj = false;
                }
            }
        }
        return out;
    }
    private static Nodo getNodoById(ArbolPanel p, int id) {
        for (Nodo n : p.getTree().getNodos()) {
            if (n.getId() == id) return n;
        }
        return null;
    }
}
