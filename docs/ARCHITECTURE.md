# 🏗️ RedBus - System Architecture Documentation

## Table of Contents
- [System Overview](#system-overview)
- [High-Level Architecture](#high-level-architecture)
- [Component Design](#component-design)
- [Data Flow](#data-flow)
- [Security Architecture](#security-architecture)
- [Database Design](#database-design)
- [API Design](#api-design)
- [Scalability Considerations](#scalability-considerations)

---

## System Overview

RedBus is a microservices-ready monolithic application built for high performance, scalability, and maintainability. The system handles:

- **50,000+ concurrent users** (design goal)
- **Real-time seat availability** updates
- **Sub-second search** response times
- **99.9% uptime** SLA

### Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Application | Spring Boot 3.2 | Core framework |
| Security | Spring Security + JWT | Authentication/Authorization |
| Database | PostgreSQL 15 | Primary data store |
| Cache | Redis 7 | Session & data caching |
| Search | Elasticsearch 8.11 | Full-text search |
| Container | Docker | Deployment |
| Build | Maven | Dependency management |

---

## High-Level Architecture

```
                        ┌──────────────────┐
                        │   Load Balancer  │
                        │    (Nginx/AWS)   │
                        └────────┬─────────┘
                                 │
                    ┌────────────┼────────────┐
                    │            │            │
              ┌─────▼────┐ ┌────▼─────┐ ┌───▼──────┐
              │ App Node │ │ App Node │ │ App Node │
              │  (8080)  │ │  (8080)  │ │  (8080)  │
              └─────┬────┘ └────┬─────┘ └───┬──────┘
                    │            │            │
                    └────────────┼────────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │                      │                      │
    ┌─────▼──────┐     ┌────────▼────────┐   ┌────────▼────────┐
    │ PostgreSQL │     │      Redis      │   │ Elasticsearch   │
    │  (Primary) │     │   (Cache/Sess)  │   │    (Search)     │
    └────────────┘     └─────────────────┘   └─────────────────┘
```

### Components

1. **Application Layer** - Spring Boot instances
2. **Data Layer** - PostgreSQL for transactional data
3. **Cache Layer** - Redis for performance
4. **Search Layer** - Elasticsearch for fast queries
5. **Security Layer** - JWT-based authentication

---

## Component Design

### 1. Controller Layer

**Responsibility:** Handle HTTP requests/responses

```
AuthController          → Authentication (register, login)
BusManagementController → Admin/Operator operations
BookingController       → Booking CRUD
BusSearchController     → Public search
BusTrackingController   → GPS tracking
PaymentController       → Payment processing
```

**Design Pattern:** RESTful API with @RestController

### 2. Service Layer

**Responsibility:** Business logic implementation

```
AuthService           → User authentication
BusService            → Bus management
RouteService          → Route operations
ScheduleService       → Schedule management
BookingService        → Booking logic
BusTrackingService    → Tracking updates
PaymentService        → Payment processing
BusSearchService      → Search orchestration
ElasticsearchSyncService → Data sync
```

**Design Pattern:** Service-oriented with dependency injection

### 3. Repository Layer

**Responsibility:** Data access

```
JPA Repositories:
- UserRepository
- BusOperatorRepository
- BusRepository
- RouteRepository
- ScheduleRepository
- BookingRepository
- SeatBookingRepository
- PaymentRepository
- BusTrackingRepository

Elasticsearch Repository:
- BusSearchRepository
```

**Design Pattern:** Repository pattern with Spring Data

### 4. Security Layer

```
JWT Filter → SecurityFilterChain → Authentication Manager
                                          ↓
                                   UserDetailsService
                                          ↓
                                     Database
```

**Components:**
- `JwtUtil` - Token generation/validation
- `JwtAuthenticationFilter` - Request interception
- `SecurityConfig` - Security rules
- `UserDetailsService` - User loading

---

## Data Flow

### 1. User Registration Flow

```
Client Request
    ↓
POST /api/auth/register
    ↓
AuthController.register()
    ↓
AuthService.register()
    ├─> Validate input
    ├─> Hash password (BCrypt)
    ├─> Save to PostgreSQL
    ├─> Generate JWT token
    └─> Return AuthResponse
```

### 2. Bus Search Flow

```
Client Request
    ↓
POST /api/public/search
    ↓
BusSearchController.searchBuses()
    ↓
BusSearchService.searchBuses()
    ├─> Query Elasticsearch
    ├─> Calculate available seats (Redis cache)
    ├─> Enrich with real-time data
    └─> Return search results
```

### 3. Booking Creation Flow

```
Client Request (with JWT)
    ↓
POST /api/bookings
    ↓
JWT Filter (validate token)
    ↓
SecurityContext (set user)
    ↓
BookingController.createBooking()
    ↓
BookingService.createBooking()
    ├─> Validate schedule
    ├─> Check seat availability
    ├─> Lock seats (pessimistic lock)
    ├─> Create booking transaction
    ├─> Generate booking reference
    ├─> Generate tracking link
    ├─> Update cache
    └─> Return BookingResponse
```

### 4. Elasticsearch Sync Flow

```
Schedule Created/Updated
    ↓
Save to PostgreSQL
    ↓
@Async method triggered
    ↓
ElasticsearchSyncService.syncSchedule()
    ├─> Map Schedule → BusSearchDocument
    ├─> Save to Elasticsearch
    └─> Update sync status

Scheduled Sync (every 30 min)
    ↓
@Scheduled method runs
    ↓
Compare DB vs ES
    ├─> Find missing documents
    ├─> Find outdated documents
    └─> Sync differences
```

---

## Security Architecture

### Authentication Flow

```
1. User Login:
   Client → POST /api/auth/login
        ↓
   Authenticate credentials
        ↓
   Generate JWT token
   {
     "sub": "username",
     "roles": ["ROLE_USER"],
     "iat": 1234567890,
     "exp": 1234654290
   }
        ↓
   Return token to client

2. Authenticated Request:
   Client → GET /api/bookings (with Bearer token)
        ↓
   JwtAuthenticationFilter intercepts
        ↓
   Extract & validate token
        ↓
   Extract user & roles
        ↓
   Set SecurityContext
        ↓
   @PreAuthorize("isAuthenticated()") check
        ↓
   Execute controller method
```

### Authorization Matrix

| Endpoint | ADMIN | OPERATOR | USER | PUBLIC |
|----------|-------|----------|------|--------|
| /auth/** | ✅ | ✅ | ✅ | ✅ |
| /public/search | ✅ | ✅ | ✅ | ✅ |
| /bookings | ✅ | ✅ | ✅ | ❌ |
| /operator/** | ✅ | ✅ | ❌ | ❌ |
| /admin/** | ✅ | ❌ | ❌ | ❌ |
| /tracking/booking/* | ✅ | ✅ | ✅ | ✅ |
| /tracking/update | ✅ | ✅ | ❌ | ❌ |

### Security Best Practices

1. **Password Hashing** - BCrypt with salt
2. **JWT Security** - HMAC-SHA256 signing
3. **Token Expiration** - 24 hours
4. **HTTPS Only** - Force SSL in production
5. **CORS Configuration** - Restrict origins
6. **SQL Injection Protection** - JPA parameterized queries
7. **XSS Protection** - Input validation
8. **CSRF Protection** - Disabled for stateless API

---

## Database Design

### Entity Relationship Diagram

```
┌──────────┐       ┌──────────────┐       ┌──────────┐
│   User   │       │ BusOperator  │       │   Bus    │
│──────────│       │──────────────│       │──────────│
│ id (PK)  │       │ id (PK)      │       │ id (PK)  │
│ username │       │ name         │       │ operator_id (FK)
│ password │       │ email        │◄──────│ bus_number
│ role     │       │ license_no   │       │ bus_type │
└────┬─────┘       └──────────────┘       └────┬─────┘
     │                                          │
     │ 1:N                                   N:1│
     │                                          │
     │         ┌──────────┐      ┌────────────┐│
     │         │  Route   │      │  Schedule  ││
     │         │──────────│      │────────────││
     │         │ id (PK)  │◄─────│ id (PK)    ││
     │         │ origin   │  N:1 │ bus_id (FK)│◄┘
     │         │ dest     │      │ route_id(FK)
     │         └──────────┘      └─────┬──────┘
     │                                 │
     │                              N:1│
     │         ┌──────────┐            │
     └────────►│ Booking  │◄───────────┘
               │──────────│
               │ id (PK)  │
               │ user_id (FK)
               │ schedule_id (FK)
               │ booking_ref
               │ status   │
               └────┬─────┘
                    │
                 1:N│
                    │
            ┌───────▼────────┐
            │ SeatBooking    │
            │────────────────│
            │ id (PK)        │
            │ booking_id (FK)│
            │ seat_number    │
            │ passenger_name │
            └────────────────┘
```

### Key Design Decisions

1. **Normalized Schema** - Minimize data redundancy
2. **Foreign Key Constraints** - Maintain referential integrity
3. **Indexes** - On frequently queried columns
4. **Enum Types** - For fixed value sets (bus_type, status)
5. **Timestamps** - Track creation/update times
6. **Soft Deletes** - Use is_active flags

### Database Indexes

```sql
-- Performance optimization
CREATE INDEX idx_schedules_route ON schedules(route_id);
CREATE INDEX idx_schedules_bus ON schedules(bus_id);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_schedule ON bookings(schedule_id);
CREATE INDEX idx_bookings_reference ON bookings(booking_reference);
CREATE INDEX idx_seat_bookings_booking ON seat_bookings(booking_id);
```

---

## API Design

### RESTful Principles

1. **Resource-based URLs** - `/api/bookings`, `/api/buses`
2. **HTTP Methods** - GET (read), POST (create), PUT (update), DELETE (remove)
3. **Status Codes** - 200 (OK), 201 (Created), 400 (Bad Request), 401 (Unauthorized), 403 (Forbidden), 404 (Not Found)
4. **JSON Format** - Request/response bodies
5. **Stateless** - No server-side sessions

### API Versioning Strategy

Current: **No versioning** (v1 implicit)

Future:
- URL-based: `/api/v2/bookings`
- Header-based: `Accept: application/vnd.redbus.v2+json`

### Error Response Format

```json
{
  "timestamp": "2025-10-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Seat 15 is already booked",
  "path": "/api/bookings"
}
```

---

## Scalability Considerations

### Horizontal Scaling

```
Load Balancer
     ↓
┌────┴────┬────────┬────────┐
│ Node 1  │ Node 2 │ Node 3 │
└────┬────┴────┬───┴────┬───┘
     │         │        │
  ┌──┴─────────┴────────┴──┐
  │   Shared Data Layer    │
  │  (PostgreSQL + Redis)  │
  └────────────────────────┘
```

**Key Points:**
- Stateless application design
- Session storage in Redis
- Database connection pooling
- Read replicas for PostgreSQL

### Caching Strategy

1. **Application Cache** - Redis
   - User sessions (24 hours)
   - Search results (5 minutes)
   - Available seats (1 minute)

2. **Database Cache** - Query cache
   - Frequently accessed routes
   - Operator information

3. **CDN Cache** - Static assets
   - Images, CSS, JS (future)

### Performance Optimizations

1. **Database:**
   - Connection pooling (HikariCP)
   - Prepared statement caching
   - Index optimization

2. **Application:**
   - Lazy loading (@Lazy)
   - Async processing (@Async)
   - Batch operations

3. **Search:**
   - Elasticsearch query optimization
   - Result pagination
   - Field filtering

### Monitoring

```
Application Metrics (Actuator)
     ↓
┌────────────────────────┐
│ /actuator/health       │ → Overall health
│ /actuator/metrics      │ → Performance metrics
│ /actuator/info         │ → App information
└────────────────────────┘
```

**Recommended Tools:**
- Prometheus - Metrics collection
- Grafana - Visualization
- ELK Stack - Log aggregation
- New Relic/DataDog - APM

---

## Future Enhancements

1. **Microservices Migration**
   - Auth Service
   - Booking Service
   - Search Service
   - Payment Service

2. **Message Queue**
   - RabbitMQ/Kafka for async processing
   - Event-driven architecture

3. **Kubernetes Deployment**
   - Auto-scaling
   - Self-healing
   - Rolling updates

4. **API Gateway**
   - Rate limiting
   - Request routing
   - Authentication aggregation

---

**Document Version:** 1.0  
**Last Updated:** October 2025  
**Authors:** RedBus Engineering Team

