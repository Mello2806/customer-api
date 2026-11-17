# customer-api

API REST para gerenciamento de clientes desenvolvida com Spring Boot e PostgreSQL. Projeto de estudo do curso de Análise e Desenvolvimento de Sistemas — evolução do [customer-management-system](https://github.com/Mello2806/customer-management-system), que fazia o mesmo cadastro no console com JDBC puro.

CRUD completo de clientes com validação de dados (incluindo os dígitos verificadores do CPF), respostas de erro padronizadas e documentação interativa com Swagger.

## Tecnologias

- Java 17
- Spring Boot 3.3 (Web, Data JPA, Validation, DevTools)
- PostgreSQL
- springdoc-openapi (Swagger)
- Maven
- JUnit 5, Mockito, MockMvc e H2 (banco em memória nos testes)

## Como rodar

1. Crie o banco e a tabela com os scripts do [customer-management-system](https://github.com/Mello2806/customer-management-system) (`sql/setup-banco.sql` e `sql/schema.sql`) — os dois projetos usam o mesmo banco `clientes_db`.
2. Copie `db.properties.example` para `db.properties` e coloque a senha do banco. Esse arquivo fica fora do git. Também dá para usar as variáveis de ambiente `DB_URL`, `DB_USUARIO` e `DB_SENHA`.
3. Suba a API:

```bash
mvn spring-boot:run
```

Depois acesse a documentação: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) — dá para testar todos os endpoints por lá.

Com o **DevTools**, a API reinicia sozinha sempre que o código é recompilado — não é preciso parar e subir de novo a cada mudança.

## Testes

```bash
mvn test
```

Os testes usam o H2 (banco em memória), então não precisam do PostgreSQL rodando.

> O projeto limita a memória da JVM (`.mvn/jvm.config` e `argLine` no `pom.xml`) para rodar bem em máquinas com 4 GB de RAM.

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/ola` | Confere se a API está no ar |
| GET | `/clientes` | Lista os clientes em ordem alfabética, **paginado** |
| GET | `/clientes?nome=ana` | Busca por parte do nome (sem diferenciar maiúsculas) |
| GET | `/clientes/{id}` | Busca um cliente |
| POST | `/clientes` | Cadastra um cliente → `201` |
| PUT | `/clientes/{id}` | Atualiza um cliente |
| DELETE | `/clientes/{id}` | Exclui um cliente → `204` |

### Paginação e busca

`GET /clientes` aceita três parâmetros opcionais:

| Parâmetro | Padrão | O que faz |
|---|---|---|
| `nome` | — | Filtra por parte do nome: `ana` acha "Ana Souza" e "Mariana" |
| `pagina` | `0` | Número da página (começa em 0) |
| `tamanho` | `10` | Itens por página (máximo 100) |

Exemplo: `GET /clientes?nome=ana&pagina=0&tamanho=5`

```json
{
  "conteudo": [ { "id": 1, "nome": "Ana Souza", "...": "..." } ],
  "pagina": 0,
  "tamanho": 5,
  "totalElementos": 7,
  "totalPaginas": 2
}
```

Exemplo de corpo para `POST`/`PUT`:

```json
{
  "nome": "Ana Souza",
  "email": "ana@email.com",
  "cpf": "529.982.247-25",
  "telefone": "(11) 99999-0000"
}
```

### Erros

Todos os erros seguem o mesmo formato:

```json
{
  "momento": "2026-11-12T23:50:11",
  "status": 400,
  "erro": "Dados inválidos",
  "campos": { "cpf": "CPF inválido", "nome": "é obrigatório" }
}
```

| Status | Quando |
|---|---|
| 400 | Campos inválidos ou JSON mal formatado |
| 404 | Cliente não encontrado |
| 409 | E-mail ou CPF já cadastrado |

## Roadmap

- [x] Projeto Spring Boot com DevTools
- [x] Endpoint de teste `/ola`
- [x] `GET /clientes` retornando JSON
- [x] Conexão com PostgreSQL (Spring Data JPA)
- [x] CRUD completo: `POST`, `GET /{id}`, `PUT`, `DELETE`
- [x] Validações com `@Valid` (nome, e-mail, CPF)
- [x] Tratamento de erros com `@ControllerAdvice`
- [x] Documentação com Swagger
- [x] Paginação em `GET /clientes`
- [x] Busca por nome
- [x] Dockerfile (imagem em duas etapas)
- [ ] Testar a imagem Docker e publicar (deploy)

## Docker

```bash
docker build -t customer-api .
docker run --rm --network host \
  -e DB_URL=jdbc:postgresql://localhost:5432/clientes_db \
  -e DB_USUARIO=clientes_app -e DB_SENHA=sua_senha \
  customer-api
```

A API sobe em http://localhost:8080 (Swagger em `/swagger-ui.html`). `--network host` deixa o contêiner enxergar o PostgreSQL que já roda no seu PC.
