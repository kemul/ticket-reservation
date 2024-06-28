# ticket-reservation

# API Documentation

## # API Contract: 
## `GET /api/concerts/available`

### Description:
Search Available Concerts, retrieves a list of available concerts based on search criteria.

### Request Parameters:
- `search`: (string, optional) Keyword to search concerts by title, artist, or venue.
- `date`: (string, optional) Date of the concert in `YYYY-MM-DD` format.
- `location`: (string, optional) City or venue location.
- `genre`: (string, optional) Genre of music.

### Request Example:
#### Success Response
```http
HTTP/1.1 200 Success
Content-Type: application/json
GET /api/concerts/available?search=rock&date=2023-07-21&location=New%20York&genre=rock

[
  {
    "id": "abc123",
    "title": "Rock Fiesta",
    "artist": "The Rockers",
    "date": "2023-07-21",
    "location": "Madison Square Garden, New York",
    "genre": "Rock",
    "available_tickets": 150,
    "price": 75.50
  },
  {
    "id": "def456",
    "title": "Summer Beats",
    "artist": "DJ Spin",
    "date": "2023-07-21",
    "location": "Central Park, New York",
    "genre": "Electronic",
    "available_tickets": 200,
    "price": 50.00
  }
]
```
#### Error Response
```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "message": "Data not found",
}

```