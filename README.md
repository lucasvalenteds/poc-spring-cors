# POC: Cross Origin Request Filter

It demonstrates how to implement a CORS filter using Spring MVC and typed configuration.

The goal is to develop a REST API with configurations related to Cross-Origin Resource Sharing (CORS) obtained from the
environment with a fallback to properties file for better developer experience. We want to specify a list of origin URLs
allowed to access the endpoints and which HTTP verbs are available as well. The properties should be type-safe.

## How to run

| Description     | Command             |
|:----------------|:--------------------|
| Run tests       | `./gradlew test`    |
| Run application | `./gradlew bootRun` |