# Online Shop Management System (OSMS)

A web-based application for managing an online shop, built with JSP, Servlet, Bootstrap, and MySQL.

## Features

- User authentication (Admin, Seller, Customer)
- Product management
- Order management
- Customer management
- Seller management
- Supplier management
- Store management
- Inventory tracking
- Low stock notifications
- Sales reports

## Prerequisites

- Java JDK 11 or higher
- Apache Tomcat 9 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd osms
   ```

2. Create the MySQL database:
   ```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   ```

3. Update database connection settings in `DatabaseUtil.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/osms";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

4. Build the project:
   ```bash
   mvn clean package
   ```

5. Deploy the WAR file to Tomcat:
   - Copy `target/osms.war` to `$CATALINA_HOME/webapps/`
   - Start Tomcat server

## Accessing the Application

1. Open your web browser and navigate to:
   ```
   http://localhost:8080/osms
   ```

2. Default login credentials:
   - Admin: admin/admin
   - Seller: seller/seller
   - Customer: customer/customer

## Project Structure

```
osms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── osms/
│   │   │           ├── model/
│   │   │           ├── servlet/
│   │   │           └── util/
│   │   ├── resources/
│   │   │   └── db/
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       ├── admin/
│   │       ├── seller/
│   │       ├── customer/
│   │       └── css/
│   └── test/
├── pom.xml
└── README.md
```

## Features by User Role

### Admin
- Manage sellers and suppliers
- View dashboard with system statistics
- Generate reports
- Validate products
- Monitor store status

### Seller
- Register new products
- View store status
- Manage assigned store
- Track inventory
- View sales reports

### Customer
- Browse products
- Place orders
- View order history
- Track shipping status
- Make payments

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.