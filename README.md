# Setup Guide

# Local
To run service on your local machine, run the scripts in order:

```bash
# Start docker containers for Postgres, Kafka, and Zookeeper
# Builds, runs tests, and packages JAR files
bash ./local-setup.sh

# Starts all backend services
bash ./local-all.sh

# Starts front-end app
bash ./local-client.sh
```

Service URLS
```text
Frontend    http://localhost:3000/
Auth        http://localhost:9000/
Gateway     http://localhost:8080/
User Data   http://localhost:8089/
Artist      http://localhost:8282/
Museum      http://localhost:8383/
Painting    http://localhost:8484/
```

# Default username and password
E2E tests either use a given username/password or register a new user.

`@ApiLogin`: Accepts username and password. If none is given the following system variables will be used.
```bash
# Default username and password when @ApiLogin is used with empty parameters
# The username and password should be pre-created
export ROCOCO_DEFAULT_USERNAME=test
export ROCOCO_DEFAULT_PASSWORD=123
```

`@User`: Registers a new user with given username or if skipped a random username will be created.

# Docker
To run services in dockerized mode, follow the steps:
Set the system-wide environment variables, in `.zshrc` (or equivalent):

```bash
# Credentials to pull and push Docker Hub
export DOCKER_USERNAME="<username>"
export DOCKER_PASSWORD="<token from docker hub>"
```

## Clean start
To run all the services and its dependencies in dockerized mode, run the following command:

```bash
# Clear existing containers and start over
bash docker-compose-dev.sh
```

Check the status of the services until they become healthy:
```bash
docker ps
```

Service URLS
```text
Frontend    http://frontend.rococo.dc/
Auth        http://auth.rococo.dc:9000/
Gateway     http://gateway.rococo.dc:8080/
User Data   http://userdata.rococo.dc:8089/
Artist      http://artist.rococo.dc:8282/
Museum      http://museum.rococo.dc:8383/
Painting    http://painting.rococo.dc:8484/
```

Edit the `/etc/hosts` file with edit permissions and add the required entries:

```text
## Rococo services
127.0.0.1       auth.rococo.dc
127.0.0.1       gateway.rococo.dc
127.0.0.1       frontend.rococo.dc
```

## Re-run
In order to stop the services (and keep the data) and start again, run the commands:

```bash
docker compose down
docker compose up
```

# Dockerized Tests
## Clean start
To run the E2E tests in dockerized mode run the script:

```bash
bash docker-compose-e2e.sh
```

The reports are sent to Allure service, and also available on local directory `{projectRoor}/allure-results`.

## Re-run
In order to stop the services and run in testing mode again, run the commands:

```bash
docker compose --profile test down
docker compose --profile test up
```

# Report
After running the dockerized E2E tests, the report is available at:
  - Allure UI http://localhost:5252/allure-docker-service-ui/projects/default
  - Allure Service http://localhost:5050/allure-docker-service/projects/default/reports/latest/index.html
  - Local directory `{projectRoor}/allure-results`

# Rococo

  Приветствую тебя, мой дорогой студент!
Если ты это читаешь - то ты собираешься сделать первый шаг в написании диплома QA.GURU Advanced.

  Это один из двух вариантов дипломной работы - второй расположен [тут, называется Rangiffler](https://github.com/qa-guru/rangiffler)
Проекты отличаются как по своей механике, так и технологиям (Rococo использует классический REST на frontend, 
тогда как Rangiffler использует GraphQL). Следует сказать, что Rangiffler может отказаться немного сложнее именно из-за GraphQL.
Выбор за тобой!

  Далее я опишу основные направления работы, но помни, что этот диплом - не шаблонная работа, а место
для творчества - прояви себя!

  Кстати, Rococo - стиль в искусстве (живописи и не только), а значит дело пахнет микросервисами, 
отвечающими за художников, их картины и музеи. И тестами на все это, которые должны стать искусством.

# Что будет являться готовым дипломом?

  Тут все просто, диплом глобально требует от тебя реализовать три вещи:

- Реализовать бэкенд на микросервисах (Spring boot, но если вдруг есть желание использовать что-то другое - мы не против)
- Реализовать полноценное покрытие тестами микросервисов и frontend (если будут какие-то
  unit-тесты - это большой плюс!)
- Красиво оформить репозиторий на гихабе, что бы любой, кто зайдет на твою страничку, смог понять,
  как все запустить, как прогнать тесты. Удели внимание этому пункту. Если я не смогу все запустить по твоему README - диплом останется без проверки

# С чего начать?

  Мы подготовили для тебя полностью рабочий frontend, а так же страницы регистрации и логина для сервиса auth.
Кроме того, у тебя есть и набор моков для [Wiremock](https://wiremock.org/docs/standalone/docker/) - благодаря этому, даже не приступая к написанию кода, ты сможешь посмотреть механику проекта Rococo. 
В этом наборе моков есть все запросы, которые тебе в конце концов, придется реализовать в своем бэкенде.
Важно понимать, что несмотря на наличие моков mutation запросов (например, удаление картины), никакого реального удаления не произойдет, и при обновлении страницы
Wiremock отдаст тот же набор данных, что и до удаления.

  И самое главное - у тебя есть проект niffler, который будет выступать образцом для подражания в разработке микросервисов.
Тестовое покрытие niffler, которого мы с тобой добились на настоящий момент, однако, является достаточно слабым - учтите это при написании тестов на Rococo - это,
все-таки, диплом для SDET / Senior QA Automation и падать в грязь лицом с десятком тестов на весь сервис
точно не стоит. Итак, приступим!

#### 1. Обнови зависимости и запускай фронт Rococо:

```posh
Dmitriis-MacBook-Pro rococo % cd rococo-client
Dmitriis-MacBook-Pro rococo-client % npm i
Dmitriis-MacBook-Pro rococo-client % npm run dev
```

  Фронт стартанет в твоем браузере на порту 3000: http://127.0.0.1:3000/
Обрати внимание! Надо использовать именно 127.0.0.1, а не localhost, но даже если ты по ошибке перейдешь на localhost, 
front автоматически тебя перенаправит.

#### 2. Запустите Wiremock, он заменит собо потенциальный сервис rococo-gateway, который только предстоит написать

```posh
docker pull wiremock/wiremock:2.35.0
docker run --name rococo-mock -p 8080:8080 -v ./wiremock/rest:/home/wiremock -d wiremock/wiremock:2.35.0 --global-response-templating --enable-stub-cors
```
  Эти команды надо запускать в корне проекта, там же есть скрипт `wiremock.sh`, делающий ровно то же самое. Можно просто запустить его.

  Wiremock стартанет на порту 8080: http://127.0.0.1:8080/ и будет готов отдавать тебе статические ответы на все запросы, уходящие
с фронта rococo. Кнопка "Войти" пока что не работает, что логично, ведь у нас нет сервиса auth.
Однако, наш Wiremock понимает некоторые запросы с oauth token, при чем ему не важно - какой именно это токен.
Поэтому, что бы смоделировать ситуация "я залогинился", то просто зайди в dev tools браузера,
перейди в Application, там - в LocalStorage и добавь туда токен:
- ключ id_token
- значение - любая строка, например "faketoken"

  После этого обнови страницу фронта и убедись, что вместо кнопки "Войти" у тебя отобразился профиль, а POST запросы заработали.

# Что дальше?

#### 1. В первую очередь, необходимо подумать над сервисами - какие тебе понадобятся.

  Например, можно предложить вот такую структуру сервисов:

<img src="services.png" width="600">

  ВАЖНО! Картинка - не догма, а лишь один из вариантов для примера.
Взаимодействие между gateway и всеми остальными сервисами можно сделать с помощью 
REST, gRPC или SOAP. Я бы посоветовал отдать предпочтение gRPC.

#### 2. Далее, необходимо реализовать сервис rococo-auth

  Фронтенд полностью готов к использованию сервиса auth на порту 9000,
твоя задача взять сервис niffler-auth и аккуратно переделать его для работы с rococo.
Страницы логина / регистрации, а так же стили и графику мы даем:

- eye.svg
- eye-active.svg
- hermitage.jpeg
- renuar.jpeg
- favicon.ico
- styles.css
- login.html
- register.html

  Основная задача - аккуратно заменить упоминания о niffler в этом сервисе, а в идеале - еще и
разобраться, как он работает. В этом будет полезно видео:
[Implementing an OAuth 2 authorization server with Spring Security - the new way! by Laurentiu Spilca](https://youtu.be/DaUGKnA7aro)
[Full Stack OAuth 2 - With Spring Security / React / Angular Part 1](https://youtu.be/SfNIjS_2H4M)
[Full Stack OAuth 2 - With Spring Security / React / Angular Part 2](https://youtu.be/3bGer6-6mdY)

#### 3. Как только у вас появилось уже 2 сервиса, есть смысл подумать о докеризации

  Чем раньше у ваc получится запустить в докере фронт и все бэкенды, тем проще будет дальше.
На самом деле, докеризация не является строго обязательным требованием, но если вы хотите в будущем
задеплоить свой сервис на прод, прикрутить CI/CD, без этого никак не обойдется.

  Я советую использовать плагин jib - как в niffler, для бэкендов, и самописный dockerfile для фронта.
Фронтенд использует фреймворк Svelte, но докеризация там работает ровно так же, как и для React в Niffler.

#### 4. Выбрать протокол взаимодействия между сервисами

  В поставляемом фронтенде классический REST. А вот взаимодействие между микросервисами можно
делать как угодно! REST, gRPC, SOAP. Делай проект я, однозначно взял бы gRPC - не писать руками кучу
model-классов, получить перформанс и простое написание тестов. Стоит сказать, что здесь не
понадобятся streaming rpc, и все ограничится простыми унарными запросами. Однако если вы хотите
использовать REST или SOAP - мы не будем возражать.

#### 5. Реализовать микросервисный backend

  Это место где, внезапно, СОВА НАРИСОВАНА! :)
На самом деле, концептуально и технически каждый сервис будет похож на что-то из niffler, поэтому
главное внимательность и аккуратность. Любые отхождения от niffler возможны - ты можешь захотеть
использовать, например, NoSQL базы или по другому организовать конфигурацию / структуру проекта -
никаких ограничений, лишь бы сервис выполнял свое прямое назначение

##### Особенности реализации backend

###### Сервис gateway, работа с пагинацией

   В отличие от Niffler, проект Rococo использует пагинацию (что бы грузить данные с бэка по частям), а это значит что в `rococo-gateway` должны быть реализованы 
Pageble контроллеры.
Пример:
```java
  @GetMapping()
  public Page<ArtistJson> getAll(@RequestParam(required = false) String name,
                                 @PageableDefault Pageable pageable) {
    return artistService.getAll(name, pageable);
  }
```
  Здесь объект `Pageable` - приходит в виде GET параметров с фронта. Спринг сам превратит GET параметры в этот объект.
Так же GET парметром может прийти (а может и нет) параметр name. Тогда запрос в БД должен включать фильтрацию по полю name (`ContainsIgnoreCase`)
Пример репозитория с запросом к БД с учетом Pageable и name (он будет, размеется, не в `rococo-gateway`, а в конечных микросервисах)
```java
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

  @Nonnull
  Page<ArtistEntity> findAllByNameContainsIgnoreCase(
          @Nonnull String name,
          @Nonnull Pageable pageable
  );
}
```
  Тип `Page<T>` - это ровно то, что ожидает от вас получит фронт, вам лишь придется преобразовать его в Page<ArtistJson>, 
для этого надо воспользоваться методом `map()`, имеющимся в классе Page.

Почитать про пагинацию, дополнительно, тут: https://www.baeldung.com/spring-data-jpa-pagination-sorting

###### Передача `Pageable` по gRPC между сервисами, возврат `Page` из сервисов

  Тут все просто. Когда вас с фронта приходит `@PageableDefault Pageable pageable` - из него можно достать две цифры -
`page` и `size`. + не забыть про третий опциональный парметр - `name`. Тогда, к примеру, gRPC сообщение в сервис с художнниками 
могло бы выглядеть так:
```protobuf
message ArtistsRequest {
  string name = 1;
  int32 page = 2;
  int32 size = 3;
}

message ArtistsResponse {
  repeated Artist artists = 1;
  int32 total_count = 2;
}
```
  Тогда мы сможем вернуть на фронт созданный руками Pageable
```java
            List<ArtistJson> artistJsonList = response.getArtistsList()
                    .stream()
                    .map(ArtistJson::fromGrpcMessage)
                    .toList();
            return new PageImpl<>(artistJsonList, pageable, response.getTotalCount());
```

  Здесь объект `pageable` - это тот, что мы изначально получили от фронта для выполнения запроса, а `response.getTotalCount()`
- общее число художников в базе.

###### Security config

   Необходим доступ без авторизации к эндпойнту `/api/session` и к GET запросам без необходимости быть
аторизованным, для этого пропишем их в security config сервиса `rococo-gateway`:
```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);

        http.authorizeHttpRequests(customizer ->
                customizer.requestMatchers(
                                antMatcher(HttpMethod.GET, "/api/session"),
                                antMatcher(HttpMethod.GET, "/api/artist/**"),
                                antMatcher(HttpMethod.GET, "/api/museum/**"),
                                antMatcher(HttpMethod.GET, "/api/painting/**"))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        ).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
```
Все прочие эндпойнты должны требовать авторизацию

   В связи с тем, что проект подразумевает GET запросы без авторизации, то и тесты должны учитывать разные кейсы, 
авторизованный пользователь и нет

#### 6. Подготовить структуру тестового "фреймворка", подумать о том какие прекондишены и как вы будете создавать

  Здесь однозначно понадобится возможность API-логина и работы со всеми возможными preconditions проекта - картинами,
художниками, музеями. Например, было бы хорошо иметь тесты примерно такого вида:
```java
@Test
@DisplayName("...")
@Tag("...")
@ApiLogin(user = @TestUser)
@TestMuseum(title = "Музей в Китай", country = "Китай", city = "Пекин")
void exampleTest(MuseumJson createdMuseum) { ... }

@Test
@DisplayName("...")
@TestPainting
@TestMuseum
@TestArtist
@Tag("...")
void exampleTest2(PaintingJson createdPainting, MuseumJson createdMuseum, ArtistJson createdArtist) { ... }
```

#### 7. Реализовать достаточное, на твой взгляд, покрытие e-2-e тестами

  На наш взгляд, только основны позитивных сценариев тут не менее трех десятков.
А если не забыть про API-тесты (будь то REST или gRPC), то наберется еще столько же.

#### 8. Оформить все красиво!

  Да, тут еще раз намекну про важность ридми, важность нарисовать топологию (схему) твоих сервисов, важность скриншотиков и прочих красот.
Очень важно думать о том, что если чего-то не будет описано в README, то и проверить я это что-то не смогу.

<img src="rococo.png" width="800">
