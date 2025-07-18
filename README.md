# Fortnox Car Rental

## Komma igång

Följ dessa steg för att köra applikationen lokalt:

### 1. Klona repo

Klona projektet från GitHub:

git clone <repo-url>
cd <repo-directory>

### 2. Skapa en databas

Skapa en databas med namnet rental i din PostgreSQL-server.\
Du kan använda följande SQL-kommando: CREATE DATABASE rental;

### 3. Ändra application.properties

Öppna filen src/main/resources/application.properties och ändra följande rader till dina egna databasuppgifter:
spring.datasource.url=jdbc:postgresql://localhost:5432/rental\
spring.datasource.username=your_username\
spring.datasource.password=your_password

### 4. Installera och starta frontend

Navigera till frontend-mappen och installera nödvändiga paket: \
cd frontend\
npm install\
npm start

### 5. Starta backend

Starta backend genom att köra main-metoden i RentalApplication-klassen. \
Du kan göra detta antingen via din IDE eller genom att köra följande kommando: ./mvnw spring-boot:run

### 6. Infoga data i databasen

Öppna din SQL-konsol och kör följande SQL-fråga för att infoga testdata i cars-tabellen:\
INSERT INTO cars (name, price_per_day) \
VALUES
('Volvo S60', 1500),\
('Volkswagen Golf', 1333),\
('Ford Mustang', 3000),\
('Ford Transit', 2400),\
('Test Car', 9999);

### 7. Kör integrationstester

Kör CarControllerIntegrationTest för att säkerställa att alla tester går igenom. \
Om testerna är godkända är programmet redo för användning.