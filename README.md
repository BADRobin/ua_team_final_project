# ENTERPRISE ORDER MANAGEMENT SYSTEM (EOMS)

Welcome to the repository **UA Team Final Project** is a collaborative project to build a robust, scalable solution to solve real-world problems. This project embodies innovation, teamwork, and a commitment to leveraging technology to achieve meaningful results.

## 🌟 Project Overview

This project is a **full-stack web application** designed to streamline and develop a system for automated enterprise order management, approval processes, and supplier order formation. Built with modern tools and a user-first approach, it aims to empower users by simplifying complex workflows while ensuring security, accessibility, and performance.


## Table of Contents
1. [Technologies used](#technologies-used)
2. [Features](#features)
3. [User Roles](#user-roles)
4. [Background Processes](#background-processes)
5. [Step-by-Step Process](#step-by-step-process)
6. [Achievements](#achievements)
7. [License](#license)


## Technologies used:
- **Backend:** Java 23, Spring Boot 3.5.0, Spring Security, Hibernate
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven
- **Cloud Storage:** DigitalOcean Spaces
- **Deployment:** DigitalOcean server

## Features
- **User and product management**
- **Order creation and approval workflow**
- **Supplier order management**
- **Order history archiving**
- **User authentication and role-based access control**

## User Roles
- **Administrator:** Manages users, uploads price lists
- **User:** Selects products and creates orders, sends orders for approval
- **Head of Department and Finance Department:** Approves or rejects employee orders
- **Supply Manager:** Forms supplier orders, manages supplier order history

## Background Processes
- **Price List Processing:**
  - Checking for new price list files
  - Validating suppliers against the database
  - Cleaning outdated data in the database
  - Parsing CSV files and storing products in the system
  - Connecting to Cloud Storage (DigitalOcean Spaces)
  - Verifying and adding product categories

## Step-by-Step Process
🚀 
1. Generates supplier orders in CSV format.
2. Saves orders to the database and archives a copy.
3. Sends supplier orders via email.
4. Tracks order history.
5. Selects internal orders for processing.
6. Distributes products among suppliers.

## Achievements
📈
- Fully automated enterprise procurement process.
- User roles and order management.
- Integration with DigitalOcean for data storage.
- Automatic integration with supplier APIs.
- Enhanced order analytics system.
- Improved UI/UX for a better user experience.


## License
All databases are stored on DigitalOcean's servers, and the files of all orders created by the Supply Manager are located there as well
