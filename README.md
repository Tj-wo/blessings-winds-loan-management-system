# Alganiug Loan Management System

A Maven, Java, Spring, Hibernate and JSF/XHTML implementation of the loan-management prototype. The project uses the `javax.*` APIs.

## Project structure

```text
loan-management-system/
+-- backend/
¦   +-- loan-management-models/
¦   +-- loan-management-services/
+-- frontend/
    +-- loan-management-views/
    +-- loan-management-web/
```

Dependency direction:

```text
loan-management-web -> loan-management-views -> loan-management-services -> loan-management-models
```

The Java package root is `com.alganiug.systems.loanManagement`. No Pahappa/company-specific packages or private dependencies are used.

## Build

```bash
mvn clean verify
```
