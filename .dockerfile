# ============================
# Etapa 1: Build
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Diretório de trabalho no container de build
WORKDIR /app

# Copia arquivos do Maven primeiro (para aproveitar cache)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixa dependências para aproveitar cache entre builds
RUN ./mvnw dependency:go-offline

# Copia o restante do código
COPY src ./src

# Compila e gera o JAR
RUN ./mvnw clean package -DskipTests

# ============================
# Etapa 2: Runtime
# ============================
FROM eclipse-temurin:17-jre-alpine

# Diretório de trabalho no container final
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta (Render substitui via variável PORT)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
