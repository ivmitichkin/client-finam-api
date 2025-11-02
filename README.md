Реализация спецификации https://tradeapi.finam.ru/docs/guides/rest/
Для запуска необходим secret.token получаемый на вкладке https://tradeapi.finam.ru/docs/tokens
secret.token передается через переменную окружения secret.key=${SECRET_KEY}, без него приложение не запустится. 
По secret.token автоматически создаются и обновляются jwt-токены для выполнения запросов, переменная в application.properties - refresh.token.interval.ms.
Для использования приложения необходимо закодировать значение api-ключа через любой сервис кодировки. Например https://bcrypt-generator.com
В переменную окружения hash.api.key=${API_KEY} вводим закодированное значение и далее в интерфейсе, на кнопке Authorize используем без кодировки.
Пример:
Text to hash:
1234
Hash:
$2a$12$Sk7qOPluu2N/BB/ig34pU.1IsInaHiPzYrBLEyMlQaxtn0lgjAv0

Значение hash вводим в переменные окружения, а 1234 в качестве авторизационного ключа.

server.port=${SERVER_PORT} - вводим значение порта, по которому будет доступно приложение.

Далее откроется swagger-интерфейс с подробным описанием методов.
