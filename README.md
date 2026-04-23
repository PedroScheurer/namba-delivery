# 🍔 Namba Delivery - Microservices System (DDD + Event-Driven)

Sistema de delivery baseado em arquitetura de microserviços utilizando Spring Boot, Spring Cloud, Apache Kafka e Docker. O projeto segue princípios de Domain-Driven Design (DDD) e Event-Driven Architecture.

---

## 🧠 Visão geral arquitetural

O sistema é dividido em bounded contexts independentes:

* 🚚 Courier Management (gestão de entregadores)
* 📦 Delivery Tracking (gestão de entregas)
* 🌐 API Gateway (entrada do sistema)
* 📡 Service Registry (descoberta de serviços)

---

## 🧱 Context Map (DDD)

```
                ┌──────────────────────┐
                │     API Gateway      │
                │     (Edge Layer)     │
                └─────────┬────────────┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼

┌────────────────┐ ┌────────────────┐ ┌────────────────────┐
│ Courier        │ │ Delivery       │ │ Service Registry   │
│ Management     │ │ Tracking       │ │ (Eureka Server)    │
│ (Core Domain)  │ │ (Core Domain)  │ │ (Infra Layer)      │
└────────────────┘ └────────────────┘ └────────────────────┘
        ▲                 ▲
        │                 │
        └──── Kafka ──────┘
   (Event-Driven Communication)
```

---

## 🚚 Courier Management (Bounded Context)

### 📌 Responsabilidade

Gestão completa de entregadores.

### 🧠 Aggregate Root

* Courier

### 📦 Endpoints

```
POST   /api/v1/couriers
PUT    /api/v1/couriers/{id}
GET    /api/v1/couriers
GET    /api/v1/couriers/{id}
POST   /api/v1/couriers/payout-calculation
```

### 🧠 Application Services

* CourierRegistrationService
* CourierPayoutService

### 📡 Integração com Kafka

* Atua como **Consumer**
* Consome eventos relacionados a entregas para atualização de contexto

### 📌 Regras de negócio

* Cadastro e atualização de entregadores
* Cálculo de payout baseado em distância

---

## 📦 Delivery Tracking (Bounded Context)

### 📌 Responsabilidade

Gerenciar o ciclo de vida das entregas.

### 🧠 Aggregate Root

* Delivery

### 📦 Estados do domínio

```
DRAFT → PLACED → PICKED_UP → COMPLETED
```

### 📦 Endpoints

```
POST   /api/v1/deliveries
PUT    /api/v1/deliveries/{id}
GET    /api/v1/deliveries
GET    /api/v1/deliveries/{id}
POST   /api/v1/deliveries/{id}/placement
POST   /api/v1/deliveries/{id}/pickups
POST   /api/v1/deliveries/{id}/completion
```

### 🧠 Application Services

* DeliveryPreparationService
* DeliveryCheckpointService

### 📡 Integração com Kafka

* Atua como **Producer**
* Publica eventos de domínio:

  * DeliveryPlacedEvent
  * DeliveryPickedUpEvent
  * DeliveryCompletedEvent

### 📌 Regras de negócio

* Delivery só pode ser “picked up” se estiver “placed”
* Delivery só pode ser “completed” se estiver “picked up”
* Courier é referenciado por ID

---

## 🌐 API Gateway (Edge Layer)

### 📌 Responsabilidade

* Entrada única do sistema
* Roteamento de requisições
* Circuit Breaker

### 🛠 Tecnologias

* Spring Cloud Gateway (WebFlux)
* Eureka Client
* Resilience4j

---

## 📡 Service Registry (Infra Layer)

### 📌 Responsabilidade

* Descoberta de serviços

### 🛠 Tecnologia

* Eureka Server

---

## 📡 Comunicação entre serviços

### 🔵 Síncrona

* REST via API Gateway

### 🟡 Assíncrona (Kafka)

Fluxo principal:

```
Delivery Tracking (Producer)
        ↓
     Kafka
        ↓
Courier Management (Consumer)
```

---

## 🧠 DDD aplicado

* Bounded contexts isolados
* Aggregates bem definidos (Courier, Delivery)
* Comunicação desacoplada via eventos
* Referência entre domínios por ID
* Separação entre domínio, aplicação e infraestrutura

---

## 🐳 Infraestrutura (Docker)

### Serviços

* PostgreSQL (porta 5433)
* pgAdmin (http://localhost:8083)
* Kafka (porta 9092)
* Kafka UI (http://localhost:8084)

### Subir ambiente

```
docker-compose up -d
```

---

## 🗄 Banco de dados

* PostgreSQL
* User: postgres
* Password: postgres

---

## 📂 Estrutura do projeto

```
namba-delivery/
├── Microservices/
│   ├── Courier-Management/
│   ├── Delivery-Tracking/
│   ├── Gateway/
│   └── Service-Registry/
│
├── docker-compose.yml
└── README.md
```

---

## 🚀 Melhorias futuras

* Autenticação (OAuth2 / Keycloak)
* Observabilidade (Prometheus + Grafana)
* CI/CD pipeline
* Kubernetes

---

## 👨‍💻 Autor

Desenvolvido por **Pedro Scheurer** durante o intensivo da **Algaworks**.
