# Bitácora de los ciclos TDD

Los 49 commits de la rama `feature/AlvarezJuanNicolas_bowling`, en orden.
Cada fase RED se verificó corriendo las pruebas antes de escribir el código:
la prueba falla en ese commit y pasa en el commit GREEN siguiente.

- 17 fases RED
- 20 fases GREEN
- 6 refactors
- 6 pruebas agregadas sobre código que ya estaba en verde

Para reproducir cualquier fase:

```bash
git checkout <sha>
mvn test
```

| Caso | Fase | Commit | Fecha | Mensaje |
|---|---|---|---|---|
| A1 RED | RED | `91eb170` | 2026-09-08 | test: RED - A1 roll(0) registra el tiro en el frame |
| A1 GREEN | GREEN | `33b17e2` | 2026-09-08 | feat: GREEN - A1 roll() registra los pinos derribados |
| A1 REFACTOR | REFACTOR | `ae3bd1e` | 2026-09-08 | refactor: extrae currentFrameOrCreate() en BowlingGame |
| A2 RED | RED | `304b229` | 2026-09-08 | test: RED - A2 roll(-1) lanza IllegalArgumentException |
| A2 GREEN | GREEN | `9e4096d` | 2026-09-08 | feat: GREEN - A2 valida pinos negativos en roll() |
| A3 RED | RED | `25d1ec7` | 2026-09-08 | test: RED - A3 roll(11) lanza IllegalArgumentException |
| A3 GREEN | GREEN | `ecd3381` | 2026-09-08 | feat: GREEN - A3 valida pinos mayores a 10 en roll() |
| A3 REFACTOR | REFACTOR | `4755305` | 2026-09-08 | refactor: extrae validatePinRange() y la constante MAX_PINS |
| A4 RED | RED | `c03f023` | 2026-09-08 | test: RED - A4 dos tiros de un frame no pueden superar 10 pinos |
| A4 GREEN | GREEN | `b4a6064` | 2026-09-08 | feat: GREEN - A4 valida la capacidad de pinos del frame |
| A4 REFACTOR | REFACTOR | `46dfb45` | 2026-09-08 | refactor: mueve la regla de capacidad de pinos a Frame |
| A5 RED | RED | `64e0f23` | 2026-09-08 | test: RED - A5 roll() con el juego terminado lanza IllegalStateException |
| A5 GREEN | GREEN | `2ca61d2` | 2026-09-08 | feat: GREEN - A5 el juego avanza de frame y rechaza tiros al terminar |
| A6 RED | RED | `b660f4e` | 2026-09-08 | test: RED - A6 roll(10) marca STRIKE y avanza de frame |
| A6 GREEN | GREEN | `9872d85` | 2026-09-08 | feat: GREEN - A6 detecta el strike y expone FrameStatus |
| A6 REFACTOR | REFACTOR | `427bbfb` | 2026-09-08 | refactor: Frame decide cuando esta cerrado con isComplete() |
| A7 RED | RED | `a6173ef` | 2026-09-08 | test: RED - A7 roll(5)+roll(5) marca SPARE |
| A7 GREEN | GREEN | `c35943b` | 2026-09-08 | feat: GREEN - A7 detecta el spare en el frame |
| A8 RED | RED | `10cf7da` | 2026-09-08 | test: RED - A8 el frame 10 con strike acepta tres tiros |
| A8 GREEN | GREEN | `d3aa719` | 2026-09-08 | feat: GREEN - A8 el frame 10 con strike admite un tercer tiro |
| A8 REFACTOR | REFACTOR | `8668fc1` | 2026-09-08 | refactor: Frame calcula los pinos en pie con standingPins() |
| C1 | TEST | `1e971e8` | 2026-09-09 | test: C1 - isComplete() es false al iniciar el juego |
| C2 | TEST | `3228293` | 2026-09-09 | test: C2 - isComplete() es false con nueve frames completos |
| C3 | TEST | `7c81d1f` | 2026-09-09 | test: C3 - isComplete() es true con diez frames normales |
| C4 RED | RED | `b942714` | 2026-09-09 | test: RED - C4 el spare del frame 10 exige el tiro de bono |
| C4 GREEN | GREEN | `edb863b` | 2026-09-09 | feat: GREEN - C4 el spare del frame 10 otorga un tercer tiro |
| C5 | TEST | `72c2444` | 2026-09-09 | test: C5 - el strike del frame 10 exige los dos tiros de bono |
| C6 | TEST | `a1f77f3` | 2026-09-09 | test: C6 - el juego perfecto queda completo tras el doceavo strike |
| C REFACTOR | REFACTOR | `dd75bd6` | 2026-09-09 | refactor: expectedRolls() y pinsWereReset() explican la regla del frame 10 |
| B1 RED | RED | `1faadec` | 2026-09-09 | test: RED - B1 BowlingScorer puntua en 0 un juego de puros ceros |
| B1 GREEN | GREEN | `7f7a9b8` | 2026-09-09 | feat: GREEN - B1 crea BowlingScorer y score() delega en el |
| B2 RED | RED | `01c8529` | 2026-09-09 | test: RED - B2 un juego sin bonos suma los pinos derribados |
| B2 GREEN | GREEN | `614ea56` | 2026-09-09 | feat: GREEN - B2 suma los pinos de todos los frames |
| B2 REFACTOR | REFACTOR | `396272e` | 2026-09-09 | refactor: extrae frameScore() en BowlingScorer |
| B3 RED | RED | `600e99a` | 2026-09-09 | test: RED - B3 el spare suma el primer tiro del frame siguiente |
| B3 GREEN | GREEN | `934b23a` | 2026-09-09 | feat: GREEN - B3 aplica el bono de spare |
| B4 RED | RED | `145c4c7` | 2026-09-09 | test: RED - B4 el strike suma los dos tiros siguientes |
| B4 GREEN | GREEN | `a1684fd` | 2026-09-09 | feat: GREEN - B4 aplica el bono de strike |
| B5 RED | RED | `e728952` | 2026-09-09 | test: RED - B5 dos strikes seguidos acumulan el bono del primero |
| B5 GREEN | GREEN | `74c0c52` | 2026-09-09 | feat: GREEN - B5 el bono de strike usa los dos tiros siguientes |
| B5 REFACTOR | REFACTOR | `ff4fc09` | 2026-09-09 | refactor: extrae rollsAfter() y las constantes de bono |
| B6 RED | RED | `af81c07` | 2026-09-09 | test: RED - B6 todos spares con tiro final de 5 puntua 150 |
| B6 GREEN | GREEN | `8cb44be` | 2026-09-09 | feat: GREEN - B6 el spare del frame 10 no recibe bono adicional |
| B7 RED | RED | `de7bf14` | 2026-09-10 | test: RED - B7 el juego perfecto puntua 300 |
| B7 GREEN | GREEN | `c08de2c` | 2026-09-10 | feat: GREEN - B7 el frame 10 puntua la suma directa de sus tiros |
| B8 RED | RED | `cbafae0` | 2026-09-10 | test: RED - B8 score() sin juego completo lanza IllegalStateException |
| B8 GREEN | GREEN | `b7413c6` | 2026-09-10 | feat: GREEN - B8 score() exige que el juego este completo |
| B REFACTOR | REFACTOR | `bc78fc3` | 2026-09-10 | refactor: contrato explicito de BowlingScorer y roll() legible |
| COBERTURA | TEST | `3cae355` | 2026-09-10 | test: pruebas de borde detectadas en el reporte de JaCoCo |

## Merges a develop

| Commit | Fecha | Merge |
|---|---|---|
| `427049f` | 2026-09-17 | Merge PR #4 - Documentacion, guia de IntelliJ y bitacora de evidencias |
| `b62f439` | 2026-09-10 | Merge PR #3 - Modulo B: calculo de puntaje con bonos (BowlingScorer) |
| `a5575b5` | 2026-09-09 | Merge PR #2 - Modulo C: reglas de cierre del juego (isComplete) |
| `f0f0a19` | 2026-09-08 | Merge PR #1 - Modulo A: validaciones y estado de roll() |
