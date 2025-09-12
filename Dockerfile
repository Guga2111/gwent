# oficial java image (temurin)
FROM maven:3.9-eclipse-temurin-17 AS builder

# define the directory
WORKDIR /app

# copy poms before downloading the dependencies
COPY pom.xml .
COPY gwent-engine/pom.xml ./gwent-engine/
COPY gwent-api-web/pom.xml ./gwent-api-web/

# download the dependecies
RUN mvn dependency:go-offline -B

# copy all the source code
COPY . .

# compile the engine module skipping tests
RUN mvn -pl gwent-engine clean package -DskipTests

# light image jre
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# copies the .jar from the previous steps
COPY --from=builder /app/gwent-engine/target/gwent-engine-1.0.0-SNAPSHOT.jar app.jar

# commando for running the class
CMD ["java", "-jar", "app.jar"]