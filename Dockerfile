# Imagem da customer-api em duas etapas:
#   1) "build": usa Maven + JDK 17 para compilar e gerar o .jar
#   2) "final": só um JRE 17 enxuto rodando o .jar (imagem bem menor, sem Maven nem código-fonte)
#
# Construir:  docker build -t customer-api .
# Rodar:      docker run --rm -p 8080:8080 --network host \
#               -e DB_URL=jdbc:postgresql://localhost:5432/clientes_db \
#               -e DB_USUARIO=clientes_app -e DB_SENHA=sua_senha customer-api

FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
# Baixa as dependências antes de copiar o código: se só o código mudar, o Docker reaproveita esta camada.
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Não roda como root dentro do contêiner (boa prática de segurança).
RUN addgroup -S app && adduser -S app -G app
USER app
COPY --from=build /app/target/customer-api-*.jar app.jar
EXPOSE 8080
# Limite de memória pensado para PCs com pouca RAM.
ENV JAVA_TOOL_OPTIONS="-Xmx256m"
ENTRYPOINT ["java", "-jar", "app.jar"]
