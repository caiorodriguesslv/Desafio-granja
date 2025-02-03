## ⓘ About
Backend for managing a duck farm.

This project involves developing a REST API to manage a duck farm, which includes registering ducks, customers, and vendors, processing duck sales, and generating reports. 
The API allows for individual duck registration with mother identification, customer registration with discount eligibility, and vendor registration with unique identification. 
It enables the sale of ducks to customers, applying discounts when applicable, and records the sale date and vendor information. 
The system also includes features for listing sold ducks, generating reports with transaction details, and ranking vendors based on their sales performance. 
The goal is to create a comprehensive management system for the farm.

## 💻 Technologies Used

* Spring Framework v.3.4.2
* Spring MVC
* Spring Boot DevTools
* Apache Maven (Dependency Injector)
* Apache POI (for generating reports in Excel)
* Java JDK 21
* JUnit 5 (for unit testing)
* Mockito (for mock testing)
* Docker (for containerization)
* Docker Compose (for managing multi-container setups)
* PostgreSQL (relational database)
* [Postman](https://www.postman.com/interstellar-moon-715825/workspace/desafio-granja/collection/21958705-df219aa6-31a8-44f4-900f-14dc1012085b?action=share&creator=21958705) (for API testing and requests)
  

## ⚙️ Running the Project

* Clone the Project
  
  1. Clone this repository:
     - git clone https://github.com/caiorodriguesslv/Desafio-granja.git
     - cd Desafio-granja

* Run Docker-Compose.yml
  
  2. In the project root directory:
     - docker compose up -d
  3. Access services:
     - PostgreSQL will be available on port 5432 on your localhost.
     - pgAdmin will be available at the URL http://localhost:5050. You can access pgAdmin using the credentials:
     - Email: admin@admin.com
     - Password: admin
  4. Connect pgAdmin to PostgreSQL:
     - In the left panel of pgAdmin, right-click on "Servers" and select "Create" > "Server...".
     - In the "Create - Server" window, fill in the details as follows:
     - Name: You can name the server connection, for example, PostgreSQL - Granja Patos.
     - Connection:
        - Host name/address: postgres (the name of the PostgreSQL service in docker-compose.yml).
        - Port: 5432 (the default PostgreSQL port).
        - Maintenance database: granja_patos_db (the database name you specified).
        - Username: admin (the username you set).
        - Password: admin (the password you set)
     - Click "Save" to establish the connection.

* Endpoint Report Service Sales
      
  5. http://localhost:8080/v1/sales/report in browser for downloading Excel.


