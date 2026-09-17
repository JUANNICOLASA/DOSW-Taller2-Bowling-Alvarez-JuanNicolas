# Bowling TDD — DOSW Taller #1 (Corte 2)

Motor de puntuación de Bowling para BowlTech S.A.S., construido desde cero
aplicando **Test-Driven Development** con JUnit 5, JaCoCo y SonarQube.

| | |
|---|---|
| **Asignatura** | DOSW — Desarrollo y Operaciones de Software |
| **Periodo** | 2026-2 |
| **Profesor** | Andrés Martín Cantor Urrego |
| **Java** | 24 |
| **Build** | Maven (`edu.eci.dosw:bowling-tdd:1.0-SNAPSHOT`) |

---

## 1. Identificación

| Campo | Valor |
|---|---|
| Nombre completo | Juan Nicolás Álvarez Muñoz |
| Código estudiantil | `<!-- COMPLETAR: tu código -->` |
| Correo institucional | `<!-- COMPLETAR: juan.alvarez-m@mail.escuelaing.edu.co -->` |
| Repositorio | `<!-- COMPLETAR: https://github.com/<usuario>/DOSW-Taller2-Bowling-Alvarez-JuanNicolas -->` |

---

## 2. Descripción

**BowlTech S.A.S.** administra pistas de bolos y hasta ahora llevaba la
puntuación a mano, con los errores típicos: bonos de strike que se olvidan,
spares que se confunden y juegos perfectos mal sumados. Este proyecto
reemplaza ese proceso manual por un motor de puntuación probado.

### Reglas del dominio implementadas

| Situación | Condición | Puntuación |
|---|---|---|
| Tiro normal | Derriba algunos pinos sin completar 10 | Solo los pinos de ese tiro |
| Spare `/` | Derriba los 10 pinos en 2 intentos del mismo frame | 10 + primer tiro del frame siguiente |
| Strike `X` | Derriba los 10 pinos en el primer intento | 10 + los dos tiros siguientes |
| Frame 10 | Si cierra en strike o en spare | Hasta 3 tiros, con los pinos reiniciados |
| Juego perfecto | 12 strikes consecutivos | 300 puntos |

### Responsabilidades de cada clase

| Clase | Responsabilidad |
|---|---|
| `BowlingGame` | **Estado del juego.** Valida cada tiro (rango 0–10, capacidad del frame, juego terminado), decide cuándo avanza de frame y cuándo el juego está completo. Delega el cálculo del puntaje. |
| `Frame` | **Reglas de un frame.** Sabe cuántos pinos quedan en pie (`standingPins`), cuándo queda cerrado (`isComplete`) y si terminó en strike o spare. Encapsula las reglas propias del frame 10. |
| `BowlingScorer` | **Cálculo del puntaje.** Clase sin estado: recibe la lista de frames y devuelve un número, aplicando el bono de spare (1 tiro) o de strike (2 tiros). El frame 10 ya contiene sus propios tiros de bono, así que puntúa la suma directa. |
| `FrameStatus` | Enum del dominio: `OPEN`, `SPARE`, `STRIKE`. |

### Estructura

```
bowling-tdd/
├── pom.xml
├── README.md
├── docs/
│   ├── GUIA_INTELLIJ.md              guía paso a paso del taller
│   └── evidence/
│       ├── bitacora-ciclos-tdd.md    los 49 pasos del ciclo TDD
│       └── (capturas de consola, JaCoCo y SonarQube)
├── src/main/java/edu/eci/dosw/bowling/
│   ├── BowlingGame.java
│   ├── BowlingScorer.java
│   ├── Frame.java
│   └── FrameStatus.java
└── src/test/java/edu/eci/dosw/bowling/
    ├── BowlingGameTest.java          módulos A (roll) y C (isComplete)
    └── BowlingScorerTest.java        módulo B (calculate)
```

### Casos de prueba

**Módulo A — `BowlingGame.roll()`** (8 casos)

| # | Caso | Resultado |
|---|---|---|
| A1 | `roll(0)` | No lanza; el frame registra 0 pinos |
| A2 | `roll(-1)` | `IllegalArgumentException` |
| A3 | `roll(11)` | `IllegalArgumentException` |
| A4 | `roll(7)` + `roll(6)` | `IllegalArgumentException` en el segundo tiro |
| A5 | `roll()` con el juego terminado | `IllegalStateException` |
| A6 | `roll(10)` | Frame marcado `STRIKE`; avanza de frame |
| A7 | `roll(5)` + `roll(5)` | Frame marcado `SPARE` |
| A8 | Frame 10 con strike | Acepta 3 tiros sin excepción |

**Módulo B — `BowlingScorer.calculate()`** (8 casos)

| # | Caso | Resultado |
|---|---|---|
| B1 | Todos los tiros en 0 | `0` |
| B2 | Sin strikes ni spares (20 × 4) | `80` |
| B3 | Spare en frame 1, luego 3 | Frame 1 puntúa `13`; total `16` |
| B4 | Strike en frame 1, luego 4 y 3 | Frame 1 puntúa `17`; total `24` |
| B5 | Dos strikes seguidos, luego 5 | Total `45` |
| B6 | Todos spares + último tiro 5 | `150` |
| B7 | Juego perfecto (12 strikes) | `300` |
| B8 | `score()` con el juego incompleto | `IllegalStateException` |

**Módulo C — `BowlingGame.isComplete()`** (6 casos)

| # | Caso | Resultado |
|---|---|---|
| C1 | Al iniciar | `false` |
| C2 | Tras 9 frames completos | `false` |
| C3 | 10 frames normales | `true` |
| C4 | Spare en frame 10 + tiro bonus | `true` (y `false` antes del bonus) |
| C5 | Strike en frame 10 + 2 bonus | `true` |
| C6 | Juego perfecto | `true` |

Más 5 pruebas de borde añadidas al revisar el reporte de cobertura (ver §7, pregunta 03).
**Total: 29 pruebas.**

---

## 3. Evidencia TDD

El historial de commits es la evidencia principal: **49 commits** en la rama
`feature/AlvarezJuanNicolas_bowling`, uno por cada fase de cada caso
(17 fases RED, 32 fases GREEN/REFACTOR). La bitácora completa está en
[`docs/evidence/bitacora-ciclos-tdd.md`](docs/evidence/bitacora-ciclos-tdd.md).

```bash
git log --oneline --graph --all
```

### Ciclo documentado: caso A4 — dos tiros de un frame no pueden superar 10 pinos

#### 🔴 RED — `c03f023` · `test: RED - A4 dos tiros de un frame no pueden superar 10 pinos`

Se escribió primero la prueba:

```java
@Test
@DisplayName("A4 - dos tiros de un frame no pueden sumar mas de 10")
void twoRollsExceedingTenPins_throwsIllegalArgumentException() {
    BowlingGame game = new BowlingGame();
    game.roll(7);

    assertThrows(IllegalArgumentException.class, () -> game.roll(6));
}
```

En ese commit `roll()` solo validaba el rango 0–10, así que `roll(6)` se
aceptaba y la prueba falló:

```
[ERROR] BowlingGameTest.twoRollsExceedingTenPins_throwsIllegalArgumentException
  Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.
[ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
[INFO] BUILD FAILURE
```

`<!-- CAPTURA: docs/evidence/a4-red.png -->`

#### 🟢 GREEN — `b4a6064` · `feat: GREEN - A4 valida la capacidad de pinos del frame`

Código mínimo para pasar, sin anticipar nada:

```java
Frame frame = currentFrameOrCreate();
int alreadyDown = 0;
for (int rolled : frame.getRolls()) {
    alreadyDown += rolled;
}
if (alreadyDown + pins > MAX_PINS) {
    throw new IllegalArgumentException(
            "Un frame no puede derribar mas de " + MAX_PINS + " pinos");
}
frame.addRoll(pins);
```

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

`<!-- CAPTURA: docs/evidence/a4-green.png -->`

#### 🔵 REFACTOR — `46dfb45` · `refactor: mueve la regla de capacidad de pinos a Frame`

Contar pinos no es responsabilidad de `BowlingGame`: el frame sabe cuántos
pinos tiene en pie. La lógica se movió a `Frame` sin cambiar el
comportamiento observable, y las 4 pruebas siguieron pasando.

```java
// BowlingGame
if (frame.wouldExceedPins(pins)) {
    throw new IllegalArgumentException(
            "Un frame no puede derribar mas de " + Frame.MAX_PINS + " pinos");
}

// Frame
public int pinsKnockedDown() { ... }
public boolean wouldExceedPins(int pins) {
    return pinsKnockedDown() + pins > MAX_PINS;
}
```

Esta extracción fue la que después hizo posible encapsular la regla del
frame 10 (`standingPins()`, commit `8668fc1`) sin tocar `BowlingGame`.

### Cómo reproducir cualquier fase RED

```bash
git checkout c03f023      # commit RED del caso A4
mvn test                  # BUILD FAILURE  <- la captura roja
git checkout b4a6064      # commit GREEN
mvn test                  # BUILD SUCCESS  <- la captura verde
git checkout feature/AlvarezJuanNicolas_bowling
```

---

## 4. JaCoCo — cobertura de código

Umbrales configurados en el `pom.xml` (no se bajaron):

| Contador | Mínimo |
|---|---|
| `LINE` | 85 % |
| `BRANCH` | 70 % |

```bash
mvn clean verify
# reporte HTML: target/site/jacoco/index.html
```

| Momento | Cobertura de líneas | Cobertura de ramas | Captura |
|---|---|---|---|
| Antes de las pruebas de borde (`bc78fc3`) | `<!-- COMPLETAR -->` | `<!-- COMPLETAR -->` | `<!-- docs/evidence/jacoco-antes.png -->` |
| Después de las pruebas de borde (`3cae355`) | `<!-- COMPLETAR -->` | `<!-- COMPLETAR -->` | `<!-- docs/evidence/jacoco-despues.png -->` |

### Qué pruebas subieron la cobertura

Las 22 pruebas de los módulos A, B y C cubren el flujo principal. El reporte
de JaCoCo mostró cuatro huecos que ninguna de ellas tocaba, y para cada uno se
añadió una prueba en el commit `3cae355`:

| Hueco detectado en el reporte | Prueba añadida |
|---|---|
| `Frame.standingPins()` — rama del frame 10 con strike seguido de un tiro parcial (los pinos **no** se reinician una segunda vez) | `tenthFrameAfterStrike_validatesRemainingPins` |
| `Frame.getNumber()` / `isLastFrame()` — nunca se consultaban directamente | `frames_areNumberedFromOneToTen` |
| `BowlingGame.getFrames()` — no se verificaba que la copia fuera inmutable | `getFrames_returnsImmutableCopy` |
| `Frame.getStatus()` — la rama `return FrameStatus.OPEN` no se ejecutaba nunca | `frameWithStandingPins_isOpen` |
| `BowlingScorer.calculate(null)` — guarda del contrato sin ejercitar | `calculateWithNullFrames_throwsIllegalArgumentException` |

---

## 5. SonarQube — análisis estático

```bash
docker pull sonarqube:26.9.0.129388-community
docker run -d --name sonarqube -p 9000:9000 sonarqube:26.9.0.129388-community
# http://localhost:9000  (admin / admin)
```

```bash
# Linux / macOS
export SONAR_TOKEN="TU_TOKEN_AQUI"
mvn clean verify sonar:sonar -Dsonar.token=$SONAR_TOKEN

# Windows PowerShell
$env:SONAR_TOKEN="TU_TOKEN_AQUI"
mvn clean verify sonar:sonar "-Dsonar.token=$env:SONAR_TOKEN"
```

> El token **no** está en el repositorio: se pasa por variable de entorno.
> `.sonarqube/`, `.scannerwork/` y `sonar-project.properties` están en `.gitignore`.

| Métrica | Valor |
|---|---|
| Quality Gate | `<!-- COMPLETAR: Passed / Failed -->` |
| Coverage | `<!-- COMPLETAR -->` |
| Bugs | `<!-- COMPLETAR -->` |
| Code Smells | `<!-- COMPLETAR -->` |
| Security Hotspots | `<!-- COMPLETAR -->` |
| Duplications | `<!-- COMPLETAR -->` |

`<!-- CAPTURA: docs/evidence/sonarqube-dashboard.png -->`

### Issues corregidos

| Issue | Regla | Corrección |
|---|---|---|
| `<!-- COMPLETAR tras ejecutar el análisis -->` | | |

---

## 6. Pull Requests

Los cambios llegaron a `develop` únicamente por Pull Request: no hay commits
directos sobre `develop` ni sobre `main`.

| PR | Módulo que cubre | Commits | Merge | Enlace |
|---|---|---|---|---|
| #1 | Módulo A — validaciones y estado de `roll()` (A1–A8) | 21 | `<!-- fecha -->` | `<!-- COMPLETAR -->` |
| #2 | Módulo C — reglas de cierre del juego (C1–C6) | 8 | `<!-- fecha -->` | `<!-- COMPLETAR -->` |
| #3 | Módulo B — cálculo de puntaje con bonos (B1–B8) | 20 | `<!-- fecha -->` | `<!-- COMPLETAR -->` |
| #4 | Documentación y evidencias | 1 | `<!-- fecha -->` | `<!-- COMPLETAR -->` |

```bash
git log --oneline --graph develop    # los merge commits --no-ff de cada PR
```

---

## 7. Reflexión técnica

### 01 · ¿Qué caso edge del Bowling fue el más difícil de implementar con TDD y por qué?

El frame 10. Es el único frame donde las dos reglas que el resto del juego da
por sentadas dejan de valerse: *un frame tiene dos tiros* y *entre los dos
tiros no pueden caer más de 10 pinos*. En el frame 10, un strike o un spare
otorgan un tercer tiro **y** levantan los pinos de nuevo, así que la
validación de capacidad tiene que preguntarse cuántos pinos hay realmente en
pie en ese momento — que no es lo mismo que `10 − pinos derribados en el frame`.

Lo difícil con TDD fue que el caso A8 solo pedía el frame 10 **con strike**, y
el código mínimo para pasarlo (`isStrike() ? 3 tiros : 2`) dejó una regla a
medias. El caso C4 (spare en el frame 10) fue el que la destapó: `isComplete()`
daba `true` con dos tiros y el tiro de bono terminaba lanzando
`IllegalStateException`. Ese RED fue el más valioso del taller, porque forzó a
darle un nombre propio a la regla en vez de ir parchando condicionales:
`expectedRolls()` y `pinsWereReset()` en `Frame`.

### 02 · ¿Qué parte del código cambió durante REFACTOR sin modificar el comportamiento observable?

Tres refactors con impacto real, todos con las pruebas en verde antes y después:

1. **`46dfb45` — la capacidad de pinos se movió de `BowlingGame` a `Frame`.**
   `BowlingGame` sumaba a mano los pinos del frame en curso. Pasó a
   `frame.wouldExceedPins(pins)`. Sin ese movimiento, la regla del frame 10
   habría quedado como una cadena de `if` dentro de `roll()`.
2. **`dd75bd6` — `expectedRolls()` y `pinsWereReset()` en `Frame`.**
   `isComplete()` y `standingPins()` tenían condicionales anidados que mezclaban
   "cuántos tiros faltan" con "cuántos pinos hay en pie". Separarlos en dos
   métodos privados con nombre dejó el frame 10 legible.
3. **`bc78fc3` — `roll()` quedó como una lista de reglas.**
   El cuerpo pasó a ser `validatePinRange` → `validateGameIsOpen` →
   `currentFrameOrCreate` → `validateFrameCapacity` → `addRoll` →
   `advanceIfClosed`. El método se lee como el enunciado del taller.

Ninguno cambió una firma pública ni un mensaje de excepción: el comportamiento
observable por las pruebas quedó idéntico.

### 03 · ¿Qué casos de prueba descubriste al revisar el reporte de cobertura de JaCoCo que no habías considerado antes?

Cinco, todos en el commit `3cae355`:

- **Frame 10 con strike y luego un tiro parcial.** Las pruebas cubrían
  `10,10,10` (juego perfecto) y `10,10,x`, pero nunca `10,7,…`. Esa es
  justamente la rama donde los pinos **no** se reinician, y estaba sin
  ejecutar: con `10,7` solo quedan 3 pinos en pie y un `roll(5)` debe fallar.
- **`Frame.getStatus()` devolviendo `OPEN`.** Las pruebas A6 y A7 consultaban
  el estado de frames en strike y en spare; ningún test preguntaba el estado de
  un frame normal, así que esa línea nunca se ejecutaba.
- **La numeración de los frames.** `getNumber()` e `isLastFrame()` se usaban
  internamente pero ninguna prueba verificaba que los frames fueran 1…10.
- **La inmutabilidad de `getFrames()`.** El método devuelve `List.copyOf`, pero
  nada comprobaba que un cliente no pudiera modificar el juego por fuera.
- **`BowlingScorer.calculate(null)`.** La guarda del contrato estaba escrita y
  nunca se ejercitaba.

La lección: el reporte de cobertura no señala pruebas que falten "en general",
señala **decisiones del código que nadie tomó nunca** durante las pruebas. Las
cuatro primeras son ramas de reglas del dominio, no adornos.

### 04 · ¿Qué hallazgo de SonarQube produjo un cambio real en el código?

`<!-- COMPLETAR tras ejecutar el análisis. Candidatos esperados en este código: -->`
`<!-- - java:S109 "Magic Number" en los tests (18, 20, 300…) -->`
`<!-- - java:S5960 aserciones sin mensaje -->`
`<!-- - java:S3776 complejidad cognitiva si un condicional del frame 10 quedó anidado -->`
`<!-- - java:S1192 cadenas de texto duplicadas en los mensajes de excepción -->`
`<!-- Describe el hallazgo, la regla, y el diff que hiciste para resolverlo. -->`

---

## 8. Cómo ejecutar

```bash
mvn test                                        # 29 pruebas
mvn clean verify                                # pruebas + JaCoCo (falla si line < 85%)
mvn clean verify sonar:sonar -Dsonar.token=$SONAR_TOKEN
```

Guía detallada paso a paso en IntelliJ IDEA: [`docs/GUIA_INTELLIJ.md`](docs/GUIA_INTELLIJ.md).
