INSERT INTO users (id, name, email, password, role, created_at) VALUES
(1, 'John Smith', 'john.smith@example.com', 'password123', 'ROLE_USER', NOW()),
(2, 'Emma Johnson', 'emma.johnson@example.com', 'password123', 'ROLE_USER', NOW()),
(3, 'Michael Brown', 'michael.brown@example.com', 'password123', 'ROLE_USER', NOW()),
(4, 'Sophia Davis', 'sophia.davis@example.com', 'password123', 'ROLE_USER', NOW()),
(5, 'William Wilson', 'william.wilson@example.com', 'password123', 'ROLE_USER', NOW()),
(6, 'Olivia Moore', 'olivia.moore@example.com', 'password123', 'ROLE_USER', NOW()),
(7, 'James Taylor', 'james.taylor@example.com', 'password123', 'ROLE_USER', NOW()),
(8, 'Ava Anderson', 'ava.anderson@example.com', 'password123', 'ROLE_USER', NOW()),
(9, 'Benjamin Thomas', 'benjamin.thomas@example.com', 'password123', 'ROLE_USER', NOW()),
(10, 'Charlotte Jackson', 'charlotte.jackson@example.com', 'password123', 'ROLE_USER', NOW()),
(11, 'Daniel White', 'daniel.white@example.com', 'password123', 'ROLE_USER', NOW()),
(12, 'Mia Harris', 'mia.harris@example.com', 'password123', 'ROLE_USER', NOW()),
(13, 'Ethan Martin', 'ethan.martin@example.com', 'password123', 'ROLE_USER', NOW()),
(14, 'Amelia Thompson', 'amelia.thompson@example.com', 'password123', 'ROLE_USER', NOW()),
(15, 'Admin User', 'admin@bank.com', 'password123', 'ROLE_ADMIN', NOW());

INSERT INTO accounts
(id, account_number, balance, status, account_type, created_at, user_id)
VALUES
(1, '100000000001', 5000.00, 'ACTIVE', 'SAVINGS', NOW(), 1), 
(2, '100000000002', 12500.50, 'ACTIVE', 'CHECKING', NOW(), 1),

(3, '100000000003', 25000.00, 'ACTIVE', 'SAVINGS', NOW(), 2),

(4, '100000000004', 8900.75, 'ACTIVE', 'CHECKING', NOW(), 3),
(5, '100000000005', 32000.00, 'ACTIVE', 'SAVINGS', NOW(), 3),

(6, '100000000006', 7800.00, 'ACTIVE', 'SAVINGS', NOW(), 4),

(7, '100000000007', 150000.00, 'ACTIVE', 'SAVINGS', NOW(), 5),

(8, '100000000008', 4200.25, 'ACTIVE', 'CHECKING', NOW(), 6),

(9, '100000000009', 18500.00, 'ACTIVE', 'SAVINGS', NOW(), 7),
(10, '100000000010', 6700.00, 'ACTIVE', 'CHECKING', NOW(), 7),

(11, '100000000011', 92000.00, 'ACTIVE', 'SAVINGS', NOW(), 8),

(12, '100000000012', 3100.00, 'ACTIVE', 'CHECKING', NOW(), 9),

(13, '100000000013', 27500.00, 'ACTIVE', 'SAVINGS', NOW(), 10),

(14, '100000000014', 11000.00, 'ACTIVE', 'SAVINGS', NOW(), 11),

(15, '100000000015', 4500.00, 'ACTIVE', 'CHECKING', NOW(), 12),

(16, '100000000016', 72000.00, 'ACTIVE', 'SAVINGS', NOW(), 13),

(17, '100000000017', 1500.00, 'FROZEN', 'SAVINGS', NOW(), 14),

(18, '100000000018', 250000.00, 'ACTIVE', 'CHECKING', NOW(), 15);