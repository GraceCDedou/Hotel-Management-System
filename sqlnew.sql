-- Drop and recreate cleanly so there are zero conflicts
DROP DATABASE IF EXISTS imperial_solis_db;
CREATE DATABASE imperial_solis_db;
USE imperial_solis_db;

-- 1. Security: Create restricted app user
CREATE USER IF NOT EXISTS 'solis_user'@'localhost' IDENTIFIED BY 'Solis@2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON imperial_solis_db.* TO 'solis_user'@'localhost';
FLUSH PRIVILEGES;

-- 2. Admins Table (Hashed passwords)
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

INSERT INTO admins (username, password_hash) VALUES
('Grace',    SHA2('admingd@1245W', 256)),
('Kc',       SHA2('adminkc@1516X', 256)),
('Michelle', SHA2('admintm@1652S', 256)),
('Inam',     SHA2('adminia@1824K', 256)),
('Rafiq',    SHA2('adminrq@1471N', 256)),
('Michele',  SHA2('adminkm@1733Z', 256)),
('Peloyame', SHA2('adminkp@1368Q', 256));

-- 3. Rooms Table
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(100) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    max_capacity INT NOT NULL,
    description TEXT
);

INSERT INTO rooms (room_name, price_per_night, max_capacity, description) VALUES
('Deluxe King Room',      820.00, 2, 'Panoramic views, king-size bed, soaking tub, Wi-Fi, and mini bar.'),
('Imperial Suite',        850.00, 4, 'Floor-to-ceiling windows, private terrace, king bed, jacuzzi, and butler service.'),
('Solis Grand Room',     1250.00, 3, 'Twin seating areas, walk-in wardrobe, king bed, smart TV, and marble bath.'),
('Executive Twin Room',   980.00, 4, 'Two queen beds, workspace, high-speed Wi-Fi, and mini bar.'),
('Penthouse Suite',      1400.00, 4, 'Two-level suite with king bed, private rooftop lounge, and dedicated chef.'),
('Garden View Room',      780.00, 2, 'Lush garden vistas, queen bed, Wi-Fi, and coffee station.'),
('Kiddies Room (Add-on)', 300.00, 2, 'Adjoining kids room with bunk bed, beach view, Wi-Fi, and play area.');

-- 4. Bookings Table
CREATE TABLE bookings (
    booking_id VARCHAR(12) PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    guest_email VARCHAR(100) NOT NULL,
    room_name VARCHAR(100) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    guests INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    extras_total DECIMAL(10, 2) DEFAULT 0.00,
    extras_summary TEXT,
    source VARCHAR(20) DEFAULT 'Website',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Notifications Table
CREATE TABLE notifications (
    notif_id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(30) NOT NULL,
    title VARCHAR(150) NOT NULL,
    body TEXT NOT NULL,
    sender_email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Automatic Trigger for Website Bookings
DELIMITER //
CREATE TRIGGER notify_on_website_booking
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    IF NEW.source = 'Website' THEN
        INSERT INTO notifications (category, title, body, sender_email)
        VALUES (
            'Booking',
            CONCAT('New Website Booking: ', NEW.booking_id),
            CONCAT(NEW.guest_name, ' booked ', NEW.room_name, ' (', NEW.check_in, ' to ', NEW.check_out, ')'),
            NEW.guest_email
        );
    END IF;
END//
DELIMITER ;
