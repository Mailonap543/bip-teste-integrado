# Sistema de Beneficios - Desafio Fullstack Integrado

## Sobre o Projeto

Sistema completo de gestao de beneficios com arquitetura enterprise em camadas:

- **Banco de Dados**: PostgreSQL com schema relacional
- **Módulo EJB**: Servico Enterprise JavaBeans com transferencias seguras (pessimistic locking)
- **Backend**: API REST com Spring Boot 3.2.5, JWT Security, Swagger/OpenAPI
- **Frontend**: Aplicacao Angular 20 com componentes standalone, auth guards, interceptors
- **CI/CD**: GitHub Actions com build, test, Docker

## Arquitetura

```
bip-teste-integrado/
├── .github/workflows/       # CI/CD Pipeline (GitHub Actions)
├── backend-module/          # Spring Boot Backend
│   ├── src/main/java/com/example/backend/
│   │   ├── config/          # Security, Swagger, CORS, Exception Handler
│   │   ├── controller/      # REST Controllers (v1 API)
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities (Beneficio, Transference, Usuario)
│   │   ├── exception/       # Custom Exceptions
│   │   ├── repository/      # Spring Data Repositories
│   │   ├── security/        # JWT Provider, Auth Filter, UserDetailsService
│   │   └── service/         # Business Logic
│   └── src/test/            # Unit & Integration Tests
├── ejb-module/              # EJB Service Module
│   └── src/main/java/com/example/ejb/
├── frontend/                # Angular 20 Frontend
│   └── src/app/
│       ├── components/      # UI Components (home, login, beneficio-list, etc.)
│       ├── guards/          # Auth Guard
│       ├── interceptors/    # JWT Interceptor
│       ├── models/          # TypeScript Interfaces
│       └── service/         # HTTP Services
├── db/                      # Database Scripts (schema.sql, seed.sql)
├── docs/                    # Documentation
├── docker-compose.yml       # Docker Orchestration
└── README.md
```

## Pré-requisitos

- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+ (ou Docker)

## Configuração Rápida

### Opção 1: Docker (Recomendado)

```bash
docker-compose up -d
```

Acesse:
- Frontend: http://localhost:4200
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### Opção 2: Desenvolvimento Local

**1. Banco de Dados (H2 em memoria para dev):**
O backend usa H2 por default em desenvolvimento. Nenhuma configuracao extra necessaria.

**2. Backend:**
```bash
cd backend-module
mvn clean install
mvn spring-boot:run
```

**3. Frontend:**
```bash
cd frontend
npm install
npm start
```

## Autenticacao

O sistema usa JWT para autenticacao.

**Usuarios padrao (seed):**

| Usuario | Senha | Roles |
|---------|-------|-------|
| admin | admin123 | ROLE_ADMIN, ROLE_USER |
| user | user123 | ROLE_USER |

**Endpoints de Auth:**

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | /api/auth/login | Login | Nao |
| POST | /api/auth/register | Registro | Nao |
| GET | /api/auth/me | Usuario atual | Sim |

## API REST - Endpoints v1

### Beneficios (/api/v1/beneficios)

| Metodo | Endpoint | Descricao | Role |
|--------|----------|-----------|------|
| GET | /api/v1/beneficios | Listar todos | Publico |
| GET | /api/v1/beneficios/{id} | Obter por ID | Publico |
| POST | /api/v1/beneficios | Criar novo | USER, ADMIN |
| PUT | /api/v1/beneficios/{id} | Atualizar | USER, ADMIN |
| DELETE | /api/v1/beneficios/{id} | Remover | ADMIN |
| POST | /api/v1/beneficios/transfer/{from}/{to}/{valor} | Transferir | USER, ADMIN |

### Transferencias (/api/v1/transferencias)

| Metodo | Endpoint | Descricao | Role |
|--------|----------|-----------|------|
| POST | /api/v1/transferencias | Transferir (body JSON) | USER, ADMIN |
| GET | /api/v1/transferencias/teste | Health check | Publico |

### Swagger/OpenAPI

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Monitoramento (Actuator)

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

## Seguranca

- **JWT**: Tokens stateless com expiracao configuravel
- **Spring Security**: Role-based access control (RBAC)
- **BCrypt**: Senhas criptografadas
- **CORS**: Configurado para localhost:4200
- **Validacao**: Jakarta Validation nos DTOs
- **Exception Handler**: Respostas padronizadas de erro

## Testes

### Backend
```bash
cd backend-module
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

## CI/CD

Pipeline GitHub Actions (.github/workflows/ci.yml):
1. Backend build + test (Java 17, Maven)
2. Frontend build + test (Node 20, npm)
3. Docker build (apenas em main)

## Tecnologias

| Camada | Tecnologia | Versao |
|--------|-----------|--------|
| Frontend | Angular | 20.x |
| Frontend HTTP | HttpClient + RxJS | - |
| Frontend Auth | JWT Interceptor + Guard | - |
| Backend | Spring Boot | 3.2.5 |
| Backend Security | Spring Security + JWT | - |
| Backend API | Spring Web + Swagger | - |
| EJB | Jakarta EJB + JPA | - |
| Database | PostgreSQL / H2 | 15 / - |
| Build | Maven / npm | - |
| CI/CD | GitHub Actions | - |
| Container | Docker + Docker Compose | - |

## Variaveis de Ambiente

| Variavel | Default | Descricao |
|----------|---------|-----------|
| SPRING_PROFILES_ACTIVE | dev | Perfil (dev/prod) |
| SPRING_DATASOURCE_URL | jdbc:h2:mem:bipdb | URL do banco |
| SPRING_DATASOURCE_USERNAME | sa | Usuario do banco |
| SPRING_DATASOURCE_PASSWORD | - | Senha do banco |
| JWT_SECRET | (see application.yml) | Chave secreta JWT |
| JWT_ACCESS_TOKEN_VALIDITY | 3600000 | Validade access token (ms) |
| JWT_REFRESH_TOKEN_VALIDITY | 86400000 | Validade refresh token (ms) |
| SERVER_PORT | 8080 | Porta do servidor |

## Licenca

Projeto desenvolvido para fins de avaliacao tecnica.
