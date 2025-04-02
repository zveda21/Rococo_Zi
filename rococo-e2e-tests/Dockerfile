FROM eclipse-temurin:21-jdk

WORKDIR /rococo
ENV TZ=Europe/Moscow
COPY ./gradle ./gradle
COPY ./rococo-e2e-tests ./rococo-e2e-tests
COPY ./gradlew ./
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD ./gradlew test -Dtest.env=docker -Dprofile=docker -Duser.timezone=Europe/Moscow