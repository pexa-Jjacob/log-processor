# Log Processor

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 8 or higher
- Maven

### Installing

Clone the repository:

```bash
git clone https://github.com/pexa-Jjacob/logprocessor.git
```

Navigate to the project directory in your local terminal or IDE:

```bash
cd log-processor
```

Build the project:

```bash
mvn clean install
```

## Running the program

To run the tests, use the following command:

```bash
mvn  test
```

to run the program, use the following command:

```bash 
mvn -q exec:java
```

## Usage

The application parses log files and processes the IP addresses and URLs. It also provides the top three most frequent entries.

## Built With

- [Java](https://www.java.com/) - The main programming language used
- [Maven](https://maven.apache.org/) - A build automation tool used primarily for Java projects
- [JUnit 5 (Jupiter)](https://junit.org/junit5/) - A unit testing framework for Java applications
- [Mockito](https://site.mockito.org/) - A mocking framework for unit tests in Java
- [AssertJ](https://assertj.github.io/doc/) - A library providing rich assertions for writing tests in Java
- [Project Lombok](https://projectlombok.org/) - A Java library that automatically plugs into your editor and build tools, spicing up your Java
- [SLF4J with Logback](http://www.slf4j.org/) - SLF4J serves as a simple facade or abstraction for various logging frameworks, in this case, Logback is used as the logging framework

## Authors

- **Jacob Clement** 
