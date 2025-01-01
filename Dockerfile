FROM eclipse-temurin:23.0.1_11-jdk-alpine as build

ARG JAR_FILE
WORKDIR /build

ADD $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM eclipse-temurin:23.0.1_11-jdk-alpine

RUN addgroup -S spring-group && adduser -S --ingroup spring-group spring-group
USER spring-group:spring-group

VOLUME /tmp
WORKDIR /app

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}
