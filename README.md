

# Pigeon - Api project to notify clients

Api project for sending news emails to clients. The project also includes control for managing clients and news.

## 🧑‍💻 Technology

* **Java 17**
* **Spring Boot 3**
* **JPA + Hibernate**
* **API REST**
* **PostgreSQL**
* **Maven**
* **Docker**
* **docker-compose**
* **JUnit 5 + Mockito (back-end tests)**
* **Swagger**
* **Mailtrap (Simulates an email server)**

## 🛠️ Tools used


![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)


## 🎯 Proposed Architecture

### Client
- **POST** `/api/clients`: Create a new customer.
- **GET** `/api/clients`: List all customers.
- **GET** `/api/clients/id`:  Find a specific customer.
- **PUT** `/api/clients/id`: Updates a specific customer.
- **DELETE** `/api/clients/id`: Removes a specific customer.

### News

- **POST** `/api/news`: Create a new news item.
- **GET** `/api/news`: List all news.
- **GET** `/api/news/id`:List a specific news.
- **PUT** `/api/news/id`: Updates a specific news.
- **POST** `/api/news/sendEmailDaily`: Send daily unprocessed news(if you want to send emails manually).
- **DELETE** `/api/news/id`: Removes a specific news.


To run the applications, you will need to have them installed:

* **Docker**
* **PostgreSQL (If you want to install locally)**
* **Java 17**
* **IntelliJ or another ide of your choice**

When running the application locally, access the swagger via the link:
* http://localhost:8080/swagger-ui/index.html#/