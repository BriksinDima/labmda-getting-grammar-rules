# GetRulesFunction – AWS Lambda (Micronaut + DynamoDB)

## Overview
**GetRulesFunction** is an AWS Lambda function written in Java (Micronaut).  
It provides an HTTP API for retrieving English grammar rules stored in **Amazon DynamoDB**.  
The project is deployed using **AWS SAM**.

---

## Architecture
- **API Gateway** – entry point for HTTP requests.
- **AWS Lambda (Micronaut)** – processes requests.
- **Amazon DynamoDB** – stores grammar rules.

---

## Prerequisites
- **JDK 21**
- **Maven 3.9+**
- **AWS SAM CLI**
- **Docker**
- **AWS CLI** with configured profile

---

## Local Development
Build and run locally:

```bash
mvn clean package
sam local start-api