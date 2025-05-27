FROM openjdk:21-slim

RUN apt-get update && apt-get install -y unzip

WORKDIR /app

COPY . .

ENV LANG C.UTF-8

RUN chmod +x ./gradlew

CMD ["./gradlew", "test", "jacocoTestReport"]
