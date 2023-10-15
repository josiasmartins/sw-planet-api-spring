FROM maven:3.8.5-openjdk-17 as build
# cria uma pasta build
WORKDIR /build
# copia tudo para a pasta build
COPY . .
# executa o comando para gear o artefato .jar
RUN mvn clean package -DsskipTests

# dependencia java (image)
FROM openjdk:17.0
# cria uma pasta app
WORKDIR /app
# copia o arquivo do jar para o WORDIR /app/sw_planet_api.jar
COPY --from=build ./build/target/*jar ./sw_planet_api.jar
# porta da imagem
EXPOSE 8080
# comando para executar o projeto
ENTRYPOINT java -jar sw_planet_api.jar