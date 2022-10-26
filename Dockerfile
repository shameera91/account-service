FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 8080
ADD build/libs/account-service-0.0.1-SNAPSHOT.jar account-service.jar
ENTRYPOINT ["java","-jar","account-service.jar"]