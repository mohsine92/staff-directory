# Aggro.io - Employee Directory Management System

A comprehensive JavaFX application for managing employee directories with role-based access control, featuring visitor and administrator interfaces.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Default Data](#default-data)
- [API Integration](#api-integration)
- [Database](#database)
- [Building and Running](#building-and-running)
- [Troubleshooting](#troubleshooting)

## Overview

Aggro.io is a desktop application built with JavaFX that provides a complete employee directory management system. The application supports two distinct user roles:

- **Visitor Mode**: Search and view employee information
- **Administrator Mode**: Full CRUD operations on Sites, Services, and Employees

The application uses Hibernate ORM for database management and includes features like PDF generation, API integration, and secure password authentication.

## Features

### Visitor Mode
- **Advanced Search**:
  - Search by name (partial input, case-insensitive)
  - Filter by site (dropdown selection)
  - Filter by service (dropdown selection)
  - Combined search with multiple criteria
- **Employee Display**:
  - Detailed employee cards showing all information
  - Table view with sortable columns
  - Real-time search results
- **PDF Generation**:
  - Generate professional PDF cards for employees
  - Save PDFs to any location
  - Includes all employee details

### Administrator Mode
- **Authentication**:
  - Secure login with email and password
  - SHA-256 password hashing
  - Session management
- **Sites Management**:
  - Create, Read, Update, Delete operations
  - City-based site information
  - Validation and error handling
- **Services Management**:
  - Create, Read, Update, Delete operations
  - Service name management
  - Validation and error handling
- **Employees Management**:
  - Create, Read, Update, Delete operations
  - Complete employee information management
  - Form validation (email, phone numbers)
  - Automatic phone number formatting
- **Data Import**:
  - Import employees from RandomUser API
  - Automatic assignment to sites and services
  - Batch import (10 employees per request)

## Project Structure

```
aggro-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/exemple/
│   │   │       ├── Main.java                    # Application entry point
│   │   │       ├── HibernateUtils.java          # Hibernate session factory
│   │   │       ├── models/                      # JPA entities
│   │   │       │   ├── User.java                # Abstract base class
│   │   │       │   ├── Admin.java               # Administrator entity
│   │   │       │   ├── Employee.java            # Employee entity
│   │   │       │   ├── Services.java            # Service entity
│   │   │       │   └── Sites.java               # Site entity
│   │   │       ├── repository/                  # Data access layer
│   │   │       │   ├── AdminRepo.java
│   │   │       │   ├── EmployeeRepository.java
│   │   │       │   ├── ServiceRepo.java
│   │   │       │   └── SitesRepo.java
│   │   │       ├── services/                    # Business logic layer
│   │   │       │   ├── AdminService.java        # Admin operations & authentication
│   │   │       │   ├── EmployeeService.java     # Employee operations
│   │   │       │   ├── ServiceService.java      # Service operations
│   │   │       │   ├── SiteService.java         # Site operations
│   │   │       │   ├── PDFService.java          # PDF generation
│   │   │       │   └── RandomUserService.java    # API integration
│   │   │       ├── view/                        # JavaFX user interfaces
│   │   │       │   ├── VisitorView.java         # Visitor interface
│   │   │       │   ├── LoginView.java           # Admin login
│   │   │       │   └── AdminView.java           # Administrator interface
│   │   │       └── util/                        # Utility classes
│   │   │           ├── DataInitializer.java     # Default data initialization
│   │   │           └── ValidationUtils.java      # Form validation
│   │   └── resources/
│   │       └── hibernate.cfg.xml               # Hibernate configuration
│   └── test/
├── pom.xml                                      # Maven configuration
└── README.md                                    # This file
```

## Technologies Used

- **Java 17**: Programming language
- **JavaFX 17**: GUI framework
- **Hibernate 6.4.4**: ORM framework
- **H2 Database 2.2.224**: Embedded database (file-based for persistence)
- **Maven**: Build and dependency management
- **OpenPDF 1.3.30**: PDF generation library
- **OkHttp 4.12.0**: HTTP client for API calls
- **Jackson 2.15.2**: JSON processing

## Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+**
- **Internet connection** (for RandomUser API import)

## Installation

1. **Clone or download the project**:
   ```bash
   cd /path/to/aggro-app
   ```

2. **Verify Java version**:
   ```bash
   java -version
   # Should show version 17 or higher
   ```

3. **Verify Maven installation**:
   ```bash
   mvn -version
   ```

## Configuration

### Database Configuration

The application uses H2 database with file-based persistence. The database file is stored in:
```
./data/annuaire-db.mv.db
```

Configuration is managed in `src/main/resources/hibernate.cfg.xml`:
- Database URL: `jdbc:h2:file:./data/annuaire-db`
- Auto-update schema: Enabled
- SQL debugging: Enabled (can be disabled in production)

### Application Settings

- **Window Mode**: All main windows open maximized (fullscreen)
- **Dialogs**: Edit dialogs and alerts remain as normal-sized popups
- **Data Persistence**: All data is automatically saved to the database file

## Usage

### Starting the Application

**Option 1: Using Maven**
```bash
cd aggro-app
mvn clean compile javafx:run
```

**Option 2: Using IntelliJ IDEA**
1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/com/exemple/Main.java`
3. Right-click → "Run 'Main.main()'"

**Option 3: Using Eclipse**
1. Import as Maven project
2. Run `Main.java` as Java Application

### Default Credentials

Upon first launch, the application automatically creates:
- **Email**: `admin@example.com`
- **Password**: `admin123`

### Visitor Mode

1. **Launch the application** - The visitor interface opens by default
2. **Search employees**:
   - Enter a name (first or last) in the search field
   - Select a site from the dropdown (optional)
   - Select a service from the dropdown (optional)
   - Click "Search" or press Enter
3. **View details**:
   - Click on an employee in the table
   - View detailed information in the details panel
4. **Generate PDF**:
   - Select an employee
   - Click "Generate PDF"
   - Choose save location
   - PDF is generated with all employee information

### Administrator Mode

1. **Access login**:
   - Click "Administrator" button in visitor view
   - Or navigate directly to login screen

2. **Login**:
   - Enter email: `admin@example.com`
   - Enter password: `admin123`
   - Click "Log in"

3. **Manage Sites**:
   - Go to "Sites" tab
   - Add: Enter city name and click "Add"
   - Edit: Select a site, click "Edit", modify and save
   - Delete: Select a site, click "Delete", confirm

4. **Manage Services**:
   - Go to "Services" tab
   - Add: Enter service name and click "Add"
   - Edit: Select a service, click "Edit", modify and save
   - Delete: Select a service, click "Delete", confirm

5. **Manage Employees**:
   - Go to "Employees" tab
   - Add: Fill in the form (all fields required)
     - First name, Last name
     - Phone (10 digits), Mobile (10 digits)
     - Email (valid format)
     - Select Site and Service
   - Edit: Select an employee, click "Edit", modify and save
   - Delete: Select an employee, click "Delete", confirm

6. **Import from RandomUser API**:
   - Click "Import from RandomUser" button in the menu bar
   - Wait for import to complete (10 employees)
   - Employees are automatically assigned to random sites and services

7. **Logout**:
   - Click "Log out" button
   - Returns to visitor view

## Default Data

On first launch, the application automatically initializes:

### Sites (7 locations)
- Paris, France
- Lyon, France
- Marseille, France
- Toulouse, France
- Londres, UK
- San Francisco, USA
- Hong Kong, Chine

### Services (8 departments)
- Comptabilité (Accounting)
- Production
- Accueil (Reception)
- Ressources Humaines (Human Resources)
- Informatique (IT)
- Marketing
- Direction Générale (General Management)
- R&D

### Employees
- 100 employees imported from RandomUser API (French users)
- Automatically assigned to random sites and services

### Administrator
- Email: `admin@example.com`
- Password: `admin123`

**Note**: If data already exists, initialization is skipped to preserve your data.

## API Integration

The application integrates with the **RandomUser API** to import employee data:

- **Endpoint**: `https://randomuser.me/api/?results=10&nat=fr`
- **Functionality**: 
  - Fetches 10 random French users per import
  - Extracts: name, email, phone, cell
  - Automatically formats phone numbers
  - Assigns random site and service
  - Creates employee records in database

**Requirements for Import**:
- At least one site must exist
- At least one service must exist
- Internet connection required

## Database

### Database Type
- **H2 Database** (file-based)
- **Location**: `./data/annuaire-db.mv.db`
- **Persistence**: Data persists between application restarts

### Schema

**Sites Table**:
- `ID` (Integer, Primary Key, Auto-increment)
- `City` (String, 60 chars, Not Null)

**Services Table**:
- `ID` (Integer, Primary Key, Auto-increment)
- `Name` (String, 60 chars, Not Null)

**ADMIN Table** (extends User):
- `ID` (Integer, Primary Key, Auto-increment)
- `FirstName` (String, 60 chars, Not Null)
- `LastName` (String, 60 chars, Not Null)
- `Phone` (String, 20 chars, Not Null)
- `Cell` (String, 20 chars, Not Null)
- `Email` (String, 60 chars, Not Null)
- `PASSWORD_HASH` (String, 255 chars, Not Null)
- `ROLE` (String, 15 chars, Not Null) = "ADMIN"

**EMPLOYEE Table** (extends User):
- `ID` (Integer, Primary Key, Auto-increment)
- `FirstName` (String, 60 chars, Not Null)
- `LastName` (String, 60 chars, Not Null)
- `Phone` (String, 20 chars, Not Null)
- `Cell` (String, 20 chars, Not Null)
- `Email` (String, 60 chars, Not Null)
- `PASSWORD_HASH` (String, 255 chars, Not Null)
- `ROLE` (String, 15 chars, Not Null) = "EMPLOYEE"
- `IdService` (Foreign Key → Services.ID, Not Null)
- `IdSite` (Foreign Key → Sites.ID, Not Null)

### Relationships
- **Employee → Service**: Many-to-One
- **Employee → Site**: Many-to-One

## Building and Running

### Build the Project
```bash
mvn clean compile
```

### Run the Application
```bash
mvn javafx:run
```

### Build JAR (if needed)
```bash
mvn clean package
```

### Clean Build
```bash
mvn clean install
```

## 🐛 Troubleshooting

### Application Won't Start

**Issue**: JavaFX not found
- **Solution**: Ensure JavaFX dependencies are correctly configured in `pom.xml`
- **Check**: Java version is 17 or higher

**Issue**: Database connection error
- **Solution**: Check that `hibernate.cfg.xml` is in `src/main/resources/`
- **Check**: Database file permissions in `./data/` directory

### Import from RandomUser Fails

**Issue**: "No sites or services available"
- **Solution**: Create at least one site and one service before importing

**Issue**: Network error
- **Solution**: Check internet connection
- **Solution**: Verify RandomUser API is accessible

### Data Not Persisting

**Issue**: Data disappears after restart
- **Solution**: Check that database file exists in `./data/annuaire-db.mv.db`
- **Solution**: Verify Hibernate configuration uses file-based database (not in-memory)

### UI Display Issues (macOS)

**Issue**: Application crashes or displays incorrectly
- **Solution**: JVM options are configured in `pom.xml` for macOS compatibility
- **Solution**: Ensure using JavaFX 17 with correct classifier (`mac-aarch64`)

### Validation Errors

**Issue**: Phone number validation fails
- **Solution**: Enter exactly 10 digits (spaces and dashes are automatically removed)
- **Example**: `0123456789` or `01 23 45 67 89`

**Issue**: Email validation fails
- **Solution**: Use valid email format: `user@domain.com`
- **Example**: `john.doe@example.com`

## Notes

- The application uses **file-based H2 database** for data persistence
- All windows open in **maximized mode** for better user experience
- **Dialogs and alerts** remain as normal-sized popups for better visibility
- **Phone numbers** are automatically cleaned (spaces, dashes removed)
- **Passwords** are hashed using SHA-256 algorithm
- The application supports **concurrent access** (multiple instances can run simultaneously)

## License

This project is provided as-is for educational and development purposes.

## Author

Developed as part of a JavaFX and Hibernate learning project.

---

**Version**: 1.0  
**Last Updated**: December 2024
