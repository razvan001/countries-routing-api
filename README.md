# Rooutes

A Spring Boot 3 application that computes land crossing routes between countries using the public countries dataset.

## Prerequisites

- Java 21 (or compatible with your project’s `pom.xml`)
- Maven 3.8+
- Internet access (to fetch dependencies and, at runtime, to retrieve the countries dataset)

## Configuration

Application properties are set in `src/main/resources/application.properties`.

Key properties:
- `server.servlet.context-path=/api/v1` – All endpoints are served under `/api/v1`.
- `countries.data.url=https://raw.githubusercontent.com/mledoze/countries/master/countries.json` – Source JSON for countries data.

## Build

```zsh
./mvnw -q -DskipTests=true clean compile
```

## Test

```zsh
./mvnw -q test
```

## Run

```zsh
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` and serve API under `/api/v1`.

## API

- GET `api/v1/routing/CZE/ITA`
  - Returns `200 OK` with body:
    ```json
    {
      "route": ["CZE", "AUT", "ITA"]
    }
    ```
  - Returns `400 Bad Request` when no land crossing route exists between origin and destination:
    ```json
    {
      "status": 400,
      "error": "Bad Request",
      "message": "No land crossing between origin and destination",
      "path": "/api/v1/routing/CZE/USA"
    }
    ```

## Swagger / OpenAPI

If `springdoc-openapi` is present (it is added in `pom.xml`), the Swagger UI is available at:

- `http://localhost:8080/api/v1/swagger-ui.html`

## Notes

- Origin and destination are expected to be ISO 3166-1 alpha-3 (CCA3) country codes (e.g., `CZE`, `AUT`, `ITA`).
- Route computation currently uses a BFS over country borders derived from the dataset.

