# RedBus - Online Bus Ticket Booking System

A comprehensive, production-ready bus ticket booking system built with Spring Boot, PostgreSQL, Redis, and Elasticsearch.

## 🚀 Quick Start (No Technical Knowledge Required)

### Prerequisites
- **Docker Desktop** installed and running ([Download here](https://www.docker.com/products/docker-desktop))
- **Python 3.7+** installed ([Download here](https://www.python.org/downloads/))

### Run the Application

**Windows:**
```bash
cd scripts
python redbus_setup_and_test.py
```

**Mac/Linux:**
```bash
cd scripts
python3 redbus_setup_and_test.py
```

The script will automatically:
1. ✅ Check system requirements
2. ✅ Build and start all services (PostgreSQL, Redis, Elasticsearch, Application)
3. ✅ Seed initial test data (operators, routes, buses, schedules)
4. ✅ Run JUnit tests with coverage analysis
5. ✅ Run comprehensive end-to-end tests (27 tests)
6. ✅ Display detailed test results with coverage metrics

**Wait time:** 2-5 minutes (first run may take longer)
**Note:** If services are already running, it skips setup and goes directly to testing

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [High-Level Design](#high-level-design)
- [Low-Level Design](#low-level-design)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

RedBus is an enterprise-grade bus ticket booking platform that provides:

- **Real-time seat availability** tracking
- **Multi-role authentication** (Admin, Operator, User)
- **Advanced search** with Elasticsearch
- **Live bus tracking** with GPS coordinates
- **Payment processing** simulation
- **Booking management** with cancellation support
- **Data synchronization** between SQL and NoSQL datastores

---

## 🏗️ Architecture

### System Architecture

```
┌─────────────┐
│   Client    │
│  (Browser)  │
└──────┬──────┘
       │ HTTPS
       ▼
┌─────────────────────────────────────────┐
│         Load Balancer (Future)          │
└──────────────┬──────────────────────────┘
               │
       ┌───────┴───────┐
       ▼               ▼
┌─────────────┐ ┌─────────────┐
│  Spring Boot │ │ Spring Boot │
│   Instance 1 │ │  Instance 2 │
└──────┬───────┘ └──────┬──────┘
       │                 │
       └────────┬────────┘
                │
    ┌───────────┼───────────┐
    │           │           │
    ▼           ▼           ▼
┌─────────┐ ┌───────┐ ┌──────────────┐
│PostgreSQL│ │ Redis │ │Elasticsearch │
│(Primary) │ │(Cache)│ │   (Search)   │
└─────────┘ └───────┘ └──────────────┘
```

### Microservices Design Pattern

The application follows a **modular monolith** approach with clear service boundaries:

1. **Authentication Service** - JWT-based auth
2. **Bus Management Service** - Operator/bus/route/schedule management
3. **Search Service** - Elasticsearch-powered search
4. **Booking Service** - Reservation management
5. **Payment Service** - Payment processing
6. **Tracking Service** - Real-time GPS tracking
7. **Sync Service** - Data synchronization

---

## ✨ Features

### User Features
- 🔍 **Advanced Bus Search** - Filter by route, date, price, bus type
- 💺 **Smart Seat Selection** - Real-time availability check
- 🎫 **Instant Booking** - Quick reservation with automatic confirmation
- 🔗 **Live Tracking Link** - Track your bus in real-time
- 📱 **Booking Management** - View history, cancel bookings
- 💳 **Secure Payments** - Multiple payment methods

### Operator Features
- 🚌 **Fleet Management** - Add/update/delete buses
- 🗺️ **Route Planning** - Create and manage routes
- ⏰ **Schedule Management** - Configure departures/arrivals
- 📊 **Analytics Dashboard** - View bookings and revenue
- 📍 **GPS Integration** - Update bus locations

### Admin Features
- 👥 **User Management** - Manage all users and operators
- 🔐 **Role Management** - Assign permissions
- 🔄 **Data Sync** - Elasticsearch synchronization
- 🏥 **System Health** - Monitor all services
- 📈 **Reports** - Generate system reports

---

## 🛠️ Technology Stack

### Backend
- **Java 17** - Modern Java LTS version
- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - Authentication & authorization
- **JWT** - Stateless authentication
- **Spring Data JPA** - Database ORM
- **Flyway** - Database migrations

### Databases
- **PostgreSQL 15** - Primary relational database
- **Redis 7** - Caching and session storage
- **Elasticsearch 8.11** - Full-text search engine

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Maven** - Dependency management

### Testing
- **Python 3** - E2E test automation
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework

---

## 📁 Project Structure

```
redbus/
├── src/
│   ├── main/
│   │   ├── java/com/redbus/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST API endpoints
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── service/          # Business logic
│   │   │   ├── security/         # Security configuration
│   │   │   └── document/         # Elasticsearch documents
│   │   └── resources/
│   │       ├── application.yml   # Main config
│   │       ├── application-docker.yml  # Docker config
│   │       └── db/migration/     # Flyway migrations
│   └── test/                     # Unit tests
│
├── scripts/                      # Automation scripts
│   └── redbus_setup_and_test.py  # Setup & test suite (cross-platform)
│
├── tests/                        # E2E tests
│   ├── redbus_setup_and_test.py # Main setup & test suite
│   ├── requirements.txt          # Python dependencies
│   └── [other test files]
│
├── docs/                         # Documentation
│   ├── API.md                    # API documentation
│   ├── SETUP_README.md           # Setup guide
│   └── [other docs]
│
├── tmp/                          # Temporary/old files (git ignored)
│
├── docker-compose.yml            # Docker services config
├── Dockerfile                    # Application container
├── pom.xml                       # Maven dependencies
└── README.md                     # This file
```

---

## 🎨 High-Level Design

### Data Flow

```
User Request
    ↓
JWT Authentication Filter
    ↓
Spring Security (Role Check)
    ↓
Controller (REST Endpoint)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
┌───────┬────────┬──────────┐
│       │        │          │
PostgreSQL  Redis  Elasticsearch
```

### Key Design Patterns

1. **MVC Pattern** - Separation of concerns
2. **Repository Pattern** - Data access abstraction
3. **DTO Pattern** - Data transfer encapsulation
4. **Builder Pattern** - Object construction
5. **Dependency Injection** - Loose coupling
6. **Filter Pattern** - Request processing pipeline

### Security Architecture

```
Request → JWT Filter → Authentication Manager
                              ↓
                         UserDetailsService
                              ↓
                      Database User Lookup
                              ↓
                       Security Context
                              ↓
                     @PreAuthorize Check
                              ↓
                    Controller Method
```

---

## 🔧 Low-Level Design

### Database Schema

#### Core Tables

**users**
- id (PK)
- username (unique)
- email (unique)
- password (hashed)
- role (ADMIN/OPERATOR/USER)
- created_at, updated_at

**bus_operators**
- id (PK)
- name
- contact_email
- contact_phone
- license_number (unique)
- is_active
- created_at, updated_at

**buses**
- id (PK)
- operator_id (FK)
- bus_number (unique)
- bus_type (AC/NON_AC/SLEEPER/LUXURY)
- total_seats
- amenities (array)
- is_active

**routes**
- id (PK)
- origin
- destination
- distance_km
- estimated_duration_hours

**schedules**
- id (PK)
- bus_id (FK)
- route_id (FK)
- departure_time
- arrival_time
- price
- is_recurring
- days_of_week (array)

**bookings**
- id (PK)
- user_id (FK)
- schedule_id (FK)
- booking_reference (unique)
- booking_date
- total_seats
- total_amount
- status (PENDING/CONFIRMED/CANCELLED)
- created_at

**seat_bookings**
- id (PK)
- booking_id (FK)
- seat_number
- passenger_name
- passenger_age
- passenger_gender

**bus_tracking**
- id (PK)
- bus_id (FK)
- latitude
- longitude
- speed_kmh
- direction_degrees
- timestamp

### API Design

#### Authentication Flow

```java
POST /api/auth/register
  → RegisterRequest (DTO)
  → AuthService.register()
  → User entity creation
  → JWT token generation
  → AuthResponse with token

POST /api/auth/login
  → LoginRequest (DTO)
  → AuthenticationManager.authenticate()
  → JWT token generation
  → AuthResponse with token
```

#### Booking Flow

```java
POST /api/bookings
  → JWT Authentication
  → Extract user from SecurityContext
  → BookingRequest validation
  → Check seat availability
  → Create booking + seat bookings
  → Generate tracking link
  → Return BookingResponse
```

### Elasticsearch Sync Strategy

```
1. On Schedule Create/Update:
   ├─> Save to PostgreSQL
   └─> Async sync to Elasticsearch

2. Scheduled Sync (every 30 min):
   ├─> Query all schedules from DB
   ├─> Query all documents from ES
   ├─> Calculate differences
   └─> Sync missing/outdated documents

3. Manual Sync:
   ├─> Trigger via /api/sync/trigger
   └─> Force full sync
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:9090/api
```

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1-555-0100",
  "role": "USER"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123!"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "id": 1,
  "username": "john_doe",
  "role": "USER"
}
```

### Bus Search

#### Search Buses
```http
POST /public/search
Content-Type: application/json

{
  "origin": "Mumbai",
  "destination": "Pune",
  "travelDate": "2025-10-15"
}

Response: 200 OK
[
  {
    "scheduleId": 1,
    "busId": 5,
    "busNumber": "MH-01-1234",
    "busType": "AC",
    "operatorName": "RedBus Express",
    "origin": "Mumbai",
    "destination": "Pune",
    "departureTime": "06:00:00",
    "arrivalTime": "09:30:00",
    "price": 500.00,
    "totalSeats": 40,
    "availableSeats": 35,
    "amenities": ["WiFi", "AC", "Water"],
    "duration": "3h 30m"
  }
]
```

### Booking Endpoints

#### Create Booking
```http
POST /bookings
Authorization: Bearer {token}
Content-Type: application/json

{
  "scheduleId": 1,
  "travelDate": "2025-10-15",
  "passengers": [
    {
      "seatNumber": 15,
      "passengerName": "John Doe",
      "passengerAge": 30,
      "passengerGender": "MALE"
    },
    {
      "seatNumber": 16,
      "passengerName": "Jane Doe",
      "passengerAge": 28,
      "passengerGender": "FEMALE"
    }
  ]
}

Response: 201 Created
{
  "id": 100,
  "bookingReference": "RB12345678",
  "bookingDate": "2025-10-15",
  "totalSeats": 2,
  "totalAmount": 1000.00,
  "status": "PENDING",
  "busNumber": "MH-01-1234",
  "origin": "Mumbai",
  "destination": "Pune",
  "departureTime": "06:00",
  "arrivalTime": "09:30",
  "trackingLink": "http://localhost:9090/api/tracking/booking/RB12345678",
  "passengers": [...]
}
```

#### Track Bus
```http
GET /tracking/booking/{bookingReference}

Response: 200 OK
{
  "bookingReference": "RB12345678",
  "busNumber": "MH-01-1234",
  "operatorName": "RedBus Express",
  "origin": "Mumbai",
  "destination": "Pune",
  "trackingAvailable": true,
  "tracking": {
    "latitude": 18.9388,
    "longitude": 73.1158,
    "speedKmh": 60.0,
    "timestamp": "2025-10-15T07:30:00"
  }
}
```

### Operator Endpoints (Requires ADMIN or OPERATOR role)

#### Create Bus
```http
POST /operator/buses
Authorization: Bearer {admin_token}
Content-Type: application/json

{
  "operatorId": 1,
  "busNumber": "MH-01-1234",
  "busType": "AC",
  "totalSeats": 40,
  "amenities": ["WiFi", "AC", "USB Charging"]
}
```

See `docs/API.md` for complete API documentation.

---

## 🧪 Testing

### Running All Tests

```bash
cd scripts
python redbus_setup_and_test.py    # Windows
python3 redbus_setup_and_test.py   # Mac/Linux
```

### Test Coverage

**JUnit Tests (Unit Tests):**
- Service layer tests
- Controller tests
- Repository tests
- Code coverage with JaCoCo
- Coverage report: `target/site/jacoco/index.html`

**E2E Test Suite (Integration Tests):**

✅ User registration and authentication  
✅ Admin/Operator management  
✅ Bus/Route/Schedule CRUD operations  
✅ Elasticsearch sync verification  
✅ Public bus search  
✅ Smart seat selection (checks availability)  
✅ Booking creation with tracking link  
✅ Duplicate booking prevention  
✅ Booking cancellation  
✅ Real-time bus tracking  
✅ System health monitoring  
✅ Data synchronization  

### Test Results

Example output:
```
JUNIT TEST COVERAGE:
  Instruction Coverage: 78%
  Branch Coverage: 65%
  Report: target/site/jacoco/index.html

END-TO-END TEST RESULTS:
Total Tests Run: 27
Tests Passed: 27
Tests Failed: 0
Success Rate: 100.0%

INITIAL TEST DATA:
  Sample data seeded for demonstration
  Login: demo_admin / Admin@123
```

### Initial Test Data

On first run, the script automatically creates sample data:
- **2 Operators** (RedBus Express, Premium Travels)
- **4 Routes** (Mumbai-Pune, Mumbai-Bangalore, Delhi-Jaipur, Chennai-Bangalore)
- **4 Buses** (various types: AC, NON_AC, SLEEPER, LUXURY)
- **8 Schedules** (morning and afternoon departures)
- **Demo Admin** (username: demo_admin, password: Admin@123)

---

## 🚀 Deployment

### Docker Deployment (Recommended)

1. **Build and Start:**
```bash
docker-compose up -d --build
```

2. **Check Status:**
```bash
docker-compose ps
```

3. **View Logs:**
```bash
docker-compose logs -f app
```

4. **Stop Services:**
```bash
docker-compose down
```

### Manual Deployment

#### Prerequisites
- Java 17
- PostgreSQL 15
- Redis 7
- Elasticsearch 8.11
- Maven 3.9+

#### Steps

1. **Configure Database:**
```bash
# Update src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/redbus
    username: your_username
    password: your_password
```

2. **Build Application:**
```bash
mvn clean package -DskipTests
```

3. **Run Application:**
```bash
java -jar target/redbus-app-1.0.0.jar
```

### Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=redbus
DB_USER=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Elasticsearch
ES_HOST=localhost
ES_PORT=9200

# JWT
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000

# Application
SERVER_PORT=8080
```

---

## 🔧 Configuration

### Application Profiles

- **default** - Local development
- **docker** - Docker deployment
- **test** - Testing environment

Activate profile:
```bash
java -jar app.jar --spring.profiles.active=docker
```

### Database Migrations

Flyway migrations are in `src/main/resources/db/migration/`:

- `V1__Create_initial_schema.sql` - Initial tables
- `V2__Add_new_bus_types.sql` - Add LUXURY bus type
- `V3__Increase_bus_number_length.sql` - Extend bus_number field

New migrations auto-run on startup.

### Elasticsearch Configuration

```yaml
elasticsearch:
  uris: http://localhost:9200
  connect-timeout: 60s
  socket-timeout: 60s
  read-timeout: 60s
```

---

## ❓ Troubleshooting

### Application won't start

**Problem:** Port already in use  
**Solution:**
```bash
# Windows
netstat -ano | findstr :9090
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:9090 | xargs kill -9
```

### Elasticsearch connection failed

**Problem:** Elasticsearch not ready  
**Solution:** Wait 30-60 seconds for ES to start fully

### Tests failing

**Problem:** Services not healthy  
**Solution:**
```bash
docker-compose down
docker-compose up -d
# Wait 2 minutes
cd tests
python redbus_setup_and_test.py
```

### Database migration errors

**Problem:** Schema conflicts  
**Solution:**
```bash
docker-compose down -v  # Remove volumes
docker-compose up -d
```

### Out of memory

**Problem:** Docker insufficient memory  
**Solution:** Increase Docker Desktop memory to 4GB+

---

## 📞 Support

### Common Issues

1. **Docker not running** - Start Docker Desktop
2. **Python not found** - Install Python 3.7+
3. **Port conflicts** - Change port in `docker-compose.yml`
4. **Slow performance** - Increase Docker resources

### Logs Location

- **Application:** `docker-compose logs app`
- **PostgreSQL:** `docker-compose logs postgres`
- **Elasticsearch:** `docker-compose logs elasticsearch`
- **Redis:** `docker-compose logs redis`

---

## 📄 License

This project is licensed under the MIT License.

---

## 👥 Contributors

Built with ❤️ by the RedBus Team

---

## 🎯 Roadmap

- [ ] Payment gateway integration
- [ ] Email/SMS notifications
- [ ] Mobile app (React Native)
- [ ] Multi-language support
- [ ] Advanced analytics dashboard
- [ ] Load balancing setup
- [ ] Kubernetes deployment
- [ ] CI/CD pipeline
- [ ] Performance monitoring
- [ ] Rate limiting

---

**Made with Spring Boot | PostgreSQL | Redis | Elasticsearch**

