// Global variables
let currentUser = null;
let searchResults = [];
let selectedBus = null;
let selectedSeats = [];
let currentBooking = null;

// API Base URL
const API_BASE = '/api';

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    // Set default travel date to tomorrow
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    document.getElementById('travelDate').value = tomorrow.toISOString().split('T')[0];
    
    // Check if user is already logged in
    checkAuthStatus();
    
    // Setup event listeners
    setupEventListeners();
});

function setupEventListeners() {
    // Search form
    document.getElementById('searchForm').addEventListener('submit', handleSearch);
    
    // Login form
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    
    // Register form
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    
    // Tracking bus selection
    document.getElementById('trackingBusSelect').addEventListener('change', handleTrackingBusChange);
}

// Authentication functions
function checkAuthStatus() {
    const token = localStorage.getItem('token');
    if (token) {
        // Verify token with backend
        fetch(`${API_BASE}/auth/me`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Invalid token');
        })
        .then(user => {
            currentUser = user;
            updateAuthUI();
        })
        .catch(() => {
            localStorage.removeItem('token');
            updateAuthUI();
        });
    }
}

function updateAuthUI() {
    const authSection = document.getElementById('authSection');
    const userSection = document.getElementById('userSection');
    
    if (currentUser) {
        authSection.classList.add('d-none');
        userSection.classList.remove('d-none');
        document.getElementById('userName').textContent = `Welcome, ${currentUser.firstName}`;
    } else {
        authSection.classList.remove('d-none');
        userSection.classList.add('d-none');
    }
}

function showLogin() {
    new bootstrap.Modal(document.getElementById('loginModal')).show();
}

function showRegister() {
    new bootstrap.Modal(document.getElementById('registerModal')).show();
}

function logout() {
    localStorage.removeItem('token');
    currentUser = null;
    updateAuthUI();
    showSection('search');
}

// API functions
async function apiCall(endpoint, options = {}) {
    const token = localStorage.getItem('token');
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };
    
    const response = await fetch(`${API_BASE}${endpoint}`, { ...defaultOptions, ...options });
    
    if (!response.ok) {
        const error = await response.text();
        throw new Error(error);
    }
    
    return response.json();
}

// Search functions
async function handleSearch(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const searchData = {
        origin: formData.get('origin') || document.getElementById('origin').value,
        destination: formData.get('destination') || document.getElementById('destination').value,
        travelDate: formData.get('travelDate') || document.getElementById('travelDate').value,
        busType: document.getElementById('busType').value || null
    };
    
    try {
        showLoading('searchResults');
        const results = await apiCall('/public/search', {
            method: 'POST',
            body: JSON.stringify(searchData)
        });
        
        displaySearchResults(results);
    } catch (error) {
        showError('searchResults', 'Failed to search buses: ' + error.message);
    }
}

function displaySearchResults(results) {
    const container = document.getElementById('searchResults');
    
    if (results.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No buses found for your search criteria.</div>';
        return;
    }
    
    container.innerHTML = results.map(bus => `
        <div class="card bus-card">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h5 class="card-title">${bus.busNumber} - ${bus.operatorName}</h5>
                        <p class="card-text">
                            <i class="fas fa-bus"></i> ${bus.busType} | 
                            <i class="fas fa-clock"></i> ${bus.departureTime} - ${bus.arrivalTime} | 
                            <i class="fas fa-users"></i> ${bus.availableSeats} seats available
                        </p>
                        <p class="text-muted">${bus.origin} → ${bus.destination} (${bus.duration})</p>
                        <div class="amenities">
                            ${bus.amenities.map(amenity => `<span class="badge bg-secondary me-1">${amenity}</span>`).join('')}
                        </div>
                    </div>
                    <div class="col-md-4 text-end">
                        <h4 class="text-primary">₹${bus.price}</h4>
                        <button class="btn btn-primary" onclick="selectBus(${JSON.stringify(bus).replace(/"/g, '&quot;')})">
                            Select Seats
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

// Bus selection and seat booking
function selectBus(bus) {
    if (!currentUser) {
        alert('Please login to book tickets');
        showLogin();
        return;
    }
    
    selectedBus = bus;
    showSeatSelection();
}

function showSeatSelection() {
    // Generate seat map
    const seatMap = document.getElementById('seatMap');
    const totalSeats = selectedBus.totalSeats;
    const seatsPerRow = 4;
    const rows = Math.ceil(totalSeats / seatsPerRow);
    
    let seatHtml = '<div class="seat-map">';
    for (let row = 1; row <= rows; row++) {
        seatHtml += '<div class="row mb-2">';
        for (let col = 1; col <= seatsPerRow; col++) {
            const seatNumber = (row - 1) * seatsPerRow + col;
            if (seatNumber <= totalSeats) {
                seatHtml += `
                    <div class="col-3">
                        <div class="seat available" data-seat="${seatNumber}" onclick="toggleSeat(${seatNumber})">
                            ${seatNumber}
                        </div>
                    </div>
                `;
            }
        }
        seatHtml += '</div>';
    }
    seatHtml += '</div>';
    
    seatMap.innerHTML = seatHtml;
    
    // Setup passenger details
    setupPassengerDetails();
    
    // Show modal
    new bootstrap.Modal(document.getElementById('seatModal')).show();
}

function toggleSeat(seatNumber) {
    const seatElement = document.querySelector(`[data-seat="${seatNumber}"]`);
    
    if (seatElement.classList.contains('selected')) {
        seatElement.classList.remove('selected');
        selectedSeats = selectedSeats.filter(seat => seat !== seatNumber);
    } else {
        if (selectedSeats.length >= 6) { // Max 6 seats per booking
            alert('Maximum 6 seats can be selected');
            return;
        }
        seatElement.classList.add('selected');
        selectedSeats.push(seatNumber);
    }
    
    updatePassengerDetails();
}

function setupPassengerDetails() {
    const container = document.getElementById('passengerDetails');
    container.innerHTML = '<p>Select seats first</p>';
}

function updatePassengerDetails() {
    const container = document.getElementById('passengerDetails');
    
    if (selectedSeats.length === 0) {
        container.innerHTML = '<p>Select seats first</p>';
        return;
    }
    
    container.innerHTML = selectedSeats.map((seatNumber, index) => `
        <div class="row mb-2">
            <div class="col-md-3">
                <label class="form-label">Seat ${seatNumber}</label>
            </div>
            <div class="col-md-3">
                <input type="text" class="form-control" placeholder="Passenger Name" id="passengerName${index}" required>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control" placeholder="Age" id="passengerAge${index}" required>
            </div>
            <div class="col-md-2">
                <select class="form-select" id="passengerGender${index}" required>
                    <option value="">Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>
        </div>
    `).join('');
}

function proceedToPayment() {
    if (selectedSeats.length === 0) {
        alert('Please select at least one seat');
        return;
    }
    
    // Validate passenger details
    for (let i = 0; i < selectedSeats.length; i++) {
        const name = document.getElementById(`passengerName${i}`).value;
        const age = document.getElementById(`passengerAge${i}`).value;
        const gender = document.getElementById(`passengerGender${i}`).value;
        
        if (!name || !age || !gender) {
            alert('Please fill all passenger details');
            return;
        }
    }
    
    // Create booking
    createBooking();
}

async function createBooking() {
    try {
        const passengers = selectedSeats.map((seatNumber, index) => ({
            seatNumber: seatNumber,
            passengerName: document.getElementById(`passengerName${index}`).value,
            passengerAge: parseInt(document.getElementById(`passengerAge${index}`).value),
            passengerGender: document.getElementById(`passengerGender${index}`).value
        }));
        
        const bookingData = {
            scheduleId: selectedBus.scheduleId,
            travelDate: document.getElementById('travelDate').value,
            passengers: passengers
        };
        
        const booking = await apiCall('/bookings', {
            method: 'POST',
            body: JSON.stringify(bookingData)
        });
        
        currentBooking = booking;
        showPaymentModal();
        
    } catch (error) {
        alert('Failed to create booking: ' + error.message);
    }
}

function showPaymentModal() {
    document.getElementById('paymentAmount').value = `₹${currentBooking.totalAmount}`;
    new bootstrap.Modal(document.getElementById('paymentModal')).show();
}

async function processPayment() {
    try {
        const paymentData = {
            bookingId: currentBooking.id,
            amount: currentBooking.totalAmount,
            paymentMethod: document.getElementById('paymentMethod').value
        };
        
        const payment = await apiCall('/payments', {
            method: 'POST',
            body: JSON.stringify(paymentData)
        });
        
        alert(`Payment ${payment.paymentStatus}! Booking Reference: ${currentBooking.bookingReference}`);
        
        // Close modals
        bootstrap.Modal.getInstance(document.getElementById('paymentModal')).hide();
        bootstrap.Modal.getInstance(document.getElementById('seatModal')).hide();
        
        // Reset selections
        selectedBus = null;
        selectedSeats = [];
        currentBooking = null;
        
    } catch (error) {
        alert('Payment failed: ' + error.message);
    }
}

// Authentication handlers
async function handleLogin(e) {
    e.preventDefault();
    
    const loginData = {
        username: document.getElementById('loginUsername').value,
        password: document.getElementById('loginPassword').value
    };
    
    try {
        const response = await apiCall('/auth/login', {
            method: 'POST',
            body: JSON.stringify(loginData)
        });
        
        localStorage.setItem('token', response.token);
        currentUser = response;
        updateAuthUI();
        
        bootstrap.Modal.getInstance(document.getElementById('loginModal')).hide();
        document.getElementById('loginForm').reset();
        
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    
    const registerData = {
        username: document.getElementById('registerUsername').value,
        email: document.getElementById('email').value,
        password: document.getElementById('registerPassword').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        phone: document.getElementById('phone').value
    };
    
    try {
        const response = await apiCall('/auth/register', {
            method: 'POST',
            body: JSON.stringify(registerData)
        });
        
        localStorage.setItem('token', response.token);
        currentUser = response;
        updateAuthUI();
        
        bootstrap.Modal.getInstance(document.getElementById('registerModal')).hide();
        document.getElementById('registerForm').reset();
        
    } catch (error) {
        alert('Registration failed: ' + error.message);
    }
}

// Navigation functions
function showSection(sectionName) {
    // Hide all sections
    document.getElementById('searchResults').style.display = 'none';
    document.getElementById('bookingsSection').classList.add('d-none');
    document.getElementById('trackingSection').classList.add('d-none');
    
    // Show selected section
    if (sectionName === 'search') {
        document.getElementById('searchResults').style.display = 'block';
    } else if (sectionName === 'bookings') {
        document.getElementById('bookingsSection').classList.remove('d-none');
        loadUserBookings();
    } else if (sectionName === 'tracking') {
        document.getElementById('trackingSection').classList.remove('d-none');
        loadTrackingBuses();
    }
}

// My Bookings functions
async function loadUserBookings() {
    if (!currentUser) {
        document.getElementById('bookingsList').innerHTML = '<div class="alert alert-warning">Please login to view your bookings</div>';
        return;
    }
    
    try {
        const bookings = await apiCall('/bookings');
        displayBookings(bookings);
    } catch (error) {
        document.getElementById('bookingsList').innerHTML = '<div class="alert alert-danger">Failed to load bookings: ' + error.message + '</div>';
    }
}

function displayBookings(bookings) {
    const container = document.getElementById('bookingsList');
    
    if (bookings.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No bookings found</div>';
        return;
    }
    
    container.innerHTML = bookings.map(booking => `
        <div class="card mb-3">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h5 class="card-title">${booking.busNumber} - ${booking.operatorName}</h5>
                        <p class="card-text">
                            <strong>Booking Reference:</strong> ${booking.bookingReference}<br>
                            <strong>Route:</strong> ${booking.origin} → ${booking.destination}<br>
                            <strong>Date:</strong> ${booking.bookingDate}<br>
                            <strong>Time:</strong> ${booking.departureTime} - ${booking.arrivalTime}<br>
                            <strong>Seats:</strong> ${booking.passengers.map(p => `Seat ${p.seatNumber} (${p.passengerName})`).join(', ')}
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <h4 class="text-primary">₹${booking.totalAmount}</h4>
                        <span class="badge bg-${getStatusColor(booking.status)}">${booking.status}</span><br>
                        ${booking.status === 'PENDING' ? `<button class="btn btn-danger btn-sm mt-2" onclick="cancelBooking(${booking.id})">Cancel</button>` : ''}
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

function getStatusColor(status) {
    switch (status) {
        case 'CONFIRMED': return 'success';
        case 'PENDING': return 'warning';
        case 'CANCELLED': return 'danger';
        case 'COMPLETED': return 'info';
        default: return 'secondary';
    }
}

async function cancelBooking(bookingId) {
    if (confirm('Are you sure you want to cancel this booking?')) {
        try {
            await apiCall(`/bookings/${bookingId}/cancel`, { method: 'PUT' });
            alert('Booking cancelled successfully');
            loadUserBookings();
        } catch (error) {
            alert('Failed to cancel booking: ' + error.message);
        }
    }
}

// Live Tracking functions
async function loadTrackingBuses() {
    try {
        const buses = await apiCall('/tracking/all-active');
        displayTrackingBuses(buses);
    } catch (error) {
        document.getElementById('trackingInfo').innerHTML = '<div class="alert alert-danger">Failed to load bus locations: ' + error.message + '</div>';
    }
}

function displayTrackingBuses(buses) {
    const select = document.getElementById('trackingBusSelect');
    select.innerHTML = '<option value="">Select a bus to track</option>' +
        buses.map(bus => `<option value="${bus.busId}">${bus.busNumber}</option>`).join('');
}

async function handleTrackingBusChange() {
    const busId = document.getElementById('trackingBusSelect').value;
    if (!busId) {
        document.getElementById('trackingInfo').innerHTML = '';
        document.getElementById('gpsCoordinates').textContent = '';
        return;
    }
    
    try {
        const tracking = await apiCall(`/tracking/bus/${busId}/current`);
        displayTrackingInfo(tracking);
    } catch (error) {
        document.getElementById('trackingInfo').innerHTML = '<div class="alert alert-danger">Failed to load tracking info: ' + error.message + '</div>';
    }
}

function displayTrackingInfo(tracking) {
    const container = document.getElementById('trackingInfo');
    const coordinates = document.getElementById('gpsCoordinates');
    
    container.innerHTML = `
        <div class="card">
            <div class="card-body">
                <h5>Bus ${tracking.busNumber}</h5>
                <p><strong>Last Updated:</strong> ${new Date(tracking.timestamp).toLocaleString()}</p>
                <p><strong>Speed:</strong> ${tracking.speedKmh || 'N/A'} km/h</p>
                <p><strong>Direction:</strong> ${tracking.directionDegrees || 'N/A'}°</p>
            </div>
        </div>
    `;
    
    coordinates.textContent = `${tracking.latitude}, ${tracking.longitude}`;
}

// Utility functions
function showLoading(containerId) {
    document.getElementById(containerId).innerHTML = '<div class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>';
}

function showError(containerId, message) {
    document.getElementById(containerId).innerHTML = `<div class="alert alert-danger">${message}</div>`;
}
