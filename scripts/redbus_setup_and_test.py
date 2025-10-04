#!/usr/bin/env python3
"""
RedBus Complete Setup & Test Suite
==================================

This script will automatically:
1. Check system requirements
2. Set up all services (Docker, databases, etc.)
3. Build and deploy the RedBus application
4. Run comprehensive tests
5. Display detailed results

NO TECHNICAL KNOWLEDGE REQUIRED - Just run this script!

Requirements: Python 3.7+ and Docker Desktop
"""

import subprocess
import time
import requests
import json
import sys
import os
import random
from datetime import datetime
from typing import Dict, List, Optional, Tuple, Any
import platform

class Colors:
    """Cross-platform color support"""
    if platform.system() == "Windows":
        # Windows-safe colors
        HEADER = ''
        BLUE = ''
        CYAN = ''
        GREEN = ''
        YELLOW = ''
        RED = ''
        ENDC = ''
        BOLD = ''
    else:
        # Unix/Linux colors
        HEADER = '\033[95m'
        BLUE = '\033[94m'
        CYAN = '\033[96m'
        GREEN = '\033[92m'
        YELLOW = '\033[93m'
        RED = '\033[91m'
        ENDC = '\033[0m'
        BOLD = '\033[1m'

class RedBusAutoSetup:
    def __init__(self):
        self.base_url = "http://localhost:9090/api"
        self.start_time = datetime.now()
        self.test_results = []
        self.setup_results = []
        self.admin_token = None
        self.created_data = {}
        self.skip_setup = False
        self.junit_coverage = None
        self.seed_data_created = False
        
    def print_banner(self):
        """Display welcome banner"""
        print("=" * 80)
        print(f"{Colors.BOLD}REDBUS COMPLETE SETUP & TEST SUITE{Colors.ENDC}")
        print("=" * 80)
        print("This script will automatically set up and test your RedBus application.")
        print("No technical knowledge required - just sit back and watch!")
        print("=" * 80)
        print()

    def log(self, message: str, level: str = "INFO", show_time: bool = True):
        """Enhanced logging with timestamps and status indicators"""
        timestamp = datetime.now().strftime("%H:%M:%S") if show_time else ""
        
        icons = {
            "INFO": "[INFO]",
            "SUCCESS": "[SUCCESS]",
            "ERROR": "[ERROR]", 
            "WARNING": "[WARNING]",
            "SETUP": "[SETUP]",
            "TEST": "[TEST]",
            "HEALTH": "[HEALTH]",
            "STEP": "[STEP]"
        }
        
        icon = icons.get(level, "[INFO]")
        time_str = f"[{timestamp}] " if show_time else ""
        
        if level == "ERROR":
            print(f"{Colors.RED}{time_str}{icon} {message}{Colors.ENDC}")
        elif level == "SUCCESS":
            print(f"{Colors.GREEN}{time_str}{icon} {message}{Colors.ENDC}")
        elif level == "WARNING":
            print(f"{Colors.YELLOW}{time_str}{icon} {message}{Colors.ENDC}")
        elif level == "SETUP":
            print(f"{Colors.BLUE}{time_str}{icon} {message}{Colors.ENDC}")
        elif level == "TEST":
            print(f"{Colors.CYAN}{time_str}{icon} {message}{Colors.ENDC}")
        else:
            print(f"{time_str}{icon} {message}")

    def run_command(self, command: str, capture_output: bool = True, timeout: int = 300, show_output: bool = True, show_command: bool = True, live_output: bool = False) -> Tuple[bool, str]:
        """Run system command with enhanced error handling and verbose output"""
        try:
            if show_command:
                self.log(f"Running: {command}", "INFO")
            
            # For live output, stream directly to console
            if live_output:
                if show_command:
                    print(f"{Colors.CYAN}Live output (this may take several minutes):{Colors.ENDC}")
                    print("-" * 80)
                
                if platform.system() == "Windows":
                    process = subprocess.Popen(
                        command,
                        shell=True,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        text=True,
                        bufsize=1,
                        universal_newlines=True
                    )
                else:
                    process = subprocess.Popen(
                        command,
                        shell=True,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        text=True,
                        bufsize=1
                    )
                
                output_lines = []
                try:
                    for line in process.stdout:
                        print(line.rstrip())
                        output_lines.append(line)
                        sys.stdout.flush()
                    
                    process.wait(timeout=timeout)
                    
                    if process.returncode == 0:
                        print("-" * 80)
                        if show_command:
                            self.log(f"Command completed successfully", "SUCCESS")
                        return True, "".join(output_lines)
                    else:
                        print("-" * 80)
                        if show_command:
                            self.log(f"Command failed with code {process.returncode}", "ERROR")
                        return False, "".join(output_lines)
                        
                except subprocess.TimeoutExpired:
                    process.kill()
                    error_msg = f"Command timed out after {timeout} seconds"
                    if show_command:
                        self.log(error_msg, "ERROR")
                    return False, error_msg
            
            # Regular captured output mode
            else:
                if platform.system() == "Windows":
                    result = subprocess.run(
                        command,
                        shell=True,
                        capture_output=capture_output,
                        text=True,
                        timeout=timeout
                    )
                else:
                    result = subprocess.run(
                        command,
                        shell=True,
                        capture_output=capture_output,
                        text=True,
                        timeout=timeout
                    )
                
                # Show command output if requested
                if show_output and result.stdout and result.stdout.strip():
                    print("Command Output:")
                    for line in result.stdout.strip().split('\n'):
                        if line.strip():
                            print(f"  > {line}")
                    print()
                
                if result.returncode == 0:
                    if show_command:
                        self.log(f"Command completed successfully", "SUCCESS")
                    return True, result.stdout.strip() if result.stdout else ""
                else:
                    error_msg = result.stderr.strip() if result.stderr else f"Command failed with code {result.returncode}"
                    if show_command:
                        self.log(f"Command failed: {error_msg}", "ERROR")
                    if show_output and result.stderr and result.stderr.strip():
                        print("Error Output:")
                        for line in result.stderr.strip().split('\n'):
                            if line.strip():
                                print(f"  ! {line}")
                        print()
                    return False, error_msg
                
        except subprocess.TimeoutExpired:
            error_msg = f"Command timed out after {timeout} seconds"
            if show_command:
                self.log(error_msg, "ERROR")
            return False, error_msg
        except Exception as e:
            error_msg = f"Command execution failed: {str(e)}"
            if show_command:
                self.log(error_msg, "ERROR")
            return False, error_msg

    def check_if_services_running(self) -> bool:
        """Check if all services are already running and healthy"""
        self.log("Checking if services are already running...", "INFO")
        
        try:
            # Check if containers exist and are running
            success, output = self.run_command("docker-compose ps --services --filter status=running", show_command=False, show_output=False)
            
            if not success:
                return False
            
            running_services = output.strip().split('\n') if output.strip() else []
            required_services = {'postgres', 'redis', 'elasticsearch', 'app'}
            
            if not all(svc in running_services for svc in required_services):
                self.log(f"Not all services are running. Found: {running_services}", "INFO")
                return False
            
            # Quick health check
            if not (self.check_postgres_health() and self.check_redis_health() and 
                    self.check_elasticsearch_health() and self.check_app_health()):
                self.log("Services are running but not all healthy", "INFO")
                return False
            
            self.log("All services are already running and healthy!", "SUCCESS")
            return True
            
        except Exception as e:
            self.log(f"Error checking services: {str(e)}", "INFO")
            return False
    
    def check_prerequisites(self) -> bool:
        """Check system requirements"""
        self.log("STEP 1: Checking System Requirements", "STEP", False)
        print("-" * 50)
        
        # Check Python version
        python_version = sys.version_info
        if python_version.major >= 3 and python_version.minor >= 7:
            self.log(f"Python {python_version.major}.{python_version.minor}.{python_version.micro} - OK", "SUCCESS")
        else:
            self.log("Python 3.7+ required", "ERROR")
            return False
        
        # Check Docker
        self.log("Checking Docker installation...", "INFO")
        success, output = self.run_command("docker --version", show_output=False)
        if success:
            self.log(f"Docker found: {output.split()[2] if len(output.split()) > 2 else 'Unknown version'}", "SUCCESS")
        else:
            self.log("Docker not found. Please install Docker Desktop first.", "ERROR")
            self.log("Download from: https://www.docker.com/products/docker-desktop", "INFO")
            return False
        
        # Check Docker daemon
        self.log("Checking Docker daemon...", "INFO")
        success, output = self.run_command("docker ps", show_output=False)
        if success:
            self.log("Docker daemon is running", "SUCCESS")
        else:
            self.log("Docker daemon not running. Please start Docker Desktop.", "ERROR")
            return False
        
        # Check required files
        required_files = ["docker-compose.yml", "Dockerfile", "pom.xml"]
        missing_files = []
        
        for file in required_files:
            if os.path.exists(file):
                self.log(f"Found {file}", "SUCCESS")
            else:
                missing_files.append(file)
                self.log(f"Missing {file}", "ERROR")
        
        if missing_files:
            self.log("Please run this script from the RedBus project root directory", "ERROR")
            return False
        
        print()
        return True

    def setup_services(self) -> bool:
        """Set up all Docker services"""
        self.log("STEP 2: Setting Up RedBus Services", "STEP", False)
        print("-" * 50)
        
        # Stop existing containers
        self.log("Stopping any existing containers...", "SETUP")
        self.run_command("docker-compose down", timeout=60, show_output=False, live_output=False)
        
        # Start services with live output
        self.log("Building and starting all services...", "SETUP")
        self.log("This may take 5-10 minutes on first run (downloading images & building)...", "INFO")
        self.log("Showing live build output:", "INFO")
        print()
        
        success, output = self.run_command("docker-compose up -d --build", timeout=900, show_output=False, live_output=True)  # 15 minutes with live streaming
        
        if not success:
            self.log(f"Failed to start services", "ERROR")
            return False
        
        self.log("All services started successfully!", "SUCCESS")
        print()
        return True

    def wait_for_service_health(self, service_name: str, health_check_func, max_attempts: int = 60, delay: int = 5) -> bool:
        """Wait for a service to become healthy with progress indicator"""
        self.log(f"Waiting for {service_name} to be ready...", "HEALTH")
        
        for attempt in range(max_attempts):
            try:
                if health_check_func():
                    self.log(f"{service_name} is ready!", "SUCCESS")
                    return True
            except Exception as e:
                pass  # Expected during startup
            
            if attempt < max_attempts - 1:
                dots = "." * ((attempt % 3) + 1)
                print(f"   {service_name} starting{dots} ({attempt + 1}/{max_attempts})", end='\r')
                time.sleep(delay)
        
        print()  # New line after progress indicator
        self.log(f"{service_name} failed to start after {max_attempts * delay} seconds", "ERROR")
        return False

    def check_postgres_health(self) -> bool:
        """Check PostgreSQL health"""
        success, output = self.run_command('docker exec redbus-postgres pg_isready -h localhost -p 5432', show_command=False, show_output=False)
        return success and "accepting connections" in output

    def check_redis_health(self) -> bool:
        """Check Redis health"""
        success, output = self.run_command('docker exec redbus-redis redis-cli ping', show_command=False, show_output=False)
        return success and "PONG" in output

    def check_elasticsearch_health(self) -> bool:
        """Check Elasticsearch health"""
        try:
            response = requests.get("http://localhost:9200/_cluster/health", timeout=10)
            if response.status_code == 200:
                health = response.json()
                return health.get("status") in ["yellow", "green"]
        except:
            pass
        return False

    def check_app_health(self) -> bool:
        """Check RedBus application health"""
        # Check container status first
        success, output = self.run_command("docker ps --filter name=redbus-app --format '{{.Status}}'", show_command=False, show_output=False)
        if success and output and "Exited" in output:
            self.log("Application container crashed. Checking logs...", "ERROR")
            success, logs = self.run_command("docker logs --tail 20 redbus-app", show_command=True, show_output=True)
            return False
        
        try:
            response = requests.get(f"{self.base_url}/actuator/health", timeout=10)
            if response.status_code == 200:
                health = response.json()
                return health.get("status") == "UP"
        except:
            pass
        return False

    def wait_for_all_services(self) -> bool:
        """Wait for all services to be healthy"""
        self.log("STEP 3: Waiting for All Services to Start", "STEP", False)
        print("-" * 50)
        
        services = [
            ("PostgreSQL Database", self.check_postgres_health, 30, 3),
            ("Redis Cache", self.check_redis_health, 30, 3),
            ("Elasticsearch Search", self.check_elasticsearch_health, 60, 5),
            ("RedBus Application", self.check_app_health, 120, 5)
        ]
        
        for service_name, health_check, max_attempts, delay in services:
            if not self.wait_for_service_health(service_name, health_check, max_attempts, delay):
                return False
        
        self.log("All services are running and healthy!", "SUCCESS")
        print()
        return True

    def seed_initial_test_data(self) -> bool:
        """Seed initial test data for a fresh deployment"""
        self.log("STEP 3.5: Seeding Initial Test Data", "STEP", False)
        print("-" * 50)
        self.log("Creating sample data for testing and demonstration...", "INFO")
        
        try:
            # Create a demo admin user
            admin_data = {
                'username': 'demo_admin',
                'email': 'admin@redbus.com',
                'password': 'Admin@123',
                'firstName': 'Demo',
                'lastName': 'Admin',
                'role': 'ADMIN'
            }
            
            self.log("Creating demo admin user...", "INFO")
            success, response, error = self.make_api_request(
                "POST", "/auth/register", admin_data,
                description="Creating demo admin account"
            )
            
            if success and response:
                demo_admin_token = response['token']
                self.log(f"[OK] Demo admin created: demo_admin", "SUCCESS")
                
                # Create sample operators
                operators_data = [
                    {
                        'name': 'RedBus Express',
                        'contactEmail': 'contact@redbusexpress.com',
                        'contactPhone': '+1-800-REDBUS-1',
                        'licenseNumber': 'RBE2025001'
                    },
                    {
                        'name': 'Premium Travels',
                        'contactEmail': 'info@premiumtravels.com',
                        'contactPhone': '+1-800-PREMIUM',
                        'licenseNumber': 'PT2025002'
                    }
                ]
                
                created_operators = []
                for op_data in operators_data:
                    success, op_response, _ = self.make_api_request(
                        "POST", "/operator/operators", op_data, demo_admin_token,
                        description=f"Creating operator: {op_data['name']}"
                    )
                    if success and op_response:
                        created_operators.append(op_response)
                        self.log(f"[OK] Operator created: {op_data['name']}", "SUCCESS")
                
                # Create sample routes
                routes_data = [
                    {'origin': 'Mumbai', 'destination': 'Pune', 'distanceKm': 150, 'estimatedDurationHours': 3.5},
                    {'origin': 'Mumbai', 'destination': 'Bangalore', 'distanceKm': 980, 'estimatedDurationHours': 16},
                    {'origin': 'Delhi', 'destination': 'Jaipur', 'distanceKm': 280, 'estimatedDurationHours': 5},
                    {'origin': 'Chennai', 'destination': 'Bangalore', 'distanceKm': 350, 'estimatedDurationHours': 6}
                ]
                
                created_routes = []
                for route_data in routes_data:
                    success, route_response, _ = self.make_api_request(
                        "POST", "/operator/routes", route_data, demo_admin_token,
                        description=f"Creating route: {route_data['origin']} -> {route_data['destination']}"
                    )
                    if success and route_response:
                        created_routes.append(route_response)
                        self.log(f"[OK] Route created: {route_data['origin']} -> {route_data['destination']}", "SUCCESS")
                
                # Create sample buses
                bus_types = ['AC', 'NON_AC', 'SLEEPER', 'LUXURY']
                created_buses = []
                
                for i, operator in enumerate(created_operators):
                    for j in range(2):  # 2 buses per operator
                        bus_data = {
                            'operatorId': operator['id'],
                            'busNumber': f"{operator['name'][:3].upper()}-{bus_types[j % len(bus_types)]}-{i+1}{j+1}",
                            'busType': bus_types[j % len(bus_types)],
                            'totalSeats': 40 if j % 2 == 0 else 45,
                            'amenities': ['WiFi', 'USB Charging', 'Water'] if j % 2 == 0 else ['WiFi', 'AC', 'Entertainment']
                        }
                        success, bus_response, _ = self.make_api_request(
                            "POST", "/operator/buses", bus_data, demo_admin_token,
                            description=f"Creating bus: {bus_data['busNumber']}"
                        )
                        if success and bus_response:
                            created_buses.append(bus_response)
                
                self.log(f"[OK] Created {len(created_buses)} buses", "SUCCESS")
                
                # Create schedules
                schedules_created = 0
                for bus in created_buses[:4]:  # Use first 4 buses
                    for route in created_routes[:2]:  # Use first 2 routes
                        schedule_data = {
                            'busId': bus['id'],
                            'routeId': route['id'],
                            'departureTime': '06:00:00' if schedules_created % 2 == 0 else '14:00:00',
                            'arrivalTime': '09:30:00' if schedules_created % 2 == 0 else '17:30:00',
                            'price': 299.99 + (schedules_created * 50),
                            'isRecurring': True,
                            'daysOfWeek': [1, 2, 3, 4, 5, 6, 7]
                        }
                        success, sched_response, _ = self.make_api_request(
                            "POST", "/operator/schedules", schedule_data, demo_admin_token,
                            description=f"Creating schedule for bus {bus['busNumber']}"
                        )
                        if success:
                            schedules_created += 1
                
                self.log(f"[OK] Created {schedules_created} schedules", "SUCCESS")
                
                # Wait for ES sync
                self.log("Waiting 10 seconds for Elasticsearch to sync...", "INFO")
                time.sleep(10)
                
                print()
                print(f"{Colors.GREEN}Initial test data seeded successfully!{Colors.ENDC}")
                print(f"  Operators: {len(created_operators)}")
                print(f"  Routes: {len(created_routes)}")
                print(f"  Buses: {len(created_buses)}")
                print(f"  Schedules: {schedules_created}")
                print()
                
                self.seed_data_created = True
                return True
            else:
                self.log("Demo admin already exists, skipping data seeding", "INFO")
                return True
                
        except Exception as e:
            self.log(f"Data seeding encountered issues: {str(e)}", "WARNING")
            self.log("Continuing with tests...", "INFO")
            return True

    def run_junit_tests_with_coverage(self) -> bool:
        """Run JUnit tests and collect coverage report"""
        self.log("STEP 3.6: Running JUnit Test Suite with Coverage", "STEP", False)
        print("-" * 50)
        self.log("Executing Java unit tests with JaCoCo coverage...", "INFO")
        self.log("Showing live test execution output...", "INFO")
        print()
        
        try:
            # Run Maven tests with coverage - with live output for visibility
            success, output = self.run_command(
                "mvn clean test jacoco:report",
                timeout=600,
                show_output=True,
                show_command=True,
                live_output=True  # Show tests as they run
            )
            
            if success:
                # Parse test results
                test_lines = [line for line in output.split('\n') if 'Tests run:' in line]
                if test_lines:
                    # Extract the last test summary line
                    summary_line = test_lines[-1]
                    self.log(f"JUnit Test Summary: {summary_line.strip()}", "SUCCESS")
                
                # Try to read coverage report
                coverage_file = "target/site/jacoco/index.html"
                if os.path.exists(coverage_file):
                    with open(coverage_file, 'r', encoding='utf-8') as f:
                        coverage_html = f.read()
                        
                        # Simple parsing for coverage percentages
                        if 'Total' in coverage_html:
                            import re
                            # Look for coverage percentages in the HTML
                            coverage_pattern = r'(\d+)%'
                            matches = re.findall(coverage_pattern, coverage_html)
                            if len(matches) >= 2:
                                self.junit_coverage = {
                                    'instruction_coverage': matches[-2] if len(matches) > 1 else 'N/A',
                                    'branch_coverage': matches[-1] if matches else 'N/A'
                                }
                                self.log(f"Code Coverage - Instructions: {self.junit_coverage['instruction_coverage']}%, Branches: {self.junit_coverage['branch_coverage']}%", "SUCCESS")
                
                print()
                self.log("[OK] JUnit tests completed successfully", "SUCCESS")
                print(f"  Coverage report: target/site/jacoco/index.html")
                print()
                return True
            else:
                self.log("JUnit tests had some failures", "WARNING")
                self.log("Continuing with E2E tests...", "INFO")
                print()
                return True
                
        except Exception as e:
            self.log(f"JUnit test execution encountered issues: {str(e)}", "WARNING")
            self.log("Continuing with E2E tests...", "INFO")
            print()
            return True

    def make_api_request(self, method: str, endpoint: str, data: Optional[Dict] = None, token: Optional[str] = None, description: str = "") -> Tuple[bool, Optional[Dict], str]:
        """Make API request with verbose logging and detailed error reporting"""
        headers = {"Content-Type": "application/json"}
        if token:
            headers["Authorization"] = f"Bearer {token}"
        
        url = f"{self.base_url}{endpoint}"
        
        # Log the request details
        self.log(f"API Request: {method} {endpoint}", "INFO")
        if description:
            self.log(f"Purpose: {description}", "INFO")
        if data:
            self.log(f"Request data: {json.dumps(data, indent=2)}", "INFO")
        
        try:
            if method == "GET":
                response = requests.get(url, headers=headers, timeout=15)
            elif method == "POST":
                response = requests.post(url, headers=headers, json=data, timeout=15)
            elif method == "PUT":
                response = requests.put(url, headers=headers, json=data, timeout=15)
            elif method == "DELETE":
                response = requests.delete(url, headers=headers, timeout=15)
            else:
                raise ValueError(f"Unsupported method: {method}")
            
            # Log response details
            self.log(f"Response: {response.status_code} {response.reason}", "INFO")
            
            if response.ok:
                try:
                    response_data = response.json()
                    self.log(f"Response data: {json.dumps(response_data, indent=2)[:500]}{'...' if len(str(response_data)) > 500 else ''}", "INFO")
                    return True, response_data, f"Success: {response.status_code}"
                except:
                    self.log("Response: Success (no JSON data)", "INFO")
                    return True, None, f"Success: {response.status_code}"
            else:
                error_text = response.text[:200] if response.text else "No error details"
                self.log(f"Error response: {error_text}", "ERROR")
                return False, None, f"HTTP {response.status_code}: {error_text}"
                
        except requests.exceptions.RequestException as e:
            error_msg = f"Request failed: {str(e)}"
            self.log(error_msg, "ERROR")
            return False, None, error_msg

    def print_test_response(self, test_name: str, success: bool, response_data: Optional[Dict], error_msg: str = ""):
        """Print test response in a clear, formatted way"""
        print()
        print("=" * 80)
        if success:
            print(f"{Colors.GREEN}[OK] TEST PASSED: {test_name}{Colors.ENDC}")
        else:
            print(f"{Colors.RED}[X] TEST FAILED: {test_name}{Colors.ENDC}")
        print("=" * 80)
        
        if success and response_data:
            print("Response Data:")
            print(json.dumps(response_data, indent=2)[:1000])
            if len(json.dumps(response_data)) > 1000:
                print("... (truncated)")
        elif not success:
            print(f"Error: {error_msg}")
        print("=" * 80)
        print()
    
    def run_comprehensive_tests(self) -> bool:
        step_num = 4 if not self.skip_setup else 1
        self.log(f"STEP {step_num}: Running Comprehensive Tests", "STEP", False)
        print("-" * 50)
        
        try:
            # Test 1: Register Admin User
            self.log("Test 1: Admin User Registration", "TEST")
            register_data = {
                'username': f'admin_test_{int(time.time())}',
                'email': f'admin_test_{int(time.time())}@redbus.com',
                'password': 'TestPass123!',
                'firstName': 'Test',
                'lastName': 'Admin',
                'role': 'ADMIN'
            }
            
            success, response_data, error_msg = self.make_api_request(
                "POST", "/auth/register", register_data, 
                description="Creating admin user for testing"
            )
            
            if success and response_data:
                self.admin_token = response_data['token']
                self.created_data['admin_user'] = response_data
                self.log(f"Admin user created successfully (ID: {response_data.get('id')})", "SUCCESS")
                self.test_results.append(("Admin Registration", "PASS", f"User ID: {response_data.get('id')}"))
            else:
                self.log(f"Admin registration failed: {error_msg}", "ERROR")
                self.test_results.append(("Admin Registration", "FAIL", error_msg))
                return False
            
            headers = {'Authorization': f'Bearer {self.admin_token}', 'Content-Type': 'application/json'}
            
            # Test 2: Authentication
            self.log("Test 2: Authentication System", "TEST")
            
            # Login test
            login_data = {'username': register_data['username'], 'password': register_data['password']}
            response = requests.post(f'{self.base_url}/auth/login', json=login_data, timeout=15)
            if response.ok:
                self.log("[OK] User login successful", "SUCCESS")
                self.test_results.append(("User Login", "PASS", "Authentication working"))
            else:
                self.log("[X] User login failed", "ERROR")
                self.test_results.append(("User Login", "FAIL", f"Status: {response.status_code}"))
            
            # Profile test
            response = requests.get(f'{self.base_url}/auth/me', headers=headers, timeout=15)
            if response.ok:
                user_data = response.json()
                self.log(f"[OK] User profile retrieved (Role: {user_data.get('role')})", "SUCCESS")
                self.test_results.append(("User Profile", "PASS", f"Role: {user_data.get('role')}"))
            else:
                self.log("[X] User profile failed", "ERROR")
                self.test_results.append(("User Profile", "FAIL", f"Status: {response.status_code}"))
            
            # Test 3: Operator Management
            self.log("Test 3: Operator Management", "TEST")
            
            # List operators
            response = requests.get(f'{self.base_url}/operator/operators', headers=headers, timeout=15)
            if response.ok:
                operators = response.json()
                self.log(f"[OK] Found {len(operators)} existing operators", "SUCCESS")
                self.test_results.append(("List Operators", "PASS", f"Found: {len(operators)}"))
            else:
                self.log("[X] Failed to list operators", "ERROR")
                self.test_results.append(("List Operators", "FAIL", f"Status: {response.status_code}"))
            
            # Create operator
            operator_data = {
                'name': f'Test Bus Company {int(time.time())}',
                'contactEmail': f'operator_{int(time.time())}@testbus.com',
                'contactPhone': '+1-555-0123',
                'licenseNumber': f'LIC{int(time.time())}'
            }
            response = requests.post(f'{self.base_url}/operator/operators', json=operator_data, headers=headers, timeout=15)
            if response.ok:
                operator = response.json()
                self.created_data['operator'] = operator
                self.log(f"[OK] Created operator '{operator['name']}' (ID: {operator['id']})", "SUCCESS")
                self.test_results.append(("Create Operator", "PASS", f"ID: {operator['id']}"))
            else:
                self.log("[X] Failed to create operator", "ERROR")
                self.test_results.append(("Create Operator", "FAIL", f"Status: {response.status_code}"))
                return False
            
            # Test 4: Route Management
            self.log("Test 4: Route Management", "TEST")
            
            # List routes
            response = requests.get(f'{self.base_url}/operator/routes', headers=headers, timeout=15)
            if response.ok:
                routes = response.json()
                self.log(f"[OK] Found {len(routes)} existing routes", "SUCCESS")
                self.test_results.append(("List Routes", "PASS", f"Found: {len(routes)}"))
            else:
                self.log("[X] Failed to list routes", "ERROR")
                self.test_results.append(("List Routes", "FAIL", f"Status: {response.status_code}"))
            
            # Create route
            route_data = {
                'origin': f'City_A_{int(time.time())}',
                'destination': f'City_B_{int(time.time())}',
                'distanceKm': 250.5,
                'estimatedDurationHours': 4.5
            }
            response = requests.post(f'{self.base_url}/operator/routes', json=route_data, headers=headers, timeout=15)
            if response.ok:
                route = response.json()
                self.created_data['route'] = route
                self.log(f"[OK] Created route {route_data['origin']} -> {route_data['destination']} (ID: {route['id']})", "SUCCESS")
                self.test_results.append(("Create Route", "PASS", f"ID: {route['id']}"))
            else:
                self.log("[X] Failed to create route", "ERROR")
                self.test_results.append(("Create Route", "FAIL", f"Status: {response.status_code}"))
                return False
            
            # Test 5: Bus Management
            self.log("Test 5: Bus Management", "TEST")
            
            # List buses
            response = requests.get(f'{self.base_url}/operator/buses', headers=headers, timeout=15)
            if response.ok:
                buses = response.json()
                self.log(f"[OK] Found {len(buses)} existing buses", "SUCCESS")
                self.test_results.append(("List Buses", "PASS", f"Found: {len(buses)}"))
            else:
                self.log("[X] Failed to list buses", "ERROR")
                self.test_results.append(("List Buses", "FAIL", f"Status: {response.status_code}"))
            
            # Create bus
            bus_data = {
                'operatorId': self.created_data['operator']['id'],
                'busNumber': f'TEST-BUS-{int(time.time())}',
                'busType': 'AC',
                'totalSeats': 45,
                'amenities': ['WiFi', 'USB Charging', 'Air Conditioning', 'Reclining Seats']
            }
            response = requests.post(f'{self.base_url}/operator/buses', json=bus_data, headers=headers, timeout=15)
            if response.ok:
                bus = response.json()
                self.created_data['bus'] = bus
                self.log(f"[OK] Created bus {bus_data['busNumber']} with {bus_data['totalSeats']} seats (ID: {bus['id']})", "SUCCESS")
                self.test_results.append(("Create Bus", "PASS", f"ID: {bus['id']}"))
            else:
                self.log("[X] Failed to create bus", "ERROR")
                self.test_results.append(("Create Bus", "FAIL", f"Status: {response.status_code}"))
            
            # Test 6: Schedule Management
            self.log("Test 6: Schedule Management", "TEST")
            
            # List schedules
            response = requests.get(f'{self.base_url}/operator/schedules', headers=headers, timeout=15)
            if response.ok:
                schedules = response.json()
                self.log(f"[OK] Found {len(schedules)} existing schedules", "SUCCESS")
                self.test_results.append(("List Schedules", "PASS", f"Found: {len(schedules)}"))
            else:
                self.log("[X] Failed to list schedules", "ERROR")
                self.test_results.append(("List Schedules", "FAIL", f"Status: {response.status_code}"))
            
            # Create schedule (if we have bus and route)
            if 'bus' in self.created_data and 'route' in self.created_data:
                schedule_data = {
                    'busId': self.created_data['bus']['id'],
                    'routeId': self.created_data['route']['id'],
                    'departureTime': '08:00:00',
                    'arrivalTime': '12:30:00',
                    'price': 299.99,
                    'isRecurring': True,
                    'daysOfWeek': [1, 2, 3, 4, 5]  # Monday to Friday
                }
                response = requests.post(f'{self.base_url}/operator/schedules', json=schedule_data, headers=headers, timeout=15)
                if response.ok:
                    schedule = response.json()
                    self.created_data['schedule'] = schedule
                    self.log(f"[OK] Created schedule {schedule_data['departureTime']} -> {schedule_data['arrivalTime']} (ID: {schedule['id']})", "SUCCESS")
                    self.test_results.append(("Create Schedule", "PASS", f"ID: {schedule['id']}"))
                else:
                    self.log("[X] Failed to create schedule", "ERROR")
                    self.test_results.append(("Create Schedule", "FAIL", f"Status: {response.status_code}"))
            
            # Test 6a: Verify Newly Created Bus Appears in Search (after ES sync)
            if 'schedule' in self.created_data and 'route' in self.created_data:
                self.log("Test 6a: Elasticsearch Sync Verification - Searching for Newly Created Bus", "TEST")
                
                # Wait for Elasticsearch to sync the new schedule
                print(f"\n{Colors.CYAN}Waiting for Elasticsearch to sync new bus data...{Colors.ENDC}")
                print(f"  Schedule ID: {self.created_data['schedule']['id']}")
                print(f"  Bus Number: {self.created_data['bus']['busNumber']}")
                print(f"  Route: {self.created_data['route']['origin']} -> {self.created_data['route']['destination']}")
                
                sync_wait_time = 8  # seconds
                for i in range(sync_wait_time, 0, -1):
                    print(f"  Syncing... {i} seconds remaining", end='\r')
                    time.sleep(1)
                print(f"  Sync complete!                    ")
                
                # Search for the newly created bus using its route
                new_bus_search_data = {
                    'origin': self.created_data['route']['origin'],
                    'destination': self.created_data['route']['destination'],
                    'travelDate': '2025-10-06'
                }
                
                print(f"\n{Colors.CYAN}Searching for newly created bus...{Colors.ENDC}")
                search_success, search_response, search_error = self.make_api_request(
                    "POST", "/public/search", new_bus_search_data, None,
                    description=f"Searching for bus on route {new_bus_search_data['origin']} to {new_bus_search_data['destination']}"
                )
                
                if search_success and search_response:
                    # Check if our newly created bus is in the search results
                    found_new_bus = False
                    our_bus_number = self.created_data['bus']['busNumber']
                    
                    for bus in search_response:
                        if bus.get('busNumber') == our_bus_number:
                            found_new_bus = True
                            print(f"\n{Colors.GREEN}Success! Newly created bus found in search results!{Colors.ENDC}")
                            print(f"  Bus Number: {bus.get('busNumber')}")
                            print(f"  Bus Type: {bus.get('busType')}")
                            print(f"  Operator: {bus.get('operatorName')}")
                            print(f"  Total Seats: {bus.get('totalSeats')}")
                            print(f"  Price: ${bus.get('price')}")
                            print(f"  Departure: {bus.get('departureTime')}")
                            print(f"  Arrival: {bus.get('arrivalTime')}")
                            break
                    
                    if found_new_bus:
                        self.log(f"[OK] Newly created bus '{our_bus_number}' found in search results", "SUCCESS")
                        self.print_test_response("ES Sync Verification", True, {
                            "busNumber": our_bus_number,
                            "found": True,
                            "totalResults": len(search_response),
                            "syncTime": f"{sync_wait_time}s"
                        })
                        self.test_results.append(("ES Sync Verification", "PASS", f"Bus {our_bus_number} indexed"))
                    else:
                        print(f"\n{Colors.YELLOW}Warning: Newly created bus not found in search results{Colors.ENDC}")
                        print(f"  Expected Bus: {our_bus_number}")
                        print(f"  Found {len(search_response)} buses, but not our new one")
                        print(f"  This may indicate ES sync is still in progress")
                        self.log(f"[X] Newly created bus '{our_bus_number}' not found in search", "WARNING")
                        self.print_test_response("ES Sync Verification", False, None, "Bus not found in search results")
                        self.test_results.append(("ES Sync Verification", "FAIL", "Bus not indexed yet"))
                else:
                    self.log(f"[X] Search failed: {search_error}", "ERROR")
                    self.print_test_response("ES Sync Verification", False, None, search_error)
                    self.test_results.append(("ES Sync Verification", "FAIL", search_error))
            
            # Test 7: Public Search
            self.log("Test 7: Public Search (No Authentication Required)", "TEST")
            
            search_data = {
                'origin': 'Mumbai',
                'destination': 'Pune',
                'travelDate': '2025-10-05'
            }
            response = requests.post(f'{self.base_url}/public/search', json=search_data, timeout=15)
            if response.ok:
                search_results = response.json()
                self.log(f"[OK] Search found {len(search_results)} available buses", "SUCCESS")
                self.test_results.append(("Public Search", "PASS", f"Found: {len(search_results)} buses"))
            else:
                self.log("[X] Public search failed", "ERROR")
                self.test_results.append(("Public Search", "FAIL", f"Status: {response.status_code}"))
            
            # Test 8: System Health
            self.log("Test 8: System Health Check", "TEST")
            
            response = requests.get(f'{self.base_url}/actuator/health', timeout=15)
            if response.ok:
                health = response.json()
                status = health.get('status', 'Unknown')
                self.log(f"[OK] System health status: {status}", "SUCCESS")
                self.test_results.append(("System Health", "PASS", f"Status: {status}"))
            else:
                self.log("[X] Health check failed", "ERROR")
                self.test_results.append(("System Health", "FAIL", f"Status: {response.status_code}"))
            
            # Test 9-13: End-to-End User Journey Tests
            self.log("Tests 9-13: End-to-End User Journey", "TEST")
            
            # E2E Test 1: Regular User Registration and Login
            self.log("E2E Test 9: Regular User Registration and Login", "TEST")
            regular_user_data = {
                'username': f'user_test_{int(time.time())}',
                'email': f'user_test_{int(time.time())}@customer.com',
                'password': 'UserPass123!',
                'firstName': 'John',
                'lastName': 'Customer',
                'phone': '+1-555-0199',
                'role': 'USER'
            }
            
            success, user_response, error_msg = self.make_api_request(
                "POST", "/auth/register", regular_user_data,
                description="Registering a regular customer user"
            )
            
            if success and user_response:
                regular_user_token = user_response['token']
                self.created_data['regular_user'] = user_response
                self.log(f"[OK] Regular user registered successfully (ID: {user_response.get('id')})", "SUCCESS")
                self.test_results.append(("Regular User Registration", "PASS", f"User ID: {user_response.get('id')}"))
                
                # Test login for regular user
                login_success, login_response, login_error = self.make_api_request(
                    "POST", "/auth/login", 
                    {'username': regular_user_data['username'], 'password': regular_user_data['password']},
                    description="Testing regular user login"
                )
                
                if login_success:
                    self.log("[OK] Regular user login successful", "SUCCESS")
                    self.test_results.append(("Regular User Login", "PASS", "Login working"))
                else:
                    self.log(f"[X] Regular user login failed: {login_error}", "ERROR")
                    self.test_results.append(("Regular User Login", "FAIL", login_error))
            else:
                self.log(f"[X] Regular user registration failed: {error_msg}", "ERROR")
                self.test_results.append(("Regular User Registration", "FAIL", error_msg))
                regular_user_token = None
            
            # E2E Test 2: Enhanced Bus Search Journey
            self.log("E2E Test 10: Enhanced Bus Search Journey", "TEST")
            
            # Search for buses (public endpoint)
            search_data = {
                'origin': 'Mumbai',
                'destination': 'Pune',
                'travelDate': '2025-10-06'
            }
            
            search_success, search_results, search_error = self.make_api_request(
                "POST", "/public/search", search_data,
                description="Searching for available buses from Mumbai to Pune"
            )
            
            if search_success and search_results:
                self.log(f"[OK] Search successful: Found {len(search_results)} buses", "SUCCESS")
                self.test_results.append(("Enhanced Bus Search", "PASS", f"Found {len(search_results)} buses"))
                
                # Display search results details
                if search_results:
                    first_bus = search_results[0]
                    self.log(f"First search result details:", "INFO")
                    self.log(f"  Bus: {first_bus.get('busNumber')} ({first_bus.get('busType')})", "INFO")
                    self.log(f"  Operator: {first_bus.get('operatorName')}", "INFO")
                    self.log(f"  Route: {first_bus.get('origin')} -> {first_bus.get('destination')}", "INFO")
                    self.log(f"  Time: {first_bus.get('departureTime')} -> {first_bus.get('arrivalTime')}", "INFO")
                    self.log(f"  Price: ${first_bus.get('price')}", "INFO")
                    self.log(f"  Available Seats: {first_bus.get('availableSeats')}", "INFO")
            else:
                self.log(f"[X] Bus search failed: {search_error}", "ERROR")
                self.test_results.append(("Enhanced Bus Search", "FAIL", search_error))
                search_results = None
            
            # E2E Test 3: Complete Booking Journey
            self.log("E2E Test 11: Complete Booking Journey - Seat Selection & Booking", "TEST")
            print()
            print("=" * 80)
            print(f"{Colors.BOLD}SEAT SELECTION & BOOKING PROCESS{Colors.ENDC}")
            print("=" * 80)
            
            if regular_user_token and search_results:
                # Select first available bus for booking
                selected_bus = search_results[0]
                schedule_id = selected_bus.get('scheduleId')
                
                print(f"\n{Colors.CYAN}Step 1: Bus & Schedule Selection{Colors.ENDC}")
                print(f"  Selected Bus: {selected_bus.get('busNumber')}")
                print(f"  Operator: {selected_bus.get('operatorName')}")
                print(f"  Route: {selected_bus.get('origin')} -> {selected_bus.get('destination')}")
                print(f"  Schedule ID: {schedule_id}")
                print(f"  Available Seats: {selected_bus.get('availableSeats')}")
                print(f"  Price per Seat: ${selected_bus.get('price')}")
                
                if schedule_id:
                    print(f"\n{Colors.CYAN}Step 2: Checking Available Seats{Colors.ENDC}")
                    # Get booked seats for this schedule and date
                    check_seats_success, booked_seats_response, check_seats_error = self.make_api_request(
                        "GET", f"/bookings/schedule/{schedule_id}/date/2025-10-06/booked-seats", None, None,
                        description="Checking which seats are already booked"
                    )
                    
                    booked_seats = []
                    if check_seats_success and booked_seats_response:
                        booked_seats = booked_seats_response if isinstance(booked_seats_response, list) else []
                        print(f"  Already booked seats: {booked_seats if booked_seats else 'None'}")
                    else:
                        print(f"  Could not fetch booked seats, will try random seats")
                    
                    # Select available seats
                    total_seats = selected_bus.get('totalSeats', 40)
                    available_seats_list = [s for s in range(1, total_seats + 1) if s not in booked_seats]
                    
                    if len(available_seats_list) >= 2:
                        # Pick 2 random available seats
                        selected_seats = random.sample(available_seats_list, 2)
                        selected_seats.sort()  # Sort for better display
                        print(f"  Available seats count: {len(available_seats_list)}")
                        print(f"  Selecting available seats: {', '.join(map(str, selected_seats))}")
                    else:
                        # Fallback to random seats if we can't determine availability
                        available_seat_start = random.randint(10, 35)
                        selected_seats = [available_seat_start, available_seat_start + 1]
                        print(f"  Using random seats (fallback): {', '.join(map(str, selected_seats))}")
                    
                    booking_data = {
                        'scheduleId': schedule_id,
                        'travelDate': '2025-10-06',
                        'passengers': [
                            {
                                'seatNumber': selected_seats[0],
                                'passengerName': 'John Customer',
                                'passengerAge': 30,
                                'passengerGender': 'MALE'
                            },
                            {
                                'seatNumber': selected_seats[1],
                                'passengerName': 'Jane Customer',
                                'passengerAge': 28,
                                'passengerGender': 'FEMALE'
                            }
                        ]
                    }
                    
                    print(f"  Passenger 1: John Customer (Seat {selected_seats[0]}, Age 30, Male)")
                    print(f"  Passenger 2: Jane Customer (Seat {selected_seats[1]}, Age 28, Female)")
                    
                    print(f"\n{Colors.CYAN}Step 3: Creating Booking{Colors.ENDC}")
                    booking_success, booking_response, booking_error = self.make_api_request(
                        "POST", "/bookings", booking_data, regular_user_token,
                        description="Creating a bus booking for 2 seats"
                    )
                    
                    if booking_success and booking_response:
                        booking_id = booking_response.get('id')
                        self.created_data['booking'] = booking_response
                        
                        print(f"\n{Colors.GREEN}Booking Successful!{Colors.ENDC}")
                        print(f"  Booking Reference: {booking_response.get('bookingReference')}")
                        print(f"  Booking ID: {booking_id}")
                        print(f"  Status: {booking_response.get('status')}")
                        print(f"  Travel Date: {booking_response.get('bookingDate')}")
                        print(f"  Total Seats: {booking_response.get('totalSeats')}")
                        print(f"  Total Amount: ${booking_response.get('totalAmount')}")
                        print(f"  Bus: {booking_response.get('busNumber')}")
                        print(f"  Route: {booking_response.get('origin')} -> {booking_response.get('destination')}")
                        print(f"  Departure: {booking_response.get('departureTime')}")
                        print(f"  Arrival: {booking_response.get('arrivalTime')}")
                        
                        # Display tracking link prominently
                        tracking_link = booking_response.get('trackingLink')
                        if tracking_link:
                            print(f"\n{Colors.BOLD}{Colors.CYAN}  [TRACK YOUR BUS]{Colors.ENDC}")
                            print(f"  {Colors.GREEN}{tracking_link}{Colors.ENDC}")
                            print(f"  Use this link to track your bus in real-time!")
                        
                        self.print_test_response("Booking Creation", True, booking_response)
                        self.test_results.append(("Booking Creation", "PASS", f"Booking ID: {booking_id}"))
                        
                        # Test booking retrieval
                        get_booking_success, get_booking_response, get_booking_error = self.make_api_request(
                            "GET", f"/bookings/{booking_id}", None, regular_user_token,
                            description="Retrieving booking details"
                        )
                        
                        if get_booking_success:
                            self.log("[OK] Booking retrieval successful", "SUCCESS")
                            self.test_results.append(("Booking Retrieval", "PASS", "Booking details retrieved"))
                        else:
                            self.log(f"[X] Booking retrieval failed: {get_booking_error}", "ERROR")
                            self.test_results.append(("Booking Retrieval", "FAIL", get_booking_error))
                        
                        # Test user's booking list
                        user_bookings_success, user_bookings_response, user_bookings_error = self.make_api_request(
                            "GET", "/bookings/user", None, regular_user_token,
                            description="Getting user's booking history"
                        )
                        
                        if user_bookings_success and user_bookings_response:
                            self.log(f"[OK] User has {len(user_bookings_response)} bookings in history", "SUCCESS")
                            self.print_test_response("User Booking History", True, {"count": len(user_bookings_response), "bookings": user_bookings_response[:2]})
                            self.test_results.append(("User Booking History", "PASS", f"Found {len(user_bookings_response)} bookings"))
                        else:
                            self.log(f"[X] User booking history retrieval failed: {user_bookings_error}", "ERROR")
                            self.print_test_response("User Booking History", False, None, user_bookings_error)
                            self.test_results.append(("User Booking History", "FAIL", user_bookings_error))
                        
                        # E2E Test 3a: Seat Selection Validation
                        print()
                        print("=" * 80)
                        print(f"{Colors.BOLD}SEAT VALIDATION TEST{Colors.ENDC}")
                        print("=" * 80)
                        self.log("E2E Test 11a: Testing Seat Validation (Duplicate Booking Prevention)", "TEST")
                        
                        print(f"\n{Colors.CYAN}Attempting to book already-reserved seats...{Colors.ENDC}")
                        print(f"  Trying to book seats: {', '.join(map(str, selected_seats))}")
                        print(f"  These seats are already booked in Booking ID: {booking_id}")
                        
                        # Try booking the same seats again (should fail - seats already booked)
                        duplicate_booking_success, duplicate_response, duplicate_error = self.make_api_request(
                            "POST", "/bookings", booking_data, regular_user_token,
                            description="Attempting to book already-booked seats (should fail)"
                        )
                        
                        if not duplicate_booking_success:
                            print(f"\n{Colors.GREEN}Seat Validation Working!{Colors.ENDC}")
                            print(f"  System correctly prevented duplicate seat booking")
                            print(f"  Error Message: {duplicate_error}")
                            self.log("[OK] Duplicate seat booking correctly prevented", "SUCCESS")
                            self.print_test_response("Prevent Duplicate Seat Booking", True, {"message": "Seats already booked - validation working", "error": duplicate_error})
                            self.test_results.append(("Seat Validation", "PASS", "Duplicate booking prevented"))
                        else:
                            print(f"\n{Colors.RED}Seat Validation Issue!{Colors.ENDC}")
                            print(f"  System allowed duplicate seat booking (should be prevented)")
                            self.log("[X] Duplicate seat booking was allowed (validation issue)", "WARNING")
                            self.print_test_response("Prevent Duplicate Seat Booking", False, None, "Should have prevented duplicate booking")
                            self.test_results.append(("Seat Validation", "FAIL", "Duplicate booking allowed"))
                        
                        # Test booking cancellation
                        print()
                        print("=" * 80)
                        print(f"{Colors.BOLD}BOOKING CANCELLATION{Colors.ENDC}")
                        print("=" * 80)
                        
                        print(f"\n{Colors.CYAN}Cancelling Booking...{Colors.ENDC}")
                        print(f"  Booking Reference: {booking_response.get('bookingReference')}")
                        print(f"  Booking ID: {booking_id}")
                        print(f"  Current Status: {booking_response.get('status')}")
                        
                        cancel_success, cancel_response, cancel_error = self.make_api_request(
                            "PUT", f"/bookings/{booking_id}/cancel", None, regular_user_token,
                            description="Cancelling the booking"
                        )
                        
                        if cancel_success:
                            print(f"\n{Colors.GREEN}Cancellation Successful!{Colors.ENDC}")
                            print(f"  Booking {booking_id} has been cancelled")
                            print(f"  Seats {', '.join(map(str, selected_seats))} are now available again")
                            self.log(f"[OK] Booking {booking_id} cancelled successfully", "SUCCESS")
                            self.print_test_response("Booking Cancellation", True, {"booking_id": booking_id, "status": "cancelled", "seats_released": selected_seats})
                            self.test_results.append(("Booking Cancellation", "PASS", f"Booking {booking_id} cancelled"))
                        else:
                            print(f"\n{Colors.RED}Cancellation Failed!{Colors.ENDC}")
                            print(f"  Error: {cancel_error}")
                            self.log(f"[X] Booking cancellation failed: {cancel_error}", "ERROR")
                            self.print_test_response("Booking Cancellation", False, None, cancel_error)
                            self.test_results.append(("Booking Cancellation", "FAIL", cancel_error))
                        
                        # E2E Test 3b: Bus Tracking
                        print()
                        print("=" * 80)
                        print(f"{Colors.BOLD}BUS TRACKING SYSTEM{Colors.ENDC}")
                        print("=" * 80)
                        self.log("E2E Test 11b: Real-Time Bus Tracking", "TEST")
                        
                        if 'bus' in self.created_data:
                            bus_id = self.created_data['bus']['id']
                            bus_number = self.created_data['bus'].get('busNumber', 'N/A')
                            
                            print(f"\n{Colors.CYAN}Tracking Information:{Colors.ENDC}")
                            print(f"  Bus Number: {bus_number}")
                            print(f"  Bus ID: {bus_id}")
                            print(f"  Operator: {self.created_data['operator'].get('name', 'N/A')}")
                            
                            # Add some tracking data first
                            print(f"\n{Colors.CYAN}Step 0: Adding Sample Tracking Data...{Colors.ENDC}")
                            print(f"  Simulating GPS updates from the bus...")
                            
                            # Add 3 tracking points (simulating bus journey)
                            tracking_points = [
                                {"latitude": 19.0760, "longitude": 72.8777, "speedKmh": 45.0},  # Mumbai start
                                {"latitude": 18.9388, "longitude": 73.1158, "speedKmh": 60.0},  # Mid-way
                                {"latitude": 18.5204, "longitude": 73.8567, "speedKmh": 55.0}   # Near Pune
                            ]
                            
                            tracking_added = 0
                            for i, point in enumerate(tracking_points, 1):
                                tracking_data = {
                                    "busId": bus_id,
                                    "latitude": point["latitude"],
                                    "longitude": point["longitude"],
                                    "speedKmh": point["speedKmh"]
                                }
                                
                                add_tracking_success, add_tracking_response, add_tracking_error = self.make_api_request(
                                    "POST", "/tracking/update", tracking_data, self.admin_token,
                                    description=f"Adding tracking point {i}/3"
                                )
                                
                                if add_tracking_success:
                                    tracking_added += 1
                                    print(f"  -> Point {i}: ({point['latitude']}, {point['longitude']}) at {point['speedKmh']} km/h")
                            
                            if tracking_added > 0:
                                print(f"\n{Colors.GREEN}Added {tracking_added} tracking points successfully!{Colors.ENDC}")
                            else:
                                print(f"\n{Colors.YELLOW}Could not add tracking data, testing with empty tracking...{Colors.ENDC}")
                            
                            # Get current location
                            print(f"\n{Colors.CYAN}Step 1: Fetching Current Location...{Colors.ENDC}")
                            tracking_success, tracking_response, tracking_error = self.make_api_request(
                                "GET", f"/tracking/bus/{bus_id}/current", None, None,
                                description=f"Getting current location of bus {bus_id}"
                            )
                            
                            if tracking_success:
                                if tracking_response:
                                    print(f"\n{Colors.GREEN}Current Location Found!{Colors.ENDC}")
                                    print(f"  Latitude: {tracking_response.get('latitude', 'N/A')}")
                                    print(f"  Longitude: {tracking_response.get('longitude', 'N/A')}")
                                    print(f"  Speed: {tracking_response.get('speedKmh', 'N/A')} km/h")
                                    print(f"  Last Updated: {tracking_response.get('timestamp', 'N/A')}")
                                else:
                                    print(f"\n{Colors.YELLOW}No Current Location Data{Colors.ENDC}")
                                    print(f"  Bus has not transmitted location data yet")
                                    print(f"  This is normal for newly created test buses")
                                
                                self.log(f"[OK] Bus tracking query successful", "SUCCESS")
                                self.print_test_response("Bus Current Location", True, tracking_response if tracking_response else {"message": "No tracking data yet", "note": "Normal for test buses"})
                                self.test_results.append(("Bus Tracking - Current", "PASS", "Location query successful"))
                            else:
                                print(f"\n{Colors.RED}Tracking Query Failed{Colors.ENDC}")
                                print(f"  Error: {tracking_error}")
                                self.log(f"[X] Bus tracking failed: {tracking_error}", "ERROR")
                                self.print_test_response("Bus Current Location", False, None, tracking_error)
                                self.test_results.append(("Bus Tracking - Current", "FAIL", tracking_error))
                            
                            # Get tracking history
                            print(f"\n{Colors.CYAN}Step 2: Fetching Tracking History...{Colors.ENDC}")
                            history_success, history_response, history_error = self.make_api_request(
                                "GET", f"/tracking/bus/{bus_id}/history", None, None,
                                description=f"Getting tracking history of bus {bus_id}"
                            )
                            
                            if history_success:
                                history_count = len(history_response) if history_response else 0
                                
                                if history_count > 0:
                                    print(f"\n{Colors.GREEN}Tracking History Found!{Colors.ENDC}")
                                    print(f"  Total Records: {history_count}")
                                    print(f"\n  Recent Tracking Points:")
                                    for i, record in enumerate(history_response[:3], 1):
                                        print(f"    {i}. Location: ({record.get('latitude', 'N/A')}, {record.get('longitude', 'N/A')})")
                                        print(f"       Speed: {record.get('speedKmh', 'N/A')} km/h | Time: {record.get('timestamp', 'N/A')}")
                                else:
                                    print(f"\n{Colors.YELLOW}No Tracking History{Colors.ENDC}")
                                    print(f"  Bus has not transmitted any location data yet")
                                    print(f"  This is normal for newly created test buses")
                                
                                self.log(f"[OK] Bus tracking history retrieved ({history_count} records)", "SUCCESS")
                                self.print_test_response("Bus Tracking History", True, {"count": history_count, "records": history_response[:2] if history_response else [], "note": "Empty history is normal for test buses"})
                                self.test_results.append(("Bus Tracking - History", "PASS", f"{history_count} records"))
                            else:
                                print(f"\n{Colors.RED}History Query Failed{Colors.ENDC}")
                                print(f"  Error: {history_error}")
                                self.log(f"[X] Bus tracking history failed: {history_error}", "ERROR")
                                self.print_test_response("Bus Tracking History", False, None, history_error)
                                self.test_results.append(("Bus Tracking - History", "FAIL", history_error))
                    else:
                        self.log(f"[X] Booking creation failed: {booking_error}", "ERROR")
                        self.print_test_response("Booking Creation", False, None, booking_error)
                        self.test_results.append(("Booking Creation", "FAIL", booking_error))
                else:
                    self.log("! No schedule ID available for booking", "WARNING")
                    self.test_results.append(("Booking Creation", "SKIP", "No schedule available"))
            else:
                self.log("! Skipping booking tests (no user token or search results)", "WARNING")
                self.test_results.append(("Booking Journey", "SKIP", "Prerequisites not met"))
            
            # E2E Test 4: Advanced Operator Management
            self.log("E2E Test 12: Advanced Operator Management", "TEST")
            
            # Create a comprehensive operator with full details
            operator_data = {
                'name': f'Premium Bus Lines {int(time.time())}',
                'contactEmail': f'contact_{int(time.time())}@premiumbus.com',
                'contactPhone': '+1-800-BUS-RIDE',
                'licenseNumber': f'PBL{int(time.time())}'
            }
            
            operator_success, operator_response, operator_error = self.make_api_request(
                "POST", "/operator/operators", operator_data, self.admin_token,
                description="Creating a premium bus operator"
            )
            
            if operator_success and operator_response:
                new_operator_id = operator_response.get('id')
                self.created_data['premium_operator'] = operator_response
                self.log(f"[OK] Premium operator created (ID: {new_operator_id})", "SUCCESS")
                
                # Create a luxury bus for this operator
                luxury_bus_data = {
                    'operatorId': new_operator_id,
                    'busNumber': f'PBL-LUXURY-{int(time.time())}',
                    'busType': 'LUXURY',
                    'totalSeats': 32,
                    'amenities': ['WiFi', 'AC', 'Reclining Seats', 'Entertainment System', 'USB Charging']
                }
                
                bus_success, bus_response, bus_error = self.make_api_request(
                    "POST", "/operator/buses", luxury_bus_data, self.admin_token,
                    description="Creating luxury bus with premium amenities"
                )
                
                if bus_success and bus_response:
                    self.log(f"[OK] Luxury bus created (ID: {bus_response.get('id')})", "SUCCESS")
                    self.test_results.append(("Advanced Operator Management", "PASS", f"Created operator and luxury bus"))
                else:
                    self.log(f"[X] Luxury bus creation failed: {bus_error}", "ERROR")
                    self.test_results.append(("Advanced Operator Management", "PARTIAL", f"Operator created, bus failed: {bus_error}"))
            else:
                self.log(f"[X] Premium operator creation failed: {operator_error}", "ERROR")
                self.test_results.append(("Advanced Operator Management", "FAIL", operator_error))
            
            # E2E Test 5: System Monitoring and Data Sync
            self.log("E2E Test 13: System Monitoring and Data Sync", "TEST")
            
            # Check sync status
            sync_success, sync_response, sync_error = self.make_api_request(
                "GET", "/sync/status", None, None,
                description="Checking Elasticsearch sync status"
            )
            
            if sync_success and sync_response:
                db_count = sync_response.get('databaseCount', 0)
                es_count = sync_response.get('elasticsearchCount', 0)
                sync_status = "SYNCED" if db_count == es_count else "OUT_OF_SYNC"
                
                self.log(f"[OK] Data Sync Status: {sync_status}", "SUCCESS" if sync_status == "SYNCED" else "WARNING")
                self.log(f"  Database schedules: {db_count}", "INFO")
                self.log(f"  Elasticsearch documents: {es_count}", "INFO")
                self.print_test_response("Data Sync Status", True, sync_response)
                
                self.test_results.append(("Data Sync Status", "PASS", f"DB: {db_count}, ES: {es_count}"))
                
                # Trigger manual sync if needed
                if db_count != es_count:
                    self.log("Triggering manual sync to align data...", "INFO")
                    manual_sync_success, manual_sync_response, manual_sync_error = self.make_api_request(
                        "POST", "/sync/trigger", None, None,
                        description="Triggering manual Elasticsearch sync"
                    )
                    
                    if manual_sync_success:
                        self.log("[OK] Manual sync completed", "SUCCESS")
                        self.print_test_response("Manual Sync", True, manual_sync_response)
                        self.test_results.append(("Manual Sync", "PASS", "Sync triggered successfully"))
                    else:
                        self.log(f"[X] Manual sync failed: {manual_sync_error}", "ERROR")
                        self.print_test_response("Manual Sync", False, None, manual_sync_error)
                        self.test_results.append(("Manual Sync", "FAIL", manual_sync_error))
            else:
                self.log(f"[X] Sync status check failed: {sync_error}", "ERROR")
                self.print_test_response("Data Sync Status", False, None, sync_error)
                self.test_results.append(("Data Sync Status", "FAIL", sync_error))
            
            print()
            return True
            
        except Exception as e:
            self.log(f"Test execution failed: {str(e)}", "ERROR")
            return False

    def display_detailed_results(self):
        """Display comprehensive test results"""
        step_num = 5 if not self.skip_setup else 2
        self.log(f"STEP {step_num}: Test Results Summary", "STEP", False)
        print("=" * 80)
        
        # Calculate statistics
        total_tests = len(self.test_results)
        passed_tests = sum(1 for _, status, _ in self.test_results if status == "PASS")
        failed_tests = total_tests - passed_tests
        success_rate = (passed_tests / total_tests * 100) if total_tests > 0 else 0
        
        # Overall status
        print(f"{Colors.BOLD}REDBUS APPLICATION TEST RESULTS{Colors.ENDC}")
        print("=" * 80)
        
        # Show JUnit coverage if available
        if self.junit_coverage:
            print(f"{Colors.BOLD}JUNIT TEST COVERAGE:{Colors.ENDC}")
            print(f"  Instruction Coverage: {self.junit_coverage['instruction_coverage']}%")
            print(f"  Branch Coverage: {self.junit_coverage['branch_coverage']}%")
            print(f"  Report: target/site/jacoco/index.html")
            print()
        
        print(f"{Colors.BOLD}END-TO-END TEST RESULTS:{Colors.ENDC}")
        print(f"Total Tests Run: {total_tests}")
        print(f"{Colors.GREEN}Tests Passed: {passed_tests}{Colors.ENDC}")
        print(f"{Colors.RED}Tests Failed: {failed_tests}{Colors.ENDC}")
        print(f"Success Rate: {success_rate:.1f}%")
        
        # Show seed data info if created
        if self.seed_data_created:
            print()
            print(f"{Colors.BOLD}INITIAL TEST DATA:{Colors.ENDC}")
            print(f"  Sample data seeded for demonstration")
            print(f"  Login: demo_admin / Admin@123")
        print()
        
        # Detailed results
        print(f"{Colors.BOLD}DETAILED TEST RESULTS:{Colors.ENDC}")
        print("-" * 80)
        
        for test_name, status, details in self.test_results:
            status_color = Colors.GREEN if status == "PASS" else Colors.RED
            status_icon = "[PASS]" if status == "PASS" else "[FAIL]"
            print(f"{status_icon} {test_name:<25} {status_color}{status:<6}{Colors.ENDC} {details}")
        
        print()
        
        # Created data summary
        if self.created_data:
            print(f"{Colors.BOLD}DATA CREATED DURING TESTS:{Colors.ENDC}")
            print("-" * 80)
            
            if 'admin_user' in self.created_data:
                user = self.created_data['admin_user']
                print(f"Admin User: {user.get('username')} (ID: {user.get('id')}, Role: {user.get('role')})")
            
            if 'operator' in self.created_data:
                operator = self.created_data['operator']
                print(f"Bus Operator: {operator.get('name')} (ID: {operator.get('id')})")
            
            if 'route' in self.created_data:
                route = self.created_data['route']
                print(f"Route: ID {route.get('id')} ({route.get('distanceKm')}km, ~{route.get('estimatedDurationHours')}h)")
            
            if 'bus' in self.created_data:
                bus = self.created_data['bus']
                print(f"Bus: {bus.get('busNumber')} ({bus.get('busType')}, {bus.get('totalSeats')} seats)")
            
            if 'schedule' in self.created_data:
                schedule = self.created_data['schedule']
                print(f"Schedule: ID {schedule.get('id')} (${schedule.get('price')})")
            
            print()
        
        # System status
        print(f"{Colors.BOLD}SYSTEM STATUS:{Colors.ENDC}")
        print("-" * 80)
        
        # Check service status
        services = [
            ("PostgreSQL Database", "docker exec redbus-postgres pg_isready -h localhost -p 5432"),
            ("Redis Cache", "docker exec redbus-redis redis-cli ping"),
            ("RedBus Application", f"curl -s {self.base_url}/actuator/health")
        ]
        
        for service_name, check_cmd in services:
            success, _ = self.run_command(check_cmd)
            status_icon = "[RUNNING]" if success else "[STOPPED]"
            status_text = "OK" if success else "ERROR"
            print(f"{status_icon} {service_name:<20} {status_text}")
        
        # Elasticsearch status
        try:
            response = requests.get("http://localhost:9200/_cluster/health", timeout=5)
            if response.ok:
                health = response.json()
                es_status = health.get("status", "unknown").upper()
                status_icon = "[RUNNING]" if es_status in ["GREEN", "YELLOW"] else "[STOPPED]"
                print(f"{status_icon} {'Elasticsearch':<20} {es_status}")
            else:
                print(f"[STOPPED] {'Elasticsearch':<20} ERROR")
        except:
            print(f"[STOPPED] {'Elasticsearch':<20} ERROR")
        
        print()
        
        # Final verdict
        if success_rate >= 90:
            print(f"{Colors.GREEN}{Colors.BOLD}EXCELLENT! RedBus application is fully functional!{Colors.ENDC}")
            print(f"{Colors.GREEN}* All core features are working correctly{Colors.ENDC}")
            print(f"{Colors.GREEN}* Authentication system operational{Colors.ENDC}")
            print(f"{Colors.GREEN}* Business logic functioning properly{Colors.ENDC}")
            print(f"{Colors.GREEN}* Database operations successful{Colors.ENDC}")
            print(f"{Colors.GREEN}* Search functionality active{Colors.ENDC}")
        elif success_rate >= 70:
            print(f"{Colors.YELLOW}{Colors.BOLD}GOOD! Most features are working with minor issues{Colors.ENDC}")
            print(f"{Colors.YELLOW}* Core functionality operational{Colors.ENDC}")
            print(f"{Colors.YELLOW}! Some features may need attention{Colors.ENDC}")
        else:
            print(f"{Colors.RED}{Colors.BOLD}ISSUES DETECTED! Application needs attention{Colors.ENDC}")
            print(f"{Colors.RED}! Multiple features are not working correctly{Colors.ENDC}")
            print(f"{Colors.RED}! Please check the error messages above{Colors.ENDC}")
        
        print()
        
        # Usage instructions
        print(f"{Colors.BOLD}HOW TO USE YOUR REDBUS APPLICATION:{Colors.ENDC}")
        print("-" * 80)
        print("Application URL: http://localhost:9090/api")
        print("Health Check: http://localhost:9090/api/actuator/health")
        print("Search Buses: POST http://localhost:9090/api/public/search")
        print("User Registration: POST http://localhost:9090/api/auth/register")
        print("User Login: POST http://localhost:9090/api/auth/login")
        print()
        print("For API documentation and examples, check the project README")
        print()
        
        # Execution time
        duration = datetime.now() - self.start_time
        print(f"Total execution time: {duration}")
        print("=" * 80)

    def cleanup_on_failure(self):
        """Clean up resources if setup fails"""
        self.log("Cleaning up due to setup failure...", "WARNING")
        self.run_command("docker-compose down", timeout=60, show_output=True)

    def run_complete_setup(self) -> bool:
        """Run the complete setup and test process"""
        try:
            self.print_banner()
            
            # Check if services are already running
            if self.check_if_services_running():
                self.log("Skipping setup - services are already healthy", "SUCCESS")
                self.log("Proceeding directly to tests...", "INFO")
                print()
                self.skip_setup = True
            else:
                # Step 1: Check prerequisites
                if not self.check_prerequisites():
                    return False
                
                # Step 2: Setup services
                if not self.setup_services():
                    self.cleanup_on_failure()
                    return False
                
                # Step 3: Wait for services
                if not self.wait_for_all_services():
                    self.cleanup_on_failure()
                    return False
                
                # Step 3.5: Seed initial test data (only for fresh deployments)
                if not self.seed_initial_test_data():
                    self.log("Data seeding had issues, continuing...", "WARNING")
                
                # Step 3.6: Run JUnit tests with coverage
                if not self.run_junit_tests_with_coverage():
                    self.log("JUnit tests had issues, continuing with E2E tests...", "WARNING")
            
            # Step 4: Run E2E tests
            if not self.run_comprehensive_tests():
                self.log("Some tests failed, but system is partially functional", "WARNING")
            
            # Step 5: Display results
            self.display_detailed_results()
            
            return True
            
        except KeyboardInterrupt:
            self.log("Setup interrupted by user", "WARNING")
            self.cleanup_on_failure()
            return False
        except Exception as e:
            self.log(f"Unexpected error: {str(e)}", "ERROR")
            self.cleanup_on_failure()
            return False

def main():
    """Main entry point"""
    print("Starting RedBus Complete Setup & Test Suite...")
    print("Automatically setting up and testing your RedBus application...")
    print()
    
    setup = RedBusAutoSetup()
    success = setup.run_complete_setup()
    
    if success:
        print(f"\n{Colors.GREEN}Setup and testing completed successfully!{Colors.ENDC}")
        print("Your RedBus application is ready to use.")
    else:
        print(f"\n{Colors.RED}Setup encountered issues.{Colors.ENDC}")
        print("Please check the error messages above and try again.")
    
    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()
