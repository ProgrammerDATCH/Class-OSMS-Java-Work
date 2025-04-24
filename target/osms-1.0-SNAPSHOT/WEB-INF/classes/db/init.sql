-- Create default admin user
INSERT INTO Users (Username, Password, UserType) VALUES 
('admin', 'admin', 'Admin');

-- Create default seller user
INSERT INTO Users (Username, Password, UserType) VALUES 
('seller', 'seller', 'Seller');

-- Create default customer user
INSERT INTO Users (Username, Password, UserType) VALUES 
('customer', 'customer', 'Customer');

-- Create default supplier user
INSERT INTO Users (Username, Password, UserType) VALUES 
('supplier', 'supplier', 'Supplier');

-- Get the user IDs
SET @admin_id = (SELECT UserId FROM Users WHERE Username = 'admin');
SET @seller_id = (SELECT UserId FROM Users WHERE Username = 'seller');
SET @customer_id = (SELECT UserId FROM Users WHERE Username = 'customer');
SET @supplier_id = (SELECT UserId FROM Users WHERE Username = 'supplier');

-- Create a default store
INSERT INTO Store (Status, Location) VALUES 
('Full', 'Main Store');

-- Get the store ID
SET @store_id = LAST_INSERT_ID();

-- Create seller profile with Rwandan name
INSERT INTO Seller (FirstName, LastName, Email, Telephone, StoreId, UserId) VALUES 
('Manzi', 'Niyonshuti', 'manzi.niyonshuti@gmail.com', '0781234567', @store_id, @seller_id);

-- Create supplier profile with Rwandan name
INSERT INTO Suppliers (FirstName, LastName, Email, Telephone, UserId) VALUES 
('David', 'Nkurunziza', 'david.nkurunziza@gmail.com', '0787654321', @supplier_id);

-- Get the supplier ID
SET @supplier_id = (SELECT SupplierId FROM Suppliers WHERE UserId = @supplier_id);

-- Create customer profile with Rwandan name
INSERT INTO Customers (FirstName, LastName, Email, Telephone, Address, UserId) VALUES 
('Gloria', 'Uwase', 'gloria.uwase@gmail.com', '0798765432', 'KN 4 Ave, Kigali, Rwanda', @customer_id);

-- Add some sample products
INSERT INTO Product (ProductName, SupplierId, ExpirationDate) VALUES 
('Laptop', @supplier_id, DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),
('Smartphone', @supplier_id, DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),
('Headphones', @supplier_id, DATE_ADD(CURDATE(), INTERVAL 1 YEAR));

-- Add categories for products
INSERT INTO Category (ProductId, Category) VALUES 
(1, 'Electronics'),
(2, 'Electronics'),
(3, 'Electronics');

-- Add some sample orders
INSERT INTO Orders (ProductId, CustomerId, Quantity, UnitPrice, TotalByProduct, Date) VALUES 
(1, @customer_id, 1, 999.99, 999.99, CURDATE()),
(2, @customer_id, 2, 499.99, 999.98, CURDATE());

-- Add UserProfiles table
CREATE TABLE IF NOT EXISTS UserProfiles (
    ProfileId INT PRIMARY KEY AUTO_INCREMENT,
    UserId INT NOT NULL,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    Email VARCHAR(100),
    Phone VARCHAR(20),
    Address TEXT,
    ProfileImage VARCHAR(255),
    LastLoginDate DATETIME,
    IsActive BOOLEAN DEFAULT true,
    FOREIGN KEY (UserId) REFERENCES Users(UserId) ON DELETE CASCADE
);