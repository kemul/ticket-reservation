# Ticket Reservation API Documentation

### 1. Get Upcoming Concerts
- **URL:** `http://localhost:8080/api/concerts/upcoming`
- **Method:** GET

### CURL Command

```bash
curl --location 'http://localhost:8080/api/concerts/upcoming'
```

### Response

#### Success Response

- **Code:** 200 OK
- **Content:** 

```json
[
    {
        "concertID": 3,
        "concertName": "Pop Extravaganza",
        "concertDate": "2024-07-25T18:00:00",
        "venue": "Theater C",
        "totalTickets": 8000,
        "availableTickets": 8000,
        "bookingStartTime": "12:00:00",
        "bookingEndTime": "12:20:00"
    },
    {
        "concertID": 4,
        "concertName": "Classical Evening",
        "concertDate": "2024-07-30T21:00:00",
        "venue": "Hall D",
        "totalTickets": 3000,
        "availableTickets": 3000,
        "bookingStartTime": "13:00:00",
        "bookingEndTime": "13:20:00"
    },
    {
        "concertID": 2,
        "concertName": "Jazz Night",
        "concertDate": "2024-07-20T20:00:00",
        "venue": "Arena B",
        "totalTickets": 5000,
        "availableTickets": 4947,
        "bookingStartTime": "22:00:00",
        "bookingEndTime": "23:59:00"
    }
]
```

### 2. Search Concerts
- **URL:** `http://localhost:8080/api/concerts/search`
- **Method:** GET
- **Query Parameters:**
  - `search` (string, optional): Keyword to search concerts by title, artist, or venue.

### CURL Command

```bash
curl --location 'http://localhost:8080/api/concerts/search?search=Night'
```

### Response

#### Success Response

- **Code:** 200 OK
- **Content:** 

```json
[
    {
        "concertID": 2,
        "concertName": "Jazz Night",
        "concertDate": "2024-07-20T20:00:00",
        "venue": "Arena B",
        "totalTickets": 5000,
        "availableTickets": 4947,
        "bookingStartTime": "22:00:00",
        "bookingEndTime": "23:59:00"
    }
]
```

### 3. Book Ticket
- **URL:** `http://localhost:8080/api/bookings/book`
- **Method:** POST
- **Content-Type:** `application/x-www-form-urlencoded`

## Request

### CURL Command

```bash
curl --location 'http://localhost:8080/api/bookings/book' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'userId=1' \
--data-urlencode 'concertId=2' \
--data-urlencode 'numberOfTickets=50'
```

### Parameters

| Parameter        | Type   | Description                          |
| ---------------- | ------ | ------------------------------------ |
| `userId`         | Long   | The ID of the user booking the ticket|
| `concertId`      | Long   | The ID of the concert to book        |
| `numberOfTickets`| Int    | The number of tickets to book        |

## Response

### Success Response

- **Code:** 200 OK
- **Content:** 

```json
{
    "bookingID": 6,
    "user": {
        "userId": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "passwordHash": "hashedpassword123",
        "createdAt": "2024-06-28T16:19:58.736183"
    },
    "concert": {
        "concertID": 2,
        "concertName": "Jazz Night",
        "concertDate": "2024-07-20T20:00:00",
        "venue": "Arena B",
        "totalTickets": 5000,
        "availableTickets": 4947,
        "bookingStartTime": "22:00:00",
        "bookingEndTime": "23:59:00"
    },
    "numberOfTickets": 50,
    "bookingTime": "2024-06-28T23:41:09.4447831"
}
```

### Error Responses

#### 400 Bad Request

- **Condition:** Missing or invalid parameters.
- **Content:**

```json
{
    "error": "User not found"
}
```

#### 404 Not Found

- **Condition:** Concert not found.
- **Content:**

```json
{
    "error": "Concert not found"
}
```

#### 500 Internal Server Error

- **Condition:** Unexpected error.
- **Content:**

```json
{
    "error": "An unexpected error occurred"
}
```