// src/Misc/SaveManager.java
package Misc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SaveManager {
    private static final Path DIR = Paths.get("saves");
    static { try { Files.createDirectories(DIR); } catch (IOException ignored) {} }

    public static void save(String id, String payload) {
        try {
            Files.writeString(DIR.resolve(safe(id)+".json"), payload, StandardCharsets.UTF_8);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static Optional<String> load(String id) {
        try {
            Path f = DIR.resolve(safe(id)+".json");
            if (!Files.exists(f)) return Optional.empty();
            return Optional.of(Files.readString(f, StandardCharsets.UTF_8));
        } catch (IOException e) { return Optional.empty(); }
    }

    public static List<String> list() {
        try (var s = Files.list(DIR)) {
            List<String> r = new ArrayList<>();
            s.filter(p -> p.toString().endsWith(".json")).forEach(p -> {
                String n = p.getFileName().toString();
                r.add(n.substring(0, n.length() - 5));
            });
            Collections.sort(r);
            return r;
        } catch (IOException e) {
            return List.of();
        }
    }

    private static String safe(String id){ return id.replaceAll("[^a-zA-Z0-9_\\-\\.]+","_"); }
}