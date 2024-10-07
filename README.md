# Библиотека

## Описание
Это система управления библиотекой с серверной частью на Spring Boot (Java) и клиентским приложением на C++ с консольным интерфейсом, использующим библиотеку cURL для взаимодействия с API.

## Запуск серверной части

### Требования:
1. Убедитесь, что у вас установлен [Docker](https://www.docker.com/products/docker-desktop) и [Docker Compose](https://docs.docker.com/compose/install/).

### Шаги для запуска:
	cd library/docker
 	docker-compose up
  
# Сборка и запуск клиентского приложения

Требования:

•	Visual Studio (версии 2019 и выше)
 
•	Установленный vcpkg для управления зависимостями
 
 
 Откройте командную строку для разработчиков Visual Studio (Developer Command Prompt for VS).
 
 Установите vcpkg и необходимые зависимости:
 
  	git clone https://github.com/microsoft/vcpkg.git
  
 	cd vcpkg
  
  	.\bootstrap-vcpkg.bat
  
  	.\vcpkg.exe integrate install
  
	.\vcpkg.exe install curl
  
  	.\vcpkg.exe install jsoncpp
  

  Visual Studio откройте проект в папке LibClient.
  
Соберите проект:
 
•	В меню Visual Studio выберите Build > Build Solution.

# Примеры использования API

Получение всех книг
	•	Метод: GET
	•	URL: /books
	•	Пример запроса:

 curl -X GET http://localhost:8080/books
