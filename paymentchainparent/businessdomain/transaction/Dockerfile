# Imagen base con Java 21 Alpine
FROM eclipse-temurin:21-jre-alpine

# Crear grupo y usuario sin privilegios
RUN addgroup -g 1028 devopsc \
    && adduser -D -G devopsc admin

# Directorio de trabajo
WORKDIR /app

# Argumento para recibir el nombre del JAR
ARG JAR_FILE=app.jar

# Copiar el jar con el nombre que venga del argumento
COPY ${JAR_FILE} app.jar

# Cambiar permisos al usuario no root
RUN chown -R admin:devopsc /app

# Cambiar a usuario rootless
USER admin

# Ejecutar la app con JAVA_OPTS opcionales
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
