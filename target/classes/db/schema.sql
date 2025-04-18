-- Create database
CREATE DATABASE IF NOT EXISTS osms;
USE osms;

-- Users table
CREATE TABLE Users (
    UserId INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    UserType ENUM('Admin', 'Seller', 'Customer') NOT NULL
);

-- Customers table
CREATE TABLE Customers (
    CustomerId INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Telephone VARCHAR(20) NOT NULL,
    Address TEXT NOT NULL,
    UserId INT,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

-- Suppliers table
CREATE TABLE Suppliers (
    SupplierId INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Telephone VARCHAR(20) NOT NULL,
    UserId INT,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

-- Store table
CREATE TABLE Store (
    StoreId INT PRIMARY KEY AUTO_INCREMENT,
    Status ENUM('Full', 'Average', 'Empty') NOT NULL,
    Location VARCHAR(255) NOT NULL
);

-- Sellers table
CREATE TABLE Seller (
    SellerId INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Telephone VARCHAR(20) NOT NULL,
    StoreId INT,
    UserId INT,
    FOREIGN KEY (StoreId) REFERENCES Store(StoreId),
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

-- Product table
CREATE TABLE Product (
    ProductId INT PRIMARY KEY AUTO_INCREMENT,
    ProductName VARCHAR(100) NOT NULL,
    SupplierId INT,
    ExpirationDate DATE,
    FOREIGN KEY (SupplierId) REFERENCES Suppliers(SupplierId)
);

-- Category table
CREATE TABLE Category (
    CategoryId INT PRIMARY KEY AUTO_INCREMENT,
    ProductId INT,
    Category VARCHAR(50) NOT NULL,
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId)
);

-- Orders table
CREATE TABLE Orders (
    OrderId INT PRIMARY KEY AUTO_INCREMENT,
    ProductId INT,
    CustomerId INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2) NOT NULL,
    TotalByProduct DECIMAL(10,2) NOT NULL,
    Date DATETIME NOT NULL,
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId),
    FOREIGN KEY (CustomerId) REFERENCES Customers(CustomerId)
);

-- Payment table
CREATE TABLE Payment (
    PaymentId INT PRIMARY KEY AUTO_INCREMENT,
    OrderId INT,
    CustomerId INT,
    PaidAmount DECIMAL(10,2) NOT NULL,
    PaymentDate DATETIME NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES Orders(OrderId),
    FOREIGN KEY (CustomerId) REFERENCES Customers(CustomerId)
);

-- Shipping table
CREATE TABLE Shipping (
    ShippingId INT PRIMARY KEY AUTO_INCREMENT,
    CustomerId INT,
    OrderId INT,
    Location VARCHAR(255) NOT NULL,
    Date DATETIME NOT NULL,
    Status ENUM('Pending', 'In Transit', 'Delivered') NOT NULL,
    FOREIGN KEY (CustomerId) REFERENCES Customers(CustomerId),
    FOREIGN KEY (OrderId) REFERENCES Orders(OrderId)
);

-- Store_Product table (to track products in store)
CREATE TABLE Store_Product (
    StoreId INT,
    ProductId INT,
    Quantity INT NOT NULL,
    LastRestockedDate DATETIME,
    PRIMARY KEY (StoreId, ProductId),
    FOREIGN KEY (StoreId) REFERENCES Store(StoreId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId)
); 