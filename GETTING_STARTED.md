# 🚀 Getting Started with RedBus

Welcome! This guide will help you get up and running with the RedBus application in just a few minutes.

---

## ⚡ Quick Start (3 Minutes)

### Prerequisites

Make sure you have these installed:
- ✅ **Docker Desktop** - [Download](https://www.docker.com/products/docker-desktop) (Must be running!)
- ✅ **Python 3.7+** - [Download](https://www.python.org/downloads/)

### One-Command Setup & Test

Navigate to the scripts folder and run the setup script:

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

> **Note**: The script is cross-platform and works on Windows, Mac, and Linux without any modifications!

### What Happens Automatically

The script intelligently handles everything:

**If services are NOT running (First Time):**
1. ✅ Checks if Docker is running
2. ✅ Builds all Docker images with live output
3. ✅ Starts all services (PostgreSQL, Redis, Elasticsearch, App)
4. ✅ Waits for services to be healthy (~2 minutes)
5. ✅ Seeds initial test data
6. ✅ Runs JUnit tests with coverage report
7. ✅ Runs 27 comprehensive end-to-end tests
8. ✅ Displays detailed results

**If services ARE already running (Subsequent Runs):**
1. ✅ Detects healthy services
2. ✅ Skips Docker setup (saves time!)
3. ✅ Runs 27 comprehensive end-to-end tests directly
4. ✅ Displays results (~15 seconds)

**Expected Output:**
```
================================================================================
REDBUS APPLICATION TEST RESULTS
================================================================================
Total Tests Run: 27
Tests Passed: 27
Tests Failed: 0
Success Rate: 100.0%

SYSTEM STATUS:
[RUNNING] PostgreSQL Database  OK
[RUNNING] Redis Cache          OK
[RUNNING] RedBus Application   OK
[RUNNING] Elasticsearch        YELLOW

EXCELLENT! RedBus application is fully functional!
================================================================================
```

🎉 **Done!** Your application is now running at http://localhost:9090

---

## 📚 What to Read Next

Choose based on your role:

### 👨‍💻 **For Developers**

1. **Understand the codebase:**
   - Read [Project Structure](docs/PROJECT_STRUCTURE.md)
   - Review [Architecture](docs/ARCHITECTURE.md)

2. **Start coding:**
   - Explore `src/main/java/com/redbus/`
   - Check existing entities in `entity/`
   - Review controllers in `controller/`

3. **Run tests:**
   ```bash
   cd scripts
   python redbus_setup_and_test.py
   ```

### 👨‍🔬 **For QA/Testers**

1. **Test the APIs:**
   - Use the comprehensive test suite in `scripts/`
   - The script automatically runs 27 end-to-end tests
   - Try manual API calls with Postman

2. **Run tests:**
   ```bash
   cd scripts
   python redbus_setup_and_test.py    # Setup + Full E2E suite (27 tests)
   ```
   
   **Note:** The script is smart - it skips setup if services are already running!

### 🏗️ **For Architects**

1. **Review system design:**
   - Read [Architecture Document](docs/ARCHITECTURE.md)
   - Understand the high-level design
   - Review database schema

2. **Analyze the components:**
   - Security architecture
   - Data flow diagrams
   - Scalability considerations

### 👨‍💼 **For Non-Technical Users**

1. **Get started:**
   - Read [Quick Start Guide](docs/QUICK_START.md)
   - Follow step-by-step instructions
   - Check troubleshooting section

2. **Understand features:**
   - Read the [Main README](README.md)
   - Check feature list
   - See what the system can do

---

## 🗺️ Repository Structure

```
redbus/
├── 📂 src/          # Java source code (application)
├── 📂 docs/         # All documentation
├── 📂 scripts/      # Setup & E2E test automation
│   └── redbus_setup_and_test.py    # Main setup & test script (cross-platform)
├── 📂 tmp/          # Old/temporary files (ignored)
├── 📄 README.md     # Comprehensive documentation
└── 📄 docker-compose.yml  # Services configuration
```

---

## 🎯 Common Tasks

### Starting the Application & Running Tests

**Recommended - One command does it all:**
```bash
cd scripts
python redbus_setup_and_test.py
```
*This script is intelligent - it will:*
- Setup everything if needed (first run)
- Skip setup if services are healthy (subsequent runs)
- Always run comprehensive tests to verify everything works

**Alternative - Docker only (no tests):**
```bash
docker-compose up -d
```

### Stopping the Application

```bash
docker-compose down
```

### Viewing Logs

```bash
docker-compose logs -f app          # Application logs
docker-compose logs -f postgres     # Database logs
docker-compose logs -f elasticsearch # Search logs
```

### Rebuilding After Code Changes

```bash
docker-compose down
docker-compose up -d --build
# Then verify with tests:
cd scripts
python redbus_setup_and_test.py
```

### Running Only Tests (without setup)

**The script automatically detects if services are running!**
```bash
cd scripts
python redbus_setup_and_test.py    # Skips setup if services are healthy
```

---

## 🔧 Configuration

### Accessing Services

| Service | URL | Port |
|---------|-----|------|
| Application API | http://localhost:9090/api | 9090 |
| PostgreSQL | localhost | 5432 |
| Redis | localhost | 6379 |
| Elasticsearch | http://localhost:9200 | 9200 |
| Health Check | http://localhost:9090/actuator/health | 9090 |

### Default Credentials

**Database:**
- Username: `postgres`
- Password: `postgres`
- Database: `redbus`

**Test Users (created by E2E tests):**
- Admin: `admin_user` / `Admin@123`
- Operator: `operator_user` / `Operator@123`
- User: `test_user` / `User@123`

---

## 📖 Documentation Guide

| Document | When to Read | Time |
|----------|-------------|------|
| **GETTING_STARTED.md** (this file) | First time setup | 5 min |
| [QUICK_START.md](docs/QUICK_START.md) | Non-technical users | 10 min |
| [README.md](README.md) | Complete overview | 20 min |
| [PROJECT_STRUCTURE.md](docs/PROJECT_STRUCTURE.md) | Navigate codebase | 15 min |
| [ARCHITECTURE.md](docs/ARCHITECTURE.md) | Understand design | 30 min |
| [SETUP_README.md](docs/SETUP_README.md) | Detailed setup | 20 min |
| [api-testing-guide.md](docs/api-testing-guide.md) | API testing | 15 min |

**Reading Order:**
1. GETTING_STARTED.md ← **You are here**
2. QUICK_START.md (if non-technical) OR README.md (if technical)
3. PROJECT_STRUCTURE.md (to navigate code)
4. ARCHITECTURE.md (to understand design)

---

## 🧪 Testing Features

The comprehensive test suite (`scripts/redbus_setup_and_test.py`) includes:

### **Unit Tests (66 tests via JUnit + JaCoCo)**
- ✅ Service layer tests (BookingService, BusService, UserService, etc.)
- ✅ Controller tests (AuthController)
- ✅ Code coverage reporting (44% instruction, 23% branch, 60% line coverage)
- **Runs on first setup or fresh deployment**

### **End-to-End Tests (27 tests)**
- ✅ **Authentication** (Admin & User registration, login)
- ✅ **Bus Management** (Operators, buses, routes, schedules)
- ✅ **Search** (Public bus search with Elasticsearch sync verification)
- ✅ **Booking Journey** (Smart seat selection, booking creation, history)
- ✅ **Seat Validation** (Duplicate booking prevention)
- ✅ **Tracking** (Real-time GPS tracking with booking reference)
- ✅ **Cancellation** (Booking cancellation with seat release)
- ✅ **Data Sync** (Elasticsearch synchronization verification)
- ✅ **System Health** (All services monitoring)
- **Runs on every execution**

**Total: 66 Unit Tests + 27 E2E Tests = 93 automated tests**

---

## ❓ Troubleshooting

### Docker not running
**Error:** `Cannot connect to Docker daemon`  
**Solution:** Start Docker Desktop and wait for it to fully start

### Port already in use
**Error:** `Port 9090 is already allocated`  
**Solution:** 
```bash
# Find and stop the process using port 9090
netstat -ano | findstr :9090    # Windows
lsof -ti:9090 | xargs kill -9   # Mac/Linux
```

### Services not healthy
**Error:** Tests failing or services not starting  
**Solution:** 
```bash
# Stop everything
docker-compose down
# Start fresh
cd scripts
python redbus_setup_and_test.py
```

### Python not found
**Error:** `python: command not found`  
**Solution:** Install Python 3.7+ and add to PATH

### Tests failing
**Solution:** 
```bash
cd scripts
python redbus_setup_and_test.py
```
The script will automatically rebuild and test everything

---

## 🚀 Next Steps

1. ✅ **You've started the application** - Great!
2. 📚 **Read the documentation** - Choose from the guide above
3. 🔍 **Explore the code** - Check `src/main/java/com/redbus/`
4. 🧪 **Run tests** - Verify everything works
5. 💻 **Start developing** - Make your first change!

---

## 📞 Need Help?

### Quick Links
- [Troubleshooting Guide](README.md#troubleshooting)
- [API Documentation](README.md#api-documentation)
- [Architecture Details](docs/ARCHITECTURE.md)
- [Project Structure](docs/PROJECT_STRUCTURE.md)

### Common Questions

**Q: How do I add a new feature?**  
A: Read [Project Structure](docs/PROJECT_STRUCTURE.md) → "I want to add a new feature"

**Q: Where are the API endpoints?**  
A: Check `src/main/java/com/redbus/controller/`

**Q: How do I modify the database?**  
A: Add a new Flyway migration in `src/main/resources/db/migration/`

**Q: Where is the business logic?**  
A: In `src/main/java/com/redbus/service/`

**Q: How do I test my changes?**  
A: Run `cd scripts && python redbus_setup_and_test.py` (runs all 93 tests)

---

## 🎉 Success Criteria

You're ready to start developing when:

- ✅ Application starts without errors
- ✅ All 66 JUnit tests pass (unit tests)
- ✅ All 27 E2E tests pass (integration tests)
- ✅ You can access http://localhost:9090/actuator/health
- ✅ You understand the project structure
- ✅ You've read the relevant documentation

**Run this to verify:**
```bash
cd scripts
python redbus_setup_and_test.py
```

---

**Welcome to RedBus! Happy coding! 🚌**

---

*Last Updated: October 2025*  
*Version: 1.0*

