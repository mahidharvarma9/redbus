# 📁 RedBus Project Structure

This document explains the organization of the RedBus codebase, making it easy for developers to navigate and understand the project.

---

## Directory Overview

```
redbus/
├── 📂 src/                  # Source code (Java application)
├── 📂 docs/                 # Documentation
├── 📂 scripts/              # Automation scripts
├── 📂 tests/                # End-to-end tests
├── 📂 tmp/                  # Temporary/old files (git ignored)
├── 📄 docker-compose.yml    # Docker services configuration
├── 📄 Dockerfile            # Application container definition
├── 📄 pom.xml               # Maven dependencies
├── 📄 .gitignore            # Git ignore rules
└── 📄 README.md             # Main documentation
```

---

## 📂 src/ - Application Source Code

### Structure

```
src/
├── main/
│   ├── java/com/redbus/              # Java source code
│   │   ├── config/                   # Configuration classes
│   │   │   ├── ElasticsearchConfig   # Elasticsearch setup
│   │   │   ├── JacksonConfig         # JSON serialization
│   │   │   └── RepositoryConfig      # JPA repository config
│   │   │
│   │   ├── controller/               # REST API endpoints
│   │   │   ├── AuthController        # /auth/** - Login/Register
│   │   │   ├── BusManagementController # /operator/** - Bus/Route/Schedule CRUD
│   │   │   ├── BookingController     # /bookings/** - Booking operations
│   │   │   ├── BusSearchController   # /public/search - Search buses
│   │   │   ├── BusTrackingController # /tracking/** - GPS tracking
│   │   │   ├── PaymentController     # /payments/** - Payment processing
│   │   │   ├── SyncController        # /sync/** - ES sync management
│   │   │   └── ElasticsearchSearchController # Advanced search
│   │   │
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── AuthRequest/Response  # Authentication DTOs
│   │   │   ├── BusRequest/Response   # Bus DTOs
│   │   │   ├── BookingRequest/Response # Booking DTOs
│   │   │   ├── BusSearchRequest/Response # Search DTOs
│   │   │   └── [15+ other DTOs]      # Various data transfer objects
│   │   │
│   │   ├── entity/                   # JPA Entities (Database tables)
│   │   │   ├── User                  # users table
│   │   │   ├── BusOperator           # bus_operators table
│   │   │   ├── Bus                   # buses table
│   │   │   ├── Route                 # routes table
│   │   │   ├── Schedule              # schedules table
│   │   │   ├── Booking               # bookings table
│   │   │   ├── SeatBooking           # seat_bookings table
│   │   │   ├── Payment               # payments table
│   │   │   └── BusTracking           # bus_tracking table
│   │   │
│   │   ├── repository/               # Data access layer
│   │   │   ├── jpa/                  # JPA repositories
│   │   │   │   ├── UserRepository
│   │   │   │   ├── BusRepository
│   │   │   │   ├── BookingRepository
│   │   │   │   └── [7+ other repos]
│   │   │   └── elasticsearch/        # Elasticsearch repositories
│   │   │       └── BusSearchRepository
│   │   │
│   │   ├── service/                  # Business logic layer
│   │   │   ├── AuthService           # User authentication
│   │   │   ├── UserService           # User management
│   │   │   ├── BusOperatorService    # Operator management
│   │   │   ├── BusService            # Bus management
│   │   │   ├── RouteService          # Route management
│   │   │   ├── ScheduleService       # Schedule management
│   │   │   ├── BookingService        # Booking logic
│   │   │   ├── PaymentService        # Payment processing
│   │   │   ├── BusTrackingService    # GPS tracking
│   │   │   ├── BusSearchService      # Search orchestration
│   │   │   └── ElasticsearchSyncService # Data synchronization
│   │   │
│   │   ├── security/                 # Security layer
│   │   │   ├── JwtUtil               # JWT token handling
│   │   │   ├── JwtAuthenticationFilter # Request filtering
│   │   │   └── SecurityConfig        # Security rules
│   │   │
│   │   ├── document/                 # Elasticsearch documents
│   │   │   └── BusSearchDocument     # Bus search index
│   │   │
│   │   └── RedBusApplication.java    # Application entry point
│   │
│   └── resources/                    # Application resources
│       ├── application.yml           # Main configuration
│       ├── application-docker.yml    # Docker-specific config
│       ├── db/migration/             # Database migrations (Flyway)
│       │   ├── V1__Create_initial_schema.sql
│       │   ├── V2__Add_new_bus_types.sql
│       │   └── V3__Increase_bus_number_length.sql
│       └── static/                   # Static files (HTML/JS)
│           ├── index.html
│           └── js/app.js
│
└── test/                             # Unit tests
    ├── java/com/redbus/
    │   ├── controller/               # Controller tests
    │   ├── service/                  # Service tests
    │   └── RedBusApplicationTests.java
    └── resources/
        └── application-test.yml      # Test configuration
```

---

## 📂 docs/ - Documentation

```
docs/
├── README.md                # Original main docs
├── QUICK_START.md           # 5-step setup guide for non-technical users
├── ARCHITECTURE.md          # System architecture & design
├── PROJECT_STRUCTURE.md     # This file
├── SETUP_README.md          # Detailed setup instructions
├── api-testing-guide.md     # API testing guide
└── README-SEPARATED.md      # Alternative setup docs
```

### Documentation Purpose

| File | Audience | Purpose |
|------|----------|---------|
| QUICK_START.md | Non-technical users | Get started in 5 minutes |
| ARCHITECTURE.md | Developers/Architects | Understand system design |
| PROJECT_STRUCTURE.md | Developers | Navigate the codebase |
| SETUP_README.md | DevOps/Developers | Detailed setup guide |
| api-testing-guide.md | QA/Developers | Test the APIs |

---

## 📂 scripts/ - Automation Scripts

```
scripts/
├── 🚀 Quick Start Scripts
│   ├── python redbus_setup_and_test.py          # Windows: One-click setup & test
│   ├── python3 redbus_setup_and_test.py           # Linux/Mac: One-click setup & test
│   ├── run.bat                       # Windows: Quick run
│   └── run.sh                        # Linux/Mac: Quick run
│
├── 🔧 Setup Scripts
│   ├── setup.bat                     # Basic setup (Windows)
│   ├── setup.sh                      # Basic setup (Linux/Mac)
│   ├── setup-automated.bat           # Automated setup
│   ├── setup-separated.bat/sh        # Separated services setup
│   └── setup-with-test-data.bat/sh   # Setup with sample data
│
├── 🧪 Testing Scripts
│   ├── test-api.ps1                  # API testing (PowerShell)
│   ├── test-end-to-end.ps1           # E2E testing (PowerShell)
│   ├── simple-test.ps1               # Simple API test
│   ├── setup_test_env.bat/sh         # Test environment setup
│
├── 🏃 Development Scripts
│   ├── start-dev.bat                 # Start dev environment (Windows)
│   ├── start-dev.sh                  # Start dev environment (Linux/Mac)
│
└── 🛠️ Utility Scripts
    └── wait-for-elasticsearch.sh     # Wait for ES to be ready
```

### Script Categories

1. **Quick Start** (⭐ Recommended for new users)
   - `python scripts/redbus_setup_and_test.py` - Complete automated setup + test
   - **Must run from project root directory** (where docker-compose.yml is)
   - Runs 66 JUnit tests + 27 E2E tests with full output
   - Shows detailed request/response for all API calls
   - Takes 10-15 min (first time) or 3-5 min (subsequent runs)

2. **Setup** (For manual control)
   - Various setup scripts for different scenarios

3. **Testing** (For QA/Developers)
   - Scripts to run different types of tests

4. **Development** (For active development)
   - Scripts to start services for development

---

## 📂 tests/ - End-to-End Tests

```
tests/
├── redbus_end_to_end_test.py    # ⭐ Main E2E test suite (27 tests)
├── redbus_e2e_test.py           # Alternative E2E test
├── run_complete_test.py         # Test runner with setup
├── demo_test.py                 # Demo/sample tests
├── simple_api_test.py           # Simple API tests
├── requirements.txt             # Python dependencies
└── test_requirements.txt        # Test-specific dependencies
```

### Test Files Overview

| File | Purpose | Tests Count |
|------|---------|-------------|
| **redbus_end_to_end_test.py** | Complete E2E suite | 27 tests |
| redbus_e2e_test.py | Alternative E2E | ~20 tests |
| run_complete_test.py | Setup + test runner | N/A |
| simple_api_test.py | Quick API verification | ~10 tests |

### Running Tests

**Recommended:**
```bash
cd scripts
./python redbus_setup_and_test.py    # Windows
./python3 redbus_setup_and_test.py     # Linux/Mac
```

**Manual:**
```bash
cd tests
python redbus_end_to_end_test.py
```

---

## 📂 tmp/ - Temporary Files (Git Ignored)

```
tmp/
├── backend/              # Old backend folder (unused)
├── frontend/             # Old frontend folder (unused)
├── e2e-test/             # Old test folder
├── target/               # Maven build output
├── docker-compose.dev.yml
├── docker-compose.separated.yml
├── Dockerfile.dev
├── maven.zip
├── init-data.sql
└── [other old files]
```

**Note:** This folder is git ignored and contains:
- Old/deprecated files
- Build artifacts
- Temporary files
- Backup copies

---

## 🔑 Key Files Explanation

### docker-compose.yml
Defines all services needed to run the application:
- **postgres** - Database (port 5432)
- **redis** - Cache (port 6379)
- **elasticsearch** - Search (port 9200)
- **app** - Spring Boot application (port 9090)

### Dockerfile
Defines how to build the Spring Boot application container:
1. Use Maven base image
2. Copy source code
3. Build JAR file
4. Create runtime image
5. Configure startup

### pom.xml
Maven Project Object Model:
- Project metadata
- Dependencies (Spring Boot, PostgreSQL, Redis, Elasticsearch)
- Build configuration
- Plugin configuration

### .gitignore
Specifies files/folders to exclude from git:
- `tmp/` folder
- `target/` folder
- IDE files (.idea, .vscode)
- Log files
- OS files (.DS_Store)

---

## 🗺️ Navigation Guide

### "I want to..."

**...understand the API**
→ Look at `src/main/java/com/redbus/controller/`

**...see business logic**
→ Look at `src/main/java/com/redbus/service/`

**...check database schema**
→ Look at `src/main/resources/db/migration/`

**...understand entities**
→ Look at `src/main/java/com/redbus/entity/`

**...modify security rules**
→ Look at `src/main/java/com/redbus/security/SecurityConfig.java`

**...change configuration**
→ Look at `src/main/resources/application.yml`

**...add a new feature**
1. Create entity in `entity/`
2. Create repository in `repository/jpa/`
3. Create service in `service/`
4. Create DTO in `dto/`
5. Create controller in `controller/`
6. Add migration in `resources/db/migration/`

**...run tests**
→ Use `scripts/python redbus_setup_and_test.py` or `.sh`

**...read documentation**
→ Start with `docs/QUICK_START.md`

---

## 📊 Code Statistics

### Lines of Code (Approximate)

| Component | Files | Lines |
|-----------|-------|-------|
| Java Source | 59 | ~8,000 |
| Test Code | 5 | ~1,500 |
| Configuration | 3 | ~300 |
| SQL Migrations | 3 | ~500 |
| Documentation | 7 | ~2,000 |
| Scripts | 20+ | ~1,000 |
| **Total** | **~97** | **~13,300** |

### File Distribution

```
Java Files:     59 files (63%)
Test Files:      5 files  (5%)
Config Files:    3 files  (3%)
Scripts:        20 files (21%)
Docs:            7 files  (7%)
Other:           3 files  (1%)
```

---

## 🚀 Development Workflow

### 1. Clone & Setup
```bash
git clone <repository>
cd redbus
cd scripts
./python3 redbus_setup_and_test.py    # Automated setup + test
```

### 2. Make Changes
```bash
# Edit code in src/main/java/com/redbus/
# Add tests in src/test/java/com/redbus/
```

### 3. Build & Test
```bash
mvn clean package
docker-compose up --build
cd tests
python redbus_end_to_end_test.py
```

### 4. Commit
```bash
git add .
git commit -m "Your message"
git push
```

---

## 📚 Related Documentation

- [Main README](../README.md) - Project overview
- [Quick Start Guide](QUICK_START.md) - Get started in 5 minutes
- [Architecture](ARCHITECTURE.md) - System design
- [Setup Guide](SETUP_README.md) - Detailed setup
- [API Guide](api-testing-guide.md) - API documentation

---

**Document Version:** 1.0  
**Last Updated:** October 2025  
**Maintained By:** RedBus Development Team

