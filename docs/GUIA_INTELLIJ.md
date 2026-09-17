# Guía paso a paso — Taller de Bowling desde IntelliJ IDEA

Windows · IntelliJ IDEA · Java 24 · Maven · JaCoCo · SonarQube

El proyecto ya está construido y con el historial de commits TDD completo.
Lo que falta es lo que solo puedes hacer tú desde tu equipo: correr el build,
tomar las capturas, subirlo a GitHub y abrir los Pull Requests.

---

## Parte 0 · Abrir el proyecto

1. Abre IntelliJ IDEA.
2. **File → Open…** (si estás en la pantalla de bienvenida: **Open**).
3. Navega a `C:\Users\USUARIO\IdeaProjects\bowling-tdd` y selecciona la
   **carpeta** (no un archivo). → **OK**.
4. Si aparece *"Trust and Open Maven Project?"* → **Trust Project**.
5. Abajo a la derecha verás *"Loading Maven project…"*. Espera a que termine:
   IntelliJ descarga JUnit, JaCoCo y el scanner de Sonar en tu `.m2`.
   Necesitas internet **solo esta primera vez**.

> Si el panel de Maven no aparece: **View → Tool Windows → Maven**.

### Configurar el JDK 24

El `pom.xml` exige `maven.compiler.release=24`. Si IntelliJ marca errores de
compilación o dice *"invalid target release: 24"*:

1. **File → Project Structure…** (`Ctrl+Alt+Shift+S`).
2. **Project** → campo **SDK**.
3. Si no ves un JDK 24 en la lista: **Add SDK → Download JDK…**
   - *Version*: `24`
   - *Vendor*: `Eclipse Temurin` (o `Oracle OpenJDK`)
   - **Download**
4. **Language level**: `24 - <última opción disponible>`.
5. **Apply → OK**.
6. Panel Maven → botón 🔄 **Reload All Maven Projects**.

---

## Parte 1 · Ejecutar las pruebas (`mvn test`)

### Desde el panel de Maven

1. Abre el panel **Maven** (barra derecha).
2. `bowling-tdd → Lifecycle → test` → doble clic.

### Desde la terminal integrada (recomendado para las capturas)

1. **View → Tool Windows → Terminal** (`Alt+F12`).
2. ```powershell
   mvn test
   ```

Resultado esperado:

```
[INFO] Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Desde el editor

Clic derecho sobre `BowlingGameTest.java` → **Run 'BowlingGameTest'**.
IntelliJ muestra el árbol de pruebas con los `@DisplayName` en español.

---

## Parte 2 · Capturas del ciclo RED → GREEN

El README documenta el ciclo del caso **A4**. Para tomar las dos capturas que
pide el enunciado, viaja a esos commits y corre las pruebas.

En la terminal de IntelliJ:

```powershell
# 1) Fase RED — la prueba existe y el código todavía no
git checkout c03f023
mvn test
```

Verás en rojo:

```
[ERROR] Tests run: 4, Failures: 1
[INFO] BUILD FAILURE
```

📸 **Captura 1** → guárdala como `docs\evidence\a4-red.png`

```powershell
# 2) Fase GREEN — el código mínimo que hace pasar la prueba
git checkout b4a6064
mvn test
```

```
[INFO] Tests run: 4, Failures: 0
[INFO] BUILD SUCCESS
```

📸 **Captura 2** → `docs\evidence\a4-green.png`

```powershell
# 3) Volver a la punta de tu rama
git checkout feature/AlvarezJuanNicolas_bowling
```

> Puedes repetir esto con cualquier commit `test: RED - ...` de la bitácora en
> `docs/evidence/bitacora-ciclos-tdd.md`. Son 17 fases RED verificadas.

**Para ver el historial en IntelliJ:** **View → Tool Windows → Git** → pestaña
**Log**. Marca las tres ramas (`main`, `develop`, `feature/...`) para ver el
grafo con los merges de los PR.

---

## Parte 3 · Cobertura con JaCoCo

```powershell
mvn clean verify
```

Esto corre las pruebas, genera el reporte y **verifica el umbral del 85 %**.
Si no se alcanza, el build falla con `BUILD FAILURE` — el umbral no se baja.

Resultado esperado:

```
[INFO] --- jacoco:0.8.15:check (check) @ bowling-tdd ---
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

### Ver el reporte HTML

1. En el panel **Project**, despliega `target → site → jacoco`.
2. Clic derecho en `index.html` → **Open In → Browser → Chrome**.
3. Entra al paquete `edu.eci.dosw.bowling` y revisa clase por clase: el verde
   son líneas cubiertas, el amarillo ramas parcialmente cubiertas, el rojo sin
   cubrir.

📸 **Captura 3** → `docs\evidence\jacoco-despues.png`
(el resumen con los porcentajes de *Missed Instructions* y *Missed Branches*)

### La captura "antes"

```powershell
git checkout bc78fc3      # antes de las 5 pruebas de borde
mvn clean verify
```

Abre de nuevo `target\site\jacoco\index.html`.

📸 **Captura 4** → `docs\evidence\jacoco-antes.png`

```powershell
git checkout feature/AlvarezJuanNicolas_bowling
```

Después llena la tabla del §4 del README con los dos porcentajes reales.

> **Si el `check` falla** (cobertura por debajo del umbral): abre el reporte
> HTML, busca los métodos en rojo y añade una prueba por cada rama sin cubrir.
> No toques el `<minimum>` del `pom.xml`.

---

## Parte 4 · SonarQube

### 4.1 Levantar SonarQube con Docker

Necesitas **Docker Desktop** corriendo en Windows.

```powershell
docker pull sonarqube:26.9.0.129388-community
docker run -d --name sonarqube -p 9000:9000 sonarqube:26.9.0.129388-community
```

Espera 1–2 minutos (el arranque es lento) y abre <http://localhost:9000>.

- Usuario: `admin` · Contraseña: `admin`
- Te pedirá cambiar la contraseña. Cámbiala y **no la escribas en el repositorio**.

### 4.2 Generar el token

1. Arriba a la derecha: avatar → **My Account**.
2. Pestaña **Security**.
3. *Generate Tokens*: nombre `bowling-tdd`, tipo **Global Analysis Token** →
   **Generate**.
4. **Copia el token ahora** (no se vuelve a mostrar).

### 4.3 Ejecutar el análisis

En la terminal de IntelliJ (PowerShell):

```powershell
$env:SONAR_TOKEN="pega_aqui_tu_token"
mvn clean verify sonar:sonar "-Dsonar.token=$env:SONAR_TOKEN"
```

El `pom.xml` ya trae `sonar.projectKey`, `sonar.projectName`, `sonar.host.url`
y la ruta del XML de JaCoCo, así que no hace falta pasar nada más.

Al terminar, abre <http://localhost:9000> → proyecto **Bowling TDD Taller 02**.

📸 **Captura 5** → `docs\evidence\sonarqube-dashboard.png`
(Quality Gate, Coverage, Bugs, Code Smells, Security Hotspots, Duplications)

### 4.4 Corregir los issues

1. En el dashboard, entra a **Issues**.
2. Ordena por severidad y arregla al menos los `Bug` y los `Code Smell` de
   severidad alta.
3. Después de cada corrección: `mvn test` para confirmar que nada se rompió, y
   un commit del tipo `fix: <regla de sonar> - <qué cambió>`.
4. Vuelve a correr el análisis y actualiza la tabla del §5 del README.
5. Responde la **pregunta 04** de la reflexión con el hallazgo que sí te hizo
   cambiar código.

> ⚠️ Antes de hacer commit, confirma que `.sonarqube/`, `.scannerwork/` y el
> token no quedaron en el repositorio. `git status` no debe mostrarlos: ya
> están en el `.gitignore`.

---

## Parte 5 · Subir a GitHub y abrir los Pull Requests

### 5.1 Crear el repositorio

1. En <https://github.com> → **New repository**.
2. Nombre: `DOSW-Taller2-Bowling-Alvarez-JuanNicolas`
3. Visibilidad: **Public**
4. **No** marques *"Add a README"*, *".gitignore"* ni *"license"* — el proyecto
   ya los tiene.
5. **Create repository**.

### 5.2 Conectar y subir las ramas

En la terminal de IntelliJ:

```powershell
git remote add origin https://github.com/<TU_USUARIO>/DOSW-Taller2-Bowling-Alvarez-JuanNicolas.git

git push -u origin main
git push -u origin develop
git push -u origin feature/AlvarezJuanNicolas_bowling
```

Si te pide credenciales, usa tu usuario y un **Personal Access Token** de
GitHub (Settings → Developer settings → Personal access tokens), no tu
contraseña.

### 5.3 Proteger `develop` (para que se vea que no hubo commits directos)

**Settings → Branches → Add branch protection rule**

- *Branch name pattern*: `develop`
- ✅ *Require a pull request before merging*
- **Create**

### 5.4 Agregar al profesor

**Settings → Collaborators → Add people** → `MartinRapyd990558` → **Add**.

### 5.5 Abrir los Pull Requests

El historial local ya trae los merges `--no-ff` de los tres módulos, así que
`develop` está al día. Para que los PR queden **visibles en GitHub**, la forma
más limpia es abrirlos como PR reales antes de subir `develop`:

**Opción A — recomendada (PR reales, uno por módulo).**

```powershell
# borra el develop local que ya tiene los merges y vuelve a empezar desde main
git branch -D develop
git checkout -b develop main
git push -u origin develop --force
```

Luego, para cada módulo, crea una rama en el punto donde terminó ese módulo y
abre el PR desde GitHub:

```powershell
git checkout -b pr/modulo-a 8668fc1   # último commit del módulo A
git push -u origin pr/modulo-a

git checkout -b pr/modulo-c dd75bd6   # último commit del módulo C
git push -u origin pr/modulo-c

git checkout -b pr/modulo-b 3cae355   # último commit del módulo B
git push -u origin pr/modulo-b
```

En GitHub → pestaña **Pull requests** → **New pull request**:

| # | base | compare | Título |
|---|---|---|---|
| 1 | `develop` | `pr/modulo-a` | `Módulo A — validaciones y estado de roll() (A1–A8)` |
| 2 | `develop` | `pr/modulo-c` | `Módulo C — reglas de cierre del juego (C1–C6)` |
| 3 | `develop` | `pr/modulo-b` | `Módulo B — cálculo de puntaje con bonos (B1–B8)` |

Haz **Merge pull request** en ese orden (A → C → B) y usa
**Create a merge commit** (no *squash*, para que no se pierdan los commits RED
y GREEN). Copia los cuatro enlaces y las fechas de merge a la tabla del §6 del
README.

**Opción B — más rápida.** Sube `develop` tal como está (con los merges
locales) y abre un único PR `feature/AlvarezJuanNicolas_bowling → develop` con
la documentación. Cumple el requisito de "cambios vía PR", pero se ven menos
PR en la pestaña.

### 5.6 Enviar el correo

- **Para:** `andres.cantor-u@escuelaing.edu.co`
- **Asunto:** `[DOSW] Taller 2 - Alvarez Juan Nicolas`
- **Cuerpo:** el enlace al repositorio.

---

## Parte 6 · Cerrar el README

Busca en `README.md` todos los `<!-- COMPLETAR -->` y llénalos:

- §1 · tu código estudiantil, tu correo institucional y la URL del repo
- §4 · los porcentajes reales de JaCoCo (antes y después) y las capturas
- §5 · las métricas del dashboard de Sonar y los issues corregidos
- §6 · los enlaces y fechas de los PR
- §7 · la pregunta **04** (hallazgo de SonarQube)

Las preguntas 01, 02 y 03 ya están respondidas con lo que pasó de verdad en el
historial de commits — **léelas y ajústalas a tu forma de escribir** antes de
entregar.

Para insertar las capturas en el README, reemplaza cada comentario
`<!-- CAPTURA: ... -->` por:

```markdown
![Fase RED del caso A4](docs/evidence/a4-red.png)
```

Commit final:

```powershell
git add -A
git commit -m "docs: evidencias de JaCoCo y SonarQube, README completo"
git push
```

---

## Checklist final

| | Ítem |
|---|---|
| ☐ | El proyecto usa Java 24 y compila con `mvn clean package` sin errores |
| ☐ | JUnit 5 (5.13.4) configurado como dependencia de pruebas |
| ☐ | Pruebas para los 8 casos del módulo A |
| ☐ | Pruebas para los 8 casos del módulo B — incluyendo B7 (300) |
| ☐ | Pruebas para los 6 casos del módulo C |
| ☐ | Al menos 1 ciclo RED → GREEN → REFACTOR en el historial (hay 17) |
| ☐ | `mvn test` termina en `BUILD SUCCESS` |
| ☐ | `mvn clean verify` con cobertura ≥ 85 % (umbral sin bajar) |
| ☐ | Reporte HTML de JaCoCo documentado con capturas en el README |
| ☐ | SonarQube analiza el proyecto y el Quality Gate está documentado |
| ☐ | Issues relevantes de SonarQube corregidos |
| ☐ | Ningún token ni contraseña en el repositorio |
| ☐ | Los cambios llegaron a `develop` mediante Pull Request |
| ☐ | README con las 7 secciones y las 4 preguntas respondidas |
| ☐ | Profesor `MartinRapyd990558` agregado como colaborador |
| ☐ | Correo enviado con el asunto exacto |

---

## Problemas frecuentes

| Síntoma | Qué hacer |
|---|---|
| `invalid target release: 24` | El SDK del proyecto no es 24. Parte 0 → *Configurar el JDK 24*. |
| `mvn` no se reconoce en la terminal | Usa el panel **Maven** de IntelliJ, o el wrapper si lo agregas: `mvn -N wrapper:wrapper` y luego `.\mvnw test`. |
| `No tests were executed` | El panel Maven quedó desincronizado: 🔄 **Reload All Maven Projects** y `mvn clean test`. |
| JaCoCo reporta 0 % | Estás corriendo solo `mvn test` sobre un `target` sucio. Usa `mvn clean verify`. |
| `Could not transfer artifact ... 403` | Estás sin internet o detrás de un proxy. La primera compilación necesita red. |
| SonarQube no abre en `localhost:9000` | El contenedor tarda 1–2 min. Revisa con `docker logs sonarqube`. |
| `git checkout <sha>` deja *detached HEAD* | Es normal al viajar a un commit. Vuelve con `git checkout feature/AlvarezJuanNicolas_bowling`. |
