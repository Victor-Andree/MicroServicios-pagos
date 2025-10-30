FROM eclipse-temurin:21-jre-alpine

# Crear usuario no-root (seguridad)
RUN addgroup -S paymentchain && adduser -S admin -G paymentchain

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR (forma correcta)
COPY target/*.jar app.jar

# Cambiar propietario del archivo
RUN chown admin:paymentchain app.jar

# Usar el usuario no-root
USER admin

# Exponer puerto del Config Server
EXPOSE 8888

# Entrypoint optimizado
ENTRYPOINT ["java", "-jar", "/app/app.jar"]