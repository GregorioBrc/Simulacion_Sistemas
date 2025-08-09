#AUTORES 
Briceño Jose V-29.544.700
Porras Axel V- 29.545.523
Zambrano Kevin V-29.929.008


````markdown
# 🧬 Juego de la Vida — README

> **Simulación incremental** con árboles de progreso (Main, Dino y Espacio), menús (Inicio → Nuevo → Cargar → Juego), **pausa con guardado/carga**, música y UI con **bordes redondeados**.  
> Hecho en **Java Swing** sin frameworks externos.

---

## 📦 Requisitos

- **Java JDK 21** (compilar **y** ejecutar con la **misma** versión).

```bash
javac -version   # → 21.x
java  -version   # → 21.x
````

> Si `javac` es 21 pero `java` es 1.8, ajusta el **PATH** para que ambos apunten al JDK 21.

* (Opcional) **VS Code** + Extension Pack for Java.

* **Estructura del proyecto**

```
src/
  audio/              # background.wav (menú) y música in-game
  controller/         # MotorJuego + listeners
  img/                # background.png, logo.png, Back_*.png/jpg
  Main/               # App.java (punto de entrada)
  Misc/               # SaveManager.java
  model/              # Árbol, Billetera, Nodos, etc.
  view/               # Ventana, Menús, Paneles
```

---

## 🚀 Compilación y ejecución

### 🪟 Windows (PowerShell)

```powershell
# Ubicado en la carpeta raíz (donde está /src)
Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
mkdir bin | Out-Null

# Compilar
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })

# Ejecutar  (en Windows el separador del classpath es ;)
java -cp "bin;src" Main.App
```

### 🐧 macOS / Linux (bash)

```bash
rm -rf bin && mkdir bin
find src -name "*.java" | xargs javac -encoding UTF-8 -d bin

# En Unix el separador del classpath es :
java -cp "bin:src" Main.App
```

> **UnsupportedClassVersionError (class file version 65)**
> Estás ejecutando con Java 8. Alinea `java -version` a **21.x**.

---

## 🕹️ Flujo y controles

* **Menú de Inicio**

  * **Iniciar Partida** → formulario (usuario + **nombre de partida**).
    El **nombre de la partida** es el **ID de guardado**.
  * **Cargar Partida** → lista de partidas desde `./saves/`.
  * **Salir** → cierra la app.

* **Juego**

  * Clic en **fondo** → genera el token principal.
  * Clic en **nodo** → activa/compra (si alcanza el recurso).
  * **Flechas** ← ↑ → ↓ → desplazan el árbol.
  * **ESC** o botón **Pausa** (arriba izquierda) → submenú:

    * **Continuar**.
    * **Guardar partida** → guarda **estado completo**.
    * **Salir al Menú** → detiene el loop y vuelve al inicio.
  * **Mapas**: **Mapa 1/2/3** (arriba derecha) → Main / Dino / Espacio.

---

## 💾 Guardado y carga

* Los saves viven en `./saves/<id>.json` (UTF-8), donde `<id>` es el **nombre de la partida**.
* Se guarda:

  * `arb` → índice del árbol activo.
  * `click` → multiplicador/valor del click.
  * `tokens` → cantidades exactas por token.
  * `trees[].nodes[]` → `{ id, act, cant }` (estado y cantidad de generadores).

**Ejemplo de save:**

```json
{
  "ver": 1,
  "arb": 0,
  "click": 1.0,
  "tokens": { "Ideas": 132.2, "Fósiles": 0.0 },
  "trees": [
    { "idx": 0, "nodes": [ { "id": 0, "act": true, "cant": 3 } ] }
  ]
}
```

**Al cargar**, se restauran tokens/click/árbol; nodos se **reactivan sin costo** y los generadores vuelven a su **cantidad**.

---

## 🧱 Arquitectura (carpetas y clases clave)

```
src/
├─ Main/
│  └─ App.java                 # Punto de entrada
├─ controller/
│  ├─ MotorJuego.java          # Loop (Timer), música, guardado/carga, flujos de menú
│  ├─ *.java                   # Interfaces: Menu/NewGame/LoadGame/Pause/Compra/Click/Cambio
├─ view/
│  ├─ Ventana.java             # Marco principal (CardLayout para menús + modo juego)
│  ├─ MenuInicio.java          # Menú de inicio (logo + botones redondeados)
│  ├─ FormNuevoJuego.java      # Form (usuario + nombre de partida)
│  ├─ MenuCargar.java          # Lista de partidas guardadas
│  ├─ PanelPausa.java          # Diálogo con Continuar/Guardar/Salir
│  ├─ ArbolPanel.java          # Render de árbol, líneas, clics y compras
│  ├─ Panel_Info_*.java        # HUD de recursos / info de nodo
├─ model/
│  ├─ Billetera.java           # Tokens y generación por tick (getCantidades/getGeneracions)
│  ├─ Arbol.java               # Cargar nodos, calcular totales
│  ├─ Nodo.java                # Base
│  ├─ Generador.java           # Cantidad, costo escalado (1.15^n), generación
│  ├─ Modificador_*.java
├─ Misc/
│  └─ SaveManager.java         # Persistencia simple (archivos .json)
└─ audio/, img/                # Recursos WAV/PNG/JPG
```

**Highlights técnicos**

* **MotorJuego**

  * `initMenuFlows(...)` + `showMenu/Nuevo/Cargar` → navegación.
  * `prepararJuego()` → crea ArbolPaneles, registra tokens y música.
  * `arrancarTick()` → `javax.swing.Timer` (tick → `Billetera.tick()` + HUD).
  * `snapshotState()/applyState()` → serialización/restore sin librerías externas.
  * `reproducirMusicaFondo()` → `Clip` con loop (WAV PCM).

* **Ventana**

  * CardLayout para menús; UI original para el juego.
  * Botón **Pausa** solo se añade en `showJuego(...)` (no aparece en menús).
  * Botones **Mapa 1/2/3** con estilo redondeado.

* **ArbolPanel**

  * Dibuja fondo escalado, aristas y nodos.
  * `Activar_Nodo` / `Comprar_Generador` actualizan cantidades y recalculan generación.

---

## 🎨 Estilo (UI)

* Botones redondeados con **borde negro** y relleno **gris claro** en menús y diálogos.
* Tarjetas semitransparentes con **bordes redondeados** sobre `img/background.png`.
* Logo escalado en el menú (`img/logo.png`).

---

## 🔊 Sonido

* **Menú**: `/audio/background.wav` (loop).
* **Juego**: música por árbol (`/audio/background.wav`, `/audio/dino.wav`, `/audio/space.wav`, etc.).
* Formato recomendado: WAV PCM.

---

## ⌨️ Atajos

* **ESC** → abre **Pausa**.
* **Flechas** → mueve el árbol.
* **Click fondo** → genera token principal.
* **Click nodo** → activar/comprar.

---

## 🧪 Troubleshooting

* **No abre con Java 8** → revisa `java -version` (debe ser 21.x).
* **Recursos no cargan** → confirma rutas `getResource("/img/...")` y `getResource("/audio/...")` y que existan en `src`.
* **No guarda** → verifica que se cree la carpeta `./saves/` y permisos de escritura.

---

## 🗺️ Roadmap

* Guardado con **Gson/Jackson** (JSON robusto).
* Metadatos por save (fecha, tiempo jugado, árbol).
* Animaciones y partículas.
* Menú de opciones (volumen, calidad).

---

## 🧑‍💻 Licencia

Uso libre para fines **educativos/personales**.
Asegúrate de usar imágenes/sonidos propios o con licencia compatible.
