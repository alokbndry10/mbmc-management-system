# 🍔 Bhandari Khaja Ghar - Restaurant Management System

A comprehensive Java-based restaurant management system with MySQL database, Swing GUI, and JDBC CRUD operations. Perfect for managing menu items, processing customer orders, and tracking order status.

## 📋 Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Technologies Used](#technologies-used)

---

## ✨ Features

### Core Features
- **User Authentication** - Secure login with role-based access (Admin/Staff)
- **Menu Management** - View, add, and remove menu items
- **Order Management** - Place orders, track status, and view order history
- **Database Integration** - MySQL backend with JDBC CRUD operations
- **Professional GUI** - Java Swing interface with intuitive navigation

### Menu Items
- 🍔 **Balen Burger** - Rs. 199
- 🍕 **Harke Pizza** - Rs. 349
- 🍔 **KP Burger** - Rs. 179
- 🍚 **Mutton Biryani** - Rs. 299

### Admin Features
- 📋 View all menu items
- 🛒 Place orders for customers
- 📦 View all orders with details
- ✏️ Update order status (Pending → Preparing → Ready → Delivered)
- ➕ Add new menu items
- 🗑️ Remove menu items

### Staff Features
- 📋 View available menu items
- 🛒 Place orders for customers

---

## 🔧 Prerequisites

### System Requirements
- **Operating System**: Windows, Linux, or macOS
- **RAM**: Minimum 2GB
- **Storage**: Minimum 500MB free space

### Software Requirements
1. **Java Development Kit (JDK) 17 or higher**
   - Download: https://adoptium.net/temurin/releases/?version=17
   - Verify: Open PowerShell/Terminal and run `java -version`

2. **MySQL Server 8.0 or higher**
   - Download: https://dev.mysql.com/downloads/installer/
   - Verify: Open PowerShell/Terminal and run `mysql --version`

3. **Maven 3.6 or higher** (Optional - IntelliJ includes Maven)
   - Download: https://maven.apache.org/download.cgi
   - Verify: Run `mvn --version`

4. **IntelliJ IDEA** (Recommended IDE)
   - Download: https://www.jetbrains.com/idea/download/
   - Any edition works (Community is free)

---

## 📥 Installation

### Step 1: Install Java JDK 17

**Windows:**
1. Download JDK 17 from: https://adoptium.net/temurin/releases/?version=17
2. Run the installer
3. Choose "Add to PATH" during installation
4. Verify installation:
   ```powershell
   java -version
   ```
   Should show: `openjdk version "17.x.x"`

**macOS/Linux:**
```bash
# Using Homebrew (macOS)
brew install openjdk@17

# Verify
java -version
```

### Step 2: Install MySQL Server

**Windows:**
1. Download MySQL Installer: https://dev.mysql.com/downloads/installer/
2. Run `mysql-installer-web-community-8.x.x.msi`
3. Choose **"Developer Default"** setup type
4. Click "Next" through the configuration
5. **Set MySQL Root Password** (important!):
   - Enter a password (e.g., "admin123") or leave empty
   - **Remember this password!**
6. Click "Execute" to complete installation
7. Verify MySQL is running:
   ```powershell
   Get-Service MySQL*
   # Should show Status: Running
   ```

**macOS:**
```bash
# Using Homebrew
brew install mysql
brew services start mysql
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install mysql-server
sudo systemctl start mysql
```

### Step 3: Start MySQL Service

**Windows:**
```powershell
# Start MySQL service
net start MySQL80

# Stop MySQL service (when needed)
net stop MySQL80
```

**macOS/Linux:**
```bash
# MySQL should auto-start, or use
sudo systemctl start mysql
```

### Step 4: Clone/Download the Project

Option A - Clone from Git:
```bash
git clone <repository-url>
cd "student management system"
```

Option B - Manual Download:
1. Download the project folder
2. Extract it to your desired location
3. Navigate to the folder

### Step 5: Open in IntelliJ IDEA

1. Launch IntelliJ IDEA
2. Click **"Open"** or **"File" → "Open"**
3. Navigate to the project folder: `C:\Users\<YourUsername>\IdeaProjects\student management system`
4. Click **"Open"**
5. IntelliJ will automatically load the Maven project
6. Wait for Maven to download dependencies (watch bottom-right progress)

---

## 🗄️ Database Setup

### Automatic Setup (Recommended)

The application **automatically creates the database and tables** on first run. Simply ensure:
1. MySQL is running
2. MySQL credentials are correct (see Configuration section)
3. Run the application

### Manual Setup (If Automatic Fails)

**Open MySQL Command Line:**

**Windows:**
- Search → "MySQL 8.0 Command Line Client"
- Enter your MySQL root password

**macOS/Linux:**
```bash
mysql -u root -p
# Enter password when prompted
```

**Run these SQL commands:**

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS restaurant_db;

-- Use the database
USE restaurant_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'staff'
);

-- Create menu_items table
CREATE TABLE IF NOT EXISTS menu_items (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    price     DECIMAL(10,2) NOT NULL,
    available TINYINT(1)   NOT NULL DEFAULT 1
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    total_price   DECIMAL(10,2) NOT NULL DEFAULT 0,
    status        VARCHAR(30)  NOT NULL DEFAULT 'Pending',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    order_id     INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity     INT NOT NULL,
    unit_price   DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id)     REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insert admin user (password: admin123)
INSERT INTO users(username, password_hash, role) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin');

-- Insert menu items
INSERT INTO menu_items(name, price, available) VALUES
('Balen Burger',   199.00, 1),
('Harke Pizza',    349.00, 1),
('KP Burger',      179.00, 1),
('Mutton Biryani', 299.00, 1);

-- Verify setup
SELECT 'Database setup complete!' AS Status;
```

---

## ⚙️ Configuration

### Update MySQL Credentials

**File:** `src/main/java/org/example/DatabaseManager.java`

**Lines 12-16:**

```java
private static final String HOST     = "localhost";    // MySQL hostname
private static final String PORT     = "3306";         // MySQL port
private static final String DATABASE = "restaurant_db"; // Database name
private static final String USER     = "root";         // MySQL username
private static final String PASSWORD = "";             // MySQL root password
```

**Examples:**

If your MySQL root password is "admin123":
```java
private static final String PASSWORD = "admin123";
```

If you left password empty:
```java
private static final String PASSWORD = "";
```

If MySQL is on a different host:
```java
private static final String HOST = "192.168.1.100";
```

**Save the file after making changes (Ctrl+S)**

---

## 🚀 Running the Application

### Method 1: IntelliJ IDE (Recommended)

**Easiest way to run:**

1. **In IntelliJ**, navigate to:
   ```
   src/main/java/org/example/Main.java
   ```

2. **Right-click** on `Main.java`

3. Select **"Run 'Main.main()'"**
   
   OR press **Shift+F10**

4. The application will:
   - Initialize the database
   - Create tables (if needed)
   - Insert seed data
   - Show the login screen

### Method 2: Command Line

**Build the project:**
```powershell
cd "C:\Users\<YourUsername>\IdeaProjects\student management system"

# Using Maven
mvn clean package

# Using Maven from IntelliJ
mvn clean package -DskipTests
```

**Run the JAR:**
```powershell
java -jar target/student_management_system-1.0-SNAPSHOT.jar
```

### Method 3: PowerShell Script

**Run the included launcher:**
```powershell
cd "C:\Users\<YourUsername>\IdeaProjects\student management system"
.\run.ps1
```

---

## 👤 Login Credentials

### Default Admin Account

| Field | Value |
|-------|-------|
| **Username** | admin |
| **Password** | admin123 |
| **Role** | Admin (Full Access) |

After successful login, you'll see the main dashboard.

---

## 📖 Usage Guide

### 1. Login Screen

When you start the application:
- **Enter Username:** admin
- **Enter Password:** admin123
- Click **LOGIN**

### 2. Main Dashboard

After login, you'll see the dashboard with options in the left sidebar:

#### For Admin Users:

**📋 View Menu**
- Shows all menu items with prices
- Click "Refresh" to reload

**🛒 Place Order**
1. Enter customer name
2. Select item from dropdown
3. Enter quantity
4. Click "Add to Cart"
5. Review items in cart
6. Click "Place Order"
7. Order ID will be displayed

**📦 View All Orders**
- Shows all customer orders
- Click "View Details" to see order items
- Click "Update Status" to change order status

**➕ Add Menu Item**
1. Enter item name
2. Enter price in Rs.
3. Click "Add Item"
4. Item appears in menu immediately

**🗑️ Remove Menu Item**
1. Select item from table
2. Click "Remove Selected"
3. Confirm deletion
4. Item is removed from menu

### 3. Order Status Flow

Orders go through these statuses:
1. **Pending** - Order received
2. **Preparing** - Preparation in progress
3. **Ready** - Ready for pickup/delivery
4. **Delivered** - Delivered to customer
5. **Cancelled** - Order cancelled

---

## 📁 Project Structure

```
student management system/
│
├── README.md                          # This file
├── pom.xml                            # Maven configuration
├── run.ps1                            # PowerShell launcher
│
├── src/
│   └── main/
│       └── java/org/example/
│           ├── Main.java              # Application entry point
│           ├── DatabaseManager.java   # DB connection & initialization
│           │
│           ├── dao/                   # Data Access Objects (CRUD)
│           │   ├── MenuDAO.java       # Menu operations
│           │   ├── OrderDAO.java      # Order operations
│           │   └── UserDAO.java       # User authentication
│           │
│           ├── model/                 # Data models (POJOs)
│           │   ├── MenuItem.java
│           │   ├── Order.java
│           │   ├── OrderItem.java
│           │   └── User.java
│           │
│           └── ui/                    # GUI components
│               ├── LoginFrame.java    # Login screen
│               ├── MainDashboard.java # Main menu
│               ├── MenuPanel.java     # View menu
│               ├── PlaceOrderPanel.java
│               ├── ViewOrdersPanel.java
│               ├── AddMenuItemPanel.java
│               └── RemoveMenuItemPanel.java
│
├── target/                            # Build output
│   └── student_management_system-1.0-SNAPSHOT.jar
│
└── .gitignore                         # Git ignore file
```

---

## 🗄️ Database Schema

### users Table
```sql
CREATE TABLE users (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    role          VARCHAR(20) NOT NULL DEFAULT 'staff'
);
```

### menu_items Table
```sql
CREATE TABLE menu_items (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    price     DECIMAL(10,2) NOT NULL,
    available TINYINT(1) NOT NULL DEFAULT 1
);
```

### orders Table
```sql
CREATE TABLE orders (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    total_price   DECIMAL(10,2) NOT NULL DEFAULT 0,
    status        VARCHAR(30) NOT NULL DEFAULT 'Pending',
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### order_items Table
```sql
CREATE TABLE order_items (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    order_id     INT NOT NULL REFERENCES orders(id),
    menu_item_id INT NOT NULL REFERENCES menu_items(id),
    quantity     INT NOT NULL,
    unit_price   DECIMAL(10,2) NOT NULL
);
```

---

## 🔍 Troubleshooting

### Problem: "Database connection failed"

**Error Message:**
```
Database connection failed:
No suitable driver found for jdbc:mysql://localhost:3306/restaurant_db
```

**Solutions:**

1. **Ensure MySQL is running:**
   ```powershell
   net start MySQL80
   ```

2. **Verify MySQL is installed:**
   ```powershell
   mysql --version
   ```

3. **Check credentials in DatabaseManager.java** (lines 12-16)

4. **Test MySQL connection manually:**
   ```powershell
   mysql -u root -p
   # Enter your password
   # If successful, type: exit
   ```

---

### Problem: "Access denied for user 'root'@'localhost'"

**Error Message:**
```
Access denied for user 'root'@'localhost' (using password: YES)
```

**Solutions:**

1. **Check your MySQL root password:**
   - Verify it matches the one you set during MySQL installation
   - If unsure, reinstall MySQL and set a new password

2. **Update DatabaseManager.java** with correct password:
   ```java
   private static final String PASSWORD = "your_password_here";
   ```

3. **Reset MySQL root password (Windows):**
   ```powershell
   # Stop MySQL
   net stop MySQL80
   
   # Restart in safe mode
   mysqld --skip-grant-tables
   
   # In another PowerShell window
   mysql -u root
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
   exit;
   ```

---

### Problem: "Cannot find symbol 'LoginFrame'" in IntelliJ

**Solutions:**

1. **Reload Maven:**
   - Right-click `pom.xml`
   - Select **Maven → Reload Project**
   - Wait for download to complete

2. **Invalidate Caches:**
   - **File → Invalidate Caches**
   - Click **Invalidate and Restart**

3. **Rebuild Project:**
   - **Build → Rebuild Project**

---

### Problem: MySQL not installed

**Solution:**

1. Download MySQL: https://dev.mysql.com/downloads/installer/
2. Run installer: `mysql-installer-web-community-8.x.x.msi`
3. Choose **"Developer Default"**
4. Complete installation
5. Start service: `net start MySQL80`

---

### Problem: Java not found

**Error:**
```
'java' is not recognized as an internal or external command
```

**Solution:**

1. Install Java 17: https://adoptium.net/temurin/releases/?version=17
2. During installation, check **"Add to PATH"**
3. Restart PowerShell
4. Verify: `java -version`

---

### Problem: Application starts but no window appears

**Solutions:**

1. Check IntelliJ console for error messages
2. Ensure MySQL is running: `net start MySQL80`
3. Rebuild project: **Build → Rebuild Project**
4. Try running again

---

## 📊 Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17+ | Programming language |
| MySQL | 8.0+ | Relational database |
| JDBC | Bundled | Database connectivity |
| Swing | Java 17 | GUI framework |
| Maven | 3.6+ | Build tool |
| mysql-connector-j | 8.3.0 | MySQL JDBC driver |

---

## 🔐 Security Notes

1. **Passwords**: Currently stored as SHA-256 hashes
2. **Default Credentials**: Change admin password in production
3. **Database**: Secure your MySQL with strong credentials
4. **Deployment**: Use HTTPS and SSL for production

---

## 📝 File Descriptions

### Core Java Classes

**Main.java**
- Application entry point
- Initializes database
- Launches GUI

**DatabaseManager.java**
- MySQL connection management
- Table creation
- Data seeding
- Password hashing (SHA-256)

### DAO Classes (Data Access Objects)

**MenuDAO.java**
- CRUD operations for menu items
- Methods: addItem, removeItem, findById, getAllItems, getAvailableItems

**OrderDAO.java**
- CRUD operations for orders
- Methods: placeOrder, getAllOrders, updateStatus

**UserDAO.java**
- User authentication
- Methods: findByUsername, registerUser, login

### Model Classes (POJOs)

**User.java** - User entity with fields: id, username, passwordHash, role

**MenuItem.java** - Menu item entity with fields: id, name, price, available

**Order.java** - Order entity with fields: id, customerName, totalPrice, status, createdAt

**OrderItem.java** - Order item entity with fields: menuItemId, menuItemName, quantity, unitPrice

### UI Classes

**LoginFrame.java** - Login screen with authentication

**MainDashboard.java** - Main menu with sidebar navigation

**MenuPanel.java** - Display menu items

**PlaceOrderPanel.java** - Create new orders

**ViewOrdersPanel.java** - View and manage orders

**AddMenuItemPanel.java** - Add new menu items

**RemoveMenuItemPanel.java** - Remove menu items

---

## 🎓 For College Demonstration

### Demo Script

1. **Show Database Schema**
   - Open MySQL and show tables
   - Explain relationships

2. **Show Code Structure**
   - Explain MVC architecture
   - Show DAO pattern
   - Explain JDBC operations

3. **Demo Features**
   - Login with admin/admin123
   - View menu
   - Place an order
   - View orders
   - Add a new menu item
   - Remove a menu item
   - Update order status

4. **Show Database Changes**
   - Open MySQL
   - Show new orders inserted
   - Verify data integrity

---

## 📞 Support

For issues or questions:
1. Check the **Troubleshooting** section
2. Verify **Configuration** section is correct
3. Ensure **Prerequisites** are installed
4. Check IntelliJ console for detailed error messages

---

## 📄 License

This project is created for educational purposes.

---

## ✅ Verification Checklist

Before submission, verify:
- [ ] MySQL is installed and running
- [ ] Java 17+ is installed
- [ ] Project opens in IntelliJ
- [ ] Maven dependencies are downloaded
- [ ] DatabaseManager.java has correct credentials
- [ ] Application launches successfully
- [ ] Login works with admin/admin123
- [ ] All menu items display
- [ ] Can place an order
- [ ] Can view orders
- [ ] Can add menu items
- [ ] Can remove menu items
- [ ] Database contains correct data

---

## 🎉 Ready to Go!

Your **Bhandari Khaja Ghar Management System** is now fully set up and ready for:
- ✅ College demonstrations
- ✅ Project submissions
- ✅ Learning JDBC and Swing
- ✅ Understanding database design

**Happy coding! 🚀**

---

**Last Updated:** March 2026
**Version:** 1.0

