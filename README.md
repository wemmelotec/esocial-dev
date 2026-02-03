# eSocial

Aplicação Spring Boot 3 + Thymeleaf + PostgreSQL para exibir mensagens do eSocial.

## Requisitos
- Java 17
- Maven 3.9+
- Docker e Docker Compose (para execução containerizada)

## Rodando em desenvolvimento
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
A aplicação sobe em `http://localhost:8080`.

## Rodando com Docker Compose
```bash
docker-compose up --build
```
- App: `http://localhost:8080`
- Banco: `localhost:5432` (db: `esocial`, user/pass: `esocial`)

## Endpoints principais
- `GET /` – página inicial (mensagem de boas-vindas)
- `GET /relatorio` – página de relatório; clique em "Gerar relatório" para buscar mensagens
- `GET /health` – healthcheck simples retornando `OK`

## Estrutura em camadas
- `controller` – endpoints web e páginas Thymeleaf
- `service` – regras e orquestração de domínio
- `repository` – acesso a dados (Spring Data JPA)
- `domain` – entidades JPA
- `dto` – objetos de transporte (reservado)
- `infra`, `config` – infraestrutura e configurações (reservado)

## Seed de dados
O arquivo `src/main/resources/data.sql` insere mensagens iniciais para testes.
