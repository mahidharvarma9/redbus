# 🎨 RedBus Visual Guide

A visual representation of the RedBus project structure and navigation.

---

## 🗂️ Folder Structure Visualization

```
📦 redbus/
│
├── 📄 GETTING_STARTED.md        ⭐ START HERE! (5 min quick start)
├── 📄 README.md                 📚 Complete documentation
├── 📄 REFACTORING_SUMMARY.md    📋 What changed during refactoring
├── 📄 .gitignore                🚫 Git ignore configuration
├── 📄 docker-compose.yml        🐳 Services orchestration
├── 📄 Dockerfile                🐳 Application container
├── 📄 pom.xml                   📦 Maven dependencies
│
├── 📂 docs/                     📚 ALL DOCUMENTATION
│   ├── INDEX.md                 📑 Documentation index
│   ├── QUICK_START.md           🚀 Non-technical setup (10 min)
│   ├── ARCHITECTURE.md          🏗️ System design (30 min)
│   ├── PROJECT_STRUCTURE.md     🗺️ Code navigation (15 min)
│   ├── SETUP_README.md          🔧 Detailed setup
│   ├── api-testing-guide.md     🧪 API testing guide
│   ├── VISUAL_GUIDE.md          🎨 This file
│   ├── README.md                📖 Original docs
│   └── README-SEPARATED.md      📖 Alternative setup
│
├── 📂 scripts/                  🤖 ALL AUTOMATION
│   ├── python redbus_setup_and_test.py     ⭐ Windows one-click setup
│   ├── python3 redbus_setup_and_test.py      ⭐ Mac/Linux one-click setup
│   ├── setup*.bat/sh            🔧 Various setup scripts
│   ├── test*.ps1                🧪 PowerShell test scripts
│   ├── start-dev.*              🏃 Development scripts
│   └── wait-for-elasticsearch.sh 🕐 Health check script
│
├── 📂 tests/                    🧪 ALL TESTS
│   ├── redbus_end_to_end_test.py ⭐ Main E2E suite (27 tests)
│   ├── simple_api_test.py       🧪 Quick API test
│   ├── run_complete_test.py     🧪 Test runner
│   ├── requirements.txt         📦 Python dependencies
│   └── test_requirements.txt    📦 Test dependencies
│
├── 📂 src/                      💻 SOURCE CODE
│   ├── main/
│   │   ├── java/com/redbus/
│   │   │   ├── config/          ⚙️ Configuration
│   │   │   ├── controller/      🌐 REST API endpoints
│   │   │   ├── dto/             📦 Data Transfer Objects
│   │   │   ├── entity/          🗃️ Database entities
│   │   │   ├── repository/      💾 Data access
│   │   │   ├── service/         🔧 Business logic
│   │   │   ├── security/        🔐 Security & JWT
│   │   │   ├── document/        🔍 Elasticsearch docs
│   │   │   └── RedBusApplication.java 🚀 Main entry
│   │   └── resources/
│   │       ├── application.yml  ⚙️ Configuration
│   │       ├── db/migration/    📊 Database migrations
│   │       └── static/          🎨 Static files
│   └── test/                    🧪 Unit tests
│
└── 📂 tmp/                      🗑️ Git ignored (old files)
    ├── backend/
    ├── frontend/
    ├── target/
    └── [deprecated files]
```

---

## 🗺️ Navigation Map

### Starting Point → Goal

```
┌─────────────────────────────────────────────────┐
│           WHAT DO YOU WANT TO DO?               │
└─────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
   ┌────────┐   ┌─────────┐   ┌──────────┐
   │RUN APP │   │UNDERSTAND│   │DEVELOP   │
   └────┬───┘   └────┬────┘   └────┬─────┘
        │            │              │
        ▼            ▼              ▼
┌───────────────┐┌─────────────┐┌──────────────┐
│GETTING_STARTED││  README.md  ││PROJECT_      │
│      .md      ││             ││STRUCTURE.md  │
└───────┬───────┘└──────┬──────┘└──────┬───────┘
        │               │               │
        ▼               ▼               ▼
┌───────────────┐┌─────────────┐┌──────────────┐
│scripts/       ││ARCHITECTURE ││src/main/java/│
│run_redbus_    ││    .md      ││com/redbus/   │
│setup.bat      ││             ││              │
└───────────────┘└─────────────┘└──────────────┘
```

---

## 👥 User Journey Maps

### 🆕 New User (Non-Technical)

```
START
  │
  ├─ [1] Read: GETTING_STARTED.md (5 min)
  │    └─> Understand: What is RedBus?
  │
  ├─ [2] Read: docs/QUICK_START.md (10 min)
  │    └─> Learn: How to install & run
  │
  ├─ [3] Run: scripts/python redbus_setup_and_test.py (5 min)
  │    └─> Action: Application starts
  │
  └─ [4] Verify: http://localhost:9090
       └─> Success! ✅

Total Time: 20 minutes
```

### 👨‍💻 Developer (New to Project)

```
START
  │
  ├─ [1] Read: GETTING_STARTED.md (5 min)
  │    └─> Get oriented
  │
  ├─ [2] Run: scripts/python redbus_setup_and_test.py (5 min)
  │    └─> App running
  │
  ├─ [3] Read: docs/PROJECT_STRUCTURE.md (15 min)
  │    └─> Understand code organization
  │
  ├─ [4] Read: docs/ARCHITECTURE.md (30 min)
  │    └─> Understand design
  │
  ├─ [5] Explore: src/main/java/com/redbus/ (30 min)
  │    └─> Read actual code
  │
  ├─ [6] Run: tests/redbus_end_to_end_test.py (5 min)
  │    └─> Verify everything works
  │
  └─ [7] Make first change
       └─> Start developing! 🚀

Total Time: ~90 minutes
```

### 🏗️ Architect (System Review)

```
START
  │
  ├─ [1] Read: README.md (20 min)
  │    └─> Complete overview
  │
  ├─ [2] Read: docs/ARCHITECTURE.md (30 min)
  │    └─> System design, patterns, flows
  │
  ├─ [3] Review: Database schema (10 min)
  │    └─> src/main/resources/db/migration/
  │
  ├─ [4] Review: API design (20 min)
  │    └─> src/main/java/com/redbus/controller/
  │
  └─ [5] Review: Security (20 min)
       └─> src/main/java/com/redbus/security/

Total Time: ~100 minutes
```

### 🧪 QA Engineer (Testing)

```
START
  │
  ├─ [1] Read: GETTING_STARTED.md (5 min)
  │    └─> Setup environment
  │
  ├─ [2] Run: scripts/python redbus_setup_and_test.py (5 min)
  │    └─> App + auto tests
  │
  ├─ [3] Read: docs/api-testing-guide.md (15 min)
  │    └─> Learn API endpoints
  │
  ├─ [4] Review: tests/redbus_end_to_end_test.py (20 min)
  │    └─> Understand test coverage
  │
  └─ [5] Run custom tests
       └─> Start testing! ✅

Total Time: ~45 minutes
```

---

## 🎯 File Purpose Quick Reference

### 📄 Root Files

| File | Icon | Purpose | Read Time |
|------|------|---------|-----------|
| GETTING_STARTED.md | ⭐ | **START HERE** - Quick orientation | 5 min |
| README.md | 📚 | Complete project documentation | 20 min |
| REFACTORING_SUMMARY.md | 📋 | What changed during refactoring | 10 min |
| .gitignore | 🚫 | Git configuration | N/A |
| docker-compose.yml | 🐳 | Services setup | N/A |
| Dockerfile | 🐳 | App container | N/A |
| pom.xml | 📦 | Maven dependencies | N/A |

### 📂 docs/ Files

| File | Icon | Audience | Read Time |
|------|------|----------|-----------|
| INDEX.md | 📑 | Everyone | 5 min |
| QUICK_START.md | 🚀 | Non-technical | 10 min |
| ARCHITECTURE.md | 🏗️ | Developers/Architects | 30 min |
| PROJECT_STRUCTURE.md | 🗺️ | Developers | 15 min |
| SETUP_README.md | 🔧 | DevOps | 20 min |
| api-testing-guide.md | 🧪 | QA/Developers | 15 min |
| VISUAL_GUIDE.md | 🎨 | Everyone | 10 min |

### 📂 scripts/ (Key Scripts)

| File | Icon | Purpose | Platform |
|------|------|---------|----------|
| python redbus_setup_and_test.py | ⭐ | **One-click setup + test** | Windows |
| python3 redbus_setup_and_test.py | ⭐ | **One-click setup + test** | Mac/Linux |
| setup.bat/sh | 🔧 | Basic setup | All |
| test-api.ps1 | 🧪 | API testing | Windows |
| start-dev.bat/sh | 🏃 | Development mode | All |

### 📂 tests/ (Key Tests)

| File | Icon | Purpose | Tests |
|------|------|---------|-------|
| redbus_end_to_end_test.py | ⭐ | **Complete E2E suite** | 27 |
| simple_api_test.py | 🧪 | Quick API verification | 10 |
| run_complete_test.py | 🤖 | Automated test runner | N/A |

---

## 🌊 Data Flow Visualization

### User Registration & Login

```
Client
  │
  │ POST /api/auth/register
  ▼
AuthController
  │
  │ registerUser(RegisterRequest)
  ▼
AuthService
  │
  ├─> Hash password (BCrypt)
  ├─> Save to PostgreSQL
  └─> Generate JWT token
  │
  ▼
Return AuthResponse
  └─> { token, user info }
```

### Bus Search

```
Client
  │
  │ POST /api/public/search
  ▼
BusSearchController
  │
  │ searchBuses(SearchRequest)
  ▼
BusSearchService
  │
  ├─> Query Elasticsearch
  ├─> Check availability (Redis)
  └─> Enrich data
  │
  ▼
Return List<BusSearchResponse>
```

### Booking Creation

```
Client (with JWT)
  │
  │ POST /api/bookings
  ▼
JWT Filter
  │ Validate token
  │ Set SecurityContext
  ▼
BookingController
  │
  │ createBooking(BookingRequest)
  ▼
BookingService
  │
  ├─> Validate schedule
  ├─> Check seat availability
  ├─> Lock seats
  ├─> Create booking (PostgreSQL)
  ├─> Generate tracking link
  └─> Update cache (Redis)
  │
  ▼
Return BookingResponse
  └─> { booking, tracking link }
```

---

## 🏗️ Architecture Layers

```
┌─────────────────────────────────────────┐
│         CLIENT (Browser/App)            │
└─────────────────┬───────────────────────┘
                  │ HTTP/REST
                  ▼
┌─────────────────────────────────────────┐
│    SECURITY LAYER (JWT Filter)          │
│  - Token validation                      │
│  - User authentication                   │
│  - Role authorization                    │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│    CONTROLLER LAYER (REST API)           │
│  - AuthController                        │
│  - BusManagementController               │
│  - BookingController                     │
│  - etc.                                  │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│    SERVICE LAYER (Business Logic)        │
│  - AuthService                           │
│  - BusService                            │
│  - BookingService                        │
│  - etc.                                  │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│    REPOSITORY LAYER (Data Access)        │
│  - JPA Repositories                      │
│  - Elasticsearch Repositories            │
└─────────────────┬───────────────────────┘
                  │
        ┌─────────┼─────────┐
        │         │         │
        ▼         ▼         ▼
┌──────────┐ ┌───────┐ ┌──────────────┐
│PostgreSQL│ │ Redis │ │Elasticsearch │
└──────────┘ └───────┘ └──────────────┘
```

---

## 🎨 Color Legend

Throughout the documentation, we use icons to help you quickly identify file types and purposes:

| Icon | Meaning |
|------|---------|
| ⭐ | **Start here / Most important** |
| 📚 | Documentation |
| 🚀 | Quick start / Setup |
| 🏗️ | Architecture / Design |
| 🗺️ | Navigation / Structure |
| 🔧 | Configuration / Setup |
| 🧪 | Testing |
| 💻 | Source code |
| 🐳 | Docker / Containers |
| 📦 | Dependencies |
| 🔐 | Security |
| 🌐 | API / Web |
| 💾 | Database |
| 🔍 | Search |
| 📊 | Data / Analytics |
| 🤖 | Automation |
| ✅ | Success / Verified |
| ❌ | Error / Issue |
| 🗑️ | Temporary / Deprecated |

---

## 📏 Scale of Documentation

```
Complexity / Detail Level
│
│  High  │ ARCHITECTURE.md (30 min)
│        │ PROJECT_STRUCTURE.md (15 min)
│        │ README.md (20 min)
│        │
│ Medium │ SETUP_README.md (20 min)
│        │ api-testing-guide.md (15 min)
│        │
│  Low   │ GETTING_STARTED.md (5 min) ⭐
│        │ QUICK_START.md (10 min)
│        │ INDEX.md (5 min)
│        │
└────────┴─────────────────────────────▶
         Beginner → Advanced (Audience)
```

---

## 🔄 Development Workflow Visualization

```
┌─────────────────┐
│  1. Clone Repo  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  2. Read Docs   │
│  - GETTING_     │
│    STARTED.md   │
│  - PROJECT_     │
│    STRUCTURE.md │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  3. Run Setup   │
│  scripts/run_   │
│  redbus_setup   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  4. Explore     │
│  - Browse src/  │
│  - Read code    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  5. Make Change │
│  - Edit code    │
│  - Add feature  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  6. Test        │
│  - Run E2E      │
│  - Verify       │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  7. Commit      │
│  - Git add      │
│  - Git commit   │
│  - Git push     │
└─────────────────┘
```

---

## 🎯 Quick Decision Tree

```
Do you want to...

├─ Run the app quickly?
│  └─> scripts/python redbus_setup_and_test.py/sh
│
├─ Understand the project?
│  └─> GETTING_STARTED.md → README.md
│
├─ Navigate the code?
│  └─> docs/PROJECT_STRUCTURE.md
│
├─ Understand the design?
│  └─> docs/ARCHITECTURE.md
│
├─ Test the APIs?
│  └─> docs/api-testing-guide.md
│
├─ Find a specific file?
│  └─> docs/PROJECT_STRUCTURE.md (navigation guide)
│
└─ See all documentation?
   └─> docs/INDEX.md
```

---

## 📖 Reading Path Visualization

### Fast Track (15 min)
```
GETTING_STARTED.md (5 min)
         ↓
QUICK_START.md (10 min)
         ↓
     RUN APP ✅
```

### Developer Path (90 min)
```
GETTING_STARTED.md (5 min)
         ↓
    RUN APP (5 min)
         ↓
PROJECT_STRUCTURE.md (15 min)
         ↓
  ARCHITECTURE.md (30 min)
         ↓
   EXPLORE CODE (30 min)
         ↓
    RUN TESTS (5 min)
         ↓
 START CODING ✅
```

### Complete Path (150 min)
```
     GETTING_STARTED.md
              ↓
         README.md
              ↓
    PROJECT_STRUCTURE.md
              ↓
      ARCHITECTURE.md
              ↓
      SETUP_README.md
              ↓
    api-testing-guide.md
              ↓
       EXPLORE CODE
              ↓
        RUN TESTS
              ↓
        EXPERT ✅
```

---

**Visual Guide v1.0 | Last Updated: October 2025**

**Next:** [GETTING_STARTED.md](../GETTING_STARTED.md) ⭐

