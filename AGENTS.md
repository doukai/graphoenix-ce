# Repository Guidelines

## Project Structure & Module Organization
This repository is a multi-module Gradle build. Top-level modules are declared in `settings.gradle` (e.g., `graphoenix-core`, `graphoenix-http-server`, `graphoenix-grpc-server`, `graphoenix-sql`, `graphoenix-admin`). Each module follows standard Gradle layout:
- Production code: `*/src/main/java`
- Tests: `*/src/test/java`
- Resources: `*/src/main/resources`

The admin UI lives under `graphoenix-admin/webapp` (React build via Gradle).

## Build, Test, and Development Commands
Use the Gradle wrapper from the repo root:
- `./gradlew build` — build all modules.
- `./gradlew test` — run the full test suite.
- `./gradlew :graphoenix-core:test` — run tests for a single module.
- `./gradlew :graphoenix-admin:buildWebapp` — build the admin webapp bundle into the module resources.

## Coding Style & Naming Conventions
No explicit formatter config is present. Follow the existing conventions in each module and keep changes consistent with surrounding files.
- Java package names are under `org.graphoenix`.
- Module names use the `graphoenix-*` prefix.

## Testing Guidelines
Tests use JUnit Jupiter (JUnit 5) with the JUnit Platform. Place new tests alongside the module they cover in `*/src/test/java` and keep test class names descriptive of the unit under test.

## Commit & Pull Request Guidelines
Recent history uses short, imperative summaries such as `Update <FileName>.java` or `update`. Keep commit subjects brief and specific to the change.

For pull requests, include:
- A clear summary of what changed and why.
- A list of affected modules (e.g., `graphoenix-core`, `graphoenix-admin`).
- Test evidence (commands run and results).
- Screenshots if the admin UI (`graphoenix-admin/webapp`) is affected.

## Configuration Notes
The build expects local Maven and Maven Central (`mavenLocal()`, `mavenCentral()`), and uses the Gradle wrapper with repository-wide version properties defined in `build.gradle` and `gradle.properties`.
