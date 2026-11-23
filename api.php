<?php
header("Content-Type: application/json");

$conn = new mysqli("localhost", "solis_user", "Solis@2026", "imperial_solis_db");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "DB Error: " . $conn->connect_error]);
    exit;
}

$action = $_POST['action'] ?? '';

if ($action === '') {
    echo json_encode(["status" => "ready", "message" => "API is connected to imperial_solis_db!"]);
    exit;
}

// 1. BOOK ROOM
if ($action === 'book_room') {
    $name     = $_POST['name'] ?? '';
    $email    = $_POST['email'] ?? '';
    $room     = $_POST['room'] ?? '';
    $checkIn  = $_POST['check_in'] ?? '';
    $checkOut = $_POST['check_out'] ?? '';
    $guests   = (int)($_POST['guests'] ?? 1);
    $requests = $_POST['requests'] ?? '';

    $stmtRoom = $conn->prepare("SELECT price_per_night FROM rooms WHERE room_name LIKE ? LIMIT 1");
    $likeRoom = "%" . $room . "%";
    $stmtRoom->bind_param("s", $likeRoom);
    $stmtRoom->execute();
    $resRoom = $stmtRoom->get_result()->fetch_assoc();
    $rate = $resRoom ? (float)$resRoom['price_per_night'] : 820.00;

    $nights = max(1, (strtotime($checkOut) - strtotime($checkIn)) / 86400);
    $total  = $nights * $rate;
    $refId  = strtoupper(substr(uniqid(), -8));
    $extras = !empty($requests) ? "Note: " . $requests : "";

    $stmt = $conn->prepare("INSERT INTO bookings (booking_id, guest_name, guest_email, room_name, check_in, check_out, guests, total_amount, payment_method, extras_summary, source) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Website Booking', ?, 'Website')");
    $stmt->bind_param("ssssssids", $refId, $name, $email, $room, $checkIn, $checkOut, $guests, $total, $extras);
    $stmt->execute();

    echo json_encode(["status" => "success", "reference" => $refId]);
}

// 2. SHARE EXPERIENCE (REVIEW)
if ($action === 'submit_review') {
    $name   = $_POST['name'] ?? 'Guest';
    $email  = $_POST['email'] ?? '';
    $rating = $_POST['rating'] ?? '5';
    $review = $_POST['review'] ?? '';

    $title = "New Review (" . $rating . " Stars) from " . $name;
    $stmt = $conn->prepare("INSERT INTO notifications (category, title, body, sender_email) VALUES ('Review', ?, ?, ?)");
    $stmt->bind_param("sss", $title, $review, $email);
    $stmt->execute();

    echo json_encode(["status" => "success"]);
}

// 3. SEND MESSAGE (CONTACT)
if ($action === 'send_message') {
    $name    = $_POST['name'] ?? 'Guest';
    $email   = $_POST['email'] ?? '';
    $message = $_POST['message'] ?? '';

    $title = "New Message from " . $name;
    $stmt = $conn->prepare("INSERT INTO notifications (category, title, body, sender_email) VALUES ('Message', ?, ?, ?)");
    $stmt->bind_param("sss", $title, $message, $email);
    $stmt->execute();

    echo json_encode(["status" => "success"]);
}

$conn->close();
?>
