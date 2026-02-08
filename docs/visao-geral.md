# Visao geral do sistema eSocial (prefeitura) com login gov.br

Este documento explica, em linguagem simples, o que existe na aplicacao, por que cada parte e importante e como o usuario final se beneficia.

## O que o sistema faz
- Disponibiliza paginas web para a prefeitura acessar mensagens e relatorios do eSocial.
- Permite entrar usando a conta gov.br (login unico do cidadao).
- Depois do login, a aplicacao guarda os dados basicos do usuario (identificador, nome, email, CPF se vier) para manter um registro local.
- Protege o conteudo: so quem esta autenticado pode acessar a home e o relatorio.
- Oferece logout, inclusive tentando encerrar a sessao no provedor gov.br quando suportado.

## Principais telas
- `/login`: pagina com um botao "Entrar com gov.br". Se a pessoa ja estiver logada, ela e redirecionada para a home.
- `/` (home, protegida): mostra a mensagem de boas-vindas e os dados do usuario autenticado (nome, email, identificador).
- `/relatorio` (protegida): lista as mensagens do eSocial. So carrega dados quando o usuario clica em "Gerar relatorio".
- `/health` e `/actuator/health`: verificacao basica se o sistema esta no ar.
- `/logout`: encerra a sessao local e, se configurado, aciona o logout do gov.br.

## Como o login gov.br funciona (resumo)
1) Na pagina `/login`, o usuario clica em "Entrar com gov.br".  
2) Ele e levado para o site do gov.br, faz a autenticacao la e autoriza o acesso.  
3) O gov.br devolve o usuario para a aplicacao em `/login/oauth2/code/govbr`.  
4) A aplicacao recebe as informacoes principais (claims) e salva/atualiza o cadastro local.  
5) O usuario segue para a home, ja autenticado.

## Componentes internos (em termos simples)
- Controladores web: cuidam das paginas (`HomeController`, `AuthController`, `HealthController`).
- Servicos: aplicam regras de negocio, como buscar mensagens e salvar o usuario que veio do gov.br.
- Repositorios: conversam com o banco para salvar e ler dados.
- Configuracao de seguranca: define rotas publicas, rotas protegidas, login via gov.br e logout.
- Templates Thymeleaf: os arquivos HTML das telas (home, relatorio, login).
- Scripts de banco: criam a tabela de usuario federado e populam mensagens de exemplo.
- Testes automatizados: garantem que o redirecionamento, o login, o logout e as paginas funcionem como esperado.

## Por que cada parte existe
- **Login gov.br**: elimina senha local e reaproveita a identidade unica do cidadao; mais seguro e aderente a politicas governamentais.
- **Protecao de rotas**: evita acesso nao autorizado a dados e relatorios.
- **Cadastro local do usuario**: guarda quem entrou (provider + identificador) e mantem nome/email/CPF sincronizados.
- **Logout**: permite sair da aplicacao e, quando possivel, encerrar tambem a sessao no provedor.
- **Healthcheck**: monitora de forma simples se o servico esta ativo.
- **Relatorio sob demanda**: so busca dados quando o usuario solicita, reduzindo carga desnecessaria.
- **Testes**: diminuem risco de regressao ao evoluir o codigo.

## Como testar localmente (alto nivel)
- Obter um client no ambiente de teste do gov.br e cadastrar o redirect `http://localhost:8080/login/oauth2/code/govbr`.
- Preencher as variaveis de ambiente (ID/secret e endpoints do gov.br). Use `.env.example` como guia.
- Rodar a aplicacao com perfil `dev`: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`.
- Abrir `http://localhost:8080/login`, fazer login no gov.br e voltar autenticado para ver a home e o relatorio.

## Beneficio para o cliente
- Autenticacao alinhada ao padrao gov.br, sem inventar senhas locais.
- Registro basico do usuario para auditoria e integracoes futuras.
- Rotas protegidas e logout claro, dando previsibilidade ao usuario.
- Paginas simples e objetivas para visualizar informacoes do eSocial.
