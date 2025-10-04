# 🚌 RedBus Complete Setup & Test Suite

## 🎯 What This Does

This script will **automatically**:
- ✅ Check if your system has everything needed
- ✅ Set up all databases and services
- ✅ Build and deploy the RedBus application
- ✅ Run comprehensive tests on all features (66 JUnit + 27 E2E tests)
- ✅ Show you detailed test outputs and responses
- ✅ Display detailed results and code coverage

**NO TECHNICAL KNOWLEDGE REQUIRED** - Just run the script!

## 📋 Requirements

1. **Python 3.7+** (Most computers have this)
2. **Docker Desktop** - Download from: https://www.docker.com/products/docker-desktop

## 🚀 How to Run

**⚠️ IMPORTANT**: The script must be run from the **project root directory** (where `docker-compose.yml` is located).

### Windows:
```cmd
cd C:\path\to\redbus
python scripts\redbus_setup_and_test.py
```

### Mac/Linux:
```bash
cd /path/to/redbus
python3 scripts/redbus_setup_and_test.py
```

## ⏱️ Execution Time Breakdown

**Total Time: 5-15 minutes** (depending on first-time setup or subsequent runs)

| Step | Description | First Time | Subsequent |
|------|-------------|-----------|-----------|
| **Step 1** | Check System Requirements | 5-10 sec | 5-10 sec |
| **Step 2** | Build & Start Services | 5-10 min | 30-60 sec |
| **Step 3** | Wait for Services Health | 1-2 min | 30-45 sec |
| **Step 3.5** | Seed Initial Test Data | 15-30 sec | 15-30 sec |
| **Step 3.6** | Run JUnit Tests (66 tests) | 1-2 min | 1-2 min |
| **Step 4** | Run E2E Tests (27 tests) | 30-60 sec | 30-60 sec |
| **Step 5** | Display Results | 5-10 sec | 5-10 sec |

**First Run**: 10-15 minutes (downloads Docker images, builds application)  
**Subsequent Runs**: 3-5 minutes (everything is cached)

## 📊 What You'll See - Detailed Test Outputs

The script provides **comprehensive real-time feedback** including:

### 🔧 Setup Phase Output
- Docker version checks
- Service startup logs (live build output)
- Database connection status
- Health check results

### 🧪 JUnit Test Output (Step 3.6)
The script runs **66 unit tests** and shows:
- Live test execution output
- Test results for each service:
  - `BookingServiceTest` (6 tests)
  - `BusOperatorServiceTest` (9 tests)
  - `BusServiceTest` (7 tests)
  - `PaymentServiceTest` (9 tests)
  - `RouteServiceTest` (11 tests)
  - `ScheduleServiceTest` (13 tests)
  - `UserServiceTest` (7 tests)
- **Code coverage report** (JaCoCo):
  - Instruction coverage percentage
  - Branch coverage percentage
  - HTML report location: `target/site/jacoco/index.html`

### 🌐 End-to-End Test Output (Step 4)
The script runs **27 comprehensive E2E tests** with detailed responses:

1. **Authentication Tests** (~10 sec)
   - Admin user registration with JWT token
   - User login with response data
   - Profile retrieval

2. **Operator Management Tests** (~15 sec)
   - List operators (shows count)
   - Create operator (shows ID and details)

3. **Route & Bus Management** (~20 sec)
   - Route creation with distance/duration
   - Bus creation with seat count and amenities
   - Schedule creation with pricing

4. **Search Tests** (~15 sec)
   - Public bus search (shows available buses)
   - Elasticsearch sync verification
   - Search result details (bus type, price, seats)

5. **Complete Booking Journey** (~30 sec)
   - Available seat checking
   - Booking creation with passengers
   - Booking reference generation
   - Tracking link provision
   - Seat validation (duplicate prevention)
   - Booking cancellation

6. **Bus Tracking Tests** (~20 sec)
   - GPS location updates
   - Current location retrieval
   - Tracking history with coordinates

7. **System Sync Tests** (~15 sec)
   - Database vs Elasticsearch count
   - Manual sync triggering

### 🎯 Each Test Shows:
- ✅ **Request Details**: HTTP method, endpoint, request body (JSON)
- ✅ **Response Data**: Full API response with status code
- ✅ **Test Result**: PASS/FAIL with specific details
- ✅ **Execution Time**: How long each test took
- ✅ **Error Messages**: Clear descriptions if anything fails

## 🎉 Final Results Summary

At the end, you'll get a **comprehensive report**:

### Test Summary
- **JUnit Coverage**: Instruction % and Branch % coverage
- **E2E Results**: Tests passed/failed (target: 27/27)
- **Success Rate**: Overall percentage
- **Total Execution Time**: Complete runtime

### System Status
- PostgreSQL: ✅ RUNNING
- Redis: ✅ RUNNING
- Elasticsearch: ✅ RUNNING (status: YELLOW/GREEN)
- RedBus Application: ✅ RUNNING

### Created Test Data
- Admin users with IDs
- Operators with license numbers
- Routes with distances
- Buses with seat counts
- Schedules with pricing
- Sample bookings

### Usage Instructions
- API endpoints
- Health check URL
- How to interact with the application

## 🆘 If Something Goes Wrong

The script will:
- Show clear error messages
- Clean up automatically
- Give you specific instructions on what to fix

Common issues:
- **Docker not running**: Start Docker Desktop first
- **Port conflicts**: The script handles this automatically
- **Missing files**: Run from the RedBus project folder

## 🌐 After Setup

Your RedBus application will be available at:
- **Main URL**: http://localhost:9090/api
- **Health Check**: http://localhost:9090/api/actuator/health

## ❓ What Gets Tested

The script tests all major features:
1. **User Registration & Login** 
2. **Bus Operator Management**
3. **Route Creation**
4. **Bus Fleet Management** 
5. **Schedule Management**
6. **Public Bus Search**
7. **System Health**
8. **Database Operations**

## 🛑 Stopping the Application

To stop all services:
```bash
docker-compose down
```

## 📞 Need Help?

If you encounter issues:
1. Check the error messages in the script output
2. Make sure Docker Desktop is running
3. Run from the correct folder (where docker-compose.yml is located)
4. Try running the script again (it cleans up automatically)

---

**That's it! Just run the script and watch your RedBus application come to life! 🚌✨**

