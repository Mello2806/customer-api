# customer-api

API REST para gerenciamento de clientes desenvolvida com Spring Boot e PostgreSQL. Projeto de estudo do curso de Análise e Desenvolvimento de Sistemas — evolução do [customer-management-system](https://github.com/Mello2806/customer-management-system), que fazia o mesmo cadastro no console com JDBC puro.

> 🚧 **Em construção.** Por enquanto a API tem só o endpoint de teste `/ola`. O CRUD de clientes vem nos próximos passos.

## Tecnologias

- Java 17
- Spring Boot 3.3 (Web, DevTools)
- Maven
- JUnit 5 + MockMvc

## Como rodar

```bash
mvn spring-boot:run
```

Depois acesse: [http://localhost:8080/ola](http://localhost:8080/ola)

Com o **DevTools**, a API reinicia sozinha sempre que o código é recompilado — não é preciso parar e subir de novo a cada mudança.

## Testes

```bash
mvn test
```

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| GET | `/ola` | Confere se a API está no ar |

## Roadmap

- [x] Projeto Spring Boot com DevTools
- [x] Endpoint de teste `/ola`
- [ ] `GET /clientes` retornando JSON
- [ ] Conexão com PostgreSQL (Spring Data JPA)
- [ ] CRUD completo: `POST`, `GET /{id}`, `PUT`, `DELETE`
- [ ] Validações com `@Valid` (nome, e-mail, CPF)
- [ ] Tratamento de erros com `@ControllerAdvice`
- [ ] Documentação com Swagger
