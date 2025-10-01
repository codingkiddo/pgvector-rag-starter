# PGVector RAG Starter (Spring Boot + Flyway + LangChain4j)

## Quickstart
```bash
docker compose up -d
export OPENAI_API_KEY=sk-...
./mvnw -U -DskipTests spring-boot:run
```

### Search
```bash
curl "http://localhost:8080/api/search?q=what%20is%20pgvector&collection=docs&k=5"
```

### Switch DSN (Docker network)
Use `application-docker.yaml` to point to `db` host.
