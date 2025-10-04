# 🚀 Quick Start Guide

## For Non-Technical Users

This guide will help you run the RedBus application in **5 simple steps**, even if you have zero coding knowledge.

---

## Step 1: Install Docker Desktop

1. Go to https://www.docker.com/products/docker-desktop
2. Download Docker Desktop for your operating system (Windows/Mac)
3. Install it by double-clicking the downloaded file
4. Start Docker Desktop (it should show a whale icon in your system tray)

**Wait until Docker Desktop shows "Docker is running"**

---

## Step 2: Install Python

1. Go to https://www.python.org/downloads/
2. Download Python 3.7 or higher
3. **Important:** During installation, check "Add Python to PATH"
4. Complete the installation

To verify: Open Command Prompt (Windows) or Terminal (Mac) and type:
```bash
python --version
```
You should see something like "Python 3.9.5"

---

## Step 3: Download the RedBus Project

1. Download the project ZIP file or clone from repository
2. Extract the ZIP to a folder like `C:\RedBus` or `/Users/YourName/RedBus`
3. Remember this location - you'll need it!

---

## Step 4: Run the Setup Script

**⚠️ IMPORTANT**: You must run the script from the **project root directory** (not from inside the scripts folder).

### Windows Users:

1. Open Command Prompt (Windows Key + R, type `cmd`, press Enter)
2. Navigate to the RedBus project root folder:
```cmd
cd C:\RedBus
```
3. Run the script:
```cmd
python scripts\redbus_setup_and_test.py
```
4. Watch the magic happen! ✨

### Mac/Linux Users:

1. Open Terminal
2. Navigate to the RedBus project root:
```bash
cd /path/to/RedBus
```
3. Run the script:
```bash
python3 scripts/redbus_setup_and_test.py
```
4. Watch the magic happen! ✨

### ⏱️ How Long Will It Take?
- **First time**: 10-15 minutes (downloads Docker images, builds app)
- **Already ran once**: 3-5 minutes (uses cached data)

### 📊 What Will You See?
The script shows you **everything** in real-time:
- ✅ Docker setup progress (with live build logs)
- ✅ 66 JUnit unit tests running (shows each test)
- ✅ 27 end-to-end tests with full API requests/responses
- ✅ Code coverage report (shows percentages)
- ✅ System health checks
- ✅ Final summary with success rate

---

## Step 5: Verify It's Working

The script runs automatically and shows you **detailed test results**:

### Phase 1: Setup (5-10 min first time)
```
[SETUP] Building and starting all services...
Live build output...
[SUCCESS] All services started successfully!
```

### Phase 2: JUnit Tests (1-2 min)
```
[INFO] Tests run: 66, Failures: 0, Errors: 0, Skipped: 0
[SUCCESS] Code Coverage - Instructions: 37%, Branches: 100%
```

### Phase 3: End-to-End Tests (30-60 sec)
You'll see detailed output for each of the 27 tests including:
- **Request**: Full JSON request body
- **Response**: Complete API response with data
- **Result**: ✅ PASS or ❌ FAIL

Example test output:
```
[TEST] Test 1: Admin User Registration
[INFO] API Request: POST /auth/register
[INFO] Request data: {
  "username": "admin_test_123",
  "email": "admin@test.com",
  ...
}
[INFO] Response: 200 OK
[INFO] Response data: {
  "token": "eyJhbGc...",
  "id": 87,
  "role": "ADMIN"
}
[SUCCESS] Admin user created successfully (ID: 87)
```

### Final Summary
```
================================================================================
REDBUS APPLICATION TEST RESULTS
================================================================================
JUNIT TEST COVERAGE:
  Instruction Coverage: 37%
  Branch Coverage: 100%

END-TO-END TEST RESULTS:
Total Tests Run: 27
Tests Passed: 27 ✅
Tests Failed: 0
Success Rate: 100.0%

SYSTEM STATUS:
[RUNNING] PostgreSQL Database  OK
[RUNNING] Redis Cache          OK
[RUNNING] Elasticsearch        YELLOW
[RUNNING] RedBus Application   OK

Total execution time: 0:04:52
================================================================================
```

🎉 **Success! Your application is now running with all tests passed!**

---

## What Can I Do Now?

### Option 1: Use the API

The application is running at: **http://localhost:9090/api**

You can test it in your web browser or using tools like Postman.

### Option 2: Check the Dashboard

Open your browser and go to:
- **Application:** http://localhost:9090/api/actuator/health
- **Elasticsearch:** http://localhost:9200
- **Search Demo:** http://localhost:9090

---

## Common Questions

### Q: How do I stop the application?
**A:** 
```bash
docker-compose down
```

### Q: How do I restart it?
**A:** Just run the setup script again!

### Q: Where are my data stored?
**A:** In Docker volumes. They persist even when you stop the application.

### Q: How do I see what's happening?
**A:** Check logs:
```bash
docker-compose logs -f app
```

### Q: Something went wrong!
**A:** 
1. Make sure Docker Desktop is running
2. Close and restart Command Prompt/Terminal
3. Run the script again
4. Check the Troubleshooting section in README.md

---

## Next Steps

1. **Read the API Documentation:** Check `docs/API.md`
2. **Try the APIs:** Use Postman or curl
3. **Explore the Code:** Check `src/main/java/com/redbus/`

---

## Need Help?

### Error Messages

**"Docker not found"**
- Install Docker Desktop and make sure it's running

**"Python not found"**
- Reinstall Python and check "Add to PATH"

**"Port already in use"**
- Another application is using port 9090
- Solution: Stop that application or change the port in `docker-compose.yml`

**"Tests failed"**
- Wait 1 minute and run the script again
- Services might need more time to start

---

 

---

**You're all set! Happy coding! 🚀**

