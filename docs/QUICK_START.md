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

### Windows Users:

1. Open File Explorer
2. Navigate to the RedBus folder → `scripts` folder
3. Double-click `python redbus_setup_and_test.py`
4. Press Enter when prompted
5. Wait 2-5 minutes

### Mac/Linux Users:

1. Open Terminal
2. Navigate to project:
```bash
cd /path/to/RedBus/scripts
```
3. Make script executable:
```bash
chmod +x python3 redbus_setup_and_test.py
```
4. Run it:
```bash
./python3 redbus_setup_and_test.py
```
5. Press Enter when prompted
6. Wait 2-5 minutes

---

## Step 5: Verify It's Working

The script will automatically:
- ✅ Start database
- ✅ Start cache
- ✅ Start search engine
- ✅ Start application
- ✅ Run 27 automated tests

At the end, you should see:
```
========================================
Tests Passed: 27
Tests Failed: 0
Success Rate: 100.0%
========================================
```

🎉 **Success! Your application is now running!**

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

