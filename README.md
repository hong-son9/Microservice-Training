# Shoes Microservices — Training Project

Hệ thống microservices bán giày, refactor từ monolith [Shoes](file:///C:/Users/SonPH/Downloads/Shoes2/Shoes/Shoes) sang kiến trúc microservices.

## 1. Phân tích domain

Domain "Shoes E-commerce" được tách thành các bounded context:

| Bounded Context | Trách nhiệm | Aggregates chính |
|---|---|---|
| **Identity** | Xác thực, phân quyền, profile user | User, Role, RefreshToken |
| **Product Catalog** | Quản lý sản phẩm, danh mục, nhãn hiệu, kho | Product, Brand, Category, ProductSize, Image |
| **Cart** | Giỏ hàng cá nhân | Cart, CartItem |
| **Order** | Đặt hàng, lịch sử, thống kê | Order, OrderItem, OrderStatusHistory |
| **Promotion** | Mã giảm giá, voucher | Promotion, PromotionUsage |

## 2. Sơ đồ kiến trúc

```
                       ┌──────────────────┐
   Client (web/mobile) │   API Gateway    │ :8080
        ──────────────►│ (Spring Cloud GW)│
                       │  + JWT validate  │
                       └────────┬─────────┘
                                │
        ┌───────────────────────┼────────────────────────┐
        │                       │                        │
        ▼                       ▼                        ▼
┌──────────────┐    ┌──────────────┐         ┌──────────────┐
│  identity    │    │   product    │   ...   │   order      │
│  :8081       │    │   :8082      │         │   :8084      │
└──────┬───────┘    └──────┬───────┘         └──────┬───────┘
       │                   │                        │
       ▼                   ▼                        ▼
  identity_db         product_db               order_db
                                                    │
              ┌─────────────────────┐               │
              │  Eureka Discovery   │◄──────────────┘
              │  :8761              │  (all services register)
              └─────────────────────┘
```

## 3. Service & port

| Service | Port | Database schema |
|---|---|---|
| discovery-server | 8761 | — |
| api-gateway | 8080 | — |
| identity-service | 8081 | `identity_db` |
| product-service | 8082 | `product_db` |
| cart-service | 8083 | `cart_db` |
| order-service | 8084 | `order_db` |
| promotion-service | 8085 | `promotion_db` |

## 4. Tech stack

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Cloud 2023.0.3**
- **Spring Data JPA + MySQL 8**
- **Spring Cloud Netflix Eureka** (service discovery)
- **Spring Cloud Gateway** (routing + JWT validation)
- **OpenFeign** (inter-service HTTP calls)
- **JJWT 0.12.6** (JWT issuing + parsing)
- **Lombok**

## 5. Cách chạy

### Yêu cầu
- JDK 17+
- Maven 3.8+
- MySQL 8 chạy trên `localhost:3306`, user `root`, password `12345` (chỉnh trong `application.yml` từng service nếu khác)

### Bước 1: Tạo DB schemas
Chạy file `db/init.sql` trong MySQL Workbench hoặc CLI:
```bash
mysql -u root -p < db/init.sql
```

### Bước 2: Build all
```bash
cd C:\MicroservideTrainng
mvn clean install -DskipTests
```

### Bước 3: Run từng service theo thứ tự

**Quan trọng**: chạy `discovery-server` TRƯỚC, đợi nó sẵn sàng rồi chạy các service khác.

```bash
# Terminal 1 — Eureka
cd discovery-server
mvn spring-boot:run

# Terminal 2 — Gateway (sau khi Eureka up)
cd api-gateway
mvn spring-boot:run

# Terminal 3-7 — Business services
cd identity-service     && mvn spring-boot:run
cd product-service      && mvn spring-boot:run
cd cart-service         && mvn spring-boot:run
cd order-service        && mvn spring-boot:run
cd promotion-service    && mvn spring-boot:run
```

### Bước 4: Kiểm tra

- Eureka dashboard: <http://localhost:8761> — phải thấy tất cả service đã register
- Gateway health: <http://localhost:8080/actuator/health>
- Test ping qua gateway:
  - <http://localhost:8080/api/products/ping>
  - <http://localhost:8080/api/identity/ping>
  - <http://localhost:8080/api/cart/ping>
  - <http://localhost:8080/api/orders/ping>
  - <http://localhost:8080/api/promotions/ping>

## 6. Roadmap

- [x] **Step 1**: Phân tích domain + bounded context
- [x] **Step 2**: Tạo skeleton 7 services + Eureka + Gateway
- [ ] **Step 3**: ERD + entity cho từng service
- [ ] **Step 4**: CRUD + repository + service + controller cho từng service
- [ ] **Step 5**: Feign client giữa các service (cart → product, order → product/promotion)
- [ ] **Step 6**: JWT authentication (identity issue token, gateway validate, services dùng token claims)
- [ ] **Step 7**: Saga / event-driven cho transaction (Kafka — optional)
- [ ] **Step 8**: Centralized config (Spring Cloud Config — optional)
- [ ] **Step 9**: Distributed tracing (Sleuth + Zipkin — optional)
- [ ] **Step 10**: Docker Compose cho cả hệ thống

## 7. Quy ước package

Tất cả service dùng prefix `com.shoes.<service-name>`:

```
com.shoes.identity.entity
com.shoes.identity.repository
com.shoes.identity.service
com.shoes.identity.controller
com.shoes.identity.dto
com.shoes.identity.config
com.shoes.identity.client      (Feign clients gọi service khác)
```
