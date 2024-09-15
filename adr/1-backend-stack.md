# Backend Stack - Kotlin and Spring Boot

* Status: accepted <!-- optional -->
* Deciders: Dennis <!-- optional -->
* Date: 2024-04-23 <!-- optional -->

Technical Story: [Kernfunktion im Backend erstellen](https://github.com/mi-classroom/mi-master-wt-beiboot-2024/issues/1) <!-- optional -->

## Context and Problem Statement

Which technology should be used to create the application backend? The Backend needs some kind of REST API. Implementing those should be done with a framework or library, to avoid security vulnerabilities and reduce programming overhead.

## Decision Drivers

* Features
* Familiarity with Technology
* Documentation
* Community support

## Considered Options

* Kotlin + Micronaut
* Kotlin + Spring

## Decision Outcome

Chosen option: "Kotlin + Spring", because it is already a familiar technology stack and time is limited this semester, which is a K.O criterion. Both Frameworks have a similar feature set e.g.

* Rest: [Spring](https://spring.io/guides/gs/rest-service), [Micronaut](https://micronaut.io/)
* Server Sent Event: [Spring](https://www.baeldung.com/spring-server-sent-events), [Micronaut](https://blogs.oracle.com/javamagazine/post/html5-server-sent-events-with-micronautio-and-java)

Have extensive documentation and community support.

### Positive Consequences
- Fast development times, due to framework familiarity
- Access to extensive documentation

### Negative Consequences
- No new technology being learned