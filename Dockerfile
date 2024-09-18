FROM openjdk:17-jdk

WORKDIR /app

COPY target/fullstack-facebook-clone-1.0.jar /app/fullstack-facebook-clone.jar

EXPOSE 8080

CMD ["java", "-jar", "fullstack-facebook-clone.jar"]
