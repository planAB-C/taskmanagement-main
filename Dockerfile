FROM java:8
ADD huawei-taskmanagement-consumer8081-1.0-SNAPSHOT.jar .
EXPOSE 8081
ENTRYPOINT ["java","-jar","-Xmx256m","huawei-taskmanagement-consumer8081-1.0-SNAPSHOT.jar"]