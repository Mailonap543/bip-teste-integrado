# Sistema de Benefícios - Desafio Fullstack Integrado

## Sobre o Projeto

Sistema completo de gestão de benefícios com arquitetura em camadas:
- **Banco de Dados**: PostgreSQL com schema relacional
- **Módulo EJB**: Serviço Enterprise JavaBeans com transferências seguras
- **Backend**: API REST com Spring Boot 3.2.5
- **Frontend**: Aplicação Angular 20 com componentes standalone

## Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                   Frontend (Angular)                │
│  ┌──────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Home    │  │  Benefícios  │  │ Transferência│  │
│  └──────────┘  └──────────────┘  └──────────────┘  │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP/REST
┌──────────────────────┴──────────────────────────────┐
│              Backend (Spring Boot)                  │
│  ┌────────────┐ ┌────────────┐ ┌────────────────┐  │
│  │ Controller │ │  Service   │ │  Repository    │  │
│  └────────────┘ └────────────┘ └────────────────┘  │
│  ┌────────────┐ ┌────────────┐ ┌────────────────┐  │
│  │  Entity    │ │    DTO     │ │   Exception    │  │
│  └────────────┘ └────────────┘ └────────────────┘  │
└──────────────────────┬──────────────────────────────┘
                       │ JPA/Hibernate
┌──────────────────────┴──────────────────────────────┐
│              Módulo EJB                             │
│  ┌──────────────────────────────────────────────┐   │
│  │  BeneficioEjbService (Pessimistic Locking)   │   │
│  └──────────────────────────────────────────────┘   │
└──────────────────────┬──────────────────────────────┘
                       │ JDBC
┌──────────────────────┴──────────────────────────────┐
│              PostgreSQL Database                    │
│  ┌──────────┐  ┌──────────┐  ┌────────────────┐    │
│  │ BENEFICIO│  │  CONTA   │  │ TRANSFERENCIA  │    │
│  └──────────┘  └──────────┘  └────────────────┘    │
└─────────────────────────────────────────────────────┘
```

## Pré-requisitos

- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+ (ou Docker)
- Angular CLI 20

## Configuração do Banco de Dados

### Opção 1: Docker (Recomendado)

```bash
docker-compose up -d
```

### Opção 2: PostgreSQL Local

1. Criar o banco de dados:
```sql
CREATE DATABASE bipdb;
```

2. Executar os scripts:
```bash
psql -U postgres -d bipdb -f db/schema.sql
psql -U postgres -d bipdb -f db/seed.sql
```

## Executando a Aplicação

### Backend (Spring Boot)

```bash
cd backend-module
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em: http://localhost:8080

### Frontend (Angular)

```bash
cd frontend
npm install
npm start
```

O frontend estará disponível em: http://localhost:4200

## API REST - Endpoints

### Benefícios (`/api/beneficios`)

| Método | Endpoint                          | Descrição            |
|--------|-----------------------------------|----------------------|
| GET    | `/api/beneficios`                 | Listar todos         |
| GET    | `/api/beneficios/{id}`            | Obter por ID         |
| POST   | `/api/beneficios`                 | Criar novo           |
| PUT    | `/api/beneficios/{id}`            | Atualizar            |
| DELETE | `/api/beneficios/{id}`            | Remover              |
| POST   | `/api/beneficios/transfer/{from}/{to}/{valor}` | Transferir |

### Transferências (`/api/transferencias`)

| Método | Endpoint                  | Descrição                    |
|--------|---------------------------|------------------------------|
| POST   | `/api/transferencias`     | Realizar transferência       |
| GET    | `/api/transferencias/teste` | Health check               |

### Swagger/OpenAPI

Documentação interativa disponível em:
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## Estrutura do Projeto

```
bip-teste-integrado/
├── backend-module/          # Spring Boot Backend
│   ├── src/main/java/com/example/backend/
│   │   ├── config/          # CORS, Swagger, Exception Handler
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities
│   │   ├── exception/       # Custom Exceptions
│   │   ├── repository/      # Spring Data Repositories
│   │   └── service/         # Business Logic
│   └── src/test/            # Unit & Integration Tests
├── ejb-module/              # EJB Service Module
│   └── src/main/java/com/example/ejb/
│       ├── entity/          # EJB Entities
│       └── BeneficioEjbService.java
├── frontend/                # Angular Frontend
│   └── src/app/
│       ├── components/      # UI Components
│       ├── models/          # TypeScript Interfaces
│       ├── service/         # HTTP Services
│       └── app.routes.ts    # Route Configuration
├── db/                      # Database Scripts
│   ├── schema.sql           # Table Definitions
│   └── seed.sql             # Test Data
├── docs/                    # Documentation
├── docker-compose.yml       # Docker Configuration
└── README.md
```

## Correção do Bug EJB

O módulo EJB original continha um bug crítico na transferência:
- **Problema**: Não verificava saldo antes de transferir, sem locking
- **Solução**: Implementada verificação de saldo + `LockModeType.PESSIMISTIC_WRITE`

Detalhes da correção em `ejb-module/src/main/java/com/example/ejb/BeneficioEjbService.java`:
- Verificação de saldo insuficiente
- Pessimistic Locking para consistência concorrente
- Validação de benefícios ativos
- Tratamento de erros com mensagens descritivas

## Testes

### Backend

```bash
cd backend-module
mvn test
```

Cobertura de testes inclui:
- Unitários para Services (CRUD + Transferência)
- Unitários para Controllers
- Cenários de erro (saldo insuficiente, IDs inválidos)

### Frontend

```bash
cd frontend
npm test
```

## Tecnologias

| Camada      | Tecnologia                     |
|-------------|--------------------------------|
| Frontend    | Angular 20, TypeScript, RxJS   |
| Backend     | Spring Boot 3.2.5, Java 17     |
| EJB         | Jakarta EJB, JPA, Hibernate    |
| Database    | PostgreSQL, H2 (testes)        |
| Build       | Maven, npm                     |
| CI/CD       | GitHub Actions                 |
| Qualidade   | Qodana                         |

## Licença

Projeto desenvolvido para fins de avaliação técnica.
