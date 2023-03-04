# Тестовое задание вакансии разработчика в С××××.
Разработать сервис, реализующий получение статистики по вопросам на StackOverflow.

1. Обслуживать HTTP запросы по URL "/search". В параметрах запроса передается параметр `tag`, содержащий ключевой тэг для поиска. Параметров может быть несколько, в этом случае мы работаем с несколькими ключевыми тэгами. Пример `http://localhost:8080/search?tag=clojure&tag=scala`. Предполагаем, что клиент будет передавать только алфавитно-цифровые запросы в ASCII. Однако, наличие корректной поддержки русского языка в кодировке UTF-8 будет плюсом.
2. Сервис должен обращаться к REST API StackOverflow для поиска. В случае, если ключевых слов передано больше одного, запросы должны выполняться параллельно (по одному HTTP запросу на ключевое слово). Должно быть ограничение на максимальное количество одновременных HTTP-соединений, это значение нельзя превышать. Если ключевых слов больше, нужно организовать очередь обработки так, чтобы более указанного количество соединений не открывалось.
3. По каждому тэгу ищем только первые 100 записей, отсортированных по дате создания. Пример запроса к API: `https://api.stackexchange.com/2.2/search?pagesize=100&order=desc&sort=creation&tagged=clojure&site=stackoverflow`. Можно использовать любые дополнительные параметры запроса, если это необходимо.
4. В результатах поиска интересует полный список тегов (поле `tags`) по каждому вопросу, а также был ли дан на вопрос ответ.
5. В результате работы запроса должна быть возвращена суммарная статистика по всем тэгам - сколько раз встречался тэг во всех вопросах и сколько раз на вопрос, содержащий тэг, был дан ответ.
6. Результат должен быть представлен в формате JSON. Выдача ответа с человеко-читаемым форматированием (`pretty print`) будет рассматриваться как плюс. Пример ответа:

	```json
	{
		"clojure": { "total": 173, "answered": 54},
		"python": { "total": 100, "answered": 9},
		"clojurescript": { "total": 193, "answered": 193}
	}
	```

### Использование сторонних компонентов
Можно использовать любые open-source компоненты, доступные на clojars или maven central. Можно использовать любое средство разработки веб-приложений, например сервлеты, spring mvc, jax-rs, ring и т.п.
Приветствуются короткие решения, использующие сторонние компоненты.


### Ожидаемый результат
Приложение:
1. [x] Должно компилироваться и работать.
2. [x] Должно собираться однострочной командой при помощи maven, lein или другого средства сборки, которое легко развернуть.
3. [x] Должно запускаться одной командой. Например через lein run или аналогичное средство.
4. [x] Должно запускаться без необходимости отдельно разворачивать сервер приложений.
5. [x] Веб-интерфейс для сервиса не обязателен.


### Материалы к размышлению:
- [Документация API StackOverflow](https://api.stackexchange.com/docs/search)


## Использование
## Разработка
```shell
clojure -M:dev
```

## Запуск
```shell
clojure -X:run
```

## Тестирование
```shell
clojure -X:test
```

## Сборка
```shell
clojure -T:build
```

## Запуск
```shell
java -jar target/assignment-0.0.17-standalone.jar
```
