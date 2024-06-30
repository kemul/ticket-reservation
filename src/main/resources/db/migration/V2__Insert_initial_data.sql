INSERT INTO concerts (
    available_tickets, booking_end_time, booking_start_time, concert_date, concert_name, total_tickets, venue
) VALUES
(150, '20:00:00', '18:00:00', '2024-07-01 20:00:00', 'Rock Night', 200, 'Madison Square Garden'),
(100, '22:00:00', '19:00:00', '2024-07-05 21:00:00', 'Jazz Evening', 150, 'Blue Note Jazz Club'),
(200, '23:00:00', '20:00:00', '2024-07-10 22:00:00', 'Classical Harmony', 250, 'Carnegie Hall'),
(50, '19:30:00', '17:30:00', '2024-07-15 19:30:00', 'Indie Fest', 100, 'Brooklyn Steel'),
(300, '21:00:00', '19:00:00', '2024-07-20 20:00:00', 'Pop Extravaganza', 350, 'Radio City Music Hall'),
(0, '20:30:00', '18:30:00', '2024-07-25 20:00:00', 'Hip-Hop Bash', 150, 'Apollo Theater'),
(80, '21:30:00', '19:30:00', '2024-07-30 21:00:00', 'Country Vibes', 100, 'Grand Ole Opry'),
(120, '22:30:00', '20:30:00', '2024-08-01 21:30:00', 'Blues Night', 150, 'House of Blues'),
(90, '20:00:00', '18:00:00', '2024-08-05 20:00:00', 'Electronic Beats', 200, 'Terminal 5'),
(110, '23:00:00', '21:00:00', '2024-08-10 22:00:00', 'Reggae Sunset', 200, 'Reggae Beach Bar');

INSERT INTO users (
    created_at, email, password_hash, username
) VALUES
('2024-06-01 10:00:00', 'alice@example.com', 'hash_password1', 'alice'),
('2024-06-02 11:00:00', 'bob@example.com', 'hash_password2', 'bob'),
('2024-06-03 12:00:00', 'charlie@example.com', 'hash_password3', 'charlie'),
('2024-06-04 13:00:00', 'dave@example.com', 'hash_password4', 'dave'),
('2024-06-05 14:00:00', 'eve@example.com', 'hash_password5', 'eve'),
('2024-06-06 15:00:00', 'frank@example.com', 'hash_password6', 'frank'),
('2024-06-07 16:00:00', 'grace@example.com', 'hash_password7', 'grace'),
('2024-06-08 17:00:00', 'heidi@example.com', 'hash_password8', 'heidi'),
('2024-06-09 18:00:00', 'ivan@example.com', 'hash_password9', 'ivan'),
('2024-06-10 19:00:00', 'judy@example.com', 'hash_password10', 'judy');
