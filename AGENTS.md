# Repository Instructions

This repository contains a Maven module `tracing-lib` that provides minimal OpenTelemetry tracing support.

## Build
Run the following command after any changes in `tracing-lib`:

```
mvn -f tracing-lib/pom.xml package
```

This builds the library jar using Java 17. There are currently no tests.

## Dependencies
`tracing-lib` uses Spring Web, the Jakarta Servlet API, and SLF4J for logging alongside OpenTelemetry SDK components.

