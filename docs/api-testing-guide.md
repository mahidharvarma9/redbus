# RedBus API Testing Guide - End-to-End Testing

## 🚀 **Prerequisites**
- RedBus application running on http://localhost:8080
- Test data populated (admin and test users created)

## 📋 **Step-by-Step API Testing**

### **Step 1: Health Check**
```bash
curl -X GET http://localhost:8080/api/actuator/health
```

### **Step 2: User Registration**
```bash
# Register Admin User
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@redbus.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User",
    "phone": "+91-9876543210"
  }'

# Register Test User
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "+91-9876543211"
  }'
```

### **Step 3: User Login**
```bash
# Login as Admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Login as Test User
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Save the JWT token from the response for subsequent API calls!**

### **Step 4: Create Bus Operators (Admin Only)**
```bash
# Create RedBus Express Operator
curl -X POST http://localhost:8080/api/operator/operators \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "name": "RedBus Express",
    "contactEmail": "contact@redbus.com",
    "contactPhone": "+91-9876543210",
    "licenseNumber": "RB001"
  }'

# Create City Transport Operator
curl -X POST http://localhost:8080/api/operator/operators \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "name": "City Transport",
    "contactEmail": "info@citytransport.com",
    "contactPhone": "+91-9876543211",
    "licenseNumber": "CT001"
  }'
```

### **Step 5: Create Routes (Admin Only)**
```bash
# Mumbai to Pune Route
curl -X POST http://localhost:8080/api/operator/routes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "origin": "Mumbai",
    "destination": "Pune",
    "distanceKm": 150.5,
    "estimatedDurationHours": 3.5
  }'

# Mumbai to Delhi Route
curl -X POST http://localhost:8080/api/operator/routes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "origin": "Mumbai",
    "destination": "Delhi",
    "distanceKm": 1400.0,
    "estimatedDurationHours": 18.0
  }'

# Delhi to Jaipur Route
curl -X POST http://localhost:8080/api/operator/routes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "origin": "Delhi",
    "destination": "Jaipur",
    "distanceKm": 280.0,
    "estimatedDurationHours": 5.0
  }'
```

### **Step 6: Create Buses (Admin Only)**
```bash
# AC Bus
curl -X POST http://localhost:8080/api/operator/buses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "operatorId": 1,
    "busNumber": "RB001",
    "busType": "AC",
    "totalSeats": 50,
    "amenities": ["WiFi", "Charging Point", "Blanket", "Water Bottle"]
  }'

# Non-AC Bus
curl -X POST http://localhost:8080/api/operator/buses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "operatorId": 1,
    "busNumber": "RB002",
    "busType": "NON_AC",
    "totalSeats": 60,
    "amenities": ["Water Bottle"]
  }'

# Sleeper Bus
curl -X POST http://localhost:8080/api/operator/buses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "operatorId": 2,
    "busNumber": "CT001",
    "busType": "SLEEPER",
    "totalSeats": 30,
    "amenities": ["WiFi", "Charging Point", "Blanket", "Pillow"]
  }'
```

### **Step 7: Create Schedules (Admin Only)**
```bash
# Morning Schedule - Mumbai to Pune
curl -X POST http://localhost:8080/api/operator/schedules \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "busId": 1,
    "routeId": 1,
    "departureTime": "06:00:00",
    "arrivalTime": "09:30:00",
    "price": 500.00,
    "isRecurring": true,
    "daysOfWeek": [1,2,3,4,5,6,7]
  }'

# Afternoon Schedule - Mumbai to Pune
curl -X POST http://localhost:8080/api/operator/schedules \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "busId": 1,
    "routeId": 1,
    "departureTime": "14:00:00",
    "arrivalTime": "17:30:00",
    "price": 500.00,
    "isRecurring": true,
    "daysOfWeek": [1,2,3,4,5,6,7]
  }'

# Evening Schedule - Mumbai to Delhi
curl -X POST http://localhost:8080/api/operator/schedules \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "busId": 3,
    "routeId": 2,
    "departureTime": "18:00:00",
    "arrivalTime": "12:00:00",
    "price": 1200.00,
    "isRecurring": true,
    "daysOfWeek": [1,2,3,4,5,6,7]
  }'
```

### **Step 8: Search for Buses (Public API)**
```bash
# Search Mumbai to Pune buses
curl -X POST http://localhost:8080/api/search/buses \
  -H "Content-Type: application/json" \
  -d '{
    "origin": "Mumbai",
    "destination": "Pune",
    "travelDate": "2024-01-15",
    "busType": "AC"
  }'

# Search all buses Mumbai to Delhi
curl -X POST http://localhost:8080/api/search/buses \
  -H "Content-Type: application/json" \
  -d '{
    "origin": "Mumbai",
    "destination": "Delhi",
    "travelDate": "2024-01-15"
  }'
```

### **Step 9: Book Seats (Authenticated User)**
```bash
# Book seats for a specific schedule
curl -X POST http://localhost:8080/api/booking/book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN" \
  -d '{
    "scheduleId": 1,
    "passengerDetails": [
      {
        "firstName": "John",
        "lastName": "Doe",
        "age": 30,
        "gender": "MALE",
        "seatNumber": "A1"
      }
    ],
    "contactEmail": "john@example.com",
    "contactPhone": "+91-9876543210"
  }'
```

### **Step 10: Make Payment**
```bash
# Process payment for booking
curl -X POST http://localhost:8080/api/payment/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN" \
  -d '{
    "bookingId": 1,
    "amount": 500.00,
    "paymentMethod": "CARD",
    "cardNumber": "4111111111111111",
    "expiryDate": "12/25",
    "cvv": "123"
  }'
```

### **Step 11: View Bookings**
```bash
# Get user's bookings
curl -X GET http://localhost:8080/api/booking/user-bookings \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN"

# Get specific booking details
curl -X GET http://localhost:8080/api/booking/1 \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN"
```

### **Step 12: Bus Tracking**
```bash
# Update bus location (Admin/Operator)
curl -X POST http://localhost:8080/api/tracking/update \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "busId": 1,
    "latitude": 19.0760,
    "longitude": 72.8777,
    "speedKmh": 65.5,
    "directionDegrees": 45.0
  }'

# Get bus tracking information
curl -X GET http://localhost:8080/api/tracking/bus/1 \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN"

# Get all active bus tracking
curl -X GET http://localhost:8080/api/tracking/active \
  -H "Authorization: Bearer YOUR_USER_JWT_TOKEN"
```

### **Step 13: Admin Operations**
```bash
# Get all operators
curl -X GET http://localhost:8080/api/operator/operators \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Get all routes
curl -X GET http://localhost:8080/api/operator/routes \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Get all buses
curl -X GET http://localhost:8080/api/operator/buses \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Get all schedules
curl -X GET http://localhost:8080/api/operator/schedules \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

## 🔧 **PowerShell Commands (Windows)**

For Windows PowerShell, use these commands:

```powershell
# Health Check
Invoke-WebRequest -Uri http://localhost:8080/api/actuator/health -UseBasicParsing

# Login and get token
$loginResponse = Invoke-WebRequest -Uri http://localhost:8080/api/auth/login -Method POST -ContentType "application/json" -Body '{"username": "admin", "password": "admin123"}' -UseBasicParsing
$token = ($loginResponse.Content | ConvertFrom-Json).token

# Search buses
Invoke-WebRequest -Uri http://localhost:8080/api/search/buses -Method POST -ContentType "application/json" -Body '{"origin": "Mumbai", "destination": "Pune", "travelDate": "2024-01-15"}' -UseBasicParsing
```

## 📊 **Expected Response Examples**

### **Login Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@redbus.com",
    "firstName": "Admin",
    "lastName": "User"
  }
}
```

### **Bus Search Response:**
```json
{
  "schedules": [
    {
      "id": 1,
      "bus": {
        "id": 1,
        "busNumber": "RB001",
        "busType": "AC",
        "totalSeats": 50,
        "amenities": ["WiFi", "Charging Point", "Blanket", "Water Bottle"]
      },
      "route": {
        "id": 1,
        "origin": "Mumbai",
        "destination": "Pune",
        "distanceKm": 150.5,
        "estimatedDurationHours": 3.5
      },
      "departureTime": "06:00:00",
      "arrivalTime": "09:30:00",
      "price": 500.00,
      "availableSeats": 50
    }
  ]
}
```

## 🧪 **Complete End-to-End Test Script**

Create a file `test-api.bat` with all the commands above and run it to test the entire flow automatically!

## 📝 **Notes**
- Replace `YOUR_ADMIN_JWT_TOKEN` and `YOUR_USER_JWT_TOKEN` with actual tokens from login responses
- All timestamps should be in ISO format (YYYY-MM-DD)
- Ensure the application is running before executing these commands
- Some endpoints require authentication (JWT token in Authorization header)

