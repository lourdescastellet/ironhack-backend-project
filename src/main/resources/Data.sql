INSERT INTO venue (venue.user_id, venue_name, venue_address, venue_city, venue_capacity) VALUES
    (4, 'Venue X', 'Address X', 'City X', 500),
    (5, 'Venue Y', 'Address Y', 'City Y', 1500),
    (6, 'Venue Z', 'Address Z', 'City Z', 200);

INSERT INTO artist (artist.user_id, name, email, password, artist_name, genre)
VALUES
    (1, 'Artist A', 'artistA@example.com', 'password', 'Artist A', 'Rock'),
    (2, 'Artist B', 'artistB@example.com', 'password', 'Artist B', 'Pop'),
    (3, 'Artist C', 'artistC@example.com', 'password', 'Artist C', 'Jazz');

INSERT INTO concert (concert_id, concert_name, artist_user_id, venue_user_id)
VALUES
    (1, 'Concert 1', 1, 4),  -- Artist A performing at Venue X
    (2, 'Concert 2', 2, 5),  -- Artist B performing at Venue Y
    (3, 'Concert 3', 3, 6);  -- Artist C performing at Venue Z

