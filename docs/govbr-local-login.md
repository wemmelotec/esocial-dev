# Login gov.br em ambiente local

Esta nota resume o que precisa ser feito fora da aplicação e como subir localmente para testar o fluxo OIDC com o gov.br.

## 1. Pré-requisitos (sem custo)
- Acesso a um client gov.br em ambiente de homolog/teste (normalmente via Conecta gov.br / Keycloak da SECOM). A concessão do client depende da sua organização, mas não há custo direto.
- Cadastrar o redirect URI: `http://localhost:8080/login/oauth2/code/govbr` (ou o valor que definir em `GOVBR_REDIRECT_URI`).

## 2. Coletar os endpoints do seu ambiente
Anote os endpoints OIDC do seu realm gov.br (exemplos genéricos; substitua pelos do seu ambiente):
- Authorization: `https://<seu-realm>/protocol/openid-connect/auth`
- Token: `https://<seu-realm>/protocol/openid-connect/token`
- JWKs: `https://<seu-realm>/protocol/openid-connect/certs`
- UserInfo: `https://<seu-realm>/protocol/openid-connect/userinfo`
- End Session (logout): `https://<seu-realm>/protocol/openid-connect/logout`
- (Opcional) Issuer: `https://<seu-realm>` — use apenas se estiver resolvível; caso contrário, continue com endpoints explícitos.

## 3. Configurar variáveis de ambiente
Preencha `.env.example` e exporte/importe no terminal antes de subir a aplicação:

### PowerShell (Windows)
```powershell
Set-Content -Path .env.local -Value (Get-Content .env.example)
# Edite .env.local com seus valores reais
Get-Content .env.local | ForEach-Object {
  if ($_ -match '^(?<k>[^=]+)=(?<v>.*)$') { [System.Environment]::SetEnvironmentVariable($matches.k, $matches.v, 'Process') }
}
```

### Bash (Linux/macOS/WSL)
```bash
cp .env.example .env.local
# Edite .env.local com seus valores reais
export $(grep -v '^#' .env.local | xargs)
```

Campos necessários:
- `GOVBR_CLIENT_ID`, `GOVBR_CLIENT_SECRET`
- `GOVBR_REDIRECT_URI` (igual ao registrado)
- `GOVBR_AUTH_URI`, `GOVBR_TOKEN_URI`, `GOVBR_JWK_URI`, `GOVBR_USERINFO_URI`, `GOVBR_END_SESSION_URI`
- Opcional: `GOVBR_ISSUER_URI` (se preferir resolver tudo via issuer)

## 4. Subir a aplicação local (profile dev)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
- App: `http://localhost:8080/login`
- Clique em “Entrar com gov.br” → será redirecionado para o provider.
- Após login, você voltará autenticado e o usuário será salvo/atualizado na tabela `usuario`.

## 5. Dicas de troubleshooting
- Se receber erro de issuer, remova `GOVBR_ISSUER_URI` e use apenas os endpoints explícitos (auth/token/jwk/userinfo).
- Garanta que o redirect cadastrado no gov.br *bate* com o `GOVBR_REDIRECT_URI` usado localmente.
- Limpe cookies da sessão gov.br se testar várias vezes o logout.
