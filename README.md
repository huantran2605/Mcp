# Chatbot with Spring AI and MCP

## Description
This project is a Spring Boot application that implements a chatbot using Spring AI, integrated with the Ollama chat model. It also functions as an MCP (Model Context Protocol) server, exposing tools for user management. The application provides a conversational interface and demonstrates how to integrate custom tools with a large language model (LLM) for enhanced functionality.

## Features
*   **Ollama-Powered Chatbot:** Interact with an AI chatbot capable of understanding and responding to natural language queries.
*   **MCP Server:** Exposes custom tools that the chatbot (or other MCP clients) can utilize to perform specific actions.
*   **User Management Tools:** Provides `getUserByUsername` and `addUser` tools to interact with a MySQL database for user data management.
*   **Spring Boot Application:** A robust and scalable backend built with Spring Boot.
*   **RESTful API:** A simple `/chat` endpoint for chatbot interaction.

## Technologies Used
*   **Java 17**
*   **Spring Boot 3.3.3**
*   **Spring AI 1.0.1**
*   **Ollama:** Local large language model for chat capabilities.
*   **Spring Data JPA:** For database interaction.
*   **MySQL:** Relational database for storing user information.
*   **Lombok:** To reduce boilerplate code.
*   **Maven:** Build automation tool.

## Getting Started

### Prerequisites
*   Java 17
*   Maven
*   Docker (for MySQL and Ollama setup)

### Setup
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/huantran2605/chatbot.git
    cd chatbot
    ```

2.  **Database Setup (MySQL):**
    The project uses a `docker-compose.yml` file to set up a MySQL database.
    ```bash
    docker-compose up -d mysql
    ```
    Ensure your `application.yml` (or `application-local.yml`/`application-dev.yml`) has the correct database configuration.

3.  **Ollama Setup:**
    You can run Ollama locally using Docker:
    ```bash
    docker-compose up -d ollama
    ```
    Once Ollama is running, pull a model (e.g., `llama2`):
    ```bash
    docker exec -it ollama ollama pull llama2
    ```
    Ensure your `application.yml` (or profile-specific configuration) points to the correct Ollama endpoint.

4.  **Build the project:**
    ```bash
    mvn clean install
    ```

5.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080` by default.

## API Endpoints

### Chat Endpoint
*   **URL:** `/chat`
*   **Method:** `GET`
*   **Parameters:**
    *   `query` (String, required): The user's message or question for the chatbot.
*   **Example:**
    ```
    http://localhost:8080/chat?query=Hello%2C%20what%20can%20you%20do%3F
    ```
*   **Response:** A string containing the chatbot's response.

## MCP Tools

This application exposes the following MCP tools for user management:

### `getUserByUsername`
*   **Description:** Looks up a user by their username and returns their profile fields.
*   **Parameters:**
    *   `username` (String, required): The username to look up.
*   **Returns:** A map containing `username`, `fullName`, `email`, `role`, or an error if the user is not found.

### `addUser`
*   **Description:** Adds a new user to the system with a unique username, full name, email, and role.
*   **Parameters:**
    *   `username` (String, required): The unique username for the new user.
    *   `fullName` (String, required): The full name of the new user.
    *   `email` (String, required): The email address of the new user.
    *   `role` (String, required): The role of the new user (e.g., 'user', 'admin').
*   **Returns:** A map containing the created user's details (`username`, `fullName`, `email`, `role`, `status: USER_CREATED`), or an error if the username already exists or required fields are missing.
