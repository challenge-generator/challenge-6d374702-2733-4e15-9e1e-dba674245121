# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Implementación de Código Limpio en Arquitectura Backend**.

| | |
|---|---|
| Tema | aplicacion de codigo limpio y eficiente |
| Nivel | senior-l2 |
| Chapter | Backend |
| Especialidad | Java |
| Stack | Java / Spring Boot 3.5 |
| Patron arquitectonico | hexagonal/clean |
| Tiempo estimado | 4 semanas |

## Receta del stack

Esqueleto obligatorio:

- `pom.xml en la raiz`
- `clase con @SpringBootApplication`
- `application.yml en src/main/resources`
- `capa de dominio con entidades y puertos`
- `capa de aplicacion con casos de uso`
- `capa de infraestructura con adaptadores y @RestController`

Trampas conocidas:

- TODA `<version>` del pom va con tres segmentos: la del parent (ej. `3.5.6`) y la de cada dependencia que la lleve (ej. Resilience4j `2.2.0`). `3.4` y `2.0` no existen como artefacto y el build muere resolviendo dependencias.
- Las dependencias que el parent POM gestiona van SIN `<version>`: `spring-boot-starter-web`, `-data-jpa`, `-validation`, `-test`, etc.
- Resilience4j publica un artefacto por linea de Spring Boot. Con Spring Boot 3 va `resilience4j-spring-boot3` con version de tres segmentos (ej. `2.2.0`, no `2.0`). `resilience4j-spring-boot2` es de Spring Boot 2 y rompe el arranque.
- Si usas anotaciones de validacion (`@NotNull`, `@Size`, `@Positive`) declara `spring-boot-starter-validation`: el starter web no las trae.
- El `spring-boot-maven-plugin` tiene que estar en `<build><plugins>` o no se empaqueta ejecutable.
- Spring Boot 3 usa `jakarta.*`, nunca `javax.*`.
- Cada archivo empieza con su `package` y con un `import` por cada clase del proyecto que viva en otro paquete. Usar `PaymentService` desde `infrastructure` sin `import com.x.application.PaymentService` no compila.

Dependencias:

- org.springframework.boot:spring-boot-starter-web 3.5.6
- org.springframework.boot:spring-boot-starter-data-jpa n/a
- org.springframework.boot:spring-boot-starter-validation n/a
- org.springframework.boot:spring-boot-starter-actuator n/a
- org.springframework.boot:spring-boot-starter-test n/a
- org.projectlombok:lombok 1.18.30
- jakarta.persistence:jakarta.persistence-api 3.1.0
- org.postgresql:postgresql 42.7.3
- io.micrometer:micrometer-registry-prometheus n/a
- org.springdoc:springdoc-openapi-starter-webmvc-ui 2.5.0

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `mvn clean compile` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `mvn clean compile` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Análisis de Código Existente**: Documento de análisis con propuestas de mejora.
- **Fase 2 — Refactorización de Código**: Código refactorizado con pruebas unitarias.
- **Fase 3 — Verificación y Documentación**: Documento de verificación y documentación del proceso.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Superficie de practica (NO completes)

Estos archivos SON el ejercicio de la persona. No los implementes; deja stubs. No toques la logica que el reto pide completar.

- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — El topic pide TDD/pruebas: este archivo es el ejercicio.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — El topic pide TDD/pruebas: este archivo es el ejercicio.

## Lo que falta y tenes que completar

### 1. Referencias colgando (46)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/config/ExceptionHandlerConfig.java` — `org.slf4j`
      El import org.slf4j.Logger pertenece a org.slf4j, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionController.java` — `io.swagger.v3`
      El import io.swagger.v3.oas.annotations.Operation pertenece a io.swagger.v3, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `com.fasterxml.jackson`
      El import com.fasterxml.jackson.databind.ObjectMapper pertenece a com.fasterxml.jackson, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/main/java/com/fintech/cleanarchitecture/CleanArchitectureApplication.java` — `ApplicationProperties.getAsyncCorePoolSize`
      Se invoca `getAsyncCorePoolSize` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/CleanArchitectureApplication.java` — `ApplicationProperties.getAsyncMaxPoolSize`
      Se invoca `getAsyncMaxPoolSize` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/CleanArchitectureApplication.java` — `ApplicationProperties.getAsyncQueueCapacity`
      Se invoca `getAsyncQueueCapacity` sobre `ApplicationProperties`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `Transaction.description`
      Se invoca `description` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `TransactionSpringDataRepository.save`
      Se invoca `save` sobre `TransactionSpringDataRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `TransactionSpringDataRepository.findById`
      Se invoca `findById` sobre `TransactionSpringDataRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java` — `TransactionSpringDataRepository.deleteById`
      Se invoca `deleteById` sobre `TransactionSpringDataRepository`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `TransactionEntity.getTimestamp`
      Se invoca `getTimestamp` sobre `TransactionEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `TransactionEntity.setTimestamp`
      Se invoca `setTimestamp` sobre `TransactionEntity`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java` — `Transaction.description`
      Se invoca `description` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.timestamp`
      Se invoca `timestamp` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java` — `Transaction.description`
      Se invoca `description` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/config/ExceptionHandlerConfig.java` — `ErrorResponse.getField`
      Se invoca `getField` sobre `ErrorResponse`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/main/java/com/fintech/cleanarchitecture/infrastructure/config/ExceptionHandlerConfig.java` — `ErrorResponse.getDefaultMessage`
      Se invoca `getDefaultMessage` sobre `ErrorResponse`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `ProcessTransactionUseCase.execute`
      Se invoca `execute` sobre `ProcessTransactionUseCase`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.currency`
      Se invoca `currency` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java` — `Transaction.status`
      Se invoca `status` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `Transaction.id`
      Se invoca `id` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `Transaction.accountId`
      Se invoca `accountId` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `Transaction.type`
      Se invoca `type` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `Transaction.amount`
      Se invoca `amount` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.
- [ ] `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java` — `Transaction.currency`
      Se invoca `currency` sobre `Transaction`, pero esa clase no declara ese metodo. Agregalo con su implementacion real, o usa uno de los que si declara.

### Presentes (16)

- `pom.xml`
- `src/main/java/com/fintech/cleanarchitecture/CleanArchitectureApplication.java`
- `src/main/resources/application.yml`
- `src/main/java/com/fintech/cleanarchitecture/domain/model/Transaction.java`
- `src/main/java/com/fintech/cleanarchitecture/domain/port/TransactionRepository.java`
- `src/main/java/com/fintech/cleanarchitecture/domain/port/TransactionService.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionJpaRepository.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionSpringDataRepository.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter/TransactionMapper.java`
- `src/main/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCase.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/config/ExceptionHandlerConfig.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionController.java`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/repository/TransactionEntity.java`
- `src/test/java/com/fintech/cleanarchitecture/application/usecase/ProcessTransactionUseCaseTest.java`
- `src/test/java/com/fintech/cleanarchitecture/infrastructure/controller/TransactionControllerTest.java`
- `README.md`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/main/java/com/fintech/cleanarchitecture`
- `src/main/java/com/fintech/cleanarchitecture/domain`
- `src/main/java/com/fintech/cleanarchitecture/domain/model`
- `src/main/java/com/fintech/cleanarchitecture/domain/port`
- `src/main/java/com/fintech/cleanarchitecture/application`
- `src/main/java/com/fintech/cleanarchitecture/application/usecase`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/adapter`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/config`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/controller`
- `src/main/java/com/fintech/cleanarchitecture/infrastructure/repository`
- `src/test/java/com/fintech/cleanarchitecture`
- `src/test/java/com/fintech/cleanarchitecture/application`
- `src/test/java/com/fintech/cleanarchitecture/infrastructure`

## Verificacion

```bash
mvn clean compile
```

El comando tiene que pasar SIN implementar los archivos de la superficie de practica: solo andamiaje.

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **hexagonal/clean**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Backend, Especialidad Desarrollador, Tecnología Java, Senior
- Brecha que el reto ataca: Aplica principios y herramientas de verificación para escribir código limpio (KISS, SOLID, YAGNI y DRY).
- Mision: Candidato Senior con experiencia en arquitectura backend, trabajando en equipos distribuidos.

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
