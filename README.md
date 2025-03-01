# Spring Microservices E-Commerce Platform

## Overview
This project is a microservices-based e-commerce platform built using Spring Boot. The system allows users to browse and purchase products, pay using Stripe, and receive invoices via email.

## Microservices
- **Order Service** – Manages orders and order processing.
- **Product Service** – Handles product listings and inventory.
- **Cart Service** – Manages user shopping carts.
- **Notification Service** – Sends email notifications and invoices.

## Technologies Used
- **Backend:** Spring Boot, Spring Web, Spring Security
- **Authentication & Authorization:** Keycloak
- **Database:** PostgreSQL, MySQL, MongoDB
- **Database Migrations:** Liquibase, Flyway
- **Messaging & Resilience:** RabbitMQ, Resilience4j
- **External Services:** AWS S3 (file storage), AWS SES (email service), Stripe (payment processing)
- **API Communication:** WebClient

## Frontend
- **Technology:** Angular (in progress)

## Features
- Product browsing and cart management
- Secure authentication and authorization (Keycloak)
- Payment processing with Stripe
- Order creation and tracking
- Email notifications with invoices
- Resilient and fault-tolerant architecture
