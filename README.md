# eSocial

Aplicacao Spring Boot 3 + Thymeleaf + PostgreSQL com login via gov.br (OpenID Connect).

## Requisitos
- Java 17
- Maven 3.9+
- Docker e Docker Compose (opcional para subir Postgres rapidamente)

## Variaveis de ambiente para o gov.br
- `GOVBR_CLIENT_ID`
- `GOVBR_CLIENT_SECRET`
- `GOVBR_REDIRECT_URI` (ex.: `http://localhost:8080/login/oauth2/code/govbr`)
- `GOVBR_ISSUER_URI` ou, se preferir manual, `GOVBR_AUTH_URI`, `GOVBR_TOKEN_URI`, `GOVBR_JWK_URI`, `GOVBR_USERINFO_URI`
- `GOVBR_END_SESSION_URI` (opcional, usado no logout OIDC)

No portal do gov.br, cadastre o redirect URI igual ao valor configurado (padrão: `{baseUrl}/login/oauth2/code/govbr`).

Passo a passo para testar o login localmente: veja `docs/govbr-local-login.md` e use o `.env.example` como base.

## Como rodar
### Desenvolvimento
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
App em `http://localhost:8080`.

### Docker Compose
```bash
docker-compose up --build
```
Servicos: app em `http://localhost:8080` e banco em `localhost:5432` (db/user/pass: `esocial`).

## Endpoints principais
- `/login` – tela com botao "Entrar com gov.br"
- `/oauth2/authorization/govbr` – inicia o fluxo OIDC
- `/logout` – encerra sessao local e tenta logout OIDC
- `/` – home protegida mostrando info do usuario autenticado
- `/relatorio` – relatorio protegido de mensagens
- `/health` e `/actuator/health` – healthcheck publico

## Banco e seeds
- `src/main/resources/schema.sql` cria a tabela `usuario` (usuario federado).
- `src/main/resources/data.sql` insere mensagens iniciais (tabela `mensagem`).

## Testes
```bash
mvn test
```
Inclui testes de seguranca com `oidcLogin` para validar redirecionamento, upsert de usuario no login e logout.
