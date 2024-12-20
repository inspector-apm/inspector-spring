﻿# Code Execution Monitoring for Spring Boot applications
 
 > Before moving on, please consider giving us a GitHub star ⭐️. Thank you!

 ## Spring 2
 Use the version 0.x for Spring Boot v2.

Add the package to your application's dependencies list in the `pom.xml` file:

```xml
<dependency>
    <groupId>dev.inspector</groupId>
    <artifactId>spring</artifactId>
    <version>0.1.0</version>
</dependency>
```

 ## Spring 3
 Use the version 1.x for Spring Boot v3.

Add the package to your application's dependencies list in the `pom.xml` file:

```xml
<dependency>
    <groupId>dev.inspector</groupId>
    <artifactId>spring</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Reload the maven dependencies:
Run the command below to update application dependencies.

```
mvn clean install
```

## Configure the ingestion key

Add the following configuration to the `application.properties` file:

```properties
inspector.ingestion-key=81e6d4df93xxxxxxxxxxxxxxxxxxxxxxxxxx
```

## Test & Deploy

Run an HTTP request against your application (or navigate it with a browser) to see the first data flowing into the Inspector dashboard.
By default Inspector monitors:

- HTTP requests
- Scheduled Tasks
- Database Queries
- Outgoing HTTP calls

Once verified that everything works and your application is connected, you can deploy the integration into the production environment.

> Use the live chat in your dashboard if you have any questions.
