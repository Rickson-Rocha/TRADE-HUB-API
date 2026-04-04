# TradeHub API

API REST de um marketplace de produtos digitais construída com Spring Boot, Spring Security e RabbitMQ.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway
- Spring HATEOAS
- RabbitMQ
- Docker + Docker Compose
- Lombok

## Arquitetura

O projeto segue arquitetura de **monolito modular** — um único deploy com módulos bem separados por domínio. Cada módulo tem seu próprio `controller`, `service`, `repository`, `entity` e `dto`. Um módulo nunca acessa o `repository` de outro diretamente — apenas os `services` públicos.

```
src/main/java/com/br/tradehub/tradehub_api/
├── auth/
│   ├── config/
│   ├── controllers/
│   ├── dto/
│   ├── entity/
│   │   ├── User.java
│   │   └── RefreshToken.java
│   ├── enums/
│   │   ├── Role.java
│   │   └── Status.java
│   ├── filter/
│   ├── repository/
│   └── service/
├── products/
│   ├── controllers/
│   ├── dto/
│   ├── entity/
│   │   └── Product.java
│   ├── enums/
│   │   ├── ProductCategory.java
│   │   └── ProductStatus.java
│   ├── repository/
│   └── service/
├── orders/
│   ├── controllers/
│   ├── dto/
│   ├── entity/
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── repository/
│   └── service/
├── payments/
│   ├── controllers/
│   ├── dto/
│   ├── entity/
│   │   └── Payment.java
│   ├── repository/
│   └── service/
├── notifications/
│   ├── dto/
│   ├── entity/
│   │   └── Notification.java
│   ├── enums/
│   │   └── NotificationType.java
│   ├── repository/
│   └── service/
├── access/
│   ├── dto/
│   ├── entity/
│   │   └── AccessGrant.java
│   ├── repository/
│   └── service/
├── outbox/
│   ├── entity/
│   │   └── OutboxEvent.java
│   ├── repository/
│   └── service/
└── shared/
    ├── dto/
    └── exception/
```

## Módulos

### Auth
Responsável por autenticação e autorização. Fluxo baseado em JWT com `accessToken` de curta duração e `refreshToken` persistido no banco, revogável no logout.

**Roles:**
- `CUSTOMER` — comprador, role padrão no cadastro
- `SELLER` — vendedor de produtos digitais
- `ADMIN` — administrador da plataforma

### Products
Gestão de produtos digitais criados por sellers.

### Orders
Criação e acompanhamento de pedidos realizados por customers.

### Payments
Processamento de pagamentos vinculados aos pedidos.

### Notifications
Registro e envio de notificações por email geradas pelos eventos do sistema.

### Access
Concessão e revogação de acesso aos produtos após pagamento aprovado.

### Outbox
Garante a publicação confiável de eventos no RabbitMQ usando o padrão Outbox — eventos são salvos no banco na mesma transação do pedido e publicados por um job separado.

## RabbitMQ

O RabbitMQ é utilizado para desacoplar o processamento de pedidos. Quando um pedido é criado, eventos são publicados em filas — cada serviço (pagamento, acesso, notificação) consome de forma independente e no seu próprio ritmo. Isso garante que uma falha num serviço não afete os demais e que o cliente receba a resposta sem esperar o processamento completo.

## Como rodar

**Pré-requisitos:** Docker, Java 21, Maven

**1. Suba o banco de dados:**
```bash
docker compose up -d
```

**2. Configure o `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tradehub
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.locations=classpath:db/migration

jwt.secret=sua-chave-secreta
jwt.access-token-expiration=3600000
jwt.refresh-token-expiration=604800000
```

**3. Rode a aplicação:**
```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Banco de dados

As tabelas são gerenciadas pelo Flyway — o Hibernate nunca cria tabelas diretamente.

| Tabela | Descrição |
|--------|-----------|
| `tb_users` | Usuários da plataforma |
| `tb_refresh_tokens` | Tokens de refresh para renovação de sessão |
| `tb_products` | Produtos digitais cadastrados por sellers |
| `tb_orders` | Pedidos realizados por customers |
| `tb_order_items` | Itens de cada pedido |
| `tb_payments` | Pagamentos vinculados aos pedidos |
| `tb_access_grants` | Concessões de acesso após pagamento aprovado |
| `tb_notifications` | Notificações geradas pelo sistema |
| `tb_outbox_events` | Eventos pendentes de publicação no RabbitMQ |

## Segurança

- Senhas armazenadas com hash BCrypt
- Autenticação stateless via JWT
- `accessToken` de curta duração (1h)
- `refreshToken` persistido no banco, revogável no logout
- Rotas protegidas por roles via Spring Security