# RedBus End-to-End Test Suite

A comprehensive, cross-platform testing suite for the RedBus application that covers all functionality from authentication to booking and tracking.

## 🚀 Features

- **Cross-Platform**: Works on Windows, macOS, and Linux
- **Comprehensive Coverage**: Tests all RedBus functionality
- **Verbose Logging**: Detailed request/response logging
- **Beautiful Output**: Colored console output with emojis
- **Test Reports**: Detailed test execution reports
- **Error Handling**: Robust error handling and recovery

## 📋 Test Coverage

### Phase 1: Infrastructure
- ✅ Application health checks
- ✅ API endpoint availability

### Phase 2: Authentication
- ✅ User registration (Admin & Regular users)
- ✅ User login with JWT tokens
- ✅ Role-based access control

### Phase 3: Operator Management
- ✅ Operator onboarding
- ✅ Operator listing and management

### Phase 4: Route Management
- ✅ Route creation with dynamic data
- ✅ Route validation and listing

### Phase 5: Bus Management
- ✅ Bus creation with various types (AC, NON_AC, SLEEPER, etc.)
- ✅ Bus listing and management
- ✅ Dynamic amenities and seat configurations

### Phase 6: Schedule Management
- ✅ Recurring schedule creation
- ✅ Multiple day patterns (weekdays, weekends, custom)
- ✅ Dynamic pricing and timing

### Phase 7: Data Synchronization
- ✅ Elasticsearch sync monitoring
- ✅ Database consistency checks

### Phase 8: Search Functionality
- ✅ Bus search by route
- ✅ Search result validation
- ✅ Dynamic search parameters

### Phase 9: Booking Process
- ✅ Seat selection simulation
- ✅ Multi-passenger bookings
- ✅ Booking reference generation

### Phase 10: Payment Processing
- ✅ Payment method testing
- ✅ Transaction processing
- ✅ Payment status tracking

### Phase 11: Bus Tracking
- ✅ Real-time location updates
- ✅ GPS coordinate validation
- ✅ Speed and direction tracking

## 📦 Installation

### Prerequisites
- Python 3.7 or higher
- pip (Python package installer)

### Quick Setup

#### Windows
```bash
# Run the setup script
setup.bat

# Or manually install
pip install -r requirements.txt
```

#### macOS/Linux
```bash
# Make setup script executable and run
chmod +x setup.sh
./setup.sh

# Or manually install
pip3 install -r requirements.txt
```

### Manual Installation
```bash
pip install requests colorama tabulate python-dateutil
```

## 🎯 Usage

### Basic Usage
```bash
# Run all tests with default settings
python redbus_e2e_test.py

# Run with verbose output
python redbus_e2e_test.py --verbose

# Generate detailed report
python redbus_e2e_test.py --report
```

### Advanced Usage
```bash
# Test against different server
python redbus_e2e_test.py --base-url http://production-server:8080

# Combine options
python redbus_e2e_test.py --verbose --base-url http://staging:8080
```

### Command Line Options
- `--base-url URL`: Specify RedBus application URL (default: http://localhost:8080)
- `--verbose, -v`: Enable detailed request/response logging
- `--report, -r`: Generate comprehensive test report

## 📊 Sample Output

```
================================================================================
RedBus End-to-End Test Suite
================================================================================
ℹ️  Test ID: a1b2c3d4
ℹ️  Base URL: http://localhost:8080
ℹ️  Started at: 2024-01-15 10:30:00

------------------------------------------------------------
Phase 1: Infrastructure & Health Checks
------------------------------------------------------------
[10:30:01] ✅   Application Health Check: PASS (0.15s) Application is healthy

------------------------------------------------------------
Phase 2: Authentication Tests
------------------------------------------------------------
[10:30:02] ✅   Admin User Registration: PASS (0.45s) User testuser_a1b2c3d4_0 registered successfully
[10:30:03] ✅   Regular User Registration: PASS (0.38s) User testuser_a1b2c3d4_1 registered successfully
[10:30:04] ✅   Admin User Login: PASS (0.32s) Login successful for testuser_a1b2c3d4_0

------------------------------------------------------------
Phase 3: Operator Onboarding
------------------------------------------------------------
[10:30:05] ✅   Operator Creation: PASS (0.28s) Operator Test Bus Company a1b2c3d4_0 created with ID 15

... (continued for all phases)

================================================================================
Test Execution Summary
================================================================================
ℹ️  Total Tests: 15
🎉  Passed: 14 (93.3%)
❌  Failed: 1 (6.7%)
ℹ️  Skipped: 0 (0.0%)
ℹ️  Total Duration: 12.45s
ℹ️  Wall Clock Time: 45.23s

🎉 ALL TESTS PASSED! RedBus application is working correctly.
```

## 🔧 Configuration

### Environment Variables
You can set these environment variables to customize the test behavior:

```bash
export REDBUS_BASE_URL="http://localhost:8080"
export REDBUS_TIMEOUT="30"
export REDBUS_VERBOSE="true"
```

### Test Data
The test suite generates unique test data for each run:
- **Users**: Unique usernames and emails with UUID-based suffixes
- **Operators**: Dynamic company names and license numbers
- **Routes**: Randomized city pairs with unique identifiers
- **Buses**: Various bus types, seat counts, and amenities
- **Schedules**: Different timing patterns and day configurations

## 🐛 Troubleshooting

### Common Issues

#### 1. Connection Refused
```
ERROR: Request failed: Connection refused
```
**Solution**: Ensure the RedBus application is running on the specified URL.

#### 2. Import Errors
```
ImportError: No module named 'requests'
```
**Solution**: Install dependencies using the setup script or manually install packages.

#### 3. Permission Errors (Linux/macOS)
```
Permission denied: ./setup.sh
```
**Solution**: Make the script executable: `chmod +x setup.sh`

#### 4. Python Not Found
```
'python' is not recognized as an internal or external command
```
**Solution**: Install Python from [python.org](https://www.python.org/downloads/) and ensure it's in your PATH.

### Debug Mode
For detailed debugging, run with maximum verbosity:
```bash
python redbus_e2e_test.py --verbose --report
```

This will show:
- Full HTTP request/response details
- JSON payload inspection
- Timing information for each operation
- Detailed error messages

## 📈 Test Results

The test suite provides multiple levels of output:

1. **Basic**: Pass/Fail status for each test
2. **Verbose**: Detailed request/response logging
3. **Report**: Comprehensive statistics and data summary

### Exit Codes
- `0`: All tests passed
- `1`: One or more tests failed
- `130`: Tests interrupted by user (Ctrl+C)

## 🤝 Contributing

To add new test cases:

1. Add test method to the `RedBusE2ETest` class
2. Follow the naming convention: `test_feature_name`
3. Return tuple: `(success: bool, message: str, data: dict)`
4. Add test execution to `run_comprehensive_tests()`

Example:
```python
def test_new_feature(self, token: str) -> Tuple[bool, str, Dict]:
    """Test new feature"""
    success, data, status = self.make_request("GET", "/new-endpoint", auth_token=token)
    
    if success:
        return True, "New feature works correctly", data
    else:
        return False, f"New feature failed: {status}", data
```

## 📄 License

MIT License - See LICENSE file for details.

## 🚌 About RedBus

This test suite is designed for the RedBus bus booking application, covering all aspects of the bus reservation system from operator onboarding to passenger booking and tracking.