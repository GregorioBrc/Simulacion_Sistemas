package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Billetera {
    // nombreToken -> cantidad
    private Map<String, Double> tokens;
    // nombreToken -> generación por tick
    private Map<String, Double> generacion_x_Tick;

    public Billetera() {
        this.tokens = new HashMap<>();
        this.generacion_x_Tick = new HashMap<>();
    }

    // ====== Registro / mutaciones básicas ======
    public void registrarToken(String nombre, double cantidadInicial) {
        tokens.put(nombre, cantidadInicial);
        generacion_x_Tick.put(nombre, 0.0);
    }

    public void Sumar_Tokens(String nombre, double cantidad) {
        tokens.put(nombre, tokens.getOrDefault(nombre, 0.0) + cantidad);
    }

    public void Sumar_Tokens(ArrayList<String> nombres, double cantidad) {
        for (String Nom : nombres) {
            tokens.put(Nom, tokens.getOrDefault(Nom, 0.0) + cantidad);
        }
    }

    public void Sumar_Tokens_Generado(String nombre, double cantidad) {
        generacion_x_Tick.put(nombre, generacion_x_Tick.getOrDefault(nombre, 0.0) + cantidad);
    }

    public void actualizarGeneracion(String nombre, double nuevaGeneracionTotal) {
        generacion_x_Tick.put(nombre, nuevaGeneracionTotal);
    }

    public void NormalizarGeneracion(Arbol[] Arb) {
        // (no usada actualmente)
    }

    // Tick del juego: agregar lo generado
    public void tick() {
        for (String nombre : tokens.keySet()) {
            Sumar_Tokens(nombre, generacion_x_Tick.getOrDefault(nombre, 0.0));
        }
    }

    public void Restar(String Nm, double Val) {
        tokens.put(Nm, tokens.get(Nm) - Val);
    }

    // ====== Getters ======
    public double getCantidad(String nombre) {
        return tokens.getOrDefault(nombre, 0.0);
    }

    public double[] getCantidades(ArrayList<String> NN) {
        double[] AxCant = new double[NN.size()];
        for (int i = 0; i < AxCant.length; i++) {
            AxCant[i] = tokens.getOrDefault(NN.get(i), 0.0);
        }
        return AxCant;
    }

    public double getGeneracion(String nombre) {
        return generacion_x_Tick.getOrDefault(nombre, 0.0);
    }

    public double[] getGeneracions(ArrayList<String> NN) {
        double[] AxGent = new double[NN.size()];
        for (int i = 0; i < AxGent.length; i++) {
            AxGent[i] = generacion_x_Tick.getOrDefault(NN.get(i), 0.0);
        }
        return AxGent;
    }

    public String[] getNombresDeTokens() {
        return tokens.keySet().toArray(new String[0]);
    }

    // ====== Setters directos (necesarios para cargar partidas) ======
    public void setCantidad(String nombre, double valor) {
        tokens.put(nombre, valor);
        // asegura llave en generacion
        generacion_x_Tick.putIfAbsent(nombre, 0.0);
    }

    public void setGeneracion(String nombre, double valor) {
        generacion_x_Tick.put(nombre, valor);
        // asegura llave en tokens
        tokens.putIfAbsent(nombre, 0.0);
    }

    public void clear() {
        tokens.clear();
        generacion_x_Tick.clear();
    }

    // ====== Serialización simple a JSON (sin librerías) ======
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"tokens\":{");
        int i = 0;
        for (Map.Entry<String, Double> e : tokens.entrySet()) {
            if (i++ > 0) sb.append(',');
            sb.append('"').append(escape(e.getKey())).append('"')
              .append(':').append(e.getValue());
        }
        sb.append("},\"gen\":{");
        i = 0;
        for (Map.Entry<String, Double> e : generacion_x_Tick.entrySet()) {
            if (i++ > 0) sb.append(',');
            sb.append('"').append(escape(e.getKey())).append('"')
              .append(':').append(e.getValue());
        }
        sb.append("}}");
        return sb.toString();
    }

    // ====== Deserialización simple desde JSON (sin librerías) ======
    public void fromJSON(String raw) {
        if (raw == null || raw.isBlank()) return;
        // tokens
        String tokBody = extractObject(raw, "\"tokens\"\\s*:\\s*\\{", "\\}");
        if (tokBody != null) {
            for (String kvp : splitByComma(tokBody)) {
                String[] kv = kvp.split(":");
                if (kv.length == 2) {
                    String name = unquote(kv[0].trim());
                    double val = parseDoubleSafe(kv[1].trim(), 0);
                    setCantidad(name, val);
                }
            }
        }
        // gen
        String genBody = extractObject(raw, "\"gen\"\\s*:\\s*\\{", "\\}");
        if (genBody != null) {
            for (String kvp : splitByComma(genBody)) {
                String[] kv = kvp.split(":");
                if (kv.length == 2) {
                    String name = unquote(kv[0].trim());
                    double val = parseDoubleSafe(kv[1].trim(), 0);
                    setGeneracion(name, val);
                }
            }
        }
    }

    // ====== Helpers mínimos de parse ======
    private static String escape(String s){
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    private static String unquote(String s){
        return s.replaceAll("^\"|\"$", "").replace("\\\"", "\"").replace("\\\\","\\");
    }
    private static double parseDoubleSafe(String s, double def){
        try { return Double.parseDouble(s); } catch(Exception e){ return def; }
    }
    private static String extractObject(String src, String startRegex, String endRegex){
        var m = java.util.regex.Pattern.compile(startRegex).matcher(src);
        if (!m.find()) return null;
        int start = m.end();
        int depth = 0;
        for (int i = start; i < src.length(); i++) {
            char c = src.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                if (depth == 0) return src.substring(start, i);
                depth--;
            }
        }
        return null;
    }
    private static java.util.List<String> splitByComma(String body) {
        java.util.List<String> out = new java.util.ArrayList<>();
        int last = 0, depth = 0;
        boolean inStr = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i-1) != '\\')) inStr = !inStr;
            if (!inStr) {
                if (c == '{' || c == '[') depth++;
                else if (c == '}' || c == ']') depth--;
                else if (c == ',' && depth == 0) {
                    out.add(body.substring(last, i).trim());
                    last = i + 1;
                }
            }
        }
        if (last < body.length()) out.add(body.substring(last).trim());
        return out;
    }
}
