# 🚌 RedBus Complete Setup & Test Suite

## 🎯 What This Does

This script will **automatically**:
- ✅ Check if your system has everything needed
- ✅ Set up all databases and services
- ✅ Build and deploy the RedBus application
- ✅ Run comprehensive tests on all features
- ✅ Show you detailed results and status

**NO TECHNICAL KNOWLEDGE REQUIRED** - Just run the script!

## 📋 Requirements

1. **Python 3.7+** (Most computers have this)
2. **Docker Desktop** - Download from: https://www.docker.com/products/docker-desktop

## 🚀 How to Run

### Windows:
```cmd
python redbus_end_to_end_test.py
```

### Mac/Linux:
```bash
python3 redbus_end_to_end_test.py
```

## ⏱️ How Long Does It Take?

- **First time**: 10-15 minutes (downloads images and builds application)
- **Subsequent runs**: 3-5 minutes (everything is cached)

## 📊 What You'll See

The script shows real-time progress with:
- 🔧 Setup steps
- 🏥 Health checks  
- 🧪 Test results
- ✅ Success indicators
- ❌ Error messages (if any)

## 🎉 Final Results

At the end, you'll get:
- **Test Summary**: How many tests passed/failed
- **System Status**: Which services are running
- **Created Data**: What test data was created
- **Usage Instructions**: How to use your application
- **Overall Verdict**: Is everything working?

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

