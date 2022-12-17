# cgm-tech-test Project

Even though I've never used it before, this project uses Quarkus.

I think I've implemented everything that was requestes.

It would be nice for me to Dockerize the application, as it seems pretty easy to do, but unfortunately I'm facing some issue with maven and some dependencies.


## Attention Notes
- At the startup of the application there won't be any patient preloaded into the database, but I think it would be possible to solve the problem using the `quarkus.hibernate-orm.sql-load-script` property and providing a SQL script containing all the initial imports.
- In order to test the data persistence, I've used a Docker container running a clean PostgresSQL 14 installation on my machine.
Obviously, the relative credentials should be stored in a more secure way, instead of putting them into the `application.properties` file, but I thought that for the purpose of the test it would have been ok.
- My "nice-to-have" was to Dockerize the finished application, and then run it using a docker-compose, with also a container for the database. 
- I implemented the unit test using Quarkus, and tested every single endpoint.
- I also wanted to implement a basic UI, but I've run out of time and I have to send you the solution. By the way, I noticed that it would be possible to implement a UI using Quarkus itself. 

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.
