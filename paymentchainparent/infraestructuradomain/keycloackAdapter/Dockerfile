FROM eclipse-temurin:21-jre-alpine

# Crear usuario y grupo para Alpine Linux
RUN addgroup -S paymentchain && adduser -S admin -G paymentchain

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR
COPY target/*.jar app.jar

# Cambiar propietario del JAR
RUN chown admin:paymentchain app.jar

# Usar el usuario no-root
USER admin:paymentchain

# Exponer puerto
EXPOSE 8088

# Variables de entorno
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]