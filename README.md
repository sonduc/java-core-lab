# java-core-lab

Java learning lab for core language topics, built to run entirely inside a Dev Container.

## Host requirements

- Docker
- VS Code
- Dev Containers extension

You do **not** need to install JDK or Maven natively on the host.

## Development workflow

1. Open `java-core-lab` in VS Code.
2. Run **Dev Containers: Reopen in Container**.
3. Let the container build from `.devcontainer/Dockerfile`.
4. Wait for `postCreateCommand` to verify the toolchain:

   ```bash
   mvn -version && java -version
   ```

Source code is bind-mounted from the host into `/workspace` inside the container.

## In-container commands

```bash
mvn test
mvn -Dtest=ExampleTest test
```

## Project structure

```text
java-core-lab/
├── .devcontainer/
├── .vscode/
├── pom.xml
├── README.md
├── .gitignore
├── .dockerignore
├── src/
│   ├── main/java/
│   └── test/java/
├── 01-basics/
│   ├── 01-oop/
│   ├── 02-collections/
│   ├── 03-exception/
│   └── 04-io/
├── 02-intermediate/
│   ├── 01-generics/
│   ├── 02-functional/
│   ├── 03-streams/
│   └── 04-reflection/
└── 03-advanced/
    ├── 01-concurrency/
    ├── 02-jmm/
    ├── 03-jvm/
    └── 04-virtual-threads/
```

The `src/main/java` and `src/test/java` folders are reserved for shared runnable code and tests under the root Maven build. Topic folders currently start as learning tracks with placeholder `README.md`, `example/`, `exercise/`, and `notes/` directories where applicable.
