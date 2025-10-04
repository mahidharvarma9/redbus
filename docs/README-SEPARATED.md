# RedBus - Separated Frontend & Backend Architecture

This is a refactored version of the RedBus application with clear separation between frontend and backend components.

## 🏗️ **Architecture Overview**

```
redbus/
├── frontend/                 # Frontend Application
│   ├── src/                 # Source code
│   │   ├── index.html       # Main HTML
│   │   ├── styles/          # CSS files
│   │   └── js/              # JavaScript modules
│   ├── package.json         # Frontend dependencies
│   ├── vite.config.js       # Build configuration
│   └── Dockerfile           # Frontend container
├── backend/                 # Backend API
│   ├── src/                 # Java source code
│   ├── pom.xml              # Maven dependencies
│   └── Dockerfile           # Backend container
├── docker-compose.yml       # Original monolithic setup
└── docker-compose.separated.yml  # Separated services
```

## 🚀 **Quick Start - Separated Architecture**

### **Option 1: Full Stack (Recommended)**
```bash
# Start all services (Frontend + Backend + Database + Search)
docker-compose -f docker-compose.separated.yml up -d

# Access applications
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Elasticsearch: http://localhost:9200
```

### **Option 2: Development Mode**
```bash
# Start only infrastructure services
docker-compose up -d postgres redis elasticsearch

# Run backend locally
cd backend
mvn spring-boot:run

# Run frontend locally (in another terminal)
cd frontend
npm install
npm run dev
```

## 📁 **Project Structure**

### **Frontend (`/frontend`)**
- **Modern ES6 Modules** - Clean, maintainable JavaScript
- **Vite Build System** - Fast development and production builds
- **Modular Architecture** - Separated concerns (API, Auth, Search, Booking, Tracking)
- **Responsive Design** - Bootstrap 5 with custom CSS
- **Nginx Production** - Optimized static file serving

### **Backend (`/backend`)**
- **Spring Boot API** - RESTful microservice architecture
- **JWT Authentication** - Stateless security
- **Elasticsearch Integration** - Advanced search capabilities
- **PostgreSQL Database** - ACID compliance
- **Comprehensive Testing** - Unit and integration tests

## 🔧 **Development Workflow**

### **Frontend Development**
```bash
cd frontend

# Install dependencies
npm install

# Development server (with hot reload)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### **Backend Development**
```bash
cd backend

# Run tests
mvn test

# Start development server
mvn spring-boot:run

# Build JAR
mvn clean package
```

## 🐳 **Docker Services**

### **Infrastructure Services**
- **PostgreSQL** - Primary database
- **Redis** - Caching layer
- **Elasticsearch** - Search engine

### **Application Services**
- **Backend API** - Java Spring Boot service
- **Frontend** - Nginx-served React-like SPA

## 📊 **API Documentation**

### **Authentication Endpoints**
```
POST /api/auth/login          # User login
POST /api/auth/register       # User registration
GET  /api/auth/me            # Current user info
```

### **Search Endpoints**
```
POST /api/public/search      # Basic bus search
GET  /api/search/advanced    # Elasticsearch-powered search
GET  /api/search/text        # Text-based search
```

### **Booking Endpoints**
```
GET  /api/bookings           # User bookings
POST /api/bookings           # Create booking
PUT  /api/bookings/{id}/cancel # Cancel booking
```

### **Payment Endpoints**
```
POST /api/payments           # Process payment
GET  /api/payments/booking/{id} # Booking payments
```

### **Tracking Endpoints**
```
GET  /api/tracking/all-active    # All active buses
GET  /api/tracking/bus/{id}/current # Bus location
```

## 🧪 **Testing**

### **Frontend Testing**
```bash
cd frontend
# Manual testing through browser
# Automated testing can be added with Jest/Vitest
```

### **Backend Testing**
```bash
cd backend
mvn test                    # Unit tests
mvn verify                  # Integration tests
```

## 🔒 **Security Features**

### **Frontend Security**
- **JWT Token Management** - Secure authentication
- **Input Validation** - Client-side validation
- **XSS Protection** - Content Security Policy
- **HTTPS Ready** - Production security headers

### **Backend Security**
- **JWT Authentication** - Stateless token-based auth
- **Role-based Access Control** - USER, ADMIN, OPERATOR roles
- **Input Validation** - Server-side validation
- **SQL Injection Protection** - JPA/Hibernate ORM
- **CORS Configuration** - Cross-origin resource sharing

## 📈 **Performance Optimizations**

### **Frontend Performance**
- **Code Splitting** - Modular JavaScript loading
- **Asset Optimization** - Minified CSS/JS
- **Caching Headers** - Static asset caching
- **CDN Ready** - Production deployment ready

### **Backend Performance**
- **Database Indexing** - Optimized queries
- **Connection Pooling** - Efficient database connections
- **Elasticsearch** - Sub-millisecond search
- **Caching Layer** - Redis for session management

## 🚀 **Production Deployment**

### **Environment Variables**
```bash
# Database
POSTGRES_DB=redbus
POSTGRES_USER=redbus
POSTGRES_PASSWORD=redbus123

# JWT
JWT_SECRET=your-production-secret-key
JWT_EXPIRATION=86400000

# Elasticsearch
ELASTICSEARCH_URL=http://elasticsearch:9200
```

### **Production Commands**
```bash
# Build and deploy
docker-compose -f docker-compose.separated.yml up -d

# Scale services
docker-compose -f docker-compose.separated.yml up -d --scale backend=3

# Health checks
curl http://localhost:8080/api/actuator/health
curl http://localhost:3000
```

## 🔄 **Migration from Monolithic**

### **Benefits of Separation**
1. **Independent Scaling** - Scale frontend and backend separately
2. **Technology Flexibility** - Use different tech stacks
3. **Team Separation** - Frontend and backend teams can work independently
4. **Deployment Flexibility** - Deploy services independently
5. **Performance Optimization** - Optimize each service separately

### **Migration Steps**
1. **Extract Frontend** - Move UI to separate service
2. **API Gateway** - Centralize API routing
3. **Service Communication** - HTTP/REST between services
4. **Data Consistency** - Event-driven architecture
5. **Monitoring** - Service-specific monitoring

## 📚 **Documentation**

- **API Documentation** - Swagger/OpenAPI integration ready
- **Frontend Documentation** - Component and service documentation
- **Deployment Guide** - Production deployment instructions
- **Development Guide** - Local development setup

## 🤝 **Contributing**

1. **Frontend Changes** - Work in `/frontend` directory
2. **Backend Changes** - Work in `/backend` directory
3. **API Changes** - Update both frontend and backend
4. **Testing** - Ensure both services work together

## 📞 **Support**

- **Frontend Issues** - Check browser console and network tab
- **Backend Issues** - Check application logs and database
- **Integration Issues** - Verify API communication
- **Performance Issues** - Monitor service metrics

---

**This separated architecture provides better maintainability, scalability, and development experience while maintaining all the original functionality!** 🚀
