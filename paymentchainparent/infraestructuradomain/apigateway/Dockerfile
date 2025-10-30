FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S paymentchain && adduser -S admin -G paymentchain

WORKDIR /app

COPY target/*.jar app.jar

RUN chown admin:paymentchain app.jar

USER admin

EXPOSE 8080

# Optimizaciones para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]