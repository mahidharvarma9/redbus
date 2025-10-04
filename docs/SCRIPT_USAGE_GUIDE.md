# 🎯 RedBus Setup Script - Complete Usage Guide

## 📍 Important: Where to Run the Script

**⚠️ CRITICAL**: The script **MUST** be run from the **project root directory**.

### ✅ Correct Usage

```bash
# Navigate to project root (where docker-compose.yml is)
cd /path/to/redbus

# Then run the script
python scripts/redbus_setup_and_test.py   # Windows
python3 scripts/redbus_setup_and_test.py  # Mac/Linux
```

### ❌ Incorrect Usage (Will Fail)

```bash
# DON'T do this - won't work!
cd scripts
python redbus_setup_and_test.py  # ❌ WRONG - missing required files
```

### Why From Project Root?

The script needs access to:
- `docker-compose.yml` - To start services
- `Dockerfile` - To build the application
- `pom.xml` - To run Maven tests
- `src/` - Source code for testing

---

## ⏱️ Execution Time Breakdown

### First-Time Setup: **10-15 minutes**

| Step | Task | Time | Description |
|------|------|------|-------------|
| **1** | System Requirements Check | 5-10 sec | Verify Python, Docker, files |
| **2** | Build & Start Services | 5-10 min | Download images, build app (live output) |
| **3** | Wait for Health | 1-2 min | PostgreSQL, Redis, Elasticsearch, App |
| **3.5** | Seed Test Data | 15-30 sec | Create sample operators, buses, routes |
| **3.6** | Run JUnit Tests | 1-2 min | 66 unit tests with coverage |
| **4** | Run E2E Tests | 30-60 sec | 27 comprehensive integration tests |
| **5** | Display Results | 5-10 sec | Summary, coverage, system status |

### Subsequent Runs: **3-5 minutes**

| Step | Task | Time | Description |
|------|------|------|-------------|
| **0** | Service Detection | 5 sec | Check if services already running |
| **3.6** | Run JUnit Tests | 1-2 min | 66 unit tests (services reused) |
| **4** | Run E2E Tests | 30-60 sec | 27 tests (fast - no setup) |
| **5** | Display Results | 5 sec | Summary and status |

---

## 📊 What You'll See - Complete Output Guide

### Phase 1: Setup (Step 1-3)

```
================================================================================
REDBUS COMPLETE SETUP & TEST SUITE
================================================================================

[INFO] Checking if services are already running...
[STEP] STEP 1: Checking System Requirements
--------------------------------------------------
[SUCCESS] Python 3.9.5 - OK
[SUCCESS] Docker found: 28.4.0
[SUCCESS] Docker daemon is running
[SUCCESS] Found docker-compose.yml
[SUCCESS] Found Dockerfile
[SUCCESS] Found pom.xml

[STEP] STEP 2: Setting Up RedBus Services
--------------------------------------------------
[SETUP] Stopping any existing containers...
[SETUP] Building and starting all services...
[INFO] Showing live build output:
```

**Live Build Output** - You'll see Maven compilation in real-time:
```
[INFO] Building redbus-app 1.0.0
[INFO] Compiling 88 source files...
[INFO] BUILD SUCCESS
```

### Phase 2: JUnit Tests (Step 3.6)

```
[STEP] STEP 3.6: Running JUnit Test Suite with Coverage
--------------------------------------------------
[INFO] Executing Java unit tests with JaCoCo coverage...
```

**Test Output** - Shows all 66 tests:
```
[INFO] Running com.redbus.service.BookingServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.BusOperatorServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.BusServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.PaymentServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.RouteServiceTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.ScheduleServiceTest
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.redbus.service.UserServiceTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] Tests run: 66, Failures: 0, Errors: 0, Skipped: 0

[SUCCESS] Code Coverage - Instructions: 37%, Branches: 100%
[SUCCESS] [OK] JUnit tests completed successfully
  Coverage report: target/site/jacoco/index.html
```

### Phase 3: End-to-End Tests (Step 4)

```
[STEP] STEP 4: Running Comprehensive Tests
--------------------------------------------------
```

**Each Test Shows Full Details:**

#### Example: Test 1 - Admin Registration
```
[TEST] Test 1: Admin User Registration
[INFO] API Request: POST /auth/register
[INFO] Purpose: Creating admin user for testing
[INFO] Request data: {
  "username": "admin_test_1759608583",
  "email": "admin_test_1759608583@redbus.com",
  "password": "TestPass123!",
  "firstName": "Test",
  "lastName": "Admin",
  "role": "ADMIN"
}
[INFO] Response: 200 
[INFO] Response data: {
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": null,
  "id": 87,
  "username": "admin_test_1759608583",
  "email": "admin_test_1759608583@redbus.com",
  "firstName": "Test",
  "lastName": "Admin",
  "role": "ADMIN"
}
[SUCCESS] Admin user created successfully (ID: 87)
```

#### Example: Test 7 - Public Search
```
[TEST] Test 7: Public Search (No Authentication Required)
[INFO] API Request: POST /public/search
[INFO] Purpose: Searching for available buses from Mumbai to Pune
[INFO] Request data: {
  "origin": "Mumbai",
  "destination": "Pune",
  "travelDate": "2025-10-06"
}
[INFO] Response: 200 
[INFO] Response data: [
  {
    "scheduleId": 4,
    "busId": 1,
    "busNumber": "RB001",
    "busType": "AC",
    "operatorName": "RedBus Express",
    "origin": "Mumbai",
    "destination": "Pune",
    "departureTime": "06:00:00",
    "arrivalTime": "09:30:00",
    "price": 500,
    "totalSeats": 40,
    "availableSeats": 40,
    "amenities": ["WiFi", "AC", "Water"],
    "duration": "3h 30m"
  },
  ... (9 more results)
]
[SUCCESS] [OK] Search found 10 available buses
```

#### Example: Test 11 - Complete Booking Journey
```
================================================================================
SEAT SELECTION & BOOKING PROCESS
================================================================================

Step 1: Bus & Schedule Selection
  Selected Bus: RB001
  Operator: RedBus Express
  Route: Mumbai -> Pune
  Schedule ID: 4
  Available Seats: 40
  Price per Seat: $500

Step 2: Checking Available Seats
[INFO] API Request: GET /bookings/schedule/4/date/2025-10-06/booked-seats
[INFO] Response: 200 
[INFO] Response data: [10, 1, 2, 19, 20, 11, 27, 8, 25, 21, 32, 5, 22, 6, 35, 14, 18, 4, 23]
  Already booked seats: [10, 1, 2, 19, 20, 11, 27, 8, 25, 21, 32, 5, 22, 6, 35, 14, 18, 4, 23]
  Available seats count: 21
  Selecting available seats: 16, 17

Step 3: Creating Booking
[INFO] API Request: POST /bookings
[INFO] Request data: {
  "scheduleId": 4,
  "travelDate": "2025-10-06",
  "passengers": [
    {
      "seatNumber": 16,
      "passengerName": "John Customer",
      "passengerAge": 30,
      "passengerGender": "MALE"
    },
    {
      "seatNumber": 17,
      "passengerName": "Jane Customer",
      "passengerAge": 28,
      "passengerGender": "FEMALE"
    }
  ]
}
[INFO] Response: 201 
[INFO] Response data: {
  "id": 15,
  "bookingReference": "RB4A795078",
  "bookingDate": "2025-10-06",
  "totalSeats": 2,
  "totalAmount": 1000.0,
  "status": "PENDING",
  "trackingLink": "http://localhost:9090/api/tracking/booking/RB4A795078",
  ...
}

Booking Successful!
  Booking Reference: RB4A795078
  Booking ID: 15
  Total Amount: $1000.0

  [TRACK YOUR BUS]
  http://localhost:9090/api/tracking/booking/RB4A795078
  Use this link to track your bus in real-time!
```

### Phase 4: Final Results (Step 5)

```
[STEP] STEP 5: Test Results Summary
================================================================================
REDBUS APPLICATION TEST RESULTS
================================================================================
JUNIT TEST COVERAGE:
  Instruction Coverage: 37%
  Branch Coverage: 100%
  Report: target/site/jacoco/index.html

END-TO-END TEST RESULTS:
Total Tests Run: 27
Tests Passed: 27
Tests Failed: 0
Success Rate: 100.0%

DETAILED TEST RESULTS:
--------------------------------------------------------------------------------
[PASS] Admin Registration        PASS   User ID: 87
[PASS] User Login                PASS   Authentication working
[PASS] User Profile              PASS   Role: ADMIN
[PASS] List Operators            PASS   Found: 58
[PASS] Create Operator           PASS   ID: 59
[PASS] List Routes               PASS   Found: 33
[PASS] Create Route              PASS   ID: 34
[PASS] List Buses                PASS   Found: 47
[PASS] Create Bus                PASS   ID: 50
[PASS] List Schedules            PASS   Found: 43
[PASS] Create Schedule           PASS   ID: 49
[PASS] ES Sync Verification      PASS   Bus TEST-BUS-1759608585 indexed
[PASS] Public Search             PASS   Found: 10 buses
[PASS] System Health             PASS   Status: UP
[PASS] Regular User Registration PASS   User ID: 88
[PASS] Regular User Login        PASS   Login working
[PASS] Enhanced Bus Search       PASS   Found 10 buses
[PASS] Booking Creation          PASS   Booking ID: 15
[PASS] Booking Retrieval         PASS   Booking details retrieved
[PASS] User Booking History      PASS   Found 1 bookings
[PASS] Seat Validation           PASS   Duplicate booking prevented
[PASS] Booking Cancellation      PASS   Booking 15 cancelled
[PASS] Bus Tracking - Current    PASS   Location query successful
[PASS] Bus Tracking - History    PASS   3 records
[PASS] Advanced Operator Management PASS   Created operator and luxury bus
[PASS] Data Sync Status          PASS   DB: 44, ES: 46
[PASS] Manual Sync               PASS   Sync triggered successfully

SYSTEM STATUS:
--------------------------------------------------------------------------------
[RUNNING] PostgreSQL Database  OK
[RUNNING] Redis Cache          OK
[RUNNING] Elasticsearch        YELLOW
[RUNNING] RedBus Application   OK

EXCELLENT! RedBus application is fully functional!
* All core features are working correctly
* Authentication system operational
* Business logic functioning properly
* Database operations successful
* Search functionality active

Total execution time: 0:04:52
================================================================================
```

---

## 🧪 All 27 End-to-End Tests Explained

### Authentication Tests (3 tests)
1. **Admin Registration** - Creates admin user, gets JWT token
2. **User Login** - Tests login with credentials
3. **User Profile** - Retrieves user profile with role

### Operator Management (2 tests)
4. **List Operators** - Gets all bus operators
5. **Create Operator** - Adds new bus company

### Route Management (2 tests)
6. **List Routes** - Gets all available routes
7. **Create Route** - Adds new city-to-city route

### Bus Management (2 tests)
8. **List Buses** - Gets all buses
9. **Create Bus** - Adds new bus with amenities

### Schedule Management (3 tests)
10. **List Schedules** - Gets all schedules
11. **Create Schedule** - Adds new bus schedule
12. **ES Sync Verification** - Checks Elasticsearch indexing

### Search Tests (2 tests)
13. **Public Search** - Searches buses without authentication
14. **System Health** - Checks application health status

### User Journey Tests (12 tests)
15. **Regular User Registration** - Creates customer account
16. **Regular User Login** - Tests customer login
17. **Enhanced Bus Search** - Detailed search with results
18. **Booking Creation** - Creates bus booking
19. **Booking Retrieval** - Gets booking details
20. **User Booking History** - Lists user's bookings
21. **Seat Validation** - Prevents duplicate seat booking
22. **Booking Cancellation** - Cancels existing booking
23. **Bus Tracking - Current** - Gets real-time location
24. **Bus Tracking - History** - Gets location history

### Advanced Tests (3 tests)
25. **Advanced Operator Management** - Creates luxury bus
26. **Data Sync Status** - Checks DB vs ES sync
27. **Manual Sync** - Triggers manual synchronization

---

## 📁 Output Files Generated

After running, you'll find:

1. **Code Coverage Report**
   - Location: `target/site/jacoco/index.html`
   - Open in browser to see detailed coverage

2. **Test Results**
   - JUnit: `target/surefire-reports/`
   - Maven logs: Console output

3. **Application Logs**
   - View with: `docker-compose logs app`

---

## 🎓 Quick Reference

### Run the script:
```bash
# From project root
python scripts/redbus_setup_and_test.py
```

### Stop services:
```bash
docker-compose down
```

### View logs:
```bash
docker-compose logs -f app
```

### Access coverage report:
```bash
# Open in browser
open target/site/jacoco/index.html     # Mac
start target/site/jacoco/index.html    # Windows
xdg-open target/site/jacoco/index.html # Linux
```

---

## 🆘 Troubleshooting

### Error: "Missing docker-compose.yml"
**Cause**: Running from wrong directory  
**Solution**: Navigate to project root first

### Error: "Docker daemon not running"
**Cause**: Docker Desktop not started  
**Solution**: Start Docker Desktop and wait for it to be ready

### Tests take too long
**Cause**: First-time image download  
**Solution**: Normal for first run (10-15 min), subsequent runs are 3-5 min

---

**Happy Testing! 🚀**

