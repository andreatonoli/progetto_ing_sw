# Prova Finale Ingegneria del Software 2023 - 2024

![Codex Naturalis](https://github.com/rivaPolimi/ing-sw-2024-riva-pieruz-sartore-tonoli/blob/master/src/main/resources/images/background.png)

Implementation of the board game [Codex Naturalis](https://www.craniocreations.it/prodotto/codex-naturalis). The project consists in implementing a single-server system capable of handling multiple games made up of 2 to 4 clients.
Each player could play using a Text User Interface (TUI) or a Graphical User Interface (GUI). They can also choose the type of their connection between socket and Remote Method Interface (RMI).

## Group Members

- Riva Stefano
- Tonoli Andrea
- Sartore Edoardo
- Pieruz Dora Francesca

## Documentation

### UML
- [High-Level UML]()
- [Low-Level UML]()

### Javadoc

The following documentation includes a description for most of the classes and methods used, follows the Java documentation techniques, and can be accessed by generating it from code or here [JavaDoc]().

### JUnit Testing

All the components have been tested using [JUnit](https://junit.org/junit5/) for the model, with the help of [Mockito](https://site.mockito.org/) to test the controller. Here are the [results](https://github.com/rivaPolimi/ing-sw-2024-riva-pieruz-sartore-tonoli/blob/network/deliverables/Test_Results.png).

## Implemented Features

|  Requirement   | Implemented |
| -------------- | ----------- |
| Complete Rules | :white_check_mark: |
| RMI | :white_check_mark: |
| Socket | :white_check_mark: |
| TUI | :white_check_mark: |
| GUI | :white_check_mark: |
| Multiple Games ("Partite Multiple") | :white_check_mark: |
| Chat ("Chat") | :white_check_mark: |
| Resilience to Disconnection ("Resilienza alle disconnessioni") | :white_check_mark: |

## Jars

The following [jars]() were used for the delivery of the project, therefore allowing the launch of the game according to the functionality described in the introduction.

## JAR Execution

The project is deployed with two jars, one to run the client and one to run the server.

### Prerequisites

To run this project you need JDK 21 or above.
If you want to play with TUI, it's highly recommended to use a terminal which support UTF-8 encoding and ANSI escape sequences, for example the latest version of Windows Terminal. The recommended size for the console is 50x150.
To play with GUI there are no specific requirements.

### Server

The server can be run with the following command:

    java -jar CodexNaturalis-server.jar

### Client

The client can be run with the following command:

    java -jar CodexNaturalis-client.jar

When run this program will ask the user if they want to play using TUI or GUI.

### Another way to run

In case you want to build the project you need to install [Maven](https://maven.apache.org/) and [JDK 21](https://www.oracle.com/it/java/technologies/downloads/#java21).

## Library and Plugins

| Library\Plugin | Description |
|----------------|-------------|
| Maven | Build automation tool used primarily for Java projects |
| JavaFX | Graphic library to create user interfaces |
| JUnit | Unit testing framework |
| Mockito | Mocking framework (used for controller test) |
| Gson | JSON parser |
| Jansi | Using ANSI escape codes on Windows |
