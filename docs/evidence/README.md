# Evidencias

Capturas que pide el enunciado. Los nombres coinciden con los comentarios
`<!-- CAPTURA: ... -->` del `README.md` principal.

| Archivo | Qué debe mostrar | Cómo obtenerlo |
|---|---|---|
| `a4-red.png` | Consola en rojo: `Tests run: 4, Failures: 1` + `BUILD FAILURE` | `git checkout c03f023 && mvn test` |
| `a4-green.png` | Consola en verde: `Tests run: 4, Failures: 0` + `BUILD SUCCESS` | `git checkout b4a6064 && mvn test` |
| `jacoco-antes.png` | Resumen de `target/site/jacoco/index.html` antes de las pruebas de borde | `git checkout bc78fc3 && mvn clean verify` |
| `jacoco-despues.png` | Resumen de JaCoCo con la cobertura final (≥ 85 %) | `mvn clean verify` en la punta de la rama |
| `sonarqube-dashboard.png` | Dashboard con Quality Gate, Coverage, Bugs, Code Smells | `mvn clean verify sonar:sonar` y abrir `localhost:9000` |

El paso a paso completo está en [`../GUIA_INTELLIJ.md`](../GUIA_INTELLIJ.md).
