## Introduction

This is a simple Spring boot application that use REST API to manage the customer data for a small shop.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

- Git
- OpenJDK >= 11

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/inoxydabl3/REST-API.git
   ```
2. Performs a "plain" build
   ```sh
   ./mvnw clean install
   ```
3. Run the application. Using our IDE we can create a launcher, or directly run it by executing the main method in the class
   _com.example.demo.DemoApplication_.

After that the application is running on the 8080 port. You can change the port by passing _JVM Arguments_:

    -Dserver.port=8087

## APIs Documentation with Swagger

This project is using Swagger that provides a user interface to access our RESTful web services via the web browser.

Swagger main page is - http://localhost:8080/swagger-ui.html

## Dockerizing the APP

### Prerequisites

- Docker

### Creating image

To create the image we just have to run the docker app and execute
   ```sh
   ./mvnw spring-boot:build-image
   ```

This process will create our image. Then we list the available docker images: 
   ```sh
   docker image ls -a
   ```

We see a line for the image we just created:
   ```sh
   demo 1.0.0 279fe4df31bf
   ```

### Run the image

We can execute the following command to run the image
   ```sh
   docker run -d -p 8080:8080 demo:1.0.0
   ```
You'll notice a few flags being used. Here's some more info on them:

- -d - run the container in detached mode (in the background)
- -p 8080:8080 - map port 8080 of the host to port 8080 in the container
- demo:1.0.0 - the image to use

Finally, if we want we can list the running containers
   ```sh
   docker container ls
   ```