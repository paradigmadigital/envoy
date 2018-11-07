FROM openjdk:9

ENV CONFIG_PATH /temp/config/application.yml

COPY ./build/libs/envoy-sds-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 8080

CMD java -Dspring.config.location=$CONFIG_PATH -jar application.jar