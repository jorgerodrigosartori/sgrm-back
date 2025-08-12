# Use uma imagem oficial do Java como base (aqui estamos usando a versão 11)
FROM openjdk:11-jre-slim

# Copie o arquivo JAR do seu projeto para dentro da imagem Docker
COPY target/sgrm-back.jar /usr/app/

# Defina o diretório de trabalho dentro do container
WORKDIR /usr/app

# O comando para rodar a aplicação
CMD ["java", "-jar", "sgrm-back.jar"]
