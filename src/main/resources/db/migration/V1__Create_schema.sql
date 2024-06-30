CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

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
