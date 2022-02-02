# Дипломный проект профессии "Тестировщик ПО" #
___
### Необходимое окружение: ###
1. Установленный Docker;
2. Установленный Node.js;
3. Браузер;

### Запуск приложения: ###
1. Склонировать репозиторий: https://github.com/AlexeyPotapenko/Diplom.git;
2. Запустить Docker контейнер ```docker-compose up --build```
3. Запустить SUT командой:
 - Для использования Postgres: ```java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar```
 - для использования MySQL: ```java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar```
4. Запутить тесты командой:
 - Для использования Postgres: ```gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"```
 - Для использования MySQL: ```gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"```
5. Остановить SUT ```CTRL + C```
6. Остановить контейнеры командой ```docker-compose stop``` , удалить контейнеры командой ```docker-compose down```
