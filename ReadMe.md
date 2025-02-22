# Tech Doc : ticket-reservation

## User Story
- As a user I need a feature to search available concerts
- As a user I can book a ticket according to selected concert within specific hour, with limited
number of tickets (ex: 10000 tickets can only purchase between 10:00 AM to 10:20 AM,
with average user access 100 user per second)



## Flow Sequnce 
```mermaid
sequenceDiagram
    actor User

    %% GetFeed
    User->>FE-reservation: Get Feed Active Concert  
    FE-reservation->>BE-reservation: api/concerts/upcoming
    BE-reservation-->>FE-reservation: List<Concert>
    FE-reservation-->>User: Feed Concert Active 

    %% BookConcertTicket
    User->>FE-reservation: Book Concert 
    FE-reservation->>BE-reservation: api/bookings/book(concertID)
    BE-reservation-->>FE-reservation: OrderTicket
    FE-reservation-->>User: Ticket

    %% Search Concert (Additional Flow)
    User->>FE-reservation: Search Ticket(concertName)  
    FE-reservation->>BE-reservation: api/concerts/search(concertName,date)
    BE-reservation-->>FE-reservation: List<Concert>
    FE-reservation-->>User: Feed Concert Active 
```

## Entity Relationship Diagram
```mermaid
erDiagram
    USERS {
        bigserial user_id PK
        timestamp created_at
        varchar email UNIQUE
        varchar password_hash
        varchar username UNIQUE
    }
    CONCERTS {
        bigserial concert_id PK
        int available_tickets
        time booking_end_time
        time booking_start_time
        timestamp concert_date
        varchar concert_name
        int total_tickets
        varchar venue
    }
    BOOKINGS {
        bigserial booking_id PK
        timestamp booking_time
        int number_of_tickets
        int8 concert_id FK "References CONCERTS(concert_id)"
        int8 user_id FK "References USERS(user_id)"
    }

    USERS ||--o{ BOOKINGS: "has many"
    CONCERTS ||--o{ BOOKINGS: "has many"

```
### Explanation:
#### Entities:
* USERS: Represents the users table.
* CONCERTS: Represents the concerts table.
* BOOKINGS: Represents the bookings table.
#### Relationships:
* USERS has many BOOKINGS.
* CONCERTS has many BOOKINGS.
* CONCERTS has many TICKETS

### Table: `users`

The `users` table stores information about the users of the ticket reservation system.

#### Table Structure:

| Column        | Data Type           | Constraints                        |
|---------------|---------------------|------------------------------------|
| `user_id`     | SERIAL              | PRIMARY KEY                        |
| `username`    | VARCHAR(255)        | NOT NULL, UNIQUE                   |
| `email`       | VARCHAR(255)        | NOT NULL, UNIQUE                   |
| `password_hash` | VARCHAR(255)      | NOT NULL                           |
| `created_at`  | TIMESTAMP           | NOT NULL                           |

#### SQL Statement:

```sql
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```

---

### Table: `concerts`

#### Table Structure:

| Column              | Data Type    | Constraints    |
|---------------------|--------------|----------------|
| `concert_id`        | SERIAL       | PRIMARY KEY    |
| `concert_name`      | VARCHAR(255) | NOT NULL       |
| `concert_date`      | TIMESTAMP    | NOT NULL       |
| `venue`             | VARCHAR(255) | NOT NULL       |
| `total_tickets`     | INT          | NOT NULL       |
| `available_tickets` | INT          | NOT NULL       |
| `booking_start_time`| TIME         | NOT NULL       |
| `booking_end_time`  | TIME         | NOT NULL       |

#### SQL Statement:

```sql
CREATE TABLE IF NOT EXISTS concerts (
    concert_id SERIAL PRIMARY KEY,
    concert_name VARCHAR(255) NOT NULL,
    concert_date TIMESTAMP NOT NULL,
    venue VARCHAR(255) NOT NULL,
    total_tickets INT NOT NULL,
    available_tickets INT NOT NULL,
    booking_start_time TIME NOT NULL,
    booking_end_time TIME NOT NULL
);
```

---

### Table: `bookings`

The `bookings` table stores information about the bookings made by users for concerts.

#### Table Structure:

| Column             | Data Type | Constraints |
|--------------------|-----------|-------------|
| `booking_id`       | SERIAL    | PRIMARY KEY |
| `user_id`          | INT       | NOT NULL, FOREIGN KEY REFERENCES `users(user_id)` ON DELETE CASCADE |
| `concert_id`       | INT       | NOT NULL, FOREIGN KEY REFERENCES `concerts(concert_id)` ON DELETE CASCADE |
| `number_of_tickets`| INT       | NOT NULL    |
| `booking_time`     | TIMESTAMP | NOT NULL    |

#### SQL Statement:

```sql
CREATE TABLE IF NOT EXISTS bookings (
    booking_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    concert_id INT NOT NULL,
    number_of_tickets INT NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id) 
        REFERENCES users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_concert
        FOREIGN KEY(concert_id)
        REFERENCES concerts(concert_id)
        ON DELETE CASCADE
);
```

## Code Stucture
```
ticket-reservation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sgedts/
│   │   │           └── ticketreservation/
│   │   │               ├── model/
│   │   │               │   ├── Booking.java       // Entity class for bookings
│   │   │               │   ├── Concert.java       // Entity class for concerts
│   │   │               │   └── User.java          // Entity class for users
│   │   │               ├── repository/
│   │   │               │   ├── BookingRepository.java  // Repository interface for bookings
│   │   │               │   ├── ConcertRepository.java  // Repository interface for concerts
│   │   │               │   └── UserRepository.java     // Repository interface for users
│   │   │               ├── service/
│   │   │               │   └── BookingService.java     // Service class for booking logic
│   │   │               ├── controller/
│   │   │               │   └── BookingController.java  // Controller class for booking endpoints
│   │   │               ├── exception/
│   │   │               │   ├── ErrorException.java     // Custom exception class for errors
│   │   │               │   └── GlobalExceptionHandler.java // Global exception handler
│   │   │               ├── config/
│   │   │               │   └── RedissonConfig.java     // Configuration class for Redisson client
│   │   │               └── util/
│   │   │                   └── Constants.java          // Utility class for constants
│   └── resources/
│       └── application.properties  // Application configuration properties
└── pom.xml                         // Maven configuration file

```

## How to Run with Docker
### 1. git clone from repository 
```bash
git clone --branch yadi_docker_implementation https://github.com/kemul/ticket-reservation.git
```
### 2. Open Project under folder `ticket-reservation`
### 3. Running Docker 
 ```bash
docker-compose up --build
```
### 4. Testing 
Follow API Doc for testing https://github.com/kemul/ticket-reservation/blob/yadi_docker_db_create_automate/API-Documentation.md

## How to Run Without Docker
### 1. git clone from repository 
```bash
git clone https://github.com/kemul/ticket-reservation.git
```
### 2. Open Project under folder `ticket-reservation`
### 3. Prepare Env 
- Running Database and Redis at Docker 

Database
 ```bash
docker container run -p 5432:5432 --name postgresql -e POSTGRES_PASSWORD=postgres -d postgres
```
Redis
 ```bash
docker run -d --rm -p 6379:6379 redis:latest
```

### 4. Run Maven Command
```bash
> mvn clean install
```

### 5. Run Java file 
```bash
> java -jar .\target\ticket-reservation-0.0.1-SNAPSHOT.jar
```
### 6. Testing 
Follow API Doc for testing https://github.com/kemul/ticket-reservation/blob/yadi_docker_db_create_automate/API-Documentation.md

## Handle Race Condition / Multiple Booking using Redis Distributed Lock
Redis locks with Redisson to handle concurrent access. By configuring Redisson as a singleton and using it to ensure 
that multiple concurrent requests are handled correctly, preventing race conditions and ensuring data integrity.

```
public void bookTicket(Long concertId, Long userId) {
        RLock lock = redissonClient.getLock("concert:" + concertId);

        try {
            // Wait for 10 seconds to acquire the lock, and hold it for 30 seconds
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // Perform booking logic here

                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Could not acquire lock for concert " + concertId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to book ticket", e);
        }
    }
}
```    

 
