# My OCI Redis Java Project

This project demonstrates how to generate an authentication token for Oracle Cloud Infrastructure (OCI) Redis and perform basic insertion and reading operations using Java.

## Project Structure

```
my-oci-redis-java
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── ociredis
│   │   │               ├── App.java
│   │   │               ├── RedisAuth.java
│   │   │               └── RedisClient.java
│   │   └── resources
│   │       └── config.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── ociredis
│                       └── AppTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed. You can check your Java version with:
   ```bash
   java -version
   ```

2. **Apache Maven**: Install Maven for dependency management and building the project. Verify the installation with:
   ```bash
   mvn -version
   ```

3. **OCI Configuration**: Set up your OCI configuration file with the necessary credentials. This file should be located at the path specified in `src/main/resources/config.properties`.

4. **Dependencies**: The project uses the OCI SDK and Jedis library. These dependencies are specified in the `pom.xml` file.

5. **Network Access**: Ensure that your environment has network access to the OCI Redis service.

## Running the Application

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd my-oci-redis-java
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.ociredis.App"
   ```

## Testing

Unit tests are included in the project to verify the functionality of the application. You can run the tests using:
```bash
mvn test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.