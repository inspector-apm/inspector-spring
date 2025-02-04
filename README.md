# Code Execution Monitoring for Spring Boot applications
 
 > Before moving on, please consider giving us a GitHub star ⭐️. Thank you!

## Requirements

- Spring Boot 3.x


## Install

Add the package to your application's dependencies list in the `pom.xml` file:

```xml
<dependency>
    <groupId>dev.inspector</groupId>
    <artifactId>spring</artifactId>
    <version>[1.0.4,2.0.0)</version>
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
## Outgoing HTTP calls

Currently we only support outgoing http monitoring for RestTemplate http client.

To enable http monitoring for RestTemplate you have to manually add `RestTemplateMonitoringInterceptor` as an interceptor when creating your RestTemplate bean.
Check the example in the snippet below:

```java
       @Autowired
       private RestTemplateMonitoringInterceptor restTemplateInterceptor;

       @Bean
       public RestTemplate restTemplate(RestTemplateBuilder builder) {
           return builder
               .interceptors(restTemplateInterceptor)
               .build();
       }
```

Other http clients will be supported soon!!

## Test & Deploy

Run an HTTP request against your application (or navigate it with a browser) to see the first data flowing into the [Inspector dashboard](https://app.inspector.dev).
By default Inspector monitors:

- HTTP requests
- Scheduled Tasks
- Database Queries
- Outgoing HTTP calls

Once verified that everything works and your application is connected, you can deploy the integration into the production environment.

## Official documentation

[Check out the Official Documentation](https://docs.inspector.dev/guides/spring-boot)

## Important notes

### Database queries

At the moment only JDBC calls are being monitored. This means that interactions with relational databases are monitored,
whether they are done via Spring Data or via low level JDBC API.

If you're using this library and not using Spring Data in your project please add the following line in your 
`application.properties` to disable Spring Boot's auto-configuration attempt of data source beans:

```
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

---

> If you need further support write an email to [support@inspector.dev](mailto:support@inspector.dev), or drop in a live chat directly from your dashboard.
