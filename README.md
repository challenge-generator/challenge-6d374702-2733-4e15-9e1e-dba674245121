# Implementación de Código Limpio en Arquitectura Backend

El equipo de desarrollo de una empresa fintech necesita mejorar la calidad del código en su arquitectura backend. El objetivo es aplicar principios y herramientas de verificación para escribir código limpio y eficiente siguiendo los principios KISS, SOLID, YAGNI y DRY. Los actores involucrados son el equipo de desarrollo, los revisores de código y los usuarios finales. Las propiedades operativas incluyen consistencia en la implementación de servicios, latencia en la respuesta de los servicios y disponibilidad del sistema. El sistema debe manejar un volumen de 10 000 solicitudes por segundo con un tiempo de respuesta promedio de 200ms y una disponibilidad del 99.9%. Los modos de falla incluyen timeouts en servicios externos y errores de validación en los datos de entrada.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | aplicacion de codigo limpio y eficiente |
| **Nivel** | senior-l2 |
| **Tipo** | practical |
| **Tiempo estimado** | 4 semanas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Análisis de Código Existente

**Objetivo:** Identificar áreas de mejora en el código existente.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Revisar el código actual para identificar violaciones a los principios KISS, SOLID, YAGNI y DRY.
- Documentar las áreas problemáticas y proponer mejoras.

**Entregable:** Documento de análisis con propuestas de mejora.

<details>
<summary>Pistas de conocimiento</summary>

- Principios de diseño de software.
- Herramientas de revisión de código.

</details>

### Fase 2: Refactorización de Código

**Objetivo:** Aplicar refactorización siguiendo los principios de código limpio.

**Tiempo estimado:** 2 semanas

**Instrucciones:**

- Implementar las mejoras propuestas en el documento de análisis.
- Asegurar que el código refactorizado cumpla con los principios KISS, SOLID, YAGNI y DRY.

**Entregable:** Código refactorizado con pruebas unitarias.

<details>
<summary>Pistas de conocimiento</summary>

- Técnicas de refactorización.
- Pruebas unitarias.

</details>

### Fase 3: Verificación y Documentación

**Objetivo:** Verificar el código refactorizado y documentar el proceso.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Realizar una revisión de código para asegurar la calidad.
- Documentar el proceso de refactorización y las decisiones tomadas.

**Entregable:** Documento de verificación y documentación del proceso.

<details>
<summary>Pistas de conocimiento</summary>

- Procesos de revisión de código.
- Documentación técnica.

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Cuáles son los principios de código limpio que se aplican en este reto?
- **paraQueSirve**: ¿Por qué es importante aplicar estos principios en el código?
- **comoSeUsa**: ¿Cómo se aplican estos principios en la refactorización del código?
- **erroresComunes**: ¿Cuáles son los errores comunes al aplicar estos principios?
- **queDecisionesImplica**: ¿Qué decisiones se deben tomar al refactorizar el código siguiendo estos principios?

## Criterios de Evaluacion

- Identificación de áreas de mejora en el código existente.
- Aplicación de refactorización siguiendo los principios de código limpio.
- Verificación y documentación del proceso de refactorización.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
mvn clean compile
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
