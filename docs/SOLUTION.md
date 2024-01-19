# Movies Details Solution

## Overview

This solution is a simple HTTP REST application that displays a list of movies and their details.
The application is built using the following technologies:

- [Spring Boot](https://spring.io/projects/spring-boot) - Java framework for building web
  applications
- [Spring Reactive Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) -
  Spring
  WebFlux reactive web framework
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc) - Spring Data R2DBC module
- [Liquidbase](https://www.liquibase.org/) - Database schema migration tool
- [MySQL](https://www.mysql.com/) - Database
- [Testcontainers](https://www.testcontainers.org/) - Docker-based integration testing framework
- [DDD](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/ddd-oriented-microservicen) -
  Domain-driven design

## Why Spring Boot?

The application is built using Spring Boot framework. The main reason for this is that Spring Boot
is a very
mature framework that provides a lot of features out of the box. It is also very easy to use and
configure.

## Why Spring Reactive Web?

The application is built using Spring Reactive Web framework. The main reason for this is that the
application is
a simple HTTP REST application that does not require a lot of CPU power. The application is mostly
IO bound, so
using a reactive framework allows us to use fewer threads to handle more requests.

## Why DDD?

The application is built using Domain-driven design. The main reason for this is that DDD allows us
to build
a complex application in a simple way. The application is divided into multiple layers, each of
which has its own
responsibilities.

## Why MySQL?

The application is built using MySQL database. The main reason for this is that MySQL is open source
and has all the
features that application needs. It is also very easy to use and configure. Application needs
flexibility which provided
by SQL language and ACID guarantees.
