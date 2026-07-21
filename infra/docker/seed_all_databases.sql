-- =============================================================================
-- UNIFIED MASTER DATABASE SEEDING SCRIPT
-- =============================================================================

\set ON_ERROR_STOP on

-- 1. SEED DATA FOR USER SERVICE (user_db)
\c user_db
BEGIN;
DELETE FROM user_role;
DELETE FROM passenger;
DELETE FROM users;

INSERT INTO users (id, email, password_hash, status, created_at, loyalty_tier, loyalty_miles, loyalty_lifetime_miles, enrolled_at) VALUES
(1, 'admin.nguyen@abs.com.vn', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'PLATINUM', 120000, 128288, '2026-01-11 08:30:00'),
(2, 'admin.tran@abs.com.vn', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-16 08:30:00', 'GOLD', 45000, 50862, '2026-01-16 08:30:00'),
(3, 'user3@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-15 08:30:00', 'SILVER', 11581, 18045, '2026-01-15 08:30:00'),
(4, 'user4@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-18 08:30:00', 'BLUE', 11113, 18356, '2026-01-18 08:30:00'),
(5, 'user5@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'SILVER', 3346, 13292, '2026-01-28 08:30:00'),
(6, 'user6@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-07 08:30:00', 'BLUE', 15985, 18642, '2026-01-07 08:30:00'),
(7, 'user7@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'SILVER', 20175, 30040, '2026-01-28 08:30:00'),
(8, 'user8@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-17 08:30:00', 'SILVER', 19889, 24214, '2026-01-17 08:30:00'),
(9, 'user9@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'BLUE', 18575, 20000, '2026-01-27 08:30:00'),
(10, 'user10@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-16 08:30:00', 'BLUE', 8587, 11703, '2026-01-16 08:30:00'),
(11, 'user11@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-23 08:30:00', 'SILVER', 23657, 31746, '2026-01-23 08:30:00'),
(12, 'user12@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-20 08:30:00', 'GOLD', 20345, 25862, '2026-01-20 08:30:00'),
(13, 'user13@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'BLUE', 6121, 8701, '2026-01-21 08:30:00'),
(14, 'user14@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'SILVER', 12510, 19807, '2026-01-27 08:30:00'),
(15, 'user15@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'SILVER', 7662, 10379, '2026-01-11 08:30:00'),
(16, 'user16@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-10 08:30:00', 'GOLD', 16473, 19323, '2026-01-10 08:30:00'),
(17, 'user17@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'BLUE', 16404, 25552, '2026-01-25 08:30:00'),
(18, 'user18@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-14 08:30:00', 'SILVER', 29615, 37149, '2026-01-14 08:30:00'),
(19, 'user19@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-10 08:30:00', 'GOLD', 12487, 18054, '2026-01-10 08:30:00'),
(20, 'user20@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-05 08:30:00', 'BLUE', 9579, 19099, '2026-01-05 08:30:00'),
(21, 'user21@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-17 08:30:00', 'BLUE', 24049, 29568, '2026-01-17 08:30:00'),
(22, 'user22@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'BLUE', 11084, 13958, '2026-01-28 08:30:00'),
(23, 'user23@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'SILVER', 26719, 34394, '2026-01-25 08:30:00'),
(24, 'user24@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-14 08:30:00', 'GOLD', 19212, 20824, '2026-01-14 08:30:00'),
(25, 'user25@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-26 08:30:00', 'SILVER', 10840, 19200, '2026-01-26 08:30:00'),
(26, 'user26@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-26 08:30:00', 'GOLD', 5820, 12408, '2026-01-26 08:30:00'),
(27, 'user27@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'GOLD', 17655, 17989, '2026-01-25 08:30:00'),
(28, 'user28@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'BLUE', 1314, 2207, '2026-01-21 08:30:00'),
(29, 'user29@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'BLUE', 20803, 28225, '2026-01-11 08:30:00'),
(30, 'user30@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'BLUE', 17116, 21674, '2026-01-25 08:30:00'),
(31, 'user31@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-03 08:30:00', 'SILVER', 5389, 6405, '2026-01-03 08:30:00'),
(32, 'user32@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-03 08:30:00', 'GOLD', 964, 3613, '2026-01-03 08:30:00'),
(33, 'user33@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'BLUE', 9306, 13887, '2026-01-27 08:30:00'),
(34, 'user34@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-08 08:30:00', 'BLUE', 15657, 16990, '2026-01-08 08:30:00'),
(35, 'user35@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-01 08:30:00', 'BLUE', 9989, 14810, '2026-01-01 08:30:00'),
(36, 'user36@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-19 08:30:00', 'BLUE', 2174, 9706, '2026-01-19 08:30:00'),
(37, 'user37@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'GOLD', 20203, 22050, '2026-01-25 08:30:00'),
(38, 'user38@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'SILVER', 25690, 34318, '2026-01-27 08:30:00'),
(39, 'user39@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-15 08:30:00', 'GOLD', 28055, 29234, '2026-01-15 08:30:00'),
(40, 'user40@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'GOLD', 22939, 29595, '2026-01-27 08:30:00'),
(41, 'user41@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-09 08:30:00', 'SILVER', 16238, 23714, '2026-01-09 08:30:00'),
(42, 'user42@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'BLUE', 18513, 26914, '2026-01-11 08:30:00'),
(43, 'user43@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-14 08:30:00', 'GOLD', 26102, 31946, '2026-01-14 08:30:00'),
(44, 'user44@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-15 08:30:00', 'GOLD', 8484, 16276, '2026-01-15 08:30:00'),
(45, 'user45@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'SILVER', 25581, 29955, '2026-01-27 08:30:00'),
(46, 'user46@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-10 08:30:00', 'GOLD', 24669, 30917, '2026-01-10 08:30:00'),
(47, 'user47@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'BLUE', 12365, 21085, '2026-01-25 08:30:00'),
(48, 'user48@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'SILVER', 8825, 9641, '2026-01-21 08:30:00'),
(49, 'user49@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-16 08:30:00', 'SILVER', 25591, 29503, '2026-01-16 08:30:00'),
(50, 'user50@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-01 08:30:00', 'BLUE', 16407, 19832, '2026-01-01 08:30:00'),
(51, 'user51@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-18 08:30:00', 'GOLD', 12245, 17805, '2026-01-18 08:30:00'),
(52, 'user52@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-24 08:30:00', 'BLUE', 11442, 20615, '2026-01-24 08:30:00'),
(53, 'user53@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-22 08:30:00', 'BLUE', 24364, 27532, '2026-01-22 08:30:00'),
(54, 'user54@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'BLUE', 24981, 32747, '2026-01-28 08:30:00'),
(55, 'user55@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-18 08:30:00', 'BLUE', 28076, 31905, '2026-01-18 08:30:00'),
(56, 'user56@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'BLUE', 20806, 24771, '2026-01-28 08:30:00'),
(57, 'user57@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'SILVER', 22709, 31641, '2026-01-21 08:30:00'),
(58, 'user58@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-05 08:30:00', 'GOLD', 2622, 4640, '2026-01-05 08:30:00'),
(59, 'user59@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-12 08:30:00', 'BLUE', 11902, 21434, '2026-01-12 08:30:00'),
(60, 'user60@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'SILVER', 22298, 22776, '2026-01-28 08:30:00'),
(61, 'user61@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-04 08:30:00', 'SILVER', 23910, 26858, '2026-01-04 08:30:00'),
(62, 'user62@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-04 08:30:00', 'BLUE', 15134, 24104, '2026-01-04 08:30:00'),
(63, 'user63@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-05 08:30:00', 'BLUE', 20400, 29654, '2026-01-05 08:30:00'),
(64, 'user64@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-06 08:30:00', 'BLUE', 29602, 39112, '2026-01-06 08:30:00'),
(65, 'user65@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-06 08:30:00', 'GOLD', 3462, 11540, '2026-01-06 08:30:00'),
(66, 'user66@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-15 08:30:00', 'BLUE', 12327, 14959, '2026-01-15 08:30:00'),
(67, 'user67@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'BLUE', 8595, 8908, '2026-01-25 08:30:00'),
(68, 'user68@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'BLUE', 10060, 15422, '2026-01-11 08:30:00'),
(69, 'user69@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-03 08:30:00', 'SILVER', 29694, 30630, '2026-01-03 08:30:00'),
(70, 'user70@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-07 08:30:00', 'BLUE', 29311, 36113, '2026-01-07 08:30:00'),
(71, 'user71@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'BLUE', 5805, 10080, '2026-01-21 08:30:00'),
(72, 'user72@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-09 08:30:00', 'SILVER', 3399, 5986, '2026-01-09 08:30:00'),
(73, 'user73@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-07 08:30:00', 'GOLD', 4055, 6169, '2026-01-07 08:30:00'),
(74, 'user74@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-06 08:30:00', 'BLUE', 5481, 6636, '2026-01-06 08:30:00'),
(75, 'user75@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-06 08:30:00', 'GOLD', 3152, 11262, '2026-01-06 08:30:00'),
(76, 'user76@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-24 08:30:00', 'BLUE', 737, 9189, '2026-01-24 08:30:00'),
(77, 'user77@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-07 08:30:00', 'GOLD', 10070, 18489, '2026-01-07 08:30:00'),
(78, 'user78@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-02 08:30:00', 'BLUE', 22141, 24829, '2026-01-02 08:30:00'),
(79, 'user79@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'SILVER', 22887, 23602, '2026-01-28 08:30:00'),
(80, 'user80@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-25 08:30:00', 'BLUE', 2572, 7580, '2026-01-25 08:30:00'),
(81, 'user81@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-08 08:30:00', 'GOLD', 4322, 8245, '2026-01-08 08:30:00'),
(82, 'user82@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-21 08:30:00', 'GOLD', 13117, 19172, '2026-01-21 08:30:00'),
(83, 'user83@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-22 08:30:00', 'SILVER', 29512, 33392, '2026-01-22 08:30:00'),
(84, 'user84@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-01 08:30:00', 'SILVER', 23283, 29470, '2026-01-01 08:30:00'),
(85, 'user85@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-24 08:30:00', 'SILVER', 20650, 27606, '2026-01-24 08:30:00'),
(86, 'user86@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'BLUE', 18633, 27041, '2026-01-27 08:30:00'),
(87, 'user87@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-16 08:30:00', 'BLUE', 8558, 11330, '2026-01-16 08:30:00'),
(88, 'user88@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-06 08:30:00', 'BLUE', 22167, 23268, '2026-01-06 08:30:00'),
(89, 'user89@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-28 08:30:00', 'SILVER', 24030, 33091, '2026-01-28 08:30:00'),
(90, 'user90@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-20 08:30:00', 'BLUE', 25507, 33929, '2026-01-20 08:30:00'),
(91, 'user91@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-14 08:30:00', 'BLUE', 13011, 13894, '2026-01-14 08:30:00'),
(92, 'user92@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-27 08:30:00', 'GOLD', 14987, 19875, '2026-01-27 08:30:00'),
(93, 'user93@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-11 08:30:00', 'SILVER', 7761, 13249, '2026-01-11 08:30:00'),
(94, 'user94@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-07 08:30:00', 'SILVER', 24665, 33837, '2026-01-07 08:30:00'),
(95, 'user95@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-13 08:30:00', 'BLUE', 3814, 4031, '2026-01-13 08:30:00'),
(96, 'user96@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-22 08:30:00', 'GOLD', 12606, 15503, '2026-01-22 08:30:00'),
(97, 'user97@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-01 08:30:00', 'BLUE', 5193, 14537, '2026-01-01 08:30:00'),
(98, 'user98@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-09 08:30:00', 'GOLD', 8647, 16436, '2026-01-09 08:30:00'),
(99, 'user99@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-17 08:30:00', 'SILVER', 10086, 10087, '2026-01-17 08:30:00'),
(100, 'user100@gmail.com', '$2a$10$8.Zp.H5.B.fG7t8m.aJk2eN8N8y8lJb7D8ePq3iF2l4v.yZk/d8iG', 'ACTIVE', '2026-01-22 08:30:00', 'BLUE', 5611, 11690, '2026-01-22 08:30:00');

INSERT INTO passenger (id, user_id, first_name, last_name, date_of_birth, phone, gender, passport_no, nationality, passport_country, passport_expiry, kyc_status) VALUES
(1, 3, 'Linh', 'Võ Ngọc', '1996-08-24', '099267325', 'FEMALE', 'B5901210', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(2, 4, 'Quân', 'Đặng Quốc', '1994-04-14', '095099466', 'MALE', 'B2686735', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(3, 5, 'Quân', 'Dương Hồng', '1999-03-11', '097312547', 'FEMALE', 'B6475191', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(4, 6, 'Vinh', 'Phan Thị', '1979-11-28', '094764688', 'FEMALE', 'B5819487', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(5, 7, 'Cường', 'Nguyễn Đức', '1977-04-10', '095450592', 'FEMALE', 'B4642384', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(6, 8, 'Hùng', 'Ngô Văn', '1997-08-22', '097911179', 'MALE', 'B6238102', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(7, 9, 'Phúc', 'Hoàng Kim', '1980-03-13', '097174862', 'MALE', 'B7241948', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(8, 10, 'Minh', 'Hoàng Minh', '1998-07-10', '094016691', 'MALE', 'B3561080', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(9, 11, 'Bình', 'Lê Đức', '1983-09-15', '093915813', 'MALE', 'B5301463', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(10, 12, 'Yến', 'Đặng Văn', '1986-04-21', '091190477', 'MALE', 'B8572197', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(11, 13, 'Minh', 'Ngô Hồng', '1990-08-06', '091088293', 'MALE', 'B2141797', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(12, 14, 'Đạt', 'Huỳnh Văn', '1983-08-17', '094642440', 'FEMALE', 'B7043353', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(13, 15, 'Đạt', 'Võ Trọng', '1981-05-02', '098228529', 'MALE', 'B1465987', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(14, 16, 'Huy', 'Hồ Hữu', '1999-10-19', '094432082', 'FEMALE', 'B8315642', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(15, 17, 'Dũng', 'Hoàng Quốc', '1987-06-16', '099750424', 'FEMALE', 'B3201887', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(16, 18, 'Nam', 'Lý Thanh', '1977-05-13', '098889517', 'MALE', 'B7008253', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(17, 19, 'Hải', 'Đặng Xuân', '1975-05-10', '092704299', 'FEMALE', 'B1483942', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(18, 20, 'Dũng', 'Bùi Minh', '2001-04-19', '091223182', 'MALE', 'B3712648', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(19, 21, 'Dũng', 'Dương Kim', '1982-08-20', '096629917', 'FEMALE', 'B7566331', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(20, 22, 'Lâm', 'Hồ Hồng', '1975-01-01', '091603920', 'FEMALE', 'B2900441', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(21, 23, 'Hùng', 'Nguyễn Xuân', '2001-05-16', '092364585', 'FEMALE', 'B9760775', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(22, 24, 'Hương', 'Hồ Thanh', '1991-10-20', '092473816', 'MALE', 'B3240176', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(23, 25, 'Phương', 'Hoàng Đức', '2000-07-15', '091289537', 'FEMALE', 'B3072521', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(24, 26, 'Vinh', 'Võ Đức', '1977-04-14', '096112400', 'FEMALE', 'B2473699', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(25, 27, 'Giang', 'Hồ Xuân', '1992-12-11', '093334219', 'FEMALE', 'B7317320', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(26, 28, 'Thảo', 'Huỳnh Thanh', '2003-04-19', '095508453', 'MALE', 'B5143004', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(27, 29, 'Đạt', 'Nguyễn Ngọc', '1982-05-02', '099477106', 'FEMALE', 'B2563140', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(28, 30, 'Phương', 'Nguyễn Ngọc', '1975-07-24', '098527043', 'FEMALE', 'B8809324', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(29, 31, 'Sơn', 'Đặng Thị', '1989-02-12', '093791939', 'FEMALE', 'B5408560', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(30, 32, 'Phương', 'Huỳnh Thị', '2003-07-18', '094936697', 'MALE', 'B5172153', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(31, 33, 'Tú', 'Phan Xuân', '2003-09-28', '098167797', 'FEMALE', 'B1256117', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(32, 34, 'Sơn', 'Hoàng Trọng', '1998-07-15', '097484211', 'MALE', 'B6310659', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(33, 35, 'Giang', 'Nguyễn Văn', '1976-09-19', '097091381', 'FEMALE', 'B4620294', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(34, 36, 'Bình', 'Ngô Đức', '1979-06-27', '095782209', 'FEMALE', 'B4679012', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(35, 37, 'Hải', 'Ngô Ngọc', '1982-07-16', '094488696', 'FEMALE', 'B2401246', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(36, 38, 'Phúc', 'Nguyễn Kim', '2005-05-05', '098490576', 'MALE', 'B8894300', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(37, 39, 'Dũng', 'Đỗ Xuân', '1987-04-25', '098910958', 'MALE', 'B3847473', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(38, 40, 'Phúc', 'Hồ Hữu', '2000-07-16', '098610044', 'FEMALE', 'B9096760', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(39, 41, 'Lâm', 'Ngô Đức', '1987-02-23', '092870722', 'MALE', 'B2684432', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(40, 42, 'Nam', 'Đặng Minh', '1995-02-07', '099417750', 'FEMALE', 'B6865771', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(41, 43, 'Sơn', 'Bùi Hữu', '1983-11-12', '091424296', 'FEMALE', 'B7168932', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(42, 44, 'Huy', 'Đặng Ngọc', '1983-10-22', '098943519', 'FEMALE', 'B8136575', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(43, 45, 'Trang', 'Huỳnh Kim', '2000-06-25', '097793858', 'FEMALE', 'B1897565', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(44, 46, 'Đạt', 'Ngô Thanh', '1976-02-13', '091424521', 'FEMALE', 'B1745642', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(45, 47, 'Yến', 'Hoàng Hồng', '1984-03-08', '097712388', 'MALE', 'B2715578', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(46, 48, 'Hải', 'Phạm Văn', '1992-04-18', '091786814', 'MALE', 'B5439425', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(47, 49, 'Trang', 'Vũ Thị', '1995-06-10', '098608753', 'MALE', 'B3131803', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(48, 50, 'Cường', 'Trần Kim', '1979-07-21', '092924195', 'FEMALE', 'B3002492', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(49, 51, 'Lâm', 'Dương Thanh', '2005-10-12', '098972249', 'FEMALE', 'B4625703', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(50, 52, 'Vy', 'Lý Trọng', '1978-12-19', '094692913', 'MALE', 'B8786657', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(51, 53, 'Quân', 'Dương Đức', '2003-11-11', '095234329', 'MALE', 'B3067024', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(52, 54, 'Hương', 'Lê Ngọc', '1996-10-23', '098405573', 'FEMALE', 'B3916873', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(53, 55, 'Sơn', 'Đặng Thị', '1991-09-14', '097334275', 'FEMALE', 'B9032237', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(54, 56, 'Linh', 'Võ Xuân', '1989-02-23', '099652335', 'FEMALE', 'B7264394', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(55, 57, 'Đạt', 'Phan Trọng', '1982-06-17', '099963646', 'MALE', 'B7044883', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(56, 58, 'Trang', 'Phan Văn', '1983-12-02', '095706972', 'MALE', 'B5935891', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(57, 59, 'Giang', 'Vũ Ngọc', '1993-05-15', '095260408', 'FEMALE', 'B9860293', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(58, 60, 'Giang', 'Phạm Hữu', '1978-06-12', '097429235', 'FEMALE', 'B9566984', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(59, 61, 'Yến', 'Ngô Hữu', '1995-06-03', '095198651', 'FEMALE', 'B2837550', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(60, 62, 'Bình', 'Lê Thanh', '1979-03-14', '098617189', 'MALE', 'B8237018', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(61, 63, 'Vy', 'Bùi Hữu', '2000-12-18', '096961597', 'MALE', 'B8613294', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(62, 64, 'Vy', 'Nguyễn Thị', '1987-08-18', '092057646', 'MALE', 'B5191008', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(63, 65, 'Khang', 'Dương Trọng', '2000-08-28', '093223387', 'MALE', 'B7006141', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(64, 66, 'Đạt', 'Bùi Kim', '1997-02-12', '094999112', 'MALE', 'B3555640', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(65, 67, 'Tuấn', 'Hồ Hồng', '1989-10-17', '091770039', 'MALE', 'B2651603', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(66, 68, 'Linh', 'Võ Quốc', '2005-03-14', '098227825', 'FEMALE', 'B1114689', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(67, 69, 'An', 'Lý Trọng', '1999-05-03', '094784328', 'MALE', 'B7950690', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(68, 70, 'Hùng', 'Trần Minh', '1990-12-02', '092676737', 'MALE', 'B4617197', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(69, 71, 'Cường', 'Đỗ Thị', '1983-02-18', '095859923', 'FEMALE', 'B9631160', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(70, 72, 'Thảo', 'Phan Trọng', '1989-12-15', '099879010', 'MALE', 'B9115975', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(71, 73, 'Hải', 'Lê Thanh', '1990-08-15', '094756567', 'MALE', 'B4458746', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(72, 74, 'Khang', 'Hồ Hồng', '1993-12-13', '095700720', 'FEMALE', 'B1609044', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(73, 75, 'Huy', 'Bùi Thị', '2004-12-04', '094263771', 'FEMALE', 'B2681262', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(74, 76, 'Tú', 'Trần Trọng', '2003-04-02', '094868126', 'MALE', 'B2730598', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(75, 77, 'Bình', 'Dương Kim', '1992-09-03', '091497319', 'FEMALE', 'B9097962', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(76, 78, 'Giang', 'Nguyễn Kim', '1990-12-03', '096118587', 'MALE', 'B1400961', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(77, 79, 'Phúc', 'Võ Văn', '1999-02-18', '093079028', 'FEMALE', 'B5827489', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(78, 80, 'Yến', 'Ngô Quốc', '1999-12-09', '095613417', 'MALE', 'B6248790', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(79, 81, 'Minh', 'Lê Kim', '2001-04-20', '098886650', 'MALE', 'B2513843', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(80, 82, 'Nam', 'Ngô Hữu', '2001-02-16', '096521248', 'MALE', 'B2975632', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(81, 83, 'Hương', 'Lê Kim', '1982-05-14', '091141940', 'FEMALE', 'B8152514', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(82, 84, 'Tuấn', 'Lê Xuân', '2004-01-12', '094818728', 'MALE', 'B3182290', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(83, 85, 'Trang', 'Bùi Kim', '1977-12-04', '099199162', 'FEMALE', 'B6651573', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(84, 86, 'Nam', 'Bùi Đức', '1985-04-11', '096239189', 'MALE', 'B1845166', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(85, 87, 'Nam', 'Huỳnh Thị', '1978-12-17', '098786707', 'FEMALE', 'B8888427', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(86, 88, 'An', 'Huỳnh Quốc', '1976-05-03', '096337665', 'MALE', 'B5988794', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(87, 89, 'Thảo', 'Nguyễn Đức', '1994-05-23', '091480606', 'FEMALE', 'B5557613', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(88, 90, 'Vy', 'Hồ Xuân', '1994-04-28', '095975382', 'MALE', 'B7396478', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(89, 91, 'Lâm', 'Vũ Thị', '1981-10-25', '097770203', 'MALE', 'B6326938', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(90, 92, 'Tuấn', 'Bùi Đức', '1998-03-05', '099885771', 'FEMALE', 'B3247476', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(91, 93, 'Lâm', 'Nguyễn Quốc', '1979-10-01', '095844879', 'FEMALE', 'B8066955', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(92, 94, 'Tuấn', 'Phan Thị', '1993-04-25', '091088359', 'MALE', 'B5949035', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(93, 95, 'Quân', 'Huỳnh Văn', '1976-11-08', '098814696', 'FEMALE', 'B4106832', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(94, 96, 'Khang', 'Đặng Minh', '1990-06-02', '097992281', 'FEMALE', 'B7072128', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(95, 97, 'Hải', 'Phan Thị', '1984-05-19', '097738389', 'MALE', 'B3394570', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(96, 98, 'Khang', 'Huỳnh Quốc', '1996-04-28', '099509165', 'MALE', 'B7707126', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(97, 99, 'Lâm', 'Vũ Đức', '1988-04-08', '091906949', 'MALE', 'B2748869', 'VN', 'VN', '2032-12-31', 'VERIFIED'),
(98, 100, 'Giang', 'Nguyễn Đức', '2000-09-12', '098182669', 'MALE', 'B6169169', 'VN', 'VN', '2032-12-31', 'VERIFIED');

INSERT INTO user_role (user_id, role) VALUES
(1, 'USER'),
(1, 'ADMIN'),
(2, 'USER'),
(2, 'ADMIN'),
(3, 'USER'),
(4, 'USER'),
(5, 'USER'),
(6, 'USER'),
(7, 'USER'),
(8, 'USER'),
(9, 'USER'),
(10, 'USER'),
(11, 'USER'),
(12, 'USER'),
(13, 'USER'),
(14, 'USER'),
(15, 'USER'),
(16, 'USER'),
(17, 'USER'),
(18, 'USER'),
(19, 'USER'),
(20, 'USER'),
(21, 'USER'),
(22, 'USER'),
(23, 'USER'),
(24, 'USER'),
(25, 'USER'),
(26, 'USER'),
(27, 'USER'),
(28, 'USER'),
(29, 'USER'),
(30, 'USER'),
(31, 'USER'),
(32, 'USER'),
(33, 'USER'),
(34, 'USER'),
(35, 'USER'),
(36, 'USER'),
(37, 'USER'),
(38, 'USER'),
(39, 'USER'),
(40, 'USER'),
(41, 'USER'),
(42, 'USER'),
(43, 'USER'),
(44, 'USER'),
(45, 'USER'),
(46, 'USER'),
(47, 'USER'),
(48, 'USER'),
(49, 'USER'),
(50, 'USER'),
(51, 'USER'),
(52, 'USER'),
(53, 'USER'),
(54, 'USER'),
(55, 'USER'),
(56, 'USER'),
(57, 'USER'),
(58, 'USER'),
(59, 'USER'),
(60, 'USER'),
(61, 'USER'),
(62, 'USER'),
(63, 'USER'),
(64, 'USER'),
(65, 'USER'),
(66, 'USER'),
(67, 'USER'),
(68, 'USER'),
(69, 'USER'),
(70, 'USER'),
(71, 'USER'),
(72, 'USER'),
(73, 'USER'),
(74, 'USER'),
(75, 'USER'),
(76, 'USER'),
(77, 'USER'),
(78, 'USER'),
(79, 'USER'),
(80, 'USER'),
(81, 'USER'),
(82, 'USER'),
(83, 'USER'),
(84, 'USER'),
(85, 'USER'),
(86, 'USER'),
(87, 'USER'),
(88, 'USER'),
(89, 'USER'),
(90, 'USER'),
(91, 'USER'),
(92, 'USER'),
(93, 'USER'),
(94, 'USER'),
(95, 'USER'),
(96, 'USER'),
(97, 'USER'),
(98, 'USER'),
(99, 'USER'),
(100, 'USER');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
COMMIT;

-- 2. SEED DATA FOR FLIGHT SEARCH SERVICE (flight_db)
\c flight_db
BEGIN;
DELETE FROM flight_seat;
DELETE FROM seat_inventory;
DELETE FROM flight;
DELETE FROM route;
DELETE FROM airline;
DELETE FROM airport;

INSERT INTO airport (iata_code, name, city, country) VALUES
('HAN', 'Sân bay Quốc tế Nội Bài', 'Hà Nội', 'VN'),
('SGN', 'Sân bay Quốc tế Tân Sơn Nhất', 'TP. Hồ Chí Minh', 'VN'),
('DAD', 'Sân bay Quốc tế Đà Nẵng', 'Đà Nẵng', 'VN'),
('CXR', 'Sân bay Quốc tế Cam Ranh', 'Cam Ranh', 'VN'),
('PQC', 'Sân bay Quốc tế Phú Quốc', 'Phú Quốc', 'VN'),
('HPH', 'Sân bay Quốc tế Cát Bi', 'Hải Phòng', 'VN'),
('HUI', 'Sân bay Quốc tế Phú Bài', 'Huế', 'VN'),
('VCA', 'Sân bay Quốc tế Cần Thơ', 'Cần Thơ', 'VN'),
('DLI', 'Sân bay Liên Khương', 'Đà Lạt', 'VN'),
('UIH', 'Sân bay Phù Cát', 'Quy Nhơn', 'VN'),
('VII', 'Sân bay Quốc tế Vinh', 'Vinh', 'VN'),
('VDO', 'Sân bay Quốc tế Vân Đồn', 'Quảng Ninh', 'VN');

INSERT INTO airline (code, name) VALUES
('VN', 'Vietnam Airlines'),
('VJ', 'VietJet Air'),
('QH', 'Bamboo Airways'),
('VU', 'Vietravel Airlines');

INSERT INTO route (id, from_airport, to_airport, distance_km) VALUES
(1, 'HAN', 'SGN', 1166),
(2, 'SGN', 'HAN', 1166),
(3, 'HAN', 'DAD', 605),
(4, 'DAD', 'HAN', 605),
(5, 'SGN', 'DAD', 608),
(6, 'DAD', 'SGN', 608),
(7, 'HAN', 'CXR', 1000),
(8, 'CXR', 'HAN', 1000),
(9, 'SGN', 'CXR', 310),
(10, 'CXR', 'SGN', 310),
(11, 'HAN', 'PQC', 1200),
(12, 'PQC', 'HAN', 1200),
(13, 'SGN', 'PQC', 300),
(14, 'PQC', 'SGN', 300),
(15, 'HAN', 'DLI', 950),
(16, 'DLI', 'HAN', 950),
(17, 'SGN', 'DLI', 250),
(18, 'DLI', 'SGN', 250),
(19, 'HPH', 'SGN', 1100),
(20, 'SGN', 'HPH', 1100),
(21, 'HAN', 'HUI', 540),
(22, 'HUI', 'HAN', 540),
(23, 'SGN', 'HUI', 650),
(24, 'HUI', 'SGN', 650),
(25, 'HAN', 'VCA', 1150),
(26, 'VCA', 'HAN', 1150),
(27, 'SGN', 'VII', 800),
(28, 'VII', 'SGN', 800),
(29, 'HAN', 'UIH', 800),
(30, 'UIH', 'HAN', 800),
(31, 'SGN', 'UIH', 450),
(32, 'UIH', 'SGN', 450),
(33, 'HAN', 'VII', 300),
(34, 'VII', 'HAN', 300);

INSERT INTO flight (id, flight_no, route_id, airline_code, departure_time, arrival_time, total_seats, base_price, aircraft_type, status) VALUES
(1, 'VN979', 13, 'VN', '2026-07-12 12:15:00', '2026-07-12 12:51:00', 300, 2380000.00, 'B787', 'DEPARTED'),
(2, 'QH856', 29, 'QH', '2026-07-12 15:15:00', '2026-07-12 16:51:00', 180, 820000.00, 'A321', 'DEPARTED'),
(3, 'VN558', 8, 'VN', '2026-07-12 11:15:00', '2026-07-12 13:15:00', 300, 1120000.00, 'B787', 'DEPARTED'),
(4, 'QH895', 3, 'QH', '2026-07-12 14:30:00', '2026-07-12 15:43:00', 220, 900000.00, 'A320', 'DEPARTED'),
(5, 'VU552', 21, 'VU', '2026-07-12 17:00:00', '2026-07-12 18:05:00', 220, 1850000.00, 'A320', 'DEPARTED'),
(6, 'QH281', 29, 'QH', '2026-07-12 16:15:00', '2026-07-12 17:51:00', 180, 1330000.00, 'A321', 'DEPARTED'),
(7, 'VJ130', 17, 'VJ', '2026-07-12 17:30:00', '2026-07-12 18:00:00', 180, 1940000.00, 'A321', 'DEPARTED'),
(8, 'VN515', 24, 'VN', '2026-07-12 07:45:00', '2026-07-12 09:03:00', 220, 880000.00, 'A320', 'DEPARTED'),
(9, 'VN746', 31, 'VN', '2026-07-12 16:30:00', '2026-07-12 17:24:00', 180, 890000.00, 'A321', 'DEPARTED'),
(10, 'VU790', 13, 'VU', '2026-07-13 14:00:00', '2026-07-13 14:36:00', 300, 2430000.00, 'B787', 'DEPARTED'),
(11, 'VU848', 6, 'VU', '2026-07-13 09:45:00', '2026-07-13 10:58:00', 180, 1610000.00, 'A321', 'DEPARTED'),
(12, 'QH885', 32, 'QH', '2026-07-13 11:45:00', '2026-07-13 12:39:00', 180, 2170000.00, 'A321', 'DEPARTED'),
(13, 'VU482', 12, 'VU', '2026-07-13 08:00:00', '2026-07-13 10:24:00', 300, 1400000.00, 'B787', 'DEPARTED'),
(14, 'QH183', 4, 'QH', '2026-07-13 17:45:00', '2026-07-13 18:58:00', 180, 1780000.00, 'A321', 'DEPARTED'),
(15, 'QH241', 25, 'QH', '2026-07-13 09:00:00', '2026-07-13 11:18:00', 220, 2260000.00, 'A320', 'DEPARTED'),
(16, 'VN192', 10, 'VN', '2026-07-13 09:30:00', '2026-07-13 10:07:00', 300, 1910000.00, 'B787', 'DEPARTED'),
(17, 'QH286', 23, 'QH', '2026-07-13 20:15:00', '2026-07-13 21:33:00', 220, 2260000.00, 'A320', 'DEPARTED'),
(18, 'VN142', 17, 'VN', '2026-07-14 10:00:00', '2026-07-14 10:30:00', 180, 1730000.00, 'A321', 'DEPARTED'),
(19, 'VN896', 13, 'VN', '2026-07-14 13:45:00', '2026-07-14 14:21:00', 180, 1710000.00, 'A321', 'DEPARTED'),
(20, 'VN959', 17, 'VN', '2026-07-14 17:30:00', '2026-07-14 18:00:00', 220, 1840000.00, 'A320', 'DEPARTED'),
(21, 'VN605', 2, 'VN', '2026-07-14 21:30:00', '2026-07-14 23:50:00', 180, 1130000.00, 'A321', 'DEPARTED'),
(22, 'QH285', 33, 'QH', '2026-07-14 17:45:00', '2026-07-14 18:21:00', 220, 1240000.00, 'A320', 'DEPARTED'),
(23, 'VN921', 25, 'VN', '2026-07-14 16:45:00', '2026-07-14 19:03:00', 220, 2470000.00, 'A320', 'DEPARTED'),
(24, 'VU830', 12, 'VU', '2026-07-14 20:45:00', '2026-07-14 23:09:00', 180, 1460000.00, 'A321', 'DEPARTED'),
(25, 'VJ578', 3, 'VJ', '2026-07-14 21:45:00', '2026-07-14 22:58:00', 180, 2010000.00, 'A321', 'DEPARTED'),
(26, 'VJ290', 8, 'VJ', '2026-07-15 21:30:00', '2026-07-15 23:30:00', 300, 1970000.00, 'B787', 'DEPARTED'),
(27, 'VJ182', 34, 'VJ', '2026-07-15 15:30:00', '2026-07-15 16:06:00', 180, 1820000.00, 'A321', 'DEPARTED'),
(28, 'VU786', 23, 'VU', '2026-07-15 08:45:00', '2026-07-15 10:03:00', 300, 1370000.00, 'B787', 'DEPARTED'),
(29, 'VJ752', 28, 'VJ', '2026-07-15 19:15:00', '2026-07-15 20:51:00', 180, 1490000.00, 'A321', 'DEPARTED'),
(30, 'VN298', 5, 'VN', '2026-07-15 13:00:00', '2026-07-15 14:13:00', 220, 1050000.00, 'A320', 'DEPARTED'),
(31, 'VU787', 18, 'VU', '2026-07-15 07:15:00', '2026-07-15 07:45:00', 300, 1990000.00, 'B787', 'DEPARTED'),
(32, 'VJ267', 21, 'VJ', '2026-07-15 19:45:00', '2026-07-15 20:50:00', 220, 2440000.00, 'A320', 'DEPARTED'),
(33, 'VU217', 29, 'VU', '2026-07-15 15:15:00', '2026-07-15 16:51:00', 300, 2190000.00, 'B787', 'DEPARTED'),
(34, 'VJ145', 24, 'VJ', '2026-07-15 08:45:00', '2026-07-15 10:03:00', 220, 1700000.00, 'A320', 'DEPARTED'),
(35, 'QH884', 24, 'QH', '2026-07-16 07:30:00', '2026-07-16 08:48:00', 220, 1570000.00, 'A320', 'DEPARTED'),
(36, 'VU284', 3, 'VU', '2026-07-16 21:00:00', '2026-07-16 22:13:00', 300, 2400000.00, 'B787', 'DEPARTED'),
(37, 'VJ169', 20, 'VJ', '2026-07-16 10:30:00', '2026-07-16 12:42:00', 300, 1540000.00, 'B787', 'DEPARTED'),
(38, 'VU824', 3, 'VU', '2026-07-16 11:15:00', '2026-07-16 12:28:00', 220, 1500000.00, 'A320', 'DEPARTED'),
(39, 'VN342', 13, 'VN', '2026-07-16 17:45:00', '2026-07-16 18:21:00', 220, 930000.00, 'A320', 'DEPARTED'),
(40, 'VU472', 21, 'VU', '2026-07-16 20:30:00', '2026-07-16 21:35:00', 220, 890000.00, 'A320', 'DEPARTED'),
(41, 'VN978', 32, 'VN', '2026-07-16 18:45:00', '2026-07-16 19:39:00', 300, 1410000.00, 'B787', 'DEPARTED'),
(42, 'VJ870', 3, 'VJ', '2026-07-16 06:30:00', '2026-07-16 07:43:00', 220, 1680000.00, 'A320', 'DEPARTED'),
(43, 'VN835', 26, 'VN', '2026-07-17 13:00:00', '2026-07-17 15:18:00', 180, 1370000.00, 'A321', 'DEPARTED'),
(44, 'VJ538', 34, 'VJ', '2026-07-17 19:30:00', '2026-07-17 20:06:00', 180, 1680000.00, 'A321', 'DEPARTED'),
(45, 'VN370', 5, 'VN', '2026-07-17 22:00:00', '2026-07-17 23:13:00', 220, 880000.00, 'A320', 'DEPARTED'),
(46, 'VU833', 11, 'VU', '2026-07-17 08:00:00', '2026-07-17 10:24:00', 180, 2210000.00, 'A321', 'DEPARTED'),
(47, 'VJ685', 34, 'VJ', '2026-07-17 06:00:00', '2026-07-17 06:36:00', 180, 2110000.00, 'A321', 'DEPARTED'),
(48, 'VU118', 5, 'VU', '2026-07-17 13:15:00', '2026-07-17 14:28:00', 180, 2430000.00, 'A321', 'DEPARTED'),
(49, 'VN455', 6, 'VN', '2026-07-17 16:45:00', '2026-07-17 17:58:00', 220, 2440000.00, 'A320', 'DEPARTED'),
(50, 'VJ692', 6, 'VJ', '2026-07-17 17:00:00', '2026-07-17 18:13:00', 220, 1780000.00, 'A320', 'DEPARTED'),
(51, 'VN586', 18, 'VN', '2026-07-18 19:15:00', '2026-07-18 19:45:00', 180, 2420000.00, 'A321', 'DEPARTED'),
(52, 'VN276', 27, 'VN', '2026-07-18 10:30:00', '2026-07-18 12:06:00', 300, 2440000.00, 'B787', 'DEPARTED'),
(53, 'VU469', 20, 'VU', '2026-07-18 22:30:00', '2026-07-19 00:42:00', 220, 1800000.00, 'A320', 'DEPARTED'),
(54, 'VN763', 23, 'VN', '2026-07-18 13:00:00', '2026-07-18 14:18:00', 180, 1130000.00, 'A321', 'DEPARTED'),
(55, 'VJ306', 3, 'VJ', '2026-07-18 07:00:00', '2026-07-18 08:13:00', 220, 1290000.00, 'A320', 'DEPARTED'),
(56, 'VN318', 31, 'VN', '2026-07-18 06:00:00', '2026-07-18 06:54:00', 300, 1300000.00, 'B787', 'DEPARTED'),
(57, 'QH468', 12, 'QH', '2026-07-18 18:30:00', '2026-07-18 20:54:00', 220, 2360000.00, 'A320', 'DEPARTED'),
(58, 'VN213', 15, 'VN', '2026-07-18 10:15:00', '2026-07-18 12:09:00', 180, 2250000.00, 'A321', 'DEPARTED'),
(59, 'VJ792', 9, 'VJ', '2026-07-18 06:15:00', '2026-07-18 06:52:00', 180, 1470000.00, 'A321', 'DEPARTED'),
(60, 'VU689', 21, 'VU', '2026-07-19 10:00:00', '2026-07-19 11:05:00', 220, 950000.00, 'A320', 'DEPARTED'),
(61, 'VJ526', 26, 'VJ', '2026-07-19 20:30:00', '2026-07-19 22:48:00', 180, 1580000.00, 'A321', 'DEPARTED'),
(62, 'QH728', 28, 'QH', '2026-07-19 08:15:00', '2026-07-19 09:51:00', 300, 2350000.00, 'B787', 'DEPARTED'),
(63, 'VJ349', 25, 'VJ', '2026-07-19 07:45:00', '2026-07-19 10:03:00', 180, 2100000.00, 'A321', 'DEPARTED'),
(64, 'VN313', 4, 'VN', '2026-07-19 09:45:00', '2026-07-19 10:58:00', 220, 880000.00, 'A320', 'DEPARTED'),
(65, 'QH725', 2, 'QH', '2026-07-19 08:30:00', '2026-07-19 10:50:00', 180, 1130000.00, 'A321', 'DEPARTED'),
(66, 'QH311', 33, 'QH', '2026-07-19 14:00:00', '2026-07-19 14:36:00', 300, 1860000.00, 'B787', 'DEPARTED'),
(67, 'VU316', 8, 'VU', '2026-07-19 15:30:00', '2026-07-19 17:30:00', 220, 1900000.00, 'A320', 'DEPARTED'),
(68, 'VU255', 34, 'VU', '2026-07-20 07:30:00', '2026-07-20 08:06:00', 300, 990000.00, 'B787', 'DEPARTED'),
(69, 'VN121', 20, 'VN', '2026-07-20 14:00:00', '2026-07-20 16:12:00', 300, 1250000.00, 'B787', 'DEPARTED'),
(70, 'VU770', 20, 'VU', '2026-07-20 17:00:00', '2026-07-20 19:12:00', 220, 2360000.00, 'A320', 'DEPARTED'),
(71, 'QH904', 27, 'QH', '2026-07-20 12:45:00', '2026-07-20 14:21:00', 180, 1290000.00, 'A321', 'DEPARTED'),
(72, 'QH603', 28, 'QH', '2026-07-20 18:15:00', '2026-07-20 19:51:00', 220, 880000.00, 'A320', 'DEPARTED'),
(73, 'QH774', 20, 'QH', '2026-07-20 14:45:00', '2026-07-20 16:57:00', 220, 880000.00, 'A320', 'DEPARTED'),
(74, 'QH225', 19, 'QH', '2026-07-20 12:30:00', '2026-07-20 14:42:00', 300, 1460000.00, 'B787', 'DEPARTED'),
(75, 'VU874', 19, 'VU', '2026-07-20 19:45:00', '2026-07-20 21:57:00', 180, 1890000.00, 'A321', 'DEPARTED'),
(76, 'VN984', 11, 'VN', '2026-07-21 14:30:00', '2026-07-21 16:54:00', 300, 2320000.00, 'B787', 'DEPARTED'),
(77, 'VJ383', 24, 'VJ', '2026-07-21 16:45:00', '2026-07-21 18:03:00', 220, 1400000.00, 'A320', 'DEPARTED'),
(78, 'VJ846', 8, 'VJ', '2026-07-21 21:30:00', '2026-07-21 23:30:00', 300, 1600000.00, 'B787', 'SCHEDULED'),
(79, 'VN675', 11, 'VN', '2026-07-21 13:45:00', '2026-07-21 16:09:00', 180, 1190000.00, 'A321', 'DEPARTED'),
(80, 'VJ954', 13, 'VJ', '2026-07-21 22:45:00', '2026-07-21 23:21:00', 180, 970000.00, 'A321', 'SCHEDULED'),
(81, 'VN653', 10, 'VN', '2026-07-21 22:15:00', '2026-07-21 22:52:00', 300, 1820000.00, 'B787', 'SCHEDULED'),
(82, 'QH312', 18, 'QH', '2026-07-21 14:45:00', '2026-07-21 15:15:00', 180, 1200000.00, 'A321', 'DEPARTED'),
(83, 'QH778', 5, 'QH', '2026-07-21 18:45:00', '2026-07-21 19:58:00', 300, 1420000.00, 'B787', 'SCHEDULED'),
(84, 'VU628', 32, 'VU', '2026-07-22 12:30:00', '2026-07-22 13:24:00', 300, 2420000.00, 'B787', 'SCHEDULED'),
(85, 'QH378', 6, 'QH', '2026-07-22 20:15:00', '2026-07-22 21:28:00', 180, 1850000.00, 'A321', 'SCHEDULED'),
(86, 'QH600', 7, 'QH', '2026-07-22 10:30:00', '2026-07-22 12:30:00', 300, 1910000.00, 'B787', 'SCHEDULED'),
(87, 'VJ759', 4, 'VJ', '2026-07-22 19:30:00', '2026-07-22 20:43:00', 220, 2040000.00, 'A320', 'SCHEDULED'),
(88, 'VU423', 19, 'VU', '2026-07-22 22:30:00', '2026-07-23 00:42:00', 180, 2430000.00, 'A321', 'SCHEDULED'),
(89, 'QH618', 16, 'QH', '2026-07-22 15:30:00', '2026-07-22 17:24:00', 300, 1350000.00, 'B787', 'SCHEDULED'),
(90, 'VU264', 10, 'VU', '2026-07-22 22:45:00', '2026-07-22 23:22:00', 300, 2090000.00, 'B787', 'SCHEDULED'),
(91, 'VJ596', 28, 'VJ', '2026-07-22 13:15:00', '2026-07-22 14:51:00', 220, 2460000.00, 'A320', 'SCHEDULED'),
(92, 'QH460', 2, 'QH', '2026-07-22 17:30:00', '2026-07-22 19:50:00', 300, 1010000.00, 'B787', 'SCHEDULED'),
(93, 'VJ152', 30, 'VJ', '2026-07-23 20:15:00', '2026-07-23 21:51:00', 300, 1220000.00, 'B787', 'SCHEDULED'),
(94, 'VJ233', 31, 'VJ', '2026-07-23 21:30:00', '2026-07-23 22:24:00', 220, 1230000.00, 'A320', 'SCHEDULED'),
(95, 'QH157', 28, 'QH', '2026-07-23 10:00:00', '2026-07-23 11:36:00', 180, 1040000.00, 'A321', 'SCHEDULED'),
(96, 'VN843', 20, 'VN', '2026-07-23 14:00:00', '2026-07-23 16:12:00', 300, 2460000.00, 'B787', 'SCHEDULED'),
(97, 'QH910', 19, 'QH', '2026-07-23 21:00:00', '2026-07-23 23:12:00', 220, 2430000.00, 'A320', 'SCHEDULED'),
(98, 'QH691', 7, 'QH', '2026-07-23 21:00:00', '2026-07-23 23:00:00', 220, 2370000.00, 'A320', 'SCHEDULED'),
(99, 'VU591', 12, 'VU', '2026-07-23 11:00:00', '2026-07-23 13:24:00', 220, 1920000.00, 'A320', 'SCHEDULED'),
(100, 'VJ827', 14, 'VJ', '2026-07-23 19:45:00', '2026-07-23 20:21:00', 180, 1620000.00, 'A321', 'SCHEDULED'),
(101, 'VN713', 9, 'VN', '2026-07-24 12:45:00', '2026-07-24 13:22:00', 300, 1160000.00, 'B787', 'SCHEDULED'),
(102, 'VJ934', 13, 'VJ', '2026-07-24 10:15:00', '2026-07-24 10:51:00', 180, 1450000.00, 'A321', 'SCHEDULED'),
(103, 'QH750', 2, 'QH', '2026-07-24 12:30:00', '2026-07-24 14:50:00', 180, 2030000.00, 'A321', 'SCHEDULED'),
(104, 'QH513', 31, 'QH', '2026-07-24 20:15:00', '2026-07-24 21:09:00', 300, 1650000.00, 'B787', 'SCHEDULED'),
(105, 'VU167', 10, 'VU', '2026-07-24 20:30:00', '2026-07-24 21:07:00', 180, 2260000.00, 'A321', 'SCHEDULED'),
(106, 'VJ784', 6, 'VJ', '2026-07-24 16:15:00', '2026-07-24 17:28:00', 220, 1690000.00, 'A320', 'SCHEDULED'),
(107, 'VJ141', 16, 'VJ', '2026-07-24 10:00:00', '2026-07-24 11:54:00', 220, 1270000.00, 'A320', 'SCHEDULED'),
(108, 'VJ615', 27, 'VJ', '2026-07-24 16:00:00', '2026-07-24 17:36:00', 180, 1140000.00, 'A321', 'SCHEDULED'),
(109, 'VJ956', 13, 'VJ', '2026-07-25 21:30:00', '2026-07-25 22:06:00', 300, 2300000.00, 'B787', 'SCHEDULED'),
(110, 'QH529', 18, 'QH', '2026-07-25 16:00:00', '2026-07-25 16:30:00', 180, 2300000.00, 'A321', 'SCHEDULED'),
(111, 'QH502', 21, 'QH', '2026-07-25 12:30:00', '2026-07-25 13:35:00', 300, 1500000.00, 'B787', 'SCHEDULED'),
(112, 'VN153', 4, 'VN', '2026-07-25 12:15:00', '2026-07-25 13:28:00', 220, 2340000.00, 'A320', 'SCHEDULED'),
(113, 'VU445', 4, 'VU', '2026-07-25 21:15:00', '2026-07-25 22:28:00', 300, 1500000.00, 'B787', 'SCHEDULED'),
(114, 'VJ854', 17, 'VJ', '2026-07-25 12:00:00', '2026-07-25 12:30:00', 300, 1290000.00, 'B787', 'SCHEDULED'),
(115, 'VU684', 25, 'VU', '2026-07-25 20:15:00', '2026-07-25 22:33:00', 180, 2100000.00, 'A321', 'SCHEDULED'),
(116, 'VJ834', 9, 'VJ', '2026-07-25 12:15:00', '2026-07-25 12:52:00', 180, 1410000.00, 'A321', 'SCHEDULED'),
(117, 'QH736', 16, 'QH', '2026-07-25 16:30:00', '2026-07-25 18:24:00', 220, 2110000.00, 'A320', 'SCHEDULED'),
(118, 'VU349', 28, 'VU', '2026-07-26 13:45:00', '2026-07-26 15:21:00', 180, 1550000.00, 'A321', 'SCHEDULED'),
(119, 'VU586', 23, 'VU', '2026-07-26 11:30:00', '2026-07-26 12:48:00', 300, 2270000.00, 'B787', 'SCHEDULED'),
(120, 'VU160', 19, 'VU', '2026-07-26 17:15:00', '2026-07-26 19:27:00', 300, 2270000.00, 'B787', 'SCHEDULED'),
(121, 'VN183', 18, 'VN', '2026-07-26 22:30:00', '2026-07-26 23:00:00', 180, 2130000.00, 'A321', 'SCHEDULED'),
(122, 'VJ128', 29, 'VJ', '2026-07-26 11:00:00', '2026-07-26 12:36:00', 220, 1690000.00, 'A320', 'SCHEDULED'),
(123, 'QH548', 16, 'QH', '2026-07-26 19:45:00', '2026-07-26 21:39:00', 180, 2380000.00, 'A321', 'SCHEDULED'),
(124, 'VN974', 24, 'VN', '2026-07-26 14:30:00', '2026-07-26 15:48:00', 220, 2500000.00, 'A320', 'SCHEDULED'),
(125, 'QH292', 20, 'QH', '2026-07-26 13:30:00', '2026-07-26 15:42:00', 300, 1750000.00, 'B787', 'SCHEDULED'),
(126, 'QH820', 19, 'QH', '2026-07-27 15:30:00', '2026-07-27 17:42:00', 180, 930000.00, 'A321', 'SCHEDULED'),
(127, 'VN526', 14, 'VN', '2026-07-27 20:30:00', '2026-07-27 21:06:00', 220, 1130000.00, 'A320', 'SCHEDULED'),
(128, 'QH202', 16, 'QH', '2026-07-27 15:15:00', '2026-07-27 17:09:00', 220, 1130000.00, 'A320', 'SCHEDULED'),
(129, 'VJ241', 3, 'VJ', '2026-07-27 08:00:00', '2026-07-27 09:13:00', 300, 1810000.00, 'B787', 'SCHEDULED'),
(130, 'VN851', 5, 'VN', '2026-07-27 12:00:00', '2026-07-27 13:13:00', 180, 2130000.00, 'A321', 'SCHEDULED'),
(131, 'QH745', 5, 'QH', '2026-07-27 10:15:00', '2026-07-27 11:28:00', 180, 1070000.00, 'A321', 'SCHEDULED'),
(132, 'VN551', 12, 'VN', '2026-07-27 09:00:00', '2026-07-27 11:24:00', 180, 820000.00, 'A321', 'SCHEDULED'),
(133, 'VU382', 5, 'VU', '2026-07-27 11:45:00', '2026-07-27 12:58:00', 220, 1370000.00, 'A320', 'SCHEDULED'),
(134, 'QH698', 20, 'QH', '2026-07-28 21:15:00', '2026-07-28 23:27:00', 300, 1710000.00, 'B787', 'SCHEDULED'),
(135, 'VN987', 14, 'VN', '2026-07-28 08:30:00', '2026-07-28 09:06:00', 180, 1050000.00, 'A321', 'SCHEDULED'),
(136, 'VU803', 10, 'VU', '2026-07-28 19:00:00', '2026-07-28 19:37:00', 300, 1240000.00, 'B787', 'SCHEDULED'),
(137, 'QH971', 32, 'QH', '2026-07-28 09:30:00', '2026-07-28 10:24:00', 180, 2050000.00, 'A321', 'SCHEDULED'),
(138, 'VJ497', 17, 'VJ', '2026-07-28 07:45:00', '2026-07-28 08:15:00', 300, 1980000.00, 'B787', 'SCHEDULED'),
(139, 'VN390', 21, 'VN', '2026-07-28 06:30:00', '2026-07-28 07:35:00', 220, 960000.00, 'A320', 'SCHEDULED'),
(140, 'VU248', 4, 'VU', '2026-07-28 15:45:00', '2026-07-28 16:58:00', 180, 1620000.00, 'A321', 'SCHEDULED'),
(141, 'VJ163', 11, 'VJ', '2026-07-28 16:15:00', '2026-07-28 18:39:00', 180, 1410000.00, 'A321', 'SCHEDULED'),
(142, 'VJ645', 14, 'VJ', '2026-07-28 21:45:00', '2026-07-28 22:21:00', 300, 1010000.00, 'B787', 'SCHEDULED'),
(143, 'VJ875', 34, 'VJ', '2026-07-29 16:15:00', '2026-07-29 16:51:00', 220, 1420000.00, 'A320', 'SCHEDULED'),
(144, 'VU267', 9, 'VU', '2026-07-29 22:00:00', '2026-07-29 22:37:00', 220, 2330000.00, 'A320', 'SCHEDULED'),
(145, 'VU274', 34, 'VU', '2026-07-29 17:30:00', '2026-07-29 18:06:00', 220, 2180000.00, 'A320', 'SCHEDULED'),
(146, 'VN450', 24, 'VN', '2026-07-29 11:45:00', '2026-07-29 13:03:00', 180, 2470000.00, 'A321', 'SCHEDULED'),
(147, 'VJ749', 20, 'VJ', '2026-07-29 22:30:00', '2026-07-30 00:42:00', 300, 1120000.00, 'B787', 'SCHEDULED'),
(148, 'VJ337', 29, 'VJ', '2026-07-29 22:30:00', '2026-07-30 00:06:00', 180, 2000000.00, 'A321', 'SCHEDULED'),
(149, 'VJ406', 15, 'VJ', '2026-07-29 14:15:00', '2026-07-29 16:09:00', 180, 2410000.00, 'A321', 'SCHEDULED'),
(150, 'VN926', 32, 'VN', '2026-07-29 13:30:00', '2026-07-29 14:24:00', 180, 1670000.00, 'A321', 'SCHEDULED'),
(151, 'VJ305', 30, 'VJ', '2026-07-30 07:00:00', '2026-07-30 08:36:00', 180, 1410000.00, 'A321', 'SCHEDULED'),
(152, 'VN120', 22, 'VN', '2026-07-30 16:15:00', '2026-07-30 17:20:00', 300, 2360000.00, 'B787', 'SCHEDULED'),
(153, 'VN736', 1, 'VN', '2026-07-30 13:00:00', '2026-07-30 15:20:00', 180, 1850000.00, 'A321', 'SCHEDULED'),
(154, 'QH371', 12, 'QH', '2026-07-30 08:45:00', '2026-07-30 11:09:00', 180, 1630000.00, 'A321', 'SCHEDULED'),
(155, 'VJ736', 30, 'VJ', '2026-07-30 21:30:00', '2026-07-30 23:06:00', 180, 840000.00, 'A321', 'SCHEDULED'),
(156, 'VU664', 14, 'VU', '2026-07-30 18:15:00', '2026-07-30 18:51:00', 300, 1300000.00, 'B787', 'SCHEDULED'),
(157, 'VJ765', 11, 'VJ', '2026-07-30 10:45:00', '2026-07-30 13:09:00', 220, 1620000.00, 'A320', 'SCHEDULED'),
(158, 'VU902', 28, 'VU', '2026-07-30 13:00:00', '2026-07-30 14:36:00', 220, 1540000.00, 'A320', 'SCHEDULED'),
(159, 'QH379', 20, 'QH', '2026-07-31 22:30:00', '2026-08-01 00:42:00', 180, 1340000.00, 'A321', 'SCHEDULED'),
(160, 'VJ375', 17, 'VJ', '2026-07-31 21:15:00', '2026-07-31 21:45:00', 220, 1180000.00, 'A320', 'SCHEDULED'),
(161, 'QH525', 17, 'QH', '2026-07-31 17:00:00', '2026-07-31 17:30:00', 300, 1110000.00, 'B787', 'SCHEDULED'),
(162, 'VJ592', 32, 'VJ', '2026-07-31 16:30:00', '2026-07-31 17:24:00', 300, 1070000.00, 'B787', 'SCHEDULED'),
(163, 'VN657', 10, 'VN', '2026-07-31 06:30:00', '2026-07-31 07:07:00', 180, 2440000.00, 'A321', 'SCHEDULED'),
(164, 'QH314', 4, 'QH', '2026-07-31 16:30:00', '2026-07-31 17:43:00', 300, 2450000.00, 'B787', 'SCHEDULED'),
(165, 'QH586', 23, 'QH', '2026-07-31 20:15:00', '2026-07-31 21:33:00', 300, 2160000.00, 'B787', 'SCHEDULED'),
(166, 'VN344', 6, 'VN', '2026-07-31 10:45:00', '2026-07-31 11:58:00', 180, 1740000.00, 'A321', 'SCHEDULED'),
(167, 'QH239', 10, 'QH', '2026-08-01 07:15:00', '2026-08-01 07:52:00', 220, 1910000.00, 'A320', 'SCHEDULED'),
(168, 'VN442', 3, 'VN', '2026-08-01 18:30:00', '2026-08-01 19:43:00', 220, 2270000.00, 'A320', 'SCHEDULED'),
(169, 'VN950', 12, 'VN', '2026-08-01 11:45:00', '2026-08-01 14:09:00', 300, 940000.00, 'B787', 'SCHEDULED'),
(170, 'VJ446', 13, 'VJ', '2026-08-01 17:45:00', '2026-08-01 18:21:00', 180, 2340000.00, 'A321', 'SCHEDULED'),
(171, 'QH850', 34, 'QH', '2026-08-01 17:45:00', '2026-08-01 18:21:00', 220, 2500000.00, 'A320', 'SCHEDULED'),
(172, 'VU929', 9, 'VU', '2026-08-01 14:15:00', '2026-08-01 14:52:00', 300, 1110000.00, 'B787', 'SCHEDULED'),
(173, 'VN336', 22, 'VN', '2026-08-01 15:45:00', '2026-08-01 16:50:00', 300, 2320000.00, 'B787', 'SCHEDULED'),
(174, 'VJ763', 23, 'VJ', '2026-08-01 21:00:00', '2026-08-01 22:18:00', 220, 1030000.00, 'A320', 'SCHEDULED'),
(175, 'VJ537', 21, 'VJ', '2026-08-01 06:45:00', '2026-08-01 07:50:00', 300, 1700000.00, 'B787', 'SCHEDULED'),
(176, 'VN533', 23, 'VN', '2026-08-02 14:00:00', '2026-08-02 15:18:00', 220, 1570000.00, 'A320', 'SCHEDULED'),
(177, 'VN350', 21, 'VN', '2026-08-02 17:00:00', '2026-08-02 18:05:00', 220, 2120000.00, 'A320', 'SCHEDULED'),
(178, 'VJ753', 15, 'VJ', '2026-08-02 21:15:00', '2026-08-02 23:09:00', 220, 1860000.00, 'A320', 'SCHEDULED'),
(179, 'VJ960', 30, 'VJ', '2026-08-02 06:15:00', '2026-08-02 07:51:00', 300, 1150000.00, 'B787', 'SCHEDULED'),
(180, 'VJ563', 1, 'VJ', '2026-08-02 09:45:00', '2026-08-02 12:05:00', 300, 1880000.00, 'B787', 'SCHEDULED'),
(181, 'VJ723', 19, 'VJ', '2026-08-02 11:45:00', '2026-08-02 13:57:00', 300, 1350000.00, 'B787', 'SCHEDULED'),
(182, 'QH144', 28, 'QH', '2026-08-02 20:15:00', '2026-08-02 21:51:00', 220, 1160000.00, 'A320', 'SCHEDULED'),
(183, 'VJ350', 33, 'VJ', '2026-08-02 15:30:00', '2026-08-02 16:06:00', 300, 1170000.00, 'B787', 'SCHEDULED'),
(184, 'QH785', 34, 'QH', '2026-08-03 11:30:00', '2026-08-03 12:06:00', 220, 2200000.00, 'A320', 'SCHEDULED'),
(185, 'VJ761', 22, 'VJ', '2026-08-03 13:45:00', '2026-08-03 14:50:00', 300, 1780000.00, 'B787', 'SCHEDULED'),
(186, 'VJ303', 32, 'VJ', '2026-08-03 07:30:00', '2026-08-03 08:24:00', 220, 2250000.00, 'A320', 'SCHEDULED'),
(187, 'VJ460', 24, 'VJ', '2026-08-03 08:30:00', '2026-08-03 09:48:00', 180, 2180000.00, 'A321', 'SCHEDULED'),
(188, 'VJ509', 17, 'VJ', '2026-08-03 21:00:00', '2026-08-03 21:30:00', 180, 1370000.00, 'A321', 'SCHEDULED'),
(189, 'VU449', 31, 'VU', '2026-08-03 13:45:00', '2026-08-03 14:39:00', 220, 1170000.00, 'A320', 'SCHEDULED'),
(190, 'VU145', 31, 'VU', '2026-08-03 10:45:00', '2026-08-03 11:39:00', 180, 1060000.00, 'A321', 'SCHEDULED'),
(191, 'VU406', 3, 'VU', '2026-08-03 17:15:00', '2026-08-03 18:28:00', 220, 1550000.00, 'A320', 'SCHEDULED'),
(192, 'VU919', 29, 'VU', '2026-08-04 20:15:00', '2026-08-04 21:51:00', 300, 2470000.00, 'B787', 'SCHEDULED'),
(193, 'VU772', 9, 'VU', '2026-08-04 12:15:00', '2026-08-04 12:52:00', 180, 1480000.00, 'A321', 'SCHEDULED'),
(194, 'QH547', 30, 'QH', '2026-08-04 06:45:00', '2026-08-04 08:21:00', 220, 1260000.00, 'A320', 'SCHEDULED'),
(195, 'VJ708', 8, 'VJ', '2026-08-04 06:30:00', '2026-08-04 08:30:00', 220, 1310000.00, 'A320', 'SCHEDULED'),
(196, 'VN481', 6, 'VN', '2026-08-04 07:00:00', '2026-08-04 08:13:00', 220, 1170000.00, 'A320', 'SCHEDULED'),
(197, 'VJ323', 18, 'VJ', '2026-08-04 16:30:00', '2026-08-04 17:00:00', 300, 1910000.00, 'B787', 'SCHEDULED'),
(198, 'VU529', 6, 'VU', '2026-08-04 09:15:00', '2026-08-04 10:28:00', 300, 1130000.00, 'B787', 'SCHEDULED'),
(199, 'VJ480', 28, 'VJ', '2026-08-04 16:00:00', '2026-08-04 17:36:00', 300, 1970000.00, 'B787', 'SCHEDULED'),
(200, 'VU701', 16, 'VU', '2026-08-04 15:45:00', '2026-08-04 17:39:00', 220, 1350000.00, 'A320', 'SCHEDULED'),
(201, 'VN585', 1, 'VN', '2026-08-05 12:45:00', '2026-08-05 15:05:00', 300, 1040000.00, 'B787', 'SCHEDULED'),
(202, 'VU831', 5, 'VU', '2026-08-05 21:15:00', '2026-08-05 22:28:00', 220, 2490000.00, 'A320', 'SCHEDULED'),
(203, 'QH640', 10, 'QH', '2026-08-05 20:00:00', '2026-08-05 20:37:00', 300, 1930000.00, 'B787', 'SCHEDULED'),
(204, 'VU358', 23, 'VU', '2026-08-05 07:15:00', '2026-08-05 08:33:00', 300, 1200000.00, 'B787', 'SCHEDULED'),
(205, 'VJ239', 20, 'VJ', '2026-08-05 20:00:00', '2026-08-05 22:12:00', 180, 890000.00, 'A321', 'SCHEDULED'),
(206, 'VU653', 2, 'VU', '2026-08-05 13:00:00', '2026-08-05 15:20:00', 180, 1360000.00, 'A321', 'SCHEDULED'),
(207, 'VN936', 19, 'VN', '2026-08-05 13:45:00', '2026-08-05 15:57:00', 220, 1910000.00, 'A320', 'SCHEDULED'),
(208, 'VU237', 34, 'VU', '2026-08-05 21:15:00', '2026-08-05 21:51:00', 300, 1130000.00, 'B787', 'SCHEDULED'),
(209, 'VU495', 17, 'VU', '2026-08-06 17:30:00', '2026-08-06 18:00:00', 180, 2160000.00, 'A321', 'SCHEDULED'),
(210, 'QH451', 17, 'QH', '2026-08-06 07:15:00', '2026-08-06 07:45:00', 300, 1880000.00, 'B787', 'SCHEDULED'),
(211, 'VN619', 32, 'VN', '2026-08-06 19:45:00', '2026-08-06 20:39:00', 220, 1590000.00, 'A320', 'SCHEDULED'),
(212, 'VJ567', 1, 'VJ', '2026-08-06 12:15:00', '2026-08-06 14:35:00', 300, 1870000.00, 'B787', 'SCHEDULED'),
(213, 'QH985', 26, 'QH', '2026-08-06 12:00:00', '2026-08-06 14:18:00', 300, 1250000.00, 'B787', 'SCHEDULED'),
(214, 'VJ672', 20, 'VJ', '2026-08-06 07:45:00', '2026-08-06 09:57:00', 220, 2340000.00, 'A320', 'SCHEDULED'),
(215, 'VJ376', 6, 'VJ', '2026-08-06 12:30:00', '2026-08-06 13:43:00', 180, 2020000.00, 'A321', 'SCHEDULED'),
(216, 'VJ745', 19, 'VJ', '2026-08-06 07:45:00', '2026-08-06 09:57:00', 300, 810000.00, 'B787', 'SCHEDULED'),
(217, 'QH821', 14, 'QH', '2026-08-07 16:00:00', '2026-08-07 16:36:00', 220, 2050000.00, 'A320', 'SCHEDULED'),
(218, 'VN499', 7, 'VN', '2026-08-07 14:45:00', '2026-08-07 16:45:00', 300, 870000.00, 'B787', 'SCHEDULED'),
(219, 'QH220', 22, 'QH', '2026-08-07 22:45:00', '2026-08-07 23:50:00', 180, 880000.00, 'A321', 'SCHEDULED'),
(220, 'VN775', 25, 'VN', '2026-08-07 22:15:00', '2026-08-08 00:33:00', 300, 1300000.00, 'B787', 'SCHEDULED'),
(221, 'QH893', 11, 'QH', '2026-08-07 14:45:00', '2026-08-07 17:09:00', 300, 1710000.00, 'B787', 'SCHEDULED'),
(222, 'VJ910', 9, 'VJ', '2026-08-07 11:45:00', '2026-08-07 12:22:00', 220, 2270000.00, 'A320', 'SCHEDULED'),
(223, 'VU453', 14, 'VU', '2026-08-07 16:15:00', '2026-08-07 16:51:00', 220, 920000.00, 'A320', 'SCHEDULED'),
(224, 'VU668', 2, 'VU', '2026-08-07 13:00:00', '2026-08-07 15:20:00', 300, 2000000.00, 'B787', 'SCHEDULED'),
(225, 'VU788', 2, 'VU', '2026-08-07 17:15:00', '2026-08-07 19:35:00', 180, 2030000.00, 'A321', 'SCHEDULED'),
(226, 'VJ478', 3, 'VJ', '2026-08-08 07:15:00', '2026-08-08 08:28:00', 220, 1620000.00, 'A320', 'SCHEDULED'),
(227, 'VN136', 6, 'VN', '2026-08-08 22:15:00', '2026-08-08 23:28:00', 180, 2150000.00, 'A321', 'SCHEDULED'),
(228, 'VN209', 12, 'VN', '2026-08-08 06:30:00', '2026-08-08 08:54:00', 220, 2270000.00, 'A320', 'SCHEDULED'),
(229, 'VU714', 3, 'VU', '2026-08-08 08:30:00', '2026-08-08 09:43:00', 300, 1800000.00, 'B787', 'SCHEDULED'),
(230, 'VU794', 31, 'VU', '2026-08-08 07:15:00', '2026-08-08 08:09:00', 300, 920000.00, 'B787', 'SCHEDULED'),
(231, 'VN263', 7, 'VN', '2026-08-08 07:15:00', '2026-08-08 09:15:00', 220, 1460000.00, 'A320', 'SCHEDULED'),
(232, 'QH782', 13, 'QH', '2026-08-08 14:15:00', '2026-08-08 14:51:00', 220, 1020000.00, 'A320', 'SCHEDULED'),
(233, 'VJ652', 30, 'VJ', '2026-08-08 15:15:00', '2026-08-08 16:51:00', 300, 1480000.00, 'B787', 'SCHEDULED'),
(234, 'QH740', 25, 'QH', '2026-08-09 09:00:00', '2026-08-09 11:18:00', 220, 2280000.00, 'A320', 'SCHEDULED'),
(235, 'VU999', 34, 'VU', '2026-08-09 13:45:00', '2026-08-09 14:21:00', 180, 2220000.00, 'A321', 'SCHEDULED'),
(236, 'VU575', 22, 'VU', '2026-08-09 12:00:00', '2026-08-09 13:05:00', 180, 1930000.00, 'A321', 'SCHEDULED'),
(237, 'VJ300', 30, 'VJ', '2026-08-09 22:15:00', '2026-08-09 23:51:00', 180, 850000.00, 'A321', 'SCHEDULED'),
(238, 'VU158', 22, 'VU', '2026-08-09 19:45:00', '2026-08-09 20:50:00', 180, 1050000.00, 'A321', 'SCHEDULED'),
(239, 'VJ555', 5, 'VJ', '2026-08-09 21:00:00', '2026-08-09 22:13:00', 300, 1150000.00, 'B787', 'SCHEDULED'),
(240, 'QH979', 34, 'QH', '2026-08-09 18:15:00', '2026-08-09 18:51:00', 180, 2110000.00, 'A321', 'SCHEDULED'),
(241, 'VN738', 3, 'VN', '2026-08-09 07:00:00', '2026-08-09 08:13:00', 180, 1200000.00, 'A321', 'SCHEDULED'),
(242, 'QH542', 9, 'QH', '2026-08-10 11:45:00', '2026-08-10 12:22:00', 300, 1620000.00, 'B787', 'SCHEDULED'),
(243, 'VJ160', 5, 'VJ', '2026-08-10 20:00:00', '2026-08-10 21:13:00', 180, 2410000.00, 'A321', 'SCHEDULED'),
(244, 'VJ179', 11, 'VJ', '2026-08-10 22:30:00', '2026-08-11 00:54:00', 300, 1900000.00, 'B787', 'SCHEDULED'),
(245, 'QH773', 27, 'QH', '2026-08-10 16:45:00', '2026-08-10 18:21:00', 180, 1380000.00, 'A321', 'SCHEDULED'),
(246, 'VN251', 12, 'VN', '2026-08-10 18:30:00', '2026-08-10 20:54:00', 180, 1570000.00, 'A321', 'SCHEDULED'),
(247, 'VN929', 18, 'VN', '2026-08-10 16:45:00', '2026-08-10 17:15:00', 180, 2410000.00, 'A321', 'SCHEDULED'),
(248, 'VJ973', 22, 'VJ', '2026-08-10 19:45:00', '2026-08-10 20:50:00', 300, 1640000.00, 'B787', 'SCHEDULED'),
(249, 'QH657', 13, 'QH', '2026-08-10 10:45:00', '2026-08-10 11:21:00', 220, 2130000.00, 'A320', 'SCHEDULED'),
(250, 'VU403', 8, 'VU', '2026-08-10 17:30:00', '2026-08-10 19:30:00', 220, 880000.00, 'A320', 'SCHEDULED');

DO $$
DECLARE
    f RECORD;
    r INT;
    letter VARCHAR;
    letters VARCHAR[] := ARRAY['A', 'B', 'C', 'D', 'E', 'F'];
    sClass VARCHAR;
    factor DECIMAL(4,2);
BEGIN
    FOR f IN SELECT id, total_seats FROM flight LOOP
        INSERT INTO seat_inventory (flight_id, total, available, held, booked, version)
        VALUES (f.id, f.total_seats, f.total_seats, 0, 0, 0)
        ON CONFLICT (flight_id) DO UPDATE
        SET total = EXCLUDED.total, available = EXCLUDED.total, held = 0, booked = 0;

        FOR r IN 1..(f.total_seats / 6) LOOP
            IF r <= 3 THEN
                sClass := 'BUSINESS';
                factor := 1.50;
            ELSE
                sClass := 'ECONOMY';
                factor := 1.00;
            END IF;
            
            FOREACH letter IN ARRAY letters LOOP
                INSERT INTO flight_seat (flight_id, seat_no, class, status, price_factor)
                VALUES (f.id, r || letter, sClass, 'AVAILABLE', factor)
                ON CONFLICT (flight_id, seat_no) DO UPDATE
                SET class = EXCLUDED.class, status = 'AVAILABLE', price_factor = EXCLUDED.price_factor;
            END LOOP;
        END LOOP;
    END LOOP;
END $$;

UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 52 AND seat_no = '11E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 53 AND seat_no = '24A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 54 AND seat_no = '17C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 55 AND seat_no = '5A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 56 AND seat_no = '19D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 57 AND seat_no = '13A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 58 AND seat_no = '19E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 59 AND seat_no = '23E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 60 AND seat_no = '17E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 61 AND seat_no = '20C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 62 AND seat_no = '24A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 63 AND seat_no = '18A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 64 AND seat_no = '22C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 65 AND seat_no = '17D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 66 AND seat_no = '1B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 67 AND seat_no = '27E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 68 AND seat_no = '9E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 69 AND seat_no = '26A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 70 AND seat_no = '13F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 71 AND seat_no = '28C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 72 AND seat_no = '14A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 73 AND seat_no = '9A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 74 AND seat_no = '18F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 75 AND seat_no = '28A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 76 AND seat_no = '23E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 77 AND seat_no = '1C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 78 AND seat_no = '23A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 79 AND seat_no = '25F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 80 AND seat_no = '20E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 81 AND seat_no = '1C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 82 AND seat_no = '13F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 83 AND seat_no = '15D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 84 AND seat_no = '3B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 85 AND seat_no = '11A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 86 AND seat_no = '15B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 87 AND seat_no = '10C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 88 AND seat_no = '19A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 89 AND seat_no = '26C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 90 AND seat_no = '19F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 91 AND seat_no = '28D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 92 AND seat_no = '20A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 93 AND seat_no = '6F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 94 AND seat_no = '22B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 95 AND seat_no = '17F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 96 AND seat_no = '3D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 97 AND seat_no = '8B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 98 AND seat_no = '9F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 99 AND seat_no = '12E';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 100 AND seat_no = '14D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 100 AND seat_no = '28F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 101 AND seat_no = '26E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 102 AND seat_no = '3F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 103 AND seat_no = '12B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 104 AND seat_no = '14F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 105 AND seat_no = '25D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 106 AND seat_no = '16D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 107 AND seat_no = '22E';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 108 AND seat_no = '18B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 108 AND seat_no = '23A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 109 AND seat_no = '7A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 110 AND seat_no = '24A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 111 AND seat_no = '14F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 112 AND seat_no = '26E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 113 AND seat_no = '25E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 114 AND seat_no = '28A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 115 AND seat_no = '19D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 116 AND seat_no = '18E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 117 AND seat_no = '19F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 118 AND seat_no = '19A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 119 AND seat_no = '23A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 120 AND seat_no = '2B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 121 AND seat_no = '20E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 122 AND seat_no = '8C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 123 AND seat_no = '19E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 124 AND seat_no = '17C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 125 AND seat_no = '4B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 126 AND seat_no = '1A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 127 AND seat_no = '23B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 128 AND seat_no = '16A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 129 AND seat_no = '7B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 130 AND seat_no = '13E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 131 AND seat_no = '4E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 132 AND seat_no = '23C';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 133 AND seat_no = '17B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 133 AND seat_no = '24B';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 134 AND seat_no = '2A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 135 AND seat_no = '4F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 136 AND seat_no = '17D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 137 AND seat_no = '17D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 138 AND seat_no = '28F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 139 AND seat_no = '28F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 140 AND seat_no = '28E';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 141 AND seat_no = '10C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 141 AND seat_no = '28A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 142 AND seat_no = '2F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 143 AND seat_no = '15C';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 144 AND seat_no = '2A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 145 AND seat_no = '20F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 146 AND seat_no = '5A';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 147 AND seat_no = '6F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 148 AND seat_no = '2F';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 149 AND seat_no = '17E';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 150 AND seat_no = '1D';
UPDATE flight_seat SET status = 'BOOKED' WHERE flight_id = 151 AND seat_no = '2E';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 162 AND seat_no = '9C';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 170 AND seat_no = '19F';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 172 AND seat_no = '5F';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 186 AND seat_no = '8D';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 212 AND seat_no = '12C';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 224 AND seat_no = '10A';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 234 AND seat_no = '23F';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 239 AND seat_no = '25C';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 239 AND seat_no = '19A';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 246 AND seat_no = '10F';
UPDATE flight_seat SET status = 'HELD' WHERE flight_id = 249 AND seat_no = '16B';

UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 52;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 53;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 54;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 55;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 56;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 57;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 58;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 59;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 60;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 61;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 62;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 63;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 64;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 65;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 66;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 67;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 68;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 69;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 70;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 71;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 72;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 73;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 74;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 75;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 76;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 77;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 78;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 79;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 80;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 81;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 82;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 83;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 84;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 85;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 86;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 87;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 88;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 89;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 90;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 91;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 92;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 93;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 94;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 95;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 96;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 97;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 98;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 99;
UPDATE seat_inventory SET booked = 1, held = 1, available = total - 1 - 1 WHERE flight_id = 100;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 101;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 102;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 103;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 104;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 105;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 106;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 107;
UPDATE seat_inventory SET booked = 1, held = 1, available = total - 1 - 1 WHERE flight_id = 108;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 109;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 110;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 111;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 112;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 113;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 114;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 115;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 116;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 117;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 118;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 119;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 120;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 121;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 122;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 123;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 124;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 125;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 126;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 127;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 128;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 129;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 130;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 131;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 132;
UPDATE seat_inventory SET booked = 1, held = 1, available = total - 1 - 1 WHERE flight_id = 133;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 134;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 135;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 136;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 137;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 138;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 139;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 140;
UPDATE seat_inventory SET booked = 1, held = 1, available = total - 1 - 1 WHERE flight_id = 141;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 142;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 143;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 144;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 145;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 146;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 147;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 148;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 149;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 150;
UPDATE seat_inventory SET booked = 1, held = 0, available = total - 1 - 0 WHERE flight_id = 151;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 162;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 170;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 172;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 186;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 212;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 224;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 234;
UPDATE seat_inventory SET booked = 0, held = 2, available = total - 0 - 2 WHERE flight_id = 239;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 246;
UPDATE seat_inventory SET booked = 0, held = 1, available = total - 0 - 1 WHERE flight_id = 249;

SELECT setval('route_id_seq', (SELECT MAX(id) FROM route));
SELECT setval('flight_id_seq', (SELECT MAX(id) FROM flight));
COMMIT;

-- 3. SEED DATA FOR BOOKING SERVICE (booking_db)
\c booking_db
BEGIN;
DELETE FROM booking_item;
DELETE FROM booking;

INSERT INTO booking (id, booking_code, user_id, flight_id, status, total_amount, currency, held_at, expires_at, confirmed_at, cancelled_at, payment_id) VALUES
(1, 'PNR001', 92, 100, 'HELD', 1620000.00, 'VND', '2026-07-21 15:17:00', '2026-07-21 15:27:00', NULL, NULL, NULL),
(2, 'PNR002', 67, 141, 'HELD', 1410000.00, 'VND', '2026-07-21 15:36:00', '2026-07-21 15:46:00', NULL, NULL, NULL),
(3, 'PNR003', 90, 162, 'HELD', 1070000.00, 'VND', '2026-07-21 15:45:00', '2026-07-21 15:55:00', NULL, NULL, NULL),
(4, 'PNR004', 24, 234, 'HELD', 2280000.00, 'VND', '2026-07-21 15:26:00', '2026-07-21 15:36:00', NULL, NULL, NULL),
(5, 'PNR005', 81, 239, 'HELD', 1150000.00, 'VND', '2026-07-21 15:50:00', '2026-07-21 16:00:00', NULL, NULL, NULL),
(6, 'PNR006', 23, 186, 'HELD', 2250000.00, 'VND', '2026-07-21 15:35:00', '2026-07-21 15:45:00', NULL, NULL, NULL),
(7, 'PNR007', 4, 224, 'HELD', 2000000.00, 'VND', '2026-07-21 15:15:00', '2026-07-21 15:25:00', NULL, NULL, NULL),
(8, 'PNR008', 79, 172, 'HELD', 1110000.00, 'VND', '2026-07-21 15:10:00', '2026-07-21 15:20:00', NULL, NULL, NULL),
(9, 'PNR009', 16, 108, 'HELD', 1140000.00, 'VND', '2026-07-21 15:36:00', '2026-07-21 15:46:00', NULL, NULL, NULL),
(10, 'PNR010', 82, 246, 'HELD', 1570000.00, 'VND', '2026-07-21 15:31:00', '2026-07-21 15:41:00', NULL, NULL, NULL),
(11, 'PNR011', 76, 212, 'HELD', 1870000.00, 'VND', '2026-07-21 15:38:00', '2026-07-21 15:48:00', NULL, NULL, NULL),
(12, 'PNR012', 13, 170, 'HELD', 2340000.00, 'VND', '2026-07-21 15:36:00', '2026-07-21 15:46:00', NULL, NULL, NULL),
(13, 'PNR013', 63, 133, 'HELD', 1370000.00, 'VND', '2026-07-21 15:29:00', '2026-07-21 15:39:00', NULL, NULL, NULL),
(14, 'PNR014', 70, 249, 'HELD', 2130000.00, 'VND', '2026-07-21 15:37:00', '2026-07-21 15:47:00', NULL, NULL, NULL),
(15, 'PNR015', 72, 239, 'HELD', 1150000.00, 'VND', '2026-07-21 15:43:00', '2026-07-21 15:53:00', NULL, NULL, NULL),
(16, 'PNR016', 70, 166, 'EXPIRED', 1740000.00, 'VND', '2026-07-20 10:16:00', '2026-07-20 10:26:00', NULL, NULL, NULL),
(17, 'PNR017', 69, 230, 'EXPIRED', 920000.00, 'VND', '2026-07-20 10:34:00', '2026-07-20 10:44:00', NULL, NULL, NULL),
(18, 'PNR018', 18, 177, 'EXPIRED', 2120000.00, 'VND', '2026-07-20 10:24:00', '2026-07-20 10:34:00', NULL, NULL, NULL),
(19, 'PNR019', 19, 124, 'EXPIRED', 2500000.00, 'VND', '2026-07-20 10:26:00', '2026-07-20 10:36:00', NULL, NULL, NULL),
(20, 'PNR020', 30, 79, 'EXPIRED', 1190000.00, 'VND', '2026-07-20 10:11:00', '2026-07-20 10:21:00', NULL, NULL, NULL),
(21, 'PNR021', 72, 205, 'EXPIRED', 890000.00, 'VND', '2026-07-20 10:44:00', '2026-07-20 10:54:00', NULL, NULL, NULL),
(22, 'PNR022', 40, 219, 'EXPIRED', 880000.00, 'VND', '2026-07-20 10:14:00', '2026-07-20 10:24:00', NULL, NULL, NULL),
(23, 'PNR023', 52, 122, 'EXPIRED', 1690000.00, 'VND', '2026-07-20 10:20:00', '2026-07-20 10:30:00', NULL, NULL, NULL),
(24, 'PNR024', 28, 125, 'EXPIRED', 1750000.00, 'VND', '2026-07-20 10:42:00', '2026-07-20 10:52:00', NULL, NULL, NULL),
(25, 'PNR025', 86, 91, 'EXPIRED', 2460000.00, 'VND', '2026-07-20 10:15:00', '2026-07-20 10:25:00', NULL, NULL, NULL),
(26, 'PNR026', 65, 160, 'EXPIRED', 1180000.00, 'VND', '2026-07-20 10:37:00', '2026-07-20 10:47:00', NULL, NULL, NULL),
(27, 'PNR027', 80, 231, 'EXPIRED', 1460000.00, 'VND', '2026-07-20 10:35:00', '2026-07-20 10:45:00', NULL, NULL, NULL),
(28, 'PNR028', 55, 170, 'EXPIRED', 2340000.00, 'VND', '2026-07-20 10:37:00', '2026-07-20 10:47:00', NULL, NULL, NULL),
(29, 'PNR029', 50, 186, 'EXPIRED', 2250000.00, 'VND', '2026-07-20 10:23:00', '2026-07-20 10:33:00', NULL, NULL, NULL),
(30, 'PNR030', 78, 93, 'EXPIRED', 1220000.00, 'VND', '2026-07-20 10:28:00', '2026-07-20 10:38:00', NULL, NULL, NULL),
(31, 'PNR031', 90, 164, 'CANCELLED', 2450000.00, 'VND', '2026-07-15 08:19:00', NULL, NULL, '2026-07-15 08:29:00', 1),
(32, 'PNR032', 15, 214, 'CANCELLED', 2340000.00, 'VND', '2026-07-15 08:34:00', NULL, NULL, '2026-07-15 08:44:00', 2),
(33, 'PNR033', 84, 202, 'CANCELLED', 2490000.00, 'VND', '2026-07-15 08:46:00', NULL, NULL, '2026-07-15 08:56:00', 3),
(34, 'PNR034', 50, 149, 'CANCELLED', 2410000.00, 'VND', '2026-07-15 08:28:00', NULL, NULL, '2026-07-15 08:38:00', 4),
(35, 'PNR035', 58, 237, 'CANCELLED', 850000.00, 'VND', '2026-07-15 08:42:00', NULL, NULL, '2026-07-15 08:52:00', 5),
(36, 'PNR036', 27, 226, 'CANCELLED', 1620000.00, 'VND', '2026-07-15 08:34:00', NULL, NULL, '2026-07-15 08:44:00', 6),
(37, 'PNR037', 85, 150, 'CANCELLED', 1670000.00, 'VND', '2026-07-15 08:39:00', NULL, NULL, '2026-07-15 08:49:00', 7),
(38, 'PNR038', 15, 81, 'CANCELLED', 1820000.00, 'VND', '2026-07-15 08:17:00', NULL, NULL, '2026-07-15 08:27:00', 8),
(39, 'PNR039', 66, 152, 'CANCELLED', 2360000.00, 'VND', '2026-07-15 08:20:00', NULL, NULL, '2026-07-15 08:30:00', 9),
(40, 'PNR040', 11, 228, 'CANCELLED', 2270000.00, 'VND', '2026-07-15 08:27:00', NULL, NULL, '2026-07-15 08:37:00', 10),
(41, 'PNR041', 87, 52, 'REFUNDED', 2440000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 11),
(42, 'PNR042', 33, 30, 'REFUNDED', 1050000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 12),
(43, 'PNR043', 28, 18, 'REFUNDED', 1730000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 13),
(44, 'PNR044', 61, 24, 'REFUNDED', 1460000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 14),
(45, 'PNR045', 72, 74, 'REFUNDED', 1460000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 15),
(46, 'PNR046', 31, 6, 'REFUNDED', 1330000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 16),
(47, 'PNR047', 19, 2, 'REFUNDED', 820000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 17),
(48, 'PNR048', 69, 44, 'REFUNDED', 1680000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 18),
(49, 'PNR049', 80, 19, 'REFUNDED', 1710000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 19),
(50, 'PNR050', 23, 4, 'REFUNDED', 900000.00, 'VND', '2026-07-13 09:00:00', NULL, '2026-07-13 09:05:00', '2026-07-14 10:00:00', 20),
(51, 'PNR051', 29, 52, 'CONFIRMED', 2440000.00, 'VND', '2026-07-14 00:30:00', NULL, '2026-07-14 00:36:00', NULL, 21),
(52, 'PNR052', 100, 53, 'CONFIRMED', 1800000.00, 'VND', '2026-07-17 18:30:00', NULL, '2026-07-17 18:36:00', NULL, 22),
(53, 'PNR053', 94, 54, 'CONFIRMED', 1130000.00, 'VND', '2026-07-16 07:00:00', NULL, '2026-07-16 07:10:00', NULL, 23),
(54, 'PNR054', 82, 55, 'CONFIRMED', 1290000.00, 'VND', '2026-07-13 02:00:00', NULL, '2026-07-13 02:04:00', NULL, 24),
(55, 'PNR055', 46, 56, 'CONFIRMED', 1300000.00, 'VND', '2026-07-14 23:00:00', NULL, '2026-07-14 23:09:00', NULL, 25),
(56, 'PNR056', 26, 57, 'CONFIRMED', 2360000.00, 'VND', '2026-07-16 14:30:00', NULL, '2026-07-16 14:36:00', NULL, 26),
(57, 'PNR057', 11, 58, 'CONFIRMED', 2250000.00, 'VND', '2026-07-16 03:15:00', NULL, '2026-07-16 03:22:00', NULL, 27),
(58, 'PNR058', 37, 59, 'CONFIRMED', 1470000.00, 'VND', '2026-07-14 21:15:00', NULL, '2026-07-14 21:24:00', NULL, 28),
(59, 'PNR059', 93, 60, 'CONFIRMED', 950000.00, 'VND', '2026-07-18 04:00:00', NULL, '2026-07-18 04:06:00', NULL, 29),
(60, 'PNR060', 97, 61, 'CONFIRMED', 1580000.00, 'VND', '2026-07-18 12:30:00', NULL, '2026-07-18 12:33:00', NULL, 30),
(61, 'PNR061', 52, 62, 'CONFIRMED', 2350000.00, 'VND', '2026-07-17 04:15:00', NULL, '2026-07-17 04:21:00', NULL, 31),
(62, 'PNR062', 65, 63, 'CONFIRMED', 2100000.00, 'VND', '2026-07-17 03:45:00', NULL, '2026-07-17 03:54:00', NULL, 32),
(63, 'PNR063', 77, 64, 'CONFIRMED', 880000.00, 'VND', '2026-07-14 23:45:00', NULL, '2026-07-14 23:52:00', NULL, 33),
(64, 'PNR064', 11, 65, 'CONFIRMED', 1130000.00, 'VND', '2026-07-16 22:30:00', NULL, '2026-07-16 22:40:00', NULL, 34),
(65, 'PNR065', 43, 66, 'CONFIRMED', 2790000.00, 'VND', '2026-07-15 09:00:00', NULL, '2026-07-15 09:08:00', NULL, 35),
(66, 'PNR066', 25, 67, 'CONFIRMED', 1900000.00, 'VND', '2026-07-18 12:30:00', NULL, '2026-07-18 12:34:00', NULL, 36),
(67, 'PNR067', 42, 68, 'CONFIRMED', 990000.00, 'VND', '2026-07-18 21:30:00', NULL, '2026-07-18 21:38:00', NULL, 37),
(68, 'PNR068', 92, 69, 'CONFIRMED', 1250000.00, 'VND', '2026-07-15 05:00:00', NULL, '2026-07-15 05:06:00', NULL, 38),
(69, 'PNR069', 42, 70, 'CONFIRMED', 2360000.00, 'VND', '2026-07-16 14:00:00', NULL, '2026-07-16 14:09:00', NULL, 39),
(70, 'PNR070', 91, 71, 'CONFIRMED', 1290000.00, 'VND', '2026-07-16 07:45:00', NULL, '2026-07-16 07:49:00', NULL, 40),
(71, 'PNR071', 15, 72, 'CONFIRMED', 880000.00, 'VND', '2026-07-15 14:15:00', NULL, '2026-07-15 14:24:00', NULL, 41),
(72, 'PNR072', 25, 73, 'CONFIRMED', 880000.00, 'VND', '2026-07-15 05:45:00', NULL, '2026-07-15 05:51:00', NULL, 42),
(73, 'PNR073', 11, 74, 'CONFIRMED', 1460000.00, 'VND', '2026-07-19 08:30:00', NULL, '2026-07-19 08:38:00', NULL, 43),
(74, 'PNR074', 65, 75, 'CONFIRMED', 1890000.00, 'VND', '2026-07-19 14:45:00', NULL, '2026-07-19 14:53:00', NULL, 44),
(75, 'PNR075', 57, 76, 'CONFIRMED', 2320000.00, 'VND', '2026-07-16 07:30:00', NULL, '2026-07-16 07:34:00', NULL, 45),
(76, 'PNR076', 15, 77, 'CONFIRMED', 2100000.00, 'VND', '2026-07-20 11:45:00', NULL, '2026-07-20 11:48:00', NULL, 46),
(77, 'PNR077', 67, 78, 'CONFIRMED', 1600000.00, 'VND', '2026-07-20 15:30:00', NULL, '2026-07-20 15:34:00', NULL, 47),
(78, 'PNR078', 88, 79, 'CONFIRMED', 1190000.00, 'VND', '2026-07-18 11:45:00', NULL, '2026-07-18 11:53:00', NULL, 48),
(79, 'PNR079', 34, 80, 'CONFIRMED', 970000.00, 'VND', '2026-07-17 19:45:00', NULL, '2026-07-17 19:52:00', NULL, 49),
(80, 'PNR080', 69, 81, 'CONFIRMED', 2730000.00, 'VND', '2026-07-16 15:15:00', NULL, '2026-07-16 15:24:00', NULL, 50),
(81, 'PNR081', 31, 82, 'CONFIRMED', 1200000.00, 'VND', '2026-07-19 08:45:00', NULL, '2026-07-19 08:48:00', NULL, 51),
(82, 'PNR082', 14, 83, 'CONFIRMED', 1420000.00, 'VND', '2026-07-20 09:45:00', NULL, '2026-07-20 09:49:00', NULL, 52),
(83, 'PNR083', 26, 84, 'CONFIRMED', 3630000.00, 'VND', '2026-07-19 07:30:00', NULL, '2026-07-19 07:34:00', NULL, 53),
(84, 'PNR084', 55, 85, 'CONFIRMED', 1850000.00, 'VND', '2026-07-17 18:15:00', NULL, '2026-07-17 18:22:00', NULL, 54),
(85, 'PNR085', 81, 86, 'CONFIRMED', 1910000.00, 'VND', '2026-07-18 09:30:00', NULL, '2026-07-18 09:40:00', NULL, 55),
(86, 'PNR086', 7, 87, 'CONFIRMED', 2040000.00, 'VND', '2026-07-21 10:30:00', NULL, '2026-07-21 10:33:00', NULL, 56),
(87, 'PNR087', 17, 88, 'CONFIRMED', 2430000.00, 'VND', '2026-07-21 18:30:00', NULL, '2026-07-21 18:35:00', NULL, 57),
(88, 'PNR088', 51, 89, 'CONFIRMED', 1350000.00, 'VND', '2026-07-18 13:30:00', NULL, '2026-07-18 13:40:00', NULL, 58),
(89, 'PNR089', 33, 90, 'CONFIRMED', 2090000.00, 'VND', '2026-07-20 17:45:00', NULL, '2026-07-20 17:49:00', NULL, 59),
(90, 'PNR090', 49, 91, 'CONFIRMED', 2460000.00, 'VND', '2026-07-20 09:15:00', NULL, '2026-07-20 09:19:00', NULL, 60),
(91, 'PNR091', 88, 92, 'CONFIRMED', 1010000.00, 'VND', '2026-07-20 15:30:00', NULL, '2026-07-20 15:38:00', NULL, 61),
(92, 'PNR092', 51, 93, 'CONFIRMED', 1220000.00, 'VND', '2026-07-19 16:15:00', NULL, '2026-07-19 16:23:00', NULL, 62),
(93, 'PNR093', 49, 94, 'CONFIRMED', 1230000.00, 'VND', '2026-07-22 15:30:00', NULL, '2026-07-22 15:38:00', NULL, 63),
(94, 'PNR094', 56, 95, 'CONFIRMED', 1040000.00, 'VND', '2026-07-20 08:00:00', NULL, '2026-07-20 08:07:00', NULL, 64),
(95, 'PNR095', 48, 96, 'CONFIRMED', 3690000.00, 'VND', '2026-07-21 09:00:00', NULL, '2026-07-21 09:06:00', NULL, 65),
(96, 'PNR096', 46, 97, 'CONFIRMED', 2430000.00, 'VND', '2026-07-18 18:00:00', NULL, '2026-07-18 18:03:00', NULL, 66),
(97, 'PNR097', 97, 98, 'CONFIRMED', 2370000.00, 'VND', '2026-07-21 15:00:00', NULL, '2026-07-21 15:06:00', NULL, 67),
(98, 'PNR098', 54, 99, 'CONFIRMED', 1920000.00, 'VND', '2026-07-18 02:00:00', NULL, '2026-07-18 02:07:00', NULL, 68),
(99, 'PNR099', 90, 100, 'CONFIRMED', 1620000.00, 'VND', '2026-07-21 12:45:00', NULL, '2026-07-21 12:52:00', NULL, 69),
(100, 'PNR100', 24, 101, 'CONFIRMED', 1160000.00, 'VND', '2026-07-20 04:45:00', NULL, '2026-07-20 04:52:00', NULL, 70),
(101, 'PNR101', 48, 102, 'CONFIRMED', 2175000.00, 'VND', '2026-07-19 09:15:00', NULL, '2026-07-19 09:23:00', NULL, 71),
(102, 'PNR102', 10, 103, 'CONFIRMED', 2030000.00, 'VND', '2026-07-19 05:30:00', NULL, '2026-07-19 05:33:00', NULL, 72),
(103, 'PNR103', 88, 104, 'CONFIRMED', 1650000.00, 'VND', '2026-07-21 12:15:00', NULL, '2026-07-21 12:25:00', NULL, 73),
(104, 'PNR104', 74, 105, 'CONFIRMED', 2260000.00, 'VND', '2026-07-23 18:30:00', NULL, '2026-07-23 18:40:00', NULL, 74),
(105, 'PNR105', 67, 106, 'CONFIRMED', 1690000.00, 'VND', '2026-07-21 06:15:00', NULL, '2026-07-21 06:20:00', NULL, 75),
(106, 'PNR106', 76, 107, 'CONFIRMED', 1270000.00, 'VND', '2026-07-19 04:00:00', NULL, '2026-07-19 04:03:00', NULL, 76),
(107, 'PNR107', 32, 108, 'CONFIRMED', 1140000.00, 'VND', '2026-07-20 11:00:00', NULL, '2026-07-20 11:10:00', NULL, 77),
(108, 'PNR108', 63, 109, 'CONFIRMED', 2300000.00, 'VND', '2026-07-23 18:30:00', NULL, '2026-07-23 18:37:00', NULL, 78),
(109, 'PNR109', 6, 110, 'CONFIRMED', 2300000.00, 'VND', '2026-07-24 14:00:00', NULL, '2026-07-24 14:05:00', NULL, 79),
(110, 'PNR110', 38, 111, 'CONFIRMED', 1500000.00, 'VND', '2026-07-20 09:30:00', NULL, '2026-07-20 09:37:00', NULL, 80),
(111, 'PNR111', 16, 112, 'CONFIRMED', 2340000.00, 'VND', '2026-07-21 02:15:00', NULL, '2026-07-21 02:24:00', NULL, 81),
(112, 'PNR112', 19, 113, 'CONFIRMED', 1500000.00, 'VND', '2026-07-24 16:15:00', NULL, '2026-07-24 16:23:00', NULL, 82),
(113, 'PNR113', 16, 114, 'CONFIRMED', 1290000.00, 'VND', '2026-07-21 11:00:00', NULL, '2026-07-21 11:03:00', NULL, 83),
(114, 'PNR114', 10, 115, 'CONFIRMED', 2100000.00, 'VND', '2026-07-20 17:15:00', NULL, '2026-07-20 17:24:00', NULL, 84),
(115, 'PNR115', 86, 116, 'CONFIRMED', 1410000.00, 'VND', '2026-07-20 02:15:00', NULL, '2026-07-20 02:25:00', NULL, 85),
(116, 'PNR116', 48, 117, 'CONFIRMED', 2110000.00, 'VND', '2026-07-23 08:30:00', NULL, '2026-07-23 08:40:00', NULL, 86),
(117, 'PNR117', 100, 118, 'CONFIRMED', 1550000.00, 'VND', '2026-07-25 03:45:00', NULL, '2026-07-25 03:55:00', NULL, 87),
(118, 'PNR118', 37, 119, 'CONFIRMED', 2270000.00, 'VND', '2026-07-21 09:30:00', NULL, '2026-07-21 09:35:00', NULL, 88),
(119, 'PNR119', 26, 120, 'CONFIRMED', 3405000.00, 'VND', '2026-07-21 15:15:00', NULL, '2026-07-21 15:23:00', NULL, 89),
(120, 'PNR120', 47, 121, 'CONFIRMED', 2130000.00, 'VND', '2026-07-22 17:30:00', NULL, '2026-07-22 17:38:00', NULL, 90),
(121, 'PNR121', 20, 122, 'CONFIRMED', 1690000.00, 'VND', '2026-07-22 09:00:00', NULL, '2026-07-22 09:03:00', NULL, 91),
(122, 'PNR122', 91, 123, 'CONFIRMED', 2380000.00, 'VND', '2026-07-21 16:45:00', NULL, '2026-07-21 16:50:00', NULL, 92),
(123, 'PNR123', 61, 124, 'CONFIRMED', 2500000.00, 'VND', '2026-07-22 11:30:00', NULL, '2026-07-22 11:35:00', NULL, 93),
(124, 'PNR124', 84, 125, 'CONFIRMED', 1750000.00, 'VND', '2026-07-24 06:30:00', NULL, '2026-07-24 06:34:00', NULL, 94),
(125, 'PNR125', 97, 126, 'CONFIRMED', 1395000.00, 'VND', '2026-07-22 08:30:00', NULL, '2026-07-22 08:33:00', NULL, 95),
(126, 'PNR126', 87, 127, 'CONFIRMED', 1130000.00, 'VND', '2026-07-26 16:30:00', NULL, '2026-07-26 16:33:00', NULL, 96),
(127, 'PNR127', 98, 128, 'CONFIRMED', 1130000.00, 'VND', '2026-07-25 05:15:00', NULL, '2026-07-25 05:22:00', NULL, 97),
(128, 'PNR128', 78, 129, 'CONFIRMED', 1810000.00, 'VND', '2026-07-25 06:00:00', NULL, '2026-07-25 06:08:00', NULL, 98),
(129, 'PNR129', 36, 130, 'CONFIRMED', 2130000.00, 'VND', '2026-07-26 05:00:00', NULL, '2026-07-26 05:07:00', NULL, 99),
(130, 'PNR130', 92, 131, 'CONFIRMED', 1070000.00, 'VND', '2026-07-24 06:15:00', NULL, '2026-07-24 06:23:00', NULL, 100),
(131, 'PNR131', 38, 132, 'CONFIRMED', 820000.00, 'VND', '2026-07-25 06:00:00', NULL, '2026-07-25 06:04:00', NULL, 101),
(132, 'PNR132', 30, 133, 'CONFIRMED', 1370000.00, 'VND', '2026-07-22 01:45:00', NULL, '2026-07-22 01:50:00', NULL, 102),
(133, 'PNR133', 76, 134, 'CONFIRMED', 2565000.00, 'VND', '2026-07-23 11:15:00', NULL, '2026-07-23 11:23:00', NULL, 103),
(134, 'PNR134', 32, 135, 'CONFIRMED', 1050000.00, 'VND', '2026-07-24 02:30:00', NULL, '2026-07-24 02:37:00', NULL, 104),
(135, 'PNR135', 51, 136, 'CONFIRMED', 1240000.00, 'VND', '2026-07-27 11:00:00', NULL, '2026-07-27 11:06:00', NULL, 105),
(136, 'PNR136', 6, 137, 'CONFIRMED', 2050000.00, 'VND', '2026-07-27 06:30:00', NULL, '2026-07-27 06:36:00', NULL, 106),
(137, 'PNR137', 94, 138, 'CONFIRMED', 1980000.00, 'VND', '2026-07-22 22:45:00', NULL, '2026-07-22 22:55:00', NULL, 107),
(138, 'PNR138', 87, 139, 'CONFIRMED', 960000.00, 'VND', '2026-07-26 05:30:00', NULL, '2026-07-26 05:34:00', NULL, 108),
(139, 'PNR139', 65, 140, 'CONFIRMED', 1620000.00, 'VND', '2026-07-24 10:45:00', NULL, '2026-07-24 10:48:00', NULL, 109),
(140, 'PNR140', 96, 141, 'CONFIRMED', 1410000.00, 'VND', '2026-07-27 15:15:00', NULL, '2026-07-27 15:24:00', NULL, 110),
(141, 'PNR141', 13, 142, 'CONFIRMED', 1515000.00, 'VND', '2026-07-26 15:45:00', NULL, '2026-07-26 15:50:00', NULL, 111),
(142, 'PNR142', 44, 143, 'CONFIRMED', 1420000.00, 'VND', '2026-07-24 09:15:00', NULL, '2026-07-24 09:25:00', NULL, 112),
(143, 'PNR143', 7, 144, 'CONFIRMED', 3495000.00, 'VND', '2026-07-27 20:00:00', NULL, '2026-07-27 20:07:00', NULL, 113),
(144, 'PNR144', 14, 145, 'CONFIRMED', 2180000.00, 'VND', '2026-07-24 14:30:00', NULL, '2026-07-24 14:38:00', NULL, 114),
(145, 'PNR145', 94, 146, 'CONFIRMED', 2470000.00, 'VND', '2026-07-28 09:45:00', NULL, '2026-07-28 09:54:00', NULL, 115),
(146, 'PNR146', 44, 147, 'CONFIRMED', 1120000.00, 'VND', '2026-07-28 13:30:00', NULL, '2026-07-28 13:34:00', NULL, 116),
(147, 'PNR147', 77, 148, 'CONFIRMED', 3000000.00, 'VND', '2026-07-26 14:30:00', NULL, '2026-07-26 14:38:00', NULL, 117),
(148, 'PNR148', 34, 149, 'CONFIRMED', 2410000.00, 'VND', '2026-07-27 10:15:00', NULL, '2026-07-27 10:22:00', NULL, 118),
(149, 'PNR149', 30, 150, 'CONFIRMED', 2505000.00, 'VND', '2026-07-24 10:30:00', NULL, '2026-07-24 10:40:00', NULL, 119),
(150, 'PNR150', 59, 151, 'CONFIRMED', 2115000.00, 'VND', '2026-07-25 06:00:00', NULL, '2026-07-25 06:06:00', NULL, 120);

INSERT INTO booking_item (id, booking_id, seat_no, passenger_name, passenger_passport, price) VALUES
(1, 1, '14D', 'Bùi Đức Tuấn', 'B3247476', 1620000.00),
(2, 2, '10C', 'Hồ Hồng Tuấn', 'B2651603', 1410000.00),
(3, 3, '9C', 'Hồ Xuân Vy', 'B7396478', 1070000.00),
(4, 4, '23F', 'Hồ Thanh Hương', 'B3240176', 2280000.00),
(5, 5, '25C', 'Lê Kim Minh', 'B2513843', 1150000.00),
(6, 6, '8D', 'Nguyễn Xuân Hùng', 'B9760775', 2250000.00),
(7, 7, '10A', 'Đặng Quốc Quân', 'B2686735', 2000000.00),
(8, 8, '5F', 'Võ Văn Phúc', 'B5827489', 1110000.00),
(9, 9, '18B', 'Hồ Hữu Huy', 'B8315642', 1140000.00),
(10, 10, '10F', 'Ngô Hữu Nam', 'B2975632', 1570000.00),
(11, 11, '12C', 'Trần Trọng Tú', 'B2730598', 1870000.00),
(12, 12, '19F', 'Ngô Hồng Minh', 'B2141797', 2340000.00),
(13, 13, '17B', 'Bùi Hữu Vy', 'B8613294', 1370000.00),
(14, 14, '16B', 'Trần Minh Hùng', 'B4617197', 2130000.00),
(15, 15, '19A', 'Phan Trọng Thảo', 'B9115975', 1150000.00),
(16, 16, '14D', 'Trần Minh Hùng', 'B4617197', 1740000.00),
(17, 17, '25D', 'Lý Trọng An', 'B7950690', 920000.00),
(18, 18, '4F', 'Lý Thanh Nam', 'B7008253', 2120000.00),
(19, 19, '20A', 'Đặng Xuân Hải', 'B1483942', 2500000.00),
(20, 20, '7A', 'Nguyễn Ngọc Phương', 'B8809324', 1190000.00),
(21, 21, '11F', 'Phan Trọng Thảo', 'B9115975', 890000.00),
(22, 22, '9E', 'Hồ Hữu Phúc', 'B9096760', 880000.00),
(23, 23, '24D', 'Lý Trọng Vy', 'B8786657', 1690000.00),
(24, 24, '12E', 'Huỳnh Thanh Thảo', 'B5143004', 1750000.00),
(25, 25, '16C', 'Bùi Đức Nam', 'B1845166', 2460000.00),
(26, 26, '9F', 'Dương Trọng Khang', 'B7006141', 1180000.00),
(27, 27, '9B', 'Ngô Quốc Yến', 'B6248790', 1460000.00),
(28, 28, '19E', 'Đặng Thị Sơn', 'B9032237', 2340000.00),
(29, 29, '10F', 'Trần Kim Cường', 'B3002492', 2250000.00),
(30, 30, '20E', 'Nguyễn Kim Giang', 'B1400961', 1220000.00),
(31, 31, '9B', 'Hồ Xuân Vy', 'B7396478', 2450000.00),
(32, 32, '4B', 'Võ Trọng Đạt', 'B1465987', 2340000.00),
(33, 33, '11B', 'Lê Xuân Tuấn', 'B3182290', 2490000.00),
(34, 34, '22F', 'Trần Kim Cường', 'B3002492', 2410000.00),
(35, 35, '9D', 'Phan Văn Trang', 'B5935891', 850000.00),
(36, 36, '13A', 'Hồ Xuân Giang', 'B7317320', 1620000.00),
(37, 37, '5A', 'Bùi Kim Trang', 'B6651573', 1670000.00),
(38, 38, '9C', 'Võ Trọng Đạt', 'B1465987', 1820000.00),
(39, 39, '17E', 'Bùi Kim Đạt', 'B3555640', 2360000.00),
(40, 40, '24C', 'Lê Đức Bình', 'B5301463', 2270000.00),
(41, 41, '18A', 'Huỳnh Thị Nam', 'B8888427', 2440000.00),
(42, 42, '10F', 'Phan Xuân Tú', 'B1256117', 1050000.00),
(43, 43, '16E', 'Huỳnh Thanh Thảo', 'B5143004', 1730000.00),
(44, 44, '5D', 'Ngô Hữu Yến', 'B2837550', 1460000.00),
(45, 45, '12D', 'Phan Trọng Thảo', 'B9115975', 1460000.00),
(46, 46, '5E', 'Đặng Thị Sơn', 'B5408560', 1330000.00),
(47, 47, '13B', 'Đặng Xuân Hải', 'B1483942', 820000.00),
(48, 48, '24A', 'Lý Trọng An', 'B7950690', 1680000.00),
(49, 49, '12D', 'Ngô Quốc Yến', 'B6248790', 1710000.00),
(50, 50, '9B', 'Nguyễn Xuân Hùng', 'B9760775', 900000.00),
(51, 51, '11E', 'Nguyễn Ngọc Đạt', 'B2563140', 2440000.00),
(52, 52, '24A', 'Nguyễn Đức Giang', 'B6169169', 1800000.00),
(53, 53, '17C', 'Phan Thị Tuấn', 'B5949035', 1130000.00),
(54, 54, '5A', 'Ngô Hữu Nam', 'B2975632', 1290000.00),
(55, 55, '19D', 'Ngô Thanh Đạt', 'B1745642', 1300000.00),
(56, 56, '13A', 'Võ Đức Vinh', 'B2473699', 2360000.00),
(57, 57, '19E', 'Lê Đức Bình', 'B5301463', 2250000.00),
(58, 58, '23E', 'Ngô Ngọc Hải', 'B2401246', 1470000.00),
(59, 59, '17E', 'Nguyễn Quốc Lâm', 'B8066955', 950000.00),
(60, 60, '20C', 'Phan Thị Hải', 'B3394570', 1580000.00),
(61, 61, '24A', 'Lý Trọng Vy', 'B8786657', 2350000.00),
(62, 62, '18A', 'Dương Trọng Khang', 'B7006141', 2100000.00),
(63, 63, '22C', 'Dương Kim Bình', 'B9097962', 880000.00),
(64, 64, '17D', 'Lê Đức Bình', 'B5301463', 1130000.00),
(65, 65, '1B', 'Bùi Hữu Sơn', 'B7168932', 2790000.00),
(66, 66, '27E', 'Hoàng Đức Phương', 'B3072521', 1900000.00),
(67, 67, '9E', 'Đặng Minh Nam', 'B6865771', 990000.00),
(68, 68, '26A', 'Bùi Đức Tuấn', 'B3247476', 1250000.00),
(69, 69, '13F', 'Đặng Minh Nam', 'B6865771', 2360000.00),
(70, 70, '28C', 'Vũ Thị Lâm', 'B6326938', 1290000.00),
(71, 71, '14A', 'Võ Trọng Đạt', 'B1465987', 880000.00),
(72, 72, '9A', 'Hoàng Đức Phương', 'B3072521', 880000.00),
(73, 73, '18F', 'Lê Đức Bình', 'B5301463', 1460000.00),
(74, 74, '28A', 'Dương Trọng Khang', 'B7006141', 1890000.00),
(75, 75, '23E', 'Phan Trọng Đạt', 'B7044883', 2320000.00),
(76, 76, '1C', 'Võ Trọng Đạt', 'B1465987', 2100000.00),
(77, 77, '23A', 'Hồ Hồng Tuấn', 'B2651603', 1600000.00),
(78, 78, '25F', 'Huỳnh Quốc An', 'B5988794', 1190000.00),
(79, 79, '20E', 'Hoàng Trọng Sơn', 'B6310659', 970000.00),
(80, 80, '1C', 'Lý Trọng An', 'B7950690', 2730000.00),
(81, 81, '13F', 'Đặng Thị Sơn', 'B5408560', 1200000.00),
(82, 82, '15D', 'Huỳnh Văn Đạt', 'B7043353', 1420000.00),
(83, 83, '3B', 'Võ Đức Vinh', 'B2473699', 3630000.00),
(84, 84, '11A', 'Đặng Thị Sơn', 'B9032237', 1850000.00),
(85, 85, '15B', 'Lê Kim Minh', 'B2513843', 1910000.00),
(86, 86, '10C', 'Nguyễn Đức Cường', 'B4642384', 2040000.00),
(87, 87, '19A', 'Hoàng Quốc Dũng', 'B3201887', 2430000.00),
(88, 88, '26C', 'Dương Thanh Lâm', 'B4625703', 1350000.00),
(89, 89, '19F', 'Phan Xuân Tú', 'B1256117', 2090000.00),
(90, 90, '28D', 'Vũ Thị Trang', 'B3131803', 2460000.00),
(91, 91, '20A', 'Huỳnh Quốc An', 'B5988794', 1010000.00),
(92, 92, '6F', 'Dương Thanh Lâm', 'B4625703', 1220000.00),
(93, 93, '22B', 'Vũ Thị Trang', 'B3131803', 1230000.00),
(94, 94, '17F', 'Võ Xuân Linh', 'B7264394', 1040000.00),
(95, 95, '3D', 'Phạm Văn Hải', 'B5439425', 3690000.00),
(96, 96, '8B', 'Ngô Thanh Đạt', 'B1745642', 2430000.00),
(97, 97, '9F', 'Phan Thị Hải', 'B3394570', 2370000.00),
(98, 98, '12E', 'Lê Ngọc Hương', 'B3916873', 1920000.00),
(99, 99, '28F', 'Hồ Xuân Vy', 'B7396478', 1620000.00),
(100, 100, '26E', 'Hồ Thanh Hương', 'B3240176', 1160000.00),
(101, 101, '3F', 'Phạm Văn Hải', 'B5439425', 2175000.00),
(102, 102, '12B', 'Hoàng Minh Minh', 'B3561080', 2030000.00),
(103, 103, '14F', 'Huỳnh Quốc An', 'B5988794', 1650000.00),
(104, 104, '25D', 'Hồ Hồng Khang', 'B1609044', 2260000.00),
(105, 105, '16D', 'Hồ Hồng Tuấn', 'B2651603', 1690000.00),
(106, 106, '22E', 'Trần Trọng Tú', 'B2730598', 1270000.00),
(107, 107, '23A', 'Huỳnh Thị Phương', 'B5172153', 1140000.00),
(108, 108, '7A', 'Bùi Hữu Vy', 'B8613294', 2300000.00),
(109, 109, '24A', 'Phan Thị Vinh', 'B5819487', 2300000.00),
(110, 110, '14F', 'Nguyễn Kim Phúc', 'B8894300', 1500000.00),
(111, 111, '26E', 'Hồ Hữu Huy', 'B8315642', 2340000.00),
(112, 112, '25E', 'Đặng Xuân Hải', 'B1483942', 1500000.00),
(113, 113, '28A', 'Hồ Hữu Huy', 'B8315642', 1290000.00),
(114, 114, '19D', 'Hoàng Minh Minh', 'B3561080', 2100000.00),
(115, 115, '18E', 'Bùi Đức Nam', 'B1845166', 1410000.00),
(116, 116, '19F', 'Phạm Văn Hải', 'B5439425', 2110000.00),
(117, 117, '19A', 'Nguyễn Đức Giang', 'B6169169', 1550000.00),
(118, 118, '23A', 'Ngô Ngọc Hải', 'B2401246', 2270000.00),
(119, 119, '2B', 'Võ Đức Vinh', 'B2473699', 3405000.00),
(120, 120, '20E', 'Hoàng Hồng Yến', 'B2715578', 2130000.00),
(121, 121, '8C', 'Bùi Minh Dũng', 'B3712648', 1690000.00),
(122, 122, '19E', 'Vũ Thị Lâm', 'B6326938', 2380000.00),
(123, 123, '17C', 'Ngô Hữu Yến', 'B2837550', 2500000.00),
(124, 124, '4B', 'Lê Xuân Tuấn', 'B3182290', 1750000.00),
(125, 125, '1A', 'Phan Thị Hải', 'B3394570', 1395000.00),
(126, 126, '23B', 'Huỳnh Thị Nam', 'B8888427', 1130000.00),
(127, 127, '16A', 'Huỳnh Quốc Khang', 'B7707126', 1130000.00),
(128, 128, '7B', 'Nguyễn Kim Giang', 'B1400961', 1810000.00),
(129, 129, '13E', 'Ngô Đức Bình', 'B4679012', 2130000.00),
(130, 130, '4E', 'Bùi Đức Tuấn', 'B3247476', 1070000.00),
(131, 131, '23C', 'Nguyễn Kim Phúc', 'B8894300', 820000.00),
(132, 132, '24B', 'Nguyễn Ngọc Phương', 'B8809324', 1370000.00),
(133, 133, '2A', 'Trần Trọng Tú', 'B2730598', 2565000.00),
(134, 134, '4F', 'Huỳnh Thị Phương', 'B5172153', 1050000.00),
(135, 135, '17D', 'Dương Thanh Lâm', 'B4625703', 1240000.00),
(136, 136, '17D', 'Phan Thị Vinh', 'B5819487', 2050000.00),
(137, 137, '28F', 'Phan Thị Tuấn', 'B5949035', 1980000.00),
(138, 138, '28F', 'Huỳnh Thị Nam', 'B8888427', 960000.00),
(139, 139, '28E', 'Dương Trọng Khang', 'B7006141', 1620000.00),
(140, 140, '28A', 'Đặng Minh Khang', 'B7072128', 1410000.00),
(141, 141, '2F', 'Ngô Hồng Minh', 'B2141797', 1515000.00),
(142, 142, '15C', 'Đặng Ngọc Huy', 'B8136575', 1420000.00),
(143, 143, '2A', 'Nguyễn Đức Cường', 'B4642384', 3495000.00),
(144, 144, '20F', 'Huỳnh Văn Đạt', 'B7043353', 2180000.00),
(145, 145, '5A', 'Phan Thị Tuấn', 'B5949035', 2470000.00),
(146, 146, '6F', 'Đặng Ngọc Huy', 'B8136575', 1120000.00),
(147, 147, '2F', 'Dương Kim Bình', 'B9097962', 3000000.00),
(148, 148, '17E', 'Hoàng Trọng Sơn', 'B6310659', 2410000.00),
(149, 149, '1D', 'Nguyễn Ngọc Phương', 'B8809324', 2505000.00),
(150, 150, '2E', 'Vũ Ngọc Giang', 'B9860293', 2115000.00);

SELECT setval('booking_id_seq', (SELECT MAX(id) FROM booking));
SELECT setval('booking_item_id_seq', (SELECT MAX(id) FROM booking_item));
COMMIT;

-- 4. SEED DATA FOR PAYMENT SERVICE (payment_db)
\c payment_db
BEGIN;
DELETE FROM refund;
DELETE FROM transaction;
DELETE FROM payment;

INSERT INTO payment (id, payment_code, booking_id, user_id, amount, currency, method, status, idempotency_key, created_at, completed_at, gateway, transfer_code, reference_code, expires_at, failure_reason) VALUES
(1, 'PMT00000001', 31, 90, 2450000.00, 'VND', 'BANK_TRANSFER', 'FAILED', 'IK-001', '2026-07-15 08:19:00', NULL, 'SEPAY', NULL, 'REF00001', NULL, 'Giao dịch hết hạn (Timeout)'),
(2, 'PMT00000002', 32, 15, 2340000.00, 'VND', 'BANK_TRANSFER', 'FAILED', 'IK-002', '2026-07-15 08:34:00', NULL, 'MOCK', NULL, 'REF00002', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(3, 'PMT00000003', 33, 84, 2490000.00, 'VND', 'WALLET', 'FAILED', 'IK-003', '2026-07-15 08:46:00', NULL, 'SEPAY', NULL, 'REF00003', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(4, 'PMT00000004', 34, 50, 2410000.00, 'VND', 'WALLET', 'FAILED', 'IK-004', '2026-07-15 08:28:00', NULL, 'MOCK', NULL, 'REF00004', NULL, 'Giao dịch bị từ chối bởi ngân hàng phát hành (05)'),
(5, 'PMT00000005', 35, 58, 850000.00, 'VND', 'WALLET', 'FAILED', 'IK-005', '2026-07-15 08:42:00', NULL, 'SEPAY', NULL, 'REF00005', NULL, 'Giao dịch hết hạn (Timeout)'),
(6, 'PMT00000006', 36, 27, 1620000.00, 'VND', 'WALLET', 'FAILED', 'IK-006', '2026-07-15 08:34:00', NULL, 'SEPAY', NULL, 'REF00006', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(7, 'PMT00000007', 37, 85, 1670000.00, 'VND', 'BANK_TRANSFER', 'FAILED', 'IK-007', '2026-07-15 08:39:00', NULL, 'SEPAY', NULL, 'REF00007', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(8, 'PMT00000008', 38, 15, 1820000.00, 'VND', 'WALLET', 'FAILED', 'IK-008', '2026-07-15 08:17:00', NULL, 'SEPAY', NULL, 'REF00008', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(9, 'PMT00000009', 39, 66, 2360000.00, 'VND', 'CARD', 'FAILED', 'IK-009', '2026-07-15 08:20:00', NULL, 'MOCK', NULL, 'REF00009', NULL, 'Giao dịch bị từ chối bởi ngân hàng phát hành (05)'),
(10, 'PMT00000010', 40, 11, 2270000.00, 'VND', 'BANK_TRANSFER', 'FAILED', 'IK-010', '2026-07-15 08:27:00', NULL, 'SEPAY', NULL, 'REF00010', NULL, 'Số dư ví điện tử không đủ để thanh toán'),
(11, 'PMT00000011', 41, 87, 2440000.00, 'VND', 'CARD', 'SUCCESS', 'IK-011', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00011', NULL, NULL),
(12, 'PMT00000012', 42, 33, 1050000.00, 'VND', 'CARD', 'SUCCESS', 'IK-012', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00012', NULL, NULL),
(13, 'PMT00000013', 43, 28, 1730000.00, 'VND', 'CARD', 'SUCCESS', 'IK-013', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00013', NULL, NULL),
(14, 'PMT00000014', 44, 61, 1460000.00, 'VND', 'CARD', 'SUCCESS', 'IK-014', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00014', NULL, NULL),
(15, 'PMT00000015', 45, 72, 1460000.00, 'VND', 'CARD', 'SUCCESS', 'IK-015', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00015', NULL, NULL),
(16, 'PMT00000016', 46, 31, 1330000.00, 'VND', 'CARD', 'SUCCESS', 'IK-016', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00016', NULL, NULL),
(17, 'PMT00000017', 47, 19, 820000.00, 'VND', 'CARD', 'SUCCESS', 'IK-017', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00017', NULL, NULL),
(18, 'PMT00000018', 48, 69, 1680000.00, 'VND', 'CARD', 'SUCCESS', 'IK-018', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00018', NULL, NULL),
(19, 'PMT00000019', 49, 80, 1710000.00, 'VND', 'CARD', 'SUCCESS', 'IK-019', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00019', NULL, NULL),
(20, 'PMT00000020', 50, 23, 900000.00, 'VND', 'CARD', 'SUCCESS', 'IK-020', '2026-07-13 09:00:00', '2026-07-13 09:05:00', 'MOCK', NULL, 'REF00020', NULL, NULL),
(21, 'PMT00000021', 51, 29, 2440000.00, 'VND', 'CARD', 'SUCCESS', 'IK-021', '2026-07-14 00:30:00', '2026-07-14 00:36:00', 'MOCK', NULL, 'REF00021', NULL, NULL),
(22, 'PMT00000022', 52, 100, 1800000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-022', '2026-07-17 18:30:00', '2026-07-17 18:36:00', 'SEPAY', 'ABS10022', 'REF00022', NULL, NULL),
(23, 'PMT00000023', 53, 94, 1130000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-023', '2026-07-16 07:00:00', '2026-07-16 07:10:00', 'SEPAY', 'ABS10023', 'REF00023', NULL, NULL),
(24, 'PMT00000024', 54, 82, 1290000.00, 'VND', 'CARD', 'SUCCESS', 'IK-024', '2026-07-13 02:00:00', '2026-07-13 02:04:00', 'MOCK', NULL, 'REF00024', NULL, NULL),
(25, 'PMT00000025', 55, 46, 1300000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-025', '2026-07-14 23:00:00', '2026-07-14 23:09:00', 'SEPAY', 'ABS10025', 'REF00025', NULL, NULL),
(26, 'PMT00000026', 56, 26, 2360000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-026', '2026-07-16 14:30:00', '2026-07-16 14:36:00', 'SEPAY', 'ABS10026', 'REF00026', NULL, NULL),
(27, 'PMT00000027', 57, 11, 2250000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-027', '2026-07-16 03:15:00', '2026-07-16 03:22:00', 'SEPAY', 'ABS10027', 'REF00027', NULL, NULL),
(28, 'PMT00000028', 58, 37, 1470000.00, 'VND', 'CARD', 'SUCCESS', 'IK-028', '2026-07-14 21:15:00', '2026-07-14 21:24:00', 'MOCK', NULL, 'REF00028', NULL, NULL),
(29, 'PMT00000029', 59, 93, 950000.00, 'VND', 'CARD', 'SUCCESS', 'IK-029', '2026-07-18 04:00:00', '2026-07-18 04:06:00', 'MOCK', NULL, 'REF00029', NULL, NULL),
(30, 'PMT00000030', 60, 97, 1580000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-030', '2026-07-18 12:30:00', '2026-07-18 12:33:00', 'SEPAY', 'ABS10030', 'REF00030', NULL, NULL),
(31, 'PMT00000031', 61, 52, 2350000.00, 'VND', 'CARD', 'SUCCESS', 'IK-031', '2026-07-17 04:15:00', '2026-07-17 04:21:00', 'MOCK', NULL, 'REF00031', NULL, NULL),
(32, 'PMT00000032', 62, 65, 2100000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-032', '2026-07-17 03:45:00', '2026-07-17 03:54:00', 'SEPAY', 'ABS10032', 'REF00032', NULL, NULL),
(33, 'PMT00000033', 63, 77, 880000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-033', '2026-07-14 23:45:00', '2026-07-14 23:52:00', 'SEPAY', 'ABS10033', 'REF00033', NULL, NULL),
(34, 'PMT00000034', 64, 11, 1130000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-034', '2026-07-16 22:30:00', '2026-07-16 22:40:00', 'SEPAY', 'ABS10034', 'REF00034', NULL, NULL),
(35, 'PMT00000035', 65, 43, 2790000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-035', '2026-07-15 09:00:00', '2026-07-15 09:08:00', 'SEPAY', 'ABS10035', 'REF00035', NULL, NULL),
(36, 'PMT00000036', 66, 25, 1900000.00, 'VND', 'CARD', 'SUCCESS', 'IK-036', '2026-07-18 12:30:00', '2026-07-18 12:34:00', 'MOCK', NULL, 'REF00036', NULL, NULL),
(37, 'PMT00000037', 67, 42, 990000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-037', '2026-07-18 21:30:00', '2026-07-18 21:38:00', 'SEPAY', 'ABS10037', 'REF00037', NULL, NULL),
(38, 'PMT00000038', 68, 92, 1250000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-038', '2026-07-15 05:00:00', '2026-07-15 05:06:00', 'SEPAY', 'ABS10038', 'REF00038', NULL, NULL),
(39, 'PMT00000039', 69, 42, 2360000.00, 'VND', 'CARD', 'SUCCESS', 'IK-039', '2026-07-16 14:00:00', '2026-07-16 14:09:00', 'MOCK', NULL, 'REF00039', NULL, NULL),
(40, 'PMT00000040', 70, 91, 1290000.00, 'VND', 'CARD', 'SUCCESS', 'IK-040', '2026-07-16 07:45:00', '2026-07-16 07:49:00', 'MOCK', NULL, 'REF00040', NULL, NULL),
(41, 'PMT00000041', 71, 15, 880000.00, 'VND', 'CARD', 'SUCCESS', 'IK-041', '2026-07-15 14:15:00', '2026-07-15 14:24:00', 'MOCK', NULL, 'REF00041', NULL, NULL),
(42, 'PMT00000042', 72, 25, 880000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-042', '2026-07-15 05:45:00', '2026-07-15 05:51:00', 'SEPAY', 'ABS10042', 'REF00042', NULL, NULL),
(43, 'PMT00000043', 73, 11, 1460000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-043', '2026-07-19 08:30:00', '2026-07-19 08:38:00', 'SEPAY', 'ABS10043', 'REF00043', NULL, NULL),
(44, 'PMT00000044', 74, 65, 1890000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-044', '2026-07-19 14:45:00', '2026-07-19 14:53:00', 'SEPAY', 'ABS10044', 'REF00044', NULL, NULL),
(45, 'PMT00000045', 75, 57, 2320000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-045', '2026-07-16 07:30:00', '2026-07-16 07:34:00', 'SEPAY', 'ABS10045', 'REF00045', NULL, NULL),
(46, 'PMT00000046', 76, 15, 2100000.00, 'VND', 'CARD', 'SUCCESS', 'IK-046', '2026-07-20 11:45:00', '2026-07-20 11:48:00', 'MOCK', NULL, 'REF00046', NULL, NULL),
(47, 'PMT00000047', 77, 67, 1600000.00, 'VND', 'CARD', 'SUCCESS', 'IK-047', '2026-07-20 15:30:00', '2026-07-20 15:34:00', 'MOCK', NULL, 'REF00047', NULL, NULL),
(48, 'PMT00000048', 78, 88, 1190000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-048', '2026-07-18 11:45:00', '2026-07-18 11:53:00', 'SEPAY', 'ABS10048', 'REF00048', NULL, NULL),
(49, 'PMT00000049', 79, 34, 970000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-049', '2026-07-17 19:45:00', '2026-07-17 19:52:00', 'SEPAY', 'ABS10049', 'REF00049', NULL, NULL),
(50, 'PMT00000050', 80, 69, 2730000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-050', '2026-07-16 15:15:00', '2026-07-16 15:24:00', 'SEPAY', 'ABS10050', 'REF00050', NULL, NULL),
(51, 'PMT00000051', 81, 31, 1200000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-051', '2026-07-19 08:45:00', '2026-07-19 08:48:00', 'SEPAY', 'ABS10051', 'REF00051', NULL, NULL),
(52, 'PMT00000052', 82, 14, 1420000.00, 'VND', 'CARD', 'SUCCESS', 'IK-052', '2026-07-20 09:45:00', '2026-07-20 09:49:00', 'MOCK', NULL, 'REF00052', NULL, NULL),
(53, 'PMT00000053', 83, 26, 3630000.00, 'VND', 'CARD', 'SUCCESS', 'IK-053', '2026-07-19 07:30:00', '2026-07-19 07:34:00', 'MOCK', NULL, 'REF00053', NULL, NULL),
(54, 'PMT00000054', 84, 55, 1850000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-054', '2026-07-17 18:15:00', '2026-07-17 18:22:00', 'SEPAY', 'ABS10054', 'REF00054', NULL, NULL),
(55, 'PMT00000055', 85, 81, 1910000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-055', '2026-07-18 09:30:00', '2026-07-18 09:40:00', 'SEPAY', 'ABS10055', 'REF00055', NULL, NULL),
(56, 'PMT00000056', 86, 7, 2040000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-056', '2026-07-21 10:30:00', '2026-07-21 10:33:00', 'SEPAY', 'ABS10056', 'REF00056', NULL, NULL),
(57, 'PMT00000057', 87, 17, 2430000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-057', '2026-07-21 18:30:00', '2026-07-21 18:35:00', 'SEPAY', 'ABS10057', 'REF00057', NULL, NULL),
(58, 'PMT00000058', 88, 51, 1350000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-058', '2026-07-18 13:30:00', '2026-07-18 13:40:00', 'SEPAY', 'ABS10058', 'REF00058', NULL, NULL),
(59, 'PMT00000059', 89, 33, 2090000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-059', '2026-07-20 17:45:00', '2026-07-20 17:49:00', 'SEPAY', 'ABS10059', 'REF00059', NULL, NULL),
(60, 'PMT00000060', 90, 49, 2460000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-060', '2026-07-20 09:15:00', '2026-07-20 09:19:00', 'SEPAY', 'ABS10060', 'REF00060', NULL, NULL),
(61, 'PMT00000061', 91, 88, 1010000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-061', '2026-07-20 15:30:00', '2026-07-20 15:38:00', 'SEPAY', 'ABS10061', 'REF00061', NULL, NULL),
(62, 'PMT00000062', 92, 51, 1220000.00, 'VND', 'CARD', 'SUCCESS', 'IK-062', '2026-07-19 16:15:00', '2026-07-19 16:23:00', 'MOCK', NULL, 'REF00062', NULL, NULL),
(63, 'PMT00000063', 93, 49, 1230000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-063', '2026-07-22 15:30:00', '2026-07-22 15:38:00', 'SEPAY', 'ABS10063', 'REF00063', NULL, NULL),
(64, 'PMT00000064', 94, 56, 1040000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-064', '2026-07-20 08:00:00', '2026-07-20 08:07:00', 'SEPAY', 'ABS10064', 'REF00064', NULL, NULL),
(65, 'PMT00000065', 95, 48, 3690000.00, 'VND', 'CARD', 'SUCCESS', 'IK-065', '2026-07-21 09:00:00', '2026-07-21 09:06:00', 'MOCK', NULL, 'REF00065', NULL, NULL),
(66, 'PMT00000066', 96, 46, 2430000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-066', '2026-07-18 18:00:00', '2026-07-18 18:03:00', 'SEPAY', 'ABS10066', 'REF00066', NULL, NULL),
(67, 'PMT00000067', 97, 97, 2370000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-067', '2026-07-21 15:00:00', '2026-07-21 15:06:00', 'SEPAY', 'ABS10067', 'REF00067', NULL, NULL),
(68, 'PMT00000068', 98, 54, 1920000.00, 'VND', 'CARD', 'SUCCESS', 'IK-068', '2026-07-18 02:00:00', '2026-07-18 02:07:00', 'MOCK', NULL, 'REF00068', NULL, NULL),
(69, 'PMT00000069', 99, 90, 1620000.00, 'VND', 'CARD', 'SUCCESS', 'IK-069', '2026-07-21 12:45:00', '2026-07-21 12:52:00', 'MOCK', NULL, 'REF00069', NULL, NULL),
(70, 'PMT00000070', 100, 24, 1160000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-070', '2026-07-20 04:45:00', '2026-07-20 04:52:00', 'SEPAY', 'ABS10070', 'REF00070', NULL, NULL),
(71, 'PMT00000071', 101, 48, 2175000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-071', '2026-07-19 09:15:00', '2026-07-19 09:23:00', 'SEPAY', 'ABS10071', 'REF00071', NULL, NULL),
(72, 'PMT00000072', 102, 10, 2030000.00, 'VND', 'CARD', 'SUCCESS', 'IK-072', '2026-07-19 05:30:00', '2026-07-19 05:33:00', 'MOCK', NULL, 'REF00072', NULL, NULL),
(73, 'PMT00000073', 103, 88, 1650000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-073', '2026-07-21 12:15:00', '2026-07-21 12:25:00', 'SEPAY', 'ABS10073', 'REF00073', NULL, NULL),
(74, 'PMT00000074', 104, 74, 2260000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-074', '2026-07-23 18:30:00', '2026-07-23 18:40:00', 'SEPAY', 'ABS10074', 'REF00074', NULL, NULL),
(75, 'PMT00000075', 105, 67, 1690000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-075', '2026-07-21 06:15:00', '2026-07-21 06:20:00', 'SEPAY', 'ABS10075', 'REF00075', NULL, NULL),
(76, 'PMT00000076', 106, 76, 1270000.00, 'VND', 'CARD', 'SUCCESS', 'IK-076', '2026-07-19 04:00:00', '2026-07-19 04:03:00', 'MOCK', NULL, 'REF00076', NULL, NULL),
(77, 'PMT00000077', 107, 32, 1140000.00, 'VND', 'CARD', 'SUCCESS', 'IK-077', '2026-07-20 11:00:00', '2026-07-20 11:10:00', 'MOCK', NULL, 'REF00077', NULL, NULL),
(78, 'PMT00000078', 108, 63, 2300000.00, 'VND', 'CARD', 'SUCCESS', 'IK-078', '2026-07-23 18:30:00', '2026-07-23 18:37:00', 'MOCK', NULL, 'REF00078', NULL, NULL),
(79, 'PMT00000079', 109, 6, 2300000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-079', '2026-07-24 14:00:00', '2026-07-24 14:05:00', 'SEPAY', 'ABS10079', 'REF00079', NULL, NULL),
(80, 'PMT00000080', 110, 38, 1500000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-080', '2026-07-20 09:30:00', '2026-07-20 09:37:00', 'SEPAY', 'ABS10080', 'REF00080', NULL, NULL),
(81, 'PMT00000081', 111, 16, 2340000.00, 'VND', 'CARD', 'SUCCESS', 'IK-081', '2026-07-21 02:15:00', '2026-07-21 02:24:00', 'MOCK', NULL, 'REF00081', NULL, NULL),
(82, 'PMT00000082', 112, 19, 1500000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-082', '2026-07-24 16:15:00', '2026-07-24 16:23:00', 'SEPAY', 'ABS10082', 'REF00082', NULL, NULL),
(83, 'PMT00000083', 113, 16, 1290000.00, 'VND', 'CARD', 'SUCCESS', 'IK-083', '2026-07-21 11:00:00', '2026-07-21 11:03:00', 'MOCK', NULL, 'REF00083', NULL, NULL),
(84, 'PMT00000084', 114, 10, 2100000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-084', '2026-07-20 17:15:00', '2026-07-20 17:24:00', 'SEPAY', 'ABS10084', 'REF00084', NULL, NULL),
(85, 'PMT00000085', 115, 86, 1410000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-085', '2026-07-20 02:15:00', '2026-07-20 02:25:00', 'SEPAY', 'ABS10085', 'REF00085', NULL, NULL),
(86, 'PMT00000086', 116, 48, 2110000.00, 'VND', 'CARD', 'SUCCESS', 'IK-086', '2026-07-23 08:30:00', '2026-07-23 08:40:00', 'MOCK', NULL, 'REF00086', NULL, NULL),
(87, 'PMT00000087', 117, 100, 1550000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-087', '2026-07-25 03:45:00', '2026-07-25 03:55:00', 'SEPAY', 'ABS10087', 'REF00087', NULL, NULL),
(88, 'PMT00000088', 118, 37, 2270000.00, 'VND', 'CARD', 'SUCCESS', 'IK-088', '2026-07-21 09:30:00', '2026-07-21 09:35:00', 'MOCK', NULL, 'REF00088', NULL, NULL),
(89, 'PMT00000089', 119, 26, 3405000.00, 'VND', 'CARD', 'SUCCESS', 'IK-089', '2026-07-21 15:15:00', '2026-07-21 15:23:00', 'MOCK', NULL, 'REF00089', NULL, NULL),
(90, 'PMT00000090', 120, 47, 2130000.00, 'VND', 'CARD', 'SUCCESS', 'IK-090', '2026-07-22 17:30:00', '2026-07-22 17:38:00', 'MOCK', NULL, 'REF00090', NULL, NULL),
(91, 'PMT00000091', 121, 20, 1690000.00, 'VND', 'CARD', 'SUCCESS', 'IK-091', '2026-07-22 09:00:00', '2026-07-22 09:03:00', 'MOCK', NULL, 'REF00091', NULL, NULL),
(92, 'PMT00000092', 122, 91, 2380000.00, 'VND', 'CARD', 'SUCCESS', 'IK-092', '2026-07-21 16:45:00', '2026-07-21 16:50:00', 'MOCK', NULL, 'REF00092', NULL, NULL),
(93, 'PMT00000093', 123, 61, 2500000.00, 'VND', 'CARD', 'SUCCESS', 'IK-093', '2026-07-22 11:30:00', '2026-07-22 11:35:00', 'MOCK', NULL, 'REF00093', NULL, NULL),
(94, 'PMT00000094', 124, 84, 1750000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-094', '2026-07-24 06:30:00', '2026-07-24 06:34:00', 'SEPAY', 'ABS10094', 'REF00094', NULL, NULL),
(95, 'PMT00000095', 125, 97, 1395000.00, 'VND', 'CARD', 'SUCCESS', 'IK-095', '2026-07-22 08:30:00', '2026-07-22 08:33:00', 'MOCK', NULL, 'REF00095', NULL, NULL),
(96, 'PMT00000096', 126, 87, 1130000.00, 'VND', 'CARD', 'SUCCESS', 'IK-096', '2026-07-26 16:30:00', '2026-07-26 16:33:00', 'MOCK', NULL, 'REF00096', NULL, NULL),
(97, 'PMT00000097', 127, 98, 1130000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-097', '2026-07-25 05:15:00', '2026-07-25 05:22:00', 'SEPAY', 'ABS10097', 'REF00097', NULL, NULL),
(98, 'PMT00000098', 128, 78, 1810000.00, 'VND', 'CARD', 'SUCCESS', 'IK-098', '2026-07-25 06:00:00', '2026-07-25 06:08:00', 'MOCK', NULL, 'REF00098', NULL, NULL),
(99, 'PMT00000099', 129, 36, 2130000.00, 'VND', 'CARD', 'SUCCESS', 'IK-099', '2026-07-26 05:00:00', '2026-07-26 05:07:00', 'MOCK', NULL, 'REF00099', NULL, NULL),
(100, 'PMT00000100', 130, 92, 1070000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-100', '2026-07-24 06:15:00', '2026-07-24 06:23:00', 'SEPAY', 'ABS10100', 'REF00100', NULL, NULL),
(101, 'PMT00000101', 131, 38, 820000.00, 'VND', 'CARD', 'SUCCESS', 'IK-101', '2026-07-25 06:00:00', '2026-07-25 06:04:00', 'MOCK', NULL, 'REF00101', NULL, NULL),
(102, 'PMT00000102', 132, 30, 1370000.00, 'VND', 'CARD', 'SUCCESS', 'IK-102', '2026-07-22 01:45:00', '2026-07-22 01:50:00', 'MOCK', NULL, 'REF00102', NULL, NULL),
(103, 'PMT00000103', 133, 76, 2565000.00, 'VND', 'CARD', 'SUCCESS', 'IK-103', '2026-07-23 11:15:00', '2026-07-23 11:23:00', 'MOCK', NULL, 'REF00103', NULL, NULL),
(104, 'PMT00000104', 134, 32, 1050000.00, 'VND', 'CARD', 'SUCCESS', 'IK-104', '2026-07-24 02:30:00', '2026-07-24 02:37:00', 'MOCK', NULL, 'REF00104', NULL, NULL),
(105, 'PMT00000105', 135, 51, 1240000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-105', '2026-07-27 11:00:00', '2026-07-27 11:06:00', 'SEPAY', 'ABS10105', 'REF00105', NULL, NULL),
(106, 'PMT00000106', 136, 6, 2050000.00, 'VND', 'CARD', 'SUCCESS', 'IK-106', '2026-07-27 06:30:00', '2026-07-27 06:36:00', 'MOCK', NULL, 'REF00106', NULL, NULL),
(107, 'PMT00000107', 137, 94, 1980000.00, 'VND', 'CARD', 'SUCCESS', 'IK-107', '2026-07-22 22:45:00', '2026-07-22 22:55:00', 'MOCK', NULL, 'REF00107', NULL, NULL),
(108, 'PMT00000108', 138, 87, 960000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-108', '2026-07-26 05:30:00', '2026-07-26 05:34:00', 'SEPAY', 'ABS10108', 'REF00108', NULL, NULL),
(109, 'PMT00000109', 139, 65, 1620000.00, 'VND', 'CARD', 'SUCCESS', 'IK-109', '2026-07-24 10:45:00', '2026-07-24 10:48:00', 'MOCK', NULL, 'REF00109', NULL, NULL),
(110, 'PMT00000110', 140, 96, 1410000.00, 'VND', 'CARD', 'SUCCESS', 'IK-110', '2026-07-27 15:15:00', '2026-07-27 15:24:00', 'MOCK', NULL, 'REF00110', NULL, NULL),
(111, 'PMT00000111', 141, 13, 1515000.00, 'VND', 'CARD', 'SUCCESS', 'IK-111', '2026-07-26 15:45:00', '2026-07-26 15:50:00', 'MOCK', NULL, 'REF00111', NULL, NULL),
(112, 'PMT00000112', 142, 44, 1420000.00, 'VND', 'CARD', 'SUCCESS', 'IK-112', '2026-07-24 09:15:00', '2026-07-24 09:25:00', 'MOCK', NULL, 'REF00112', NULL, NULL),
(113, 'PMT00000113', 143, 7, 3495000.00, 'VND', 'CARD', 'SUCCESS', 'IK-113', '2026-07-27 20:00:00', '2026-07-27 20:07:00', 'MOCK', NULL, 'REF00113', NULL, NULL),
(114, 'PMT00000114', 144, 14, 2180000.00, 'VND', 'CARD', 'SUCCESS', 'IK-114', '2026-07-24 14:30:00', '2026-07-24 14:38:00', 'MOCK', NULL, 'REF00114', NULL, NULL),
(115, 'PMT00000115', 145, 94, 2470000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-115', '2026-07-28 09:45:00', '2026-07-28 09:54:00', 'SEPAY', 'ABS10115', 'REF00115', NULL, NULL),
(116, 'PMT00000116', 146, 44, 1120000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-116', '2026-07-28 13:30:00', '2026-07-28 13:34:00', 'SEPAY', 'ABS10116', 'REF00116', NULL, NULL),
(117, 'PMT00000117', 147, 77, 3000000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-117', '2026-07-26 14:30:00', '2026-07-26 14:38:00', 'SEPAY', 'ABS10117', 'REF00117', NULL, NULL),
(118, 'PMT00000118', 148, 34, 2410000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-118', '2026-07-27 10:15:00', '2026-07-27 10:22:00', 'SEPAY', 'ABS10118', 'REF00118', NULL, NULL),
(119, 'PMT00000119', 149, 30, 2505000.00, 'VND', 'BANK_TRANSFER', 'SUCCESS', 'IK-119', '2026-07-24 10:30:00', '2026-07-24 10:40:00', 'SEPAY', 'ABS10119', 'REF00119', NULL, NULL),
(120, 'PMT00000120', 150, 59, 2115000.00, 'VND', 'CARD', 'SUCCESS', 'IK-120', '2026-07-25 06:00:00', '2026-07-25 06:06:00', 'MOCK', NULL, 'REF00120', NULL, NULL);

INSERT INTO transaction (id, payment_id, gateway_txn_id, gateway_response, status, created_at) VALUES
(1, 1, 'TXN-MOCK-001', '{"errorCode":"05","message":"Do not honor - Giao dịch hết hạn (Timeout)","amount":2450000}', 'FAILED', '2026-07-15 08:19:00'),
(2, 2, 'TXN-MOCK-002', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":2340000}', 'FAILED', '2026-07-15 08:34:00'),
(3, 3, 'TXN-MOCK-003', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":2490000}', 'FAILED', '2026-07-15 08:46:00'),
(4, 4, 'TXN-MOCK-004', '{"errorCode":"05","message":"Do not honor - Giao dịch bị từ chối bởi ngân hàng phát hành (05)","amount":2410000}', 'FAILED', '2026-07-15 08:28:00'),
(5, 5, 'TXN-MOCK-005', '{"errorCode":"05","message":"Do not honor - Giao dịch hết hạn (Timeout)","amount":850000}', 'FAILED', '2026-07-15 08:42:00'),
(6, 6, 'TXN-MOCK-006', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":1620000}', 'FAILED', '2026-07-15 08:34:00'),
(7, 7, 'TXN-MOCK-007', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":1670000}', 'FAILED', '2026-07-15 08:39:00'),
(8, 8, 'TXN-MOCK-008', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":1820000}', 'FAILED', '2026-07-15 08:17:00'),
(9, 9, 'TXN-MOCK-009', '{"errorCode":"05","message":"Do not honor - Giao dịch bị từ chối bởi ngân hàng phát hành (05)","amount":2360000}', 'FAILED', '2026-07-15 08:20:00'),
(10, 10, 'TXN-MOCK-010', '{"errorCode":"05","message":"Do not honor - Số dư ví điện tử không đủ để thanh toán","amount":2270000}', 'FAILED', '2026-07-15 08:27:00'),
(11, 11, 'TXN-MOCK-011', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345611"}', 'SUCCESS', '2026-07-13 09:05:00'),
(12, 12, 'TXN-MOCK-012', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345612"}', 'SUCCESS', '2026-07-13 09:05:00'),
(13, 13, 'TXN-MOCK-013', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345613"}', 'SUCCESS', '2026-07-13 09:05:00'),
(14, 14, 'TXN-MOCK-014', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345614"}', 'SUCCESS', '2026-07-13 09:05:00'),
(15, 15, 'TXN-MOCK-015', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345615"}', 'SUCCESS', '2026-07-13 09:05:00'),
(16, 16, 'TXN-MOCK-016', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345616"}', 'SUCCESS', '2026-07-13 09:05:00'),
(17, 17, 'TXN-MOCK-017', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345617"}', 'SUCCESS', '2026-07-13 09:05:00'),
(18, 18, 'TXN-MOCK-018', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345618"}', 'SUCCESS', '2026-07-13 09:05:00'),
(19, 19, 'TXN-MOCK-019', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345619"}', 'SUCCESS', '2026-07-13 09:05:00'),
(20, 20, 'TXN-MOCK-020', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345620"}', 'SUCCESS', '2026-07-13 09:05:00'),
(21, 21, 'TXN-MOCK-021', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345621"}', 'SUCCESS', '2026-07-14 00:36:00'),
(22, 22, 'TXN-SEPAY-022', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345622"}', 'SUCCESS', '2026-07-17 18:36:00'),
(23, 23, 'TXN-SEPAY-023', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345623"}', 'SUCCESS', '2026-07-16 07:10:00'),
(24, 24, 'TXN-MOCK-024', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345624"}', 'SUCCESS', '2026-07-13 02:04:00'),
(25, 25, 'TXN-SEPAY-025', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345625"}', 'SUCCESS', '2026-07-14 23:09:00'),
(26, 26, 'TXN-SEPAY-026', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345626"}', 'SUCCESS', '2026-07-16 14:36:00'),
(27, 27, 'TXN-SEPAY-027', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345627"}', 'SUCCESS', '2026-07-16 03:22:00'),
(28, 28, 'TXN-MOCK-028', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345628"}', 'SUCCESS', '2026-07-14 21:24:00'),
(29, 29, 'TXN-MOCK-029', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345629"}', 'SUCCESS', '2026-07-18 04:06:00'),
(30, 30, 'TXN-SEPAY-030', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345630"}', 'SUCCESS', '2026-07-18 12:33:00'),
(31, 31, 'TXN-MOCK-031', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345631"}', 'SUCCESS', '2026-07-17 04:21:00'),
(32, 32, 'TXN-SEPAY-032', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345632"}', 'SUCCESS', '2026-07-17 03:54:00'),
(33, 33, 'TXN-SEPAY-033', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345633"}', 'SUCCESS', '2026-07-14 23:52:00'),
(34, 34, 'TXN-SEPAY-034', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345634"}', 'SUCCESS', '2026-07-16 22:40:00'),
(35, 35, 'TXN-SEPAY-035', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345635"}', 'SUCCESS', '2026-07-15 09:08:00'),
(36, 36, 'TXN-MOCK-036', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345636"}', 'SUCCESS', '2026-07-18 12:34:00'),
(37, 37, 'TXN-SEPAY-037', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345637"}', 'SUCCESS', '2026-07-18 21:38:00'),
(38, 38, 'TXN-SEPAY-038', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345638"}', 'SUCCESS', '2026-07-15 05:06:00'),
(39, 39, 'TXN-MOCK-039', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345639"}', 'SUCCESS', '2026-07-16 14:09:00'),
(40, 40, 'TXN-MOCK-040', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345640"}', 'SUCCESS', '2026-07-16 07:49:00'),
(41, 41, 'TXN-MOCK-041', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345641"}', 'SUCCESS', '2026-07-15 14:24:00'),
(42, 42, 'TXN-SEPAY-042', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345642"}', 'SUCCESS', '2026-07-15 05:51:00'),
(43, 43, 'TXN-SEPAY-043', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345643"}', 'SUCCESS', '2026-07-19 08:38:00'),
(44, 44, 'TXN-SEPAY-044', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345644"}', 'SUCCESS', '2026-07-19 14:53:00'),
(45, 45, 'TXN-SEPAY-045', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345645"}', 'SUCCESS', '2026-07-16 07:34:00'),
(46, 46, 'TXN-MOCK-046', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345646"}', 'SUCCESS', '2026-07-20 11:48:00'),
(47, 47, 'TXN-MOCK-047', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345647"}', 'SUCCESS', '2026-07-20 15:34:00'),
(48, 48, 'TXN-SEPAY-048', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345648"}', 'SUCCESS', '2026-07-18 11:53:00'),
(49, 49, 'TXN-SEPAY-049', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345649"}', 'SUCCESS', '2026-07-17 19:52:00'),
(50, 50, 'TXN-SEPAY-050', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345650"}', 'SUCCESS', '2026-07-16 15:24:00'),
(51, 51, 'TXN-SEPAY-051', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345651"}', 'SUCCESS', '2026-07-19 08:48:00'),
(52, 52, 'TXN-MOCK-052', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345652"}', 'SUCCESS', '2026-07-20 09:49:00'),
(53, 53, 'TXN-MOCK-053', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345653"}', 'SUCCESS', '2026-07-19 07:34:00'),
(54, 54, 'TXN-SEPAY-054', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345654"}', 'SUCCESS', '2026-07-17 18:22:00'),
(55, 55, 'TXN-SEPAY-055', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345655"}', 'SUCCESS', '2026-07-18 09:40:00'),
(56, 56, 'TXN-SEPAY-056', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345656"}', 'SUCCESS', '2026-07-21 10:33:00'),
(57, 57, 'TXN-SEPAY-057', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345657"}', 'SUCCESS', '2026-07-21 18:35:00'),
(58, 58, 'TXN-SEPAY-058', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345658"}', 'SUCCESS', '2026-07-18 13:40:00'),
(59, 59, 'TXN-SEPAY-059', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345659"}', 'SUCCESS', '2026-07-20 17:49:00'),
(60, 60, 'TXN-SEPAY-060', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345660"}', 'SUCCESS', '2026-07-20 09:19:00'),
(61, 61, 'TXN-SEPAY-061', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345661"}', 'SUCCESS', '2026-07-20 15:38:00'),
(62, 62, 'TXN-MOCK-062', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345662"}', 'SUCCESS', '2026-07-19 16:23:00'),
(63, 63, 'TXN-SEPAY-063', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345663"}', 'SUCCESS', '2026-07-22 15:38:00'),
(64, 64, 'TXN-SEPAY-064', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345664"}', 'SUCCESS', '2026-07-20 08:07:00'),
(65, 65, 'TXN-MOCK-065', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345665"}', 'SUCCESS', '2026-07-21 09:06:00'),
(66, 66, 'TXN-SEPAY-066', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345666"}', 'SUCCESS', '2026-07-18 18:03:00'),
(67, 67, 'TXN-SEPAY-067', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345667"}', 'SUCCESS', '2026-07-21 15:06:00'),
(68, 68, 'TXN-MOCK-068', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345668"}', 'SUCCESS', '2026-07-18 02:07:00'),
(69, 69, 'TXN-MOCK-069', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345669"}', 'SUCCESS', '2026-07-21 12:52:00'),
(70, 70, 'TXN-SEPAY-070', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345670"}', 'SUCCESS', '2026-07-20 04:52:00'),
(71, 71, 'TXN-SEPAY-071', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345671"}', 'SUCCESS', '2026-07-19 09:23:00'),
(72, 72, 'TXN-MOCK-072', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345672"}', 'SUCCESS', '2026-07-19 05:33:00'),
(73, 73, 'TXN-SEPAY-073', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345673"}', 'SUCCESS', '2026-07-21 12:25:00'),
(74, 74, 'TXN-SEPAY-074', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345674"}', 'SUCCESS', '2026-07-23 18:40:00'),
(75, 75, 'TXN-SEPAY-075', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345675"}', 'SUCCESS', '2026-07-21 06:20:00'),
(76, 76, 'TXN-MOCK-076', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345676"}', 'SUCCESS', '2026-07-19 04:03:00'),
(77, 77, 'TXN-MOCK-077', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345677"}', 'SUCCESS', '2026-07-20 11:10:00'),
(78, 78, 'TXN-MOCK-078', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345678"}', 'SUCCESS', '2026-07-23 18:37:00'),
(79, 79, 'TXN-SEPAY-079', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345679"}', 'SUCCESS', '2026-07-24 14:05:00'),
(80, 80, 'TXN-SEPAY-080', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345680"}', 'SUCCESS', '2026-07-20 09:37:00'),
(81, 81, 'TXN-MOCK-081', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345681"}', 'SUCCESS', '2026-07-21 02:24:00'),
(82, 82, 'TXN-SEPAY-082', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345682"}', 'SUCCESS', '2026-07-24 16:23:00'),
(83, 83, 'TXN-MOCK-083', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345683"}', 'SUCCESS', '2026-07-21 11:03:00'),
(84, 84, 'TXN-SEPAY-084', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345684"}', 'SUCCESS', '2026-07-20 17:24:00'),
(85, 85, 'TXN-SEPAY-085', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345685"}', 'SUCCESS', '2026-07-20 02:25:00'),
(86, 86, 'TXN-MOCK-086', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345686"}', 'SUCCESS', '2026-07-23 08:40:00'),
(87, 87, 'TXN-SEPAY-087', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345687"}', 'SUCCESS', '2026-07-25 03:55:00'),
(88, 88, 'TXN-MOCK-088', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345688"}', 'SUCCESS', '2026-07-21 09:35:00'),
(89, 89, 'TXN-MOCK-089', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345689"}', 'SUCCESS', '2026-07-21 15:23:00'),
(90, 90, 'TXN-MOCK-090', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345690"}', 'SUCCESS', '2026-07-22 17:38:00'),
(91, 91, 'TXN-MOCK-091', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345691"}', 'SUCCESS', '2026-07-22 09:03:00'),
(92, 92, 'TXN-MOCK-092', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345692"}', 'SUCCESS', '2026-07-21 16:50:00'),
(93, 93, 'TXN-MOCK-093', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345693"}', 'SUCCESS', '2026-07-22 11:35:00'),
(94, 94, 'TXN-SEPAY-094', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345694"}', 'SUCCESS', '2026-07-24 06:34:00'),
(95, 95, 'TXN-MOCK-095', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345695"}', 'SUCCESS', '2026-07-22 08:33:00'),
(96, 96, 'TXN-MOCK-096', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345696"}', 'SUCCESS', '2026-07-26 16:33:00'),
(97, 97, 'TXN-SEPAY-097', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345697"}', 'SUCCESS', '2026-07-25 05:22:00'),
(98, 98, 'TXN-MOCK-098', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345698"}', 'SUCCESS', '2026-07-25 06:08:00'),
(99, 99, 'TXN-MOCK-099', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"12345699"}', 'SUCCESS', '2026-07-26 05:07:00'),
(100, 100, 'TXN-SEPAY-100', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456100"}', 'SUCCESS', '2026-07-24 06:23:00'),
(101, 101, 'TXN-MOCK-101', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456101"}', 'SUCCESS', '2026-07-25 06:04:00'),
(102, 102, 'TXN-MOCK-102', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456102"}', 'SUCCESS', '2026-07-22 01:50:00'),
(103, 103, 'TXN-MOCK-103', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456103"}', 'SUCCESS', '2026-07-23 11:23:00'),
(104, 104, 'TXN-MOCK-104', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456104"}', 'SUCCESS', '2026-07-24 02:37:00'),
(105, 105, 'TXN-SEPAY-105', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456105"}', 'SUCCESS', '2026-07-27 11:06:00'),
(106, 106, 'TXN-MOCK-106', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456106"}', 'SUCCESS', '2026-07-27 06:36:00'),
(107, 107, 'TXN-MOCK-107', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456107"}', 'SUCCESS', '2026-07-22 22:55:00'),
(108, 108, 'TXN-SEPAY-108', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456108"}', 'SUCCESS', '2026-07-26 05:34:00'),
(109, 109, 'TXN-MOCK-109', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456109"}', 'SUCCESS', '2026-07-24 10:48:00'),
(110, 110, 'TXN-MOCK-110', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456110"}', 'SUCCESS', '2026-07-27 15:24:00'),
(111, 111, 'TXN-MOCK-111', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456111"}', 'SUCCESS', '2026-07-26 15:50:00'),
(112, 112, 'TXN-MOCK-112', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456112"}', 'SUCCESS', '2026-07-24 09:25:00'),
(113, 113, 'TXN-MOCK-113', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456113"}', 'SUCCESS', '2026-07-27 20:07:00'),
(114, 114, 'TXN-MOCK-114', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456114"}', 'SUCCESS', '2026-07-24 14:38:00'),
(115, 115, 'TXN-SEPAY-115', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456115"}', 'SUCCESS', '2026-07-28 09:54:00'),
(116, 116, 'TXN-SEPAY-116', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456116"}', 'SUCCESS', '2026-07-28 13:34:00'),
(117, 117, 'TXN-SEPAY-117', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456117"}', 'SUCCESS', '2026-07-26 14:38:00'),
(118, 118, 'TXN-SEPAY-118', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456118"}', 'SUCCESS', '2026-07-27 10:22:00'),
(119, 119, 'TXN-SEPAY-119', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456119"}', 'SUCCESS', '2026-07-24 10:40:00'),
(120, 120, 'TXN-MOCK-120', '{"status":"success","code":"00","message":"Giao dịch thành công","transactionNo":"123456120"}', 'SUCCESS', '2026-07-25 06:06:00');

INSERT INTO refund (id, payment_id, amount, reason, status, created_at) VALUES
(1, 11, 2440000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(2, 12, 1050000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(3, 13, 1730000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(4, 14, 1460000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(5, 15, 1460000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(6, 16, 1330000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(7, 17, 820000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(8, 18, 1680000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(9, 19, 1710000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00'),
(10, 20, 900000.00, 'Khách hàng yêu cầu hủy vé và hoàn tiền (chính sách PNR)', 'SUCCESS', '2026-07-14 10:00:00');

SELECT setval('payment_id_seq', (SELECT MAX(id) FROM payment));
SELECT setval('transaction_id_seq', (SELECT MAX(id) FROM transaction));
SELECT setval('refund_id_seq', (SELECT MAX(id) FROM refund));
COMMIT;

-- 5. SEED DATA FOR NOTIFICATION SERVICE (notify_db)
\c notify_db
BEGIN;
DELETE FROM flight_reminder_log;
DELETE FROM notification;

INSERT INTO notification (id, template_code, user_id, channel, recipient, variables, status, retry_count, error_message, created_at, sent_at, dedup_key) VALUES
(1, 'PAYMENT_FAILED', 90, 'EMAIL', 'user90@gmail.com', '{"reason":"Giao dịch hết hạn (Timeout)","bookingCode":"PNR031","passengerName":"Hồ Xuân Vy"}', 'SENT', 0, NULL, '2026-07-15 08:19:00', '2026-07-15 08:19:00', 'payment_failed:PNR031'),
(2, 'BOOKING_CANCELLED', 90, 'EMAIL', 'user90@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR031","passengerName":"Hồ Xuân Vy"}', 'SENT', 0, NULL, '2026-07-15 08:29:00', '2026-07-15 08:29:00', 'booking_cancelled:PNR031'),
(3, 'PAYMENT_FAILED', 15, 'EMAIL', 'user15@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR032","passengerName":"Võ Trọng Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:34:00', '2026-07-15 08:34:00', 'payment_failed:PNR032'),
(4, 'BOOKING_CANCELLED', 15, 'EMAIL', 'user15@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR032","passengerName":"Võ Trọng Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:44:00', '2026-07-15 08:44:00', 'booking_cancelled:PNR032'),
(5, 'PAYMENT_FAILED', 84, 'EMAIL', 'user84@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR033","passengerName":"Lê Xuân Tuấn"}', 'SENT', 0, NULL, '2026-07-15 08:46:00', '2026-07-15 08:46:00', 'payment_failed:PNR033'),
(6, 'BOOKING_CANCELLED', 84, 'EMAIL', 'user84@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR033","passengerName":"Lê Xuân Tuấn"}', 'SENT', 0, NULL, '2026-07-15 08:56:00', '2026-07-15 08:56:00', 'booking_cancelled:PNR033'),
(7, 'PAYMENT_FAILED', 50, 'EMAIL', 'user50@gmail.com', '{"reason":"Giao dịch bị từ chối bởi ngân hàng phát hành (05)","bookingCode":"PNR034","passengerName":"Trần Kim Cường"}', 'SENT', 0, NULL, '2026-07-15 08:28:00', '2026-07-15 08:28:00', 'payment_failed:PNR034'),
(8, 'BOOKING_CANCELLED', 50, 'EMAIL', 'user50@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR034","passengerName":"Trần Kim Cường"}', 'SENT', 0, NULL, '2026-07-15 08:38:00', '2026-07-15 08:38:00', 'booking_cancelled:PNR034'),
(9, 'PAYMENT_FAILED', 58, 'EMAIL', 'user58@gmail.com', '{"reason":"Giao dịch hết hạn (Timeout)","bookingCode":"PNR035","passengerName":"Phan Văn Trang"}', 'SENT', 0, NULL, '2026-07-15 08:42:00', '2026-07-15 08:42:00', 'payment_failed:PNR035'),
(10, 'BOOKING_CANCELLED', 58, 'EMAIL', 'user58@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR035","passengerName":"Phan Văn Trang"}', 'SENT', 0, NULL, '2026-07-15 08:52:00', '2026-07-15 08:52:00', 'booking_cancelled:PNR035'),
(11, 'PAYMENT_FAILED', 27, 'EMAIL', 'user27@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR036","passengerName":"Hồ Xuân Giang"}', 'SENT', 0, NULL, '2026-07-15 08:34:00', '2026-07-15 08:34:00', 'payment_failed:PNR036'),
(12, 'BOOKING_CANCELLED', 27, 'EMAIL', 'user27@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR036","passengerName":"Hồ Xuân Giang"}', 'SENT', 0, NULL, '2026-07-15 08:44:00', '2026-07-15 08:44:00', 'booking_cancelled:PNR036'),
(13, 'PAYMENT_FAILED', 85, 'EMAIL', 'user85@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR037","passengerName":"Bùi Kim Trang"}', 'SENT', 0, NULL, '2026-07-15 08:39:00', '2026-07-15 08:39:00', 'payment_failed:PNR037'),
(14, 'BOOKING_CANCELLED', 85, 'EMAIL', 'user85@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR037","passengerName":"Bùi Kim Trang"}', 'SENT', 0, NULL, '2026-07-15 08:49:00', '2026-07-15 08:49:00', 'booking_cancelled:PNR037'),
(15, 'PAYMENT_FAILED', 15, 'EMAIL', 'user15@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR038","passengerName":"Võ Trọng Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:17:00', '2026-07-15 08:17:00', 'payment_failed:PNR038'),
(16, 'BOOKING_CANCELLED', 15, 'EMAIL', 'user15@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR038","passengerName":"Võ Trọng Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:27:00', '2026-07-15 08:27:00', 'booking_cancelled:PNR038'),
(17, 'PAYMENT_FAILED', 66, 'EMAIL', 'user66@gmail.com', '{"reason":"Giao dịch bị từ chối bởi ngân hàng phát hành (05)","bookingCode":"PNR039","passengerName":"Bùi Kim Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:20:00', '2026-07-15 08:20:00', 'payment_failed:PNR039'),
(18, 'BOOKING_CANCELLED', 66, 'EMAIL', 'user66@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR039","passengerName":"Bùi Kim Đạt"}', 'SENT', 0, NULL, '2026-07-15 08:30:00', '2026-07-15 08:30:00', 'booking_cancelled:PNR039'),
(19, 'PAYMENT_FAILED', 11, 'EMAIL', 'user11@gmail.com', '{"reason":"Số dư ví điện tử không đủ để thanh toán","bookingCode":"PNR040","passengerName":"Lê Đức Bình"}', 'SENT', 0, NULL, '2026-07-15 08:27:00', '2026-07-15 08:27:00', 'payment_failed:PNR040'),
(20, 'BOOKING_CANCELLED', 11, 'EMAIL', 'user11@gmail.com', '{"reason":"Không hoàn tất thanh toán đúng hạn","bookingCode":"PNR040","passengerName":"Lê Đức Bình"}', 'SENT', 0, NULL, '2026-07-15 08:37:00', '2026-07-15 08:37:00', 'booking_cancelled:PNR040'),
(21, 'BOOKING_CONFIRMED', 87, 'EMAIL', 'user87@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Vinh","amount":"2440000","seatNo":"18A","currency":"VND","flightNo":"VN276","bookingCode":"PNR041","passengerName":"Huỳnh Thị Nam","departureTime":"2026-07-18 10:30:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR041'),
(22, 'BOOKING_CANCELLED', 87, 'EMAIL', 'user87@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR041","passengerName":"Huỳnh Thị Nam"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR041'),
(23, 'BOOKING_CONFIRMED', 33, 'EMAIL', 'user33@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","amount":"1050000","seatNo":"10F","currency":"VND","flightNo":"VN298","bookingCode":"PNR042","passengerName":"Phan Xuân Tú","departureTime":"2026-07-15 13:00:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR042'),
(24, 'BOOKING_CANCELLED', 33, 'EMAIL', 'user33@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR042","passengerName":"Phan Xuân Tú"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR042'),
(25, 'BOOKING_CONFIRMED', 28, 'EMAIL', 'user28@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Lạt","amount":"1730000","seatNo":"16E","currency":"VND","flightNo":"VN142","bookingCode":"PNR043","passengerName":"Huỳnh Thanh Thảo","departureTime":"2026-07-14 10:00:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR043'),
(26, 'BOOKING_CANCELLED', 28, 'EMAIL', 'user28@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR043","passengerName":"Huỳnh Thanh Thảo"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR043'),
(27, 'BOOKING_CONFIRMED', 61, 'EMAIL', 'user61@gmail.com', '{"from":"Phú Quốc","to":"Hà Nội","amount":"1460000","seatNo":"5D","currency":"VND","flightNo":"VU830","bookingCode":"PNR044","passengerName":"Ngô Hữu Yến","departureTime":"2026-07-14 20:45:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR044'),
(28, 'BOOKING_CANCELLED', 61, 'EMAIL', 'user61@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR044","passengerName":"Ngô Hữu Yến"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR044'),
(29, 'BOOKING_CONFIRMED', 72, 'EMAIL', 'user72@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"1460000","seatNo":"12D","currency":"VND","flightNo":"QH225","bookingCode":"PNR045","passengerName":"Phan Trọng Thảo","departureTime":"2026-07-20 12:30:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR045'),
(30, 'BOOKING_CANCELLED', 72, 'EMAIL', 'user72@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR045","passengerName":"Phan Trọng Thảo"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR045'),
(31, 'BOOKING_CONFIRMED', 31, 'EMAIL', 'user31@gmail.com', '{"from":"Hà Nội","to":"Quy Nhơn","amount":"1330000","seatNo":"5E","currency":"VND","flightNo":"QH281","bookingCode":"PNR046","passengerName":"Đặng Thị Sơn","departureTime":"2026-07-12 16:15:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR046'),
(32, 'BOOKING_CANCELLED', 31, 'EMAIL', 'user31@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR046","passengerName":"Đặng Thị Sơn"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR046'),
(33, 'BOOKING_CONFIRMED', 19, 'EMAIL', 'user19@gmail.com', '{"from":"Hà Nội","to":"Quy Nhơn","amount":"820000","seatNo":"13B","currency":"VND","flightNo":"QH856","bookingCode":"PNR047","passengerName":"Đặng Xuân Hải","departureTime":"2026-07-12 15:15:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR047'),
(34, 'BOOKING_CANCELLED', 19, 'EMAIL', 'user19@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR047","passengerName":"Đặng Xuân Hải"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR047'),
(35, 'BOOKING_CONFIRMED', 69, 'EMAIL', 'user69@gmail.com', '{"from":"Vinh","to":"Hà Nội","amount":"1680000","seatNo":"24A","currency":"VND","flightNo":"VJ538","bookingCode":"PNR048","passengerName":"Lý Trọng An","departureTime":"2026-07-17 19:30:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR048'),
(36, 'BOOKING_CANCELLED', 69, 'EMAIL', 'user69@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR048","passengerName":"Lý Trọng An"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR048'),
(37, 'BOOKING_CONFIRMED', 80, 'EMAIL', 'user80@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Phú Quốc","amount":"1710000","seatNo":"12D","currency":"VND","flightNo":"VN896","bookingCode":"PNR049","passengerName":"Ngô Quốc Yến","departureTime":"2026-07-14 13:45:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR049'),
(38, 'BOOKING_CANCELLED', 80, 'EMAIL', 'user80@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR049","passengerName":"Ngô Quốc Yến"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR049'),
(39, 'BOOKING_CONFIRMED', 23, 'EMAIL', 'user23@gmail.com', '{"from":"Hà Nội","to":"Đà Nẵng","amount":"900000","seatNo":"9B","currency":"VND","flightNo":"QH895","bookingCode":"PNR050","passengerName":"Nguyễn Xuân Hùng","departureTime":"2026-07-12 14:30:00"}', 'SENT', 0, NULL, '2026-07-13 09:05:00', '2026-07-13 09:05:00', 'booking_confirmed:PNR050'),
(40, 'BOOKING_CANCELLED', 23, 'EMAIL', 'user23@gmail.com', '{"reason":"Hành khách yêu cầu hủy vé và hoàn tiền","bookingCode":"PNR050","passengerName":"Nguyễn Xuân Hùng"}', 'SENT', 0, NULL, '2026-07-14 10:00:00', '2026-07-14 10:00:00', 'booking_cancelled:PNR050'),
(41, 'BOOKING_CONFIRMED', 29, 'EMAIL', 'user29@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Vinh","amount":"2440000","seatNo":"11E","currency":"VND","flightNo":"VN276","bookingCode":"PNR051","passengerName":"Nguyễn Ngọc Đạt","departureTime":"2026-07-18 10:30:00"}', 'SENT', 0, NULL, '2026-07-14 00:36:00', '2026-07-14 00:36:00', 'booking_confirmed:PNR051'),
(42, 'BOOKING_CONFIRMED', 100, 'EMAIL', 'user100@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"1800000","seatNo":"24A","currency":"VND","flightNo":"VU469","bookingCode":"PNR052","passengerName":"Nguyễn Đức Giang","departureTime":"2026-07-18 22:30:00"}', 'SENT', 0, NULL, '2026-07-17 18:36:00', '2026-07-17 18:36:00', 'booking_confirmed:PNR052'),
(43, 'BOOKING_CONFIRMED', 94, 'EMAIL', 'user94@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Huế","amount":"1130000","seatNo":"17C","currency":"VND","flightNo":"VN763","bookingCode":"PNR053","passengerName":"Phan Thị Tuấn","departureTime":"2026-07-18 13:00:00"}', 'SENT', 0, NULL, '2026-07-16 07:10:00', '2026-07-16 07:10:00', 'booking_confirmed:PNR053'),
(44, 'BOOKING_CONFIRMED', 82, 'EMAIL', 'user82@gmail.com', '{"from":"Hà Nội","to":"Đà Nẵng","amount":"1290000","seatNo":"5A","currency":"VND","flightNo":"VJ306","bookingCode":"PNR054","passengerName":"Ngô Hữu Nam","departureTime":"2026-07-18 07:00:00"}', 'SENT', 0, NULL, '2026-07-13 02:04:00', '2026-07-13 02:04:00', 'booking_confirmed:PNR054'),
(45, 'BOOKING_CONFIRMED', 46, 'EMAIL', 'user46@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Quy Nhơn","amount":"1300000","seatNo":"19D","currency":"VND","flightNo":"VN318","bookingCode":"PNR055","passengerName":"Ngô Thanh Đạt","departureTime":"2026-07-18 06:00:00"}', 'SENT', 0, NULL, '2026-07-14 23:09:00', '2026-07-14 23:09:00', 'booking_confirmed:PNR055'),
(46, 'BOOKING_CONFIRMED', 46, 'SMS', '091424521', '{"from":"TP. Hồ Chí Minh","to":"Quy Nhơn","seatNo":"19D","flightNo":"VN318","bookingCode":"PNR055","departureTime":"2026-07-18 06:00:00"}', 'SENT', 0, NULL, '2026-07-14 23:09:00', '2026-07-14 23:09:00', 'booking_confirmed_sms:PNR055'),
(47, 'BOOKING_CONFIRMED', 26, 'EMAIL', 'user26@gmail.com', '{"from":"Phú Quốc","to":"Hà Nội","amount":"2360000","seatNo":"13A","currency":"VND","flightNo":"QH468","bookingCode":"PNR056","passengerName":"Võ Đức Vinh","departureTime":"2026-07-18 18:30:00"}', 'SENT', 0, NULL, '2026-07-16 14:36:00', '2026-07-16 14:36:00', 'booking_confirmed:PNR056'),
(48, 'BOOKING_CONFIRMED', 11, 'EMAIL', 'user11@gmail.com', '{"from":"Hà Nội","to":"Đà Lạt","amount":"2250000","seatNo":"19E","currency":"VND","flightNo":"VN213","bookingCode":"PNR057","passengerName":"Lê Đức Bình","departureTime":"2026-07-18 10:15:00"}', 'SENT', 0, NULL, '2026-07-16 03:22:00', '2026-07-16 03:22:00', 'booking_confirmed:PNR057'),
(49, 'BOOKING_CONFIRMED', 37, 'EMAIL', 'user37@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","amount":"1470000","seatNo":"23E","currency":"VND","flightNo":"VJ792","bookingCode":"PNR058","passengerName":"Ngô Ngọc Hải","departureTime":"2026-07-18 06:15:00"}', 'SENT', 0, NULL, '2026-07-14 21:24:00', '2026-07-14 21:24:00', 'booking_confirmed:PNR058'),
(50, 'BOOKING_CONFIRMED', 93, 'EMAIL', 'user93@gmail.com', '{"from":"Hà Nội","to":"Huế","amount":"950000","seatNo":"17E","currency":"VND","flightNo":"VU689","bookingCode":"PNR059","passengerName":"Nguyễn Quốc Lâm","departureTime":"2026-07-19 10:00:00"}', 'SENT', 0, NULL, '2026-07-18 04:06:00', '2026-07-18 04:06:00', 'booking_confirmed:PNR059'),
(51, 'BOOKING_CONFIRMED', 97, 'EMAIL', 'user97@gmail.com', '{"from":"Cần Thơ","to":"Hà Nội","amount":"1580000","seatNo":"20C","currency":"VND","flightNo":"VJ526","bookingCode":"PNR060","passengerName":"Phan Thị Hải","departureTime":"2026-07-19 20:30:00"}', 'SENT', 0, NULL, '2026-07-18 12:33:00', '2026-07-18 12:33:00', 'booking_confirmed:PNR060'),
(52, 'BOOKING_CONFIRMED', 97, 'SMS', '097738389', '{"from":"Cần Thơ","to":"Hà Nội","seatNo":"20C","flightNo":"VJ526","bookingCode":"PNR060","departureTime":"2026-07-19 20:30:00"}', 'SENT', 0, NULL, '2026-07-18 12:33:00', '2026-07-18 12:33:00', 'booking_confirmed_sms:PNR060'),
(53, 'BOOKING_CONFIRMED', 52, 'EMAIL', 'user52@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","amount":"2350000","seatNo":"24A","currency":"VND","flightNo":"QH728","bookingCode":"PNR061","passengerName":"Lý Trọng Vy","departureTime":"2026-07-19 08:15:00"}', 'SENT', 0, NULL, '2026-07-17 04:21:00', '2026-07-17 04:21:00', 'booking_confirmed:PNR061'),
(54, 'BOOKING_CONFIRMED', 65, 'EMAIL', 'user65@gmail.com', '{"from":"Hà Nội","to":"Cần Thơ","amount":"2100000","seatNo":"18A","currency":"VND","flightNo":"VJ349","bookingCode":"PNR062","passengerName":"Dương Trọng Khang","departureTime":"2026-07-19 07:45:00"}', 'SENT', 0, NULL, '2026-07-17 03:54:00', '2026-07-17 03:54:00', 'booking_confirmed:PNR062'),
(55, 'BOOKING_CONFIRMED', 77, 'EMAIL', 'user77@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","amount":"880000","seatNo":"22C","currency":"VND","flightNo":"VN313","bookingCode":"PNR063","passengerName":"Dương Kim Bình","departureTime":"2026-07-19 09:45:00"}', 'SENT', 0, NULL, '2026-07-14 23:52:00', '2026-07-14 23:52:00', 'booking_confirmed:PNR063'),
(56, 'BOOKING_CONFIRMED', 11, 'EMAIL', 'user11@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hà Nội","amount":"1130000","seatNo":"17D","currency":"VND","flightNo":"QH725","bookingCode":"PNR064","passengerName":"Lê Đức Bình","departureTime":"2026-07-19 08:30:00"}', 'SENT', 0, NULL, '2026-07-16 22:40:00', '2026-07-16 22:40:00', 'booking_confirmed:PNR064'),
(57, 'BOOKING_CONFIRMED', 43, 'EMAIL', 'user43@gmail.com', '{"from":"Hà Nội","to":"Vinh","amount":"2790000","seatNo":"1B","currency":"VND","flightNo":"QH311","bookingCode":"PNR065","passengerName":"Bùi Hữu Sơn","departureTime":"2026-07-19 14:00:00"}', 'SENT', 0, NULL, '2026-07-15 09:08:00', '2026-07-15 09:08:00', 'booking_confirmed:PNR065'),
(58, 'BOOKING_CONFIRMED', 43, 'SMS', '091424296', '{"from":"Hà Nội","to":"Vinh","seatNo":"1B","flightNo":"QH311","bookingCode":"PNR065","departureTime":"2026-07-19 14:00:00"}', 'SENT', 0, NULL, '2026-07-15 09:08:00', '2026-07-15 09:08:00', 'booking_confirmed_sms:PNR065'),
(59, 'BOOKING_CONFIRMED', 25, 'EMAIL', 'user25@gmail.com', '{"from":"Cam Ranh","to":"Hà Nội","amount":"1900000","seatNo":"27E","currency":"VND","flightNo":"VU316","bookingCode":"PNR066","passengerName":"Hoàng Đức Phương","departureTime":"2026-07-19 15:30:00"}', 'SENT', 0, NULL, '2026-07-18 12:34:00', '2026-07-18 12:34:00', 'booking_confirmed:PNR066'),
(60, 'BOOKING_CONFIRMED', 42, 'EMAIL', 'user42@gmail.com', '{"from":"Vinh","to":"Hà Nội","amount":"990000","seatNo":"9E","currency":"VND","flightNo":"VU255","bookingCode":"PNR067","passengerName":"Đặng Minh Nam","departureTime":"2026-07-20 07:30:00"}', 'SENT', 0, NULL, '2026-07-18 21:38:00', '2026-07-18 21:38:00', 'booking_confirmed:PNR067'),
(61, 'BOOKING_CONFIRMED', 92, 'EMAIL', 'user92@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"1250000","seatNo":"26A","currency":"VND","flightNo":"VN121","bookingCode":"PNR068","passengerName":"Bùi Đức Tuấn","departureTime":"2026-07-20 14:00:00"}', 'SENT', 0, NULL, '2026-07-15 05:06:00', '2026-07-15 05:06:00', 'booking_confirmed:PNR068'),
(62, 'BOOKING_CONFIRMED', 42, 'EMAIL', 'user42@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"2360000","seatNo":"13F","currency":"VND","flightNo":"VU770","bookingCode":"PNR069","passengerName":"Đặng Minh Nam","departureTime":"2026-07-20 17:00:00"}', 'SENT', 0, NULL, '2026-07-16 14:09:00', '2026-07-16 14:09:00', 'booking_confirmed:PNR069'),
(63, 'BOOKING_CONFIRMED', 91, 'EMAIL', 'user91@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Vinh","amount":"1290000","seatNo":"28C","currency":"VND","flightNo":"QH904","bookingCode":"PNR070","passengerName":"Vũ Thị Lâm","departureTime":"2026-07-20 12:45:00"}', 'SENT', 0, NULL, '2026-07-16 07:49:00', '2026-07-16 07:49:00', 'booking_confirmed:PNR070'),
(64, 'BOOKING_CONFIRMED', 91, 'SMS', '097770203', '{"from":"TP. Hồ Chí Minh","to":"Vinh","seatNo":"28C","flightNo":"QH904","bookingCode":"PNR070","departureTime":"2026-07-20 12:45:00"}', 'SENT', 0, NULL, '2026-07-16 07:49:00', '2026-07-16 07:49:00', 'booking_confirmed_sms:PNR070'),
(65, 'BOOKING_CONFIRMED', 15, 'EMAIL', 'user15@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","amount":"880000","seatNo":"14A","currency":"VND","flightNo":"QH603","bookingCode":"PNR071","passengerName":"Võ Trọng Đạt","departureTime":"2026-07-20 18:15:00"}', 'SENT', 0, NULL, '2026-07-15 14:24:00', '2026-07-15 14:24:00', 'booking_confirmed:PNR071'),
(66, 'BOOKING_CONFIRMED', 25, 'EMAIL', 'user25@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"880000","seatNo":"9A","currency":"VND","flightNo":"QH774","bookingCode":"PNR072","passengerName":"Hoàng Đức Phương","departureTime":"2026-07-20 14:45:00"}', 'SENT', 0, NULL, '2026-07-15 05:51:00', '2026-07-15 05:51:00', 'booking_confirmed:PNR072'),
(67, 'BOOKING_CONFIRMED', 11, 'EMAIL', 'user11@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"1460000","seatNo":"18F","currency":"VND","flightNo":"QH225","bookingCode":"PNR073","passengerName":"Lê Đức Bình","departureTime":"2026-07-20 12:30:00"}', 'SENT', 0, NULL, '2026-07-19 08:38:00', '2026-07-19 08:38:00', 'booking_confirmed:PNR073'),
(68, 'BOOKING_CONFIRMED', 65, 'EMAIL', 'user65@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"1890000","seatNo":"28A","currency":"VND","flightNo":"VU874","bookingCode":"PNR074","passengerName":"Dương Trọng Khang","departureTime":"2026-07-20 19:45:00"}', 'SENT', 0, NULL, '2026-07-19 14:53:00', '2026-07-19 14:53:00', 'booking_confirmed:PNR074'),
(69, 'BOOKING_CONFIRMED', 57, 'EMAIL', 'user57@gmail.com', '{"from":"Hà Nội","to":"Phú Quốc","amount":"2320000","seatNo":"23E","currency":"VND","flightNo":"VN984","bookingCode":"PNR075","passengerName":"Phan Trọng Đạt","departureTime":"2026-07-21 14:30:00"}', 'SENT', 0, NULL, '2026-07-16 07:34:00', '2026-07-16 07:34:00', 'booking_confirmed:PNR075'),
(70, 'BOOKING_CONFIRMED', 57, 'SMS', '099963646', '{"from":"Hà Nội","to":"Phú Quốc","seatNo":"23E","flightNo":"VN984","bookingCode":"PNR075","departureTime":"2026-07-21 14:30:00"}', 'SENT', 0, NULL, '2026-07-16 07:34:00', '2026-07-16 07:34:00', 'booking_confirmed_sms:PNR075'),
(71, 'BOOKING_CONFIRMED', 15, 'EMAIL', 'user15@gmail.com', '{"from":"Huế","to":"TP. Hồ Chí Minh","amount":"2100000","seatNo":"1C","currency":"VND","flightNo":"VJ383","bookingCode":"PNR076","passengerName":"Võ Trọng Đạt","departureTime":"2026-07-21 16:45:00"}', 'SENT', 0, NULL, '2026-07-20 11:48:00', '2026-07-20 11:48:00', 'booking_confirmed:PNR076'),
(72, 'BOOKING_CONFIRMED', 67, 'EMAIL', 'user67@gmail.com', '{"from":"Cam Ranh","to":"Hà Nội","amount":"1600000","seatNo":"23A","currency":"VND","flightNo":"VJ846","bookingCode":"PNR077","passengerName":"Hồ Hồng Tuấn","departureTime":"2026-07-21 21:30:00"}', 'SENT', 0, NULL, '2026-07-20 15:34:00', '2026-07-20 15:34:00', 'booking_confirmed:PNR077'),
(73, 'BOOKING_CONFIRMED', 88, 'EMAIL', 'user88@gmail.com', '{"from":"Hà Nội","to":"Phú Quốc","amount":"1190000","seatNo":"25F","currency":"VND","flightNo":"VN675","bookingCode":"PNR078","passengerName":"Huỳnh Quốc An","departureTime":"2026-07-21 13:45:00"}', 'SENT', 0, NULL, '2026-07-18 11:53:00', '2026-07-18 11:53:00', 'booking_confirmed:PNR078'),
(74, 'BOOKING_CONFIRMED', 34, 'EMAIL', 'user34@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Phú Quốc","amount":"970000","seatNo":"20E","currency":"VND","flightNo":"VJ954","bookingCode":"PNR079","passengerName":"Hoàng Trọng Sơn","departureTime":"2026-07-21 22:45:00"}', 'SENT', 0, NULL, '2026-07-17 19:52:00', '2026-07-17 19:52:00', 'booking_confirmed:PNR079'),
(75, 'BOOKING_CONFIRMED', 69, 'EMAIL', 'user69@gmail.com', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","amount":"2730000","seatNo":"1C","currency":"VND","flightNo":"VN653","bookingCode":"PNR080","passengerName":"Lý Trọng An","departureTime":"2026-07-21 22:15:00"}', 'SENT', 0, NULL, '2026-07-16 15:24:00', '2026-07-16 15:24:00', 'booking_confirmed:PNR080'),
(76, 'BOOKING_CONFIRMED', 69, 'SMS', '094784328', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","seatNo":"1C","flightNo":"VN653","bookingCode":"PNR080","departureTime":"2026-07-21 22:15:00"}', 'SENT', 0, NULL, '2026-07-16 15:24:00', '2026-07-16 15:24:00', 'booking_confirmed_sms:PNR080'),
(77, 'BOOKING_CONFIRMED', 31, 'EMAIL', 'user31@gmail.com', '{"from":"Đà Lạt","to":"TP. Hồ Chí Minh","amount":"1200000","seatNo":"13F","currency":"VND","flightNo":"QH312","bookingCode":"PNR081","passengerName":"Đặng Thị Sơn","departureTime":"2026-07-21 14:45:00"}', 'SENT', 0, NULL, '2026-07-19 08:48:00', '2026-07-19 08:48:00', 'booking_confirmed:PNR081'),
(78, 'BOOKING_CONFIRMED', 14, 'EMAIL', 'user14@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","amount":"1420000","seatNo":"15D","currency":"VND","flightNo":"QH778","bookingCode":"PNR082","passengerName":"Huỳnh Văn Đạt","departureTime":"2026-07-21 18:45:00"}', 'SENT', 0, NULL, '2026-07-20 09:49:00', '2026-07-20 09:49:00', 'booking_confirmed:PNR082'),
(79, 'BOOKING_CONFIRMED', 26, 'EMAIL', 'user26@gmail.com', '{"from":"Quy Nhơn","to":"TP. Hồ Chí Minh","amount":"3630000","seatNo":"3B","currency":"VND","flightNo":"VU628","bookingCode":"PNR083","passengerName":"Võ Đức Vinh","departureTime":"2026-07-22 12:30:00"}', 'SENT', 0, NULL, '2026-07-19 07:34:00', '2026-07-19 07:34:00', 'booking_confirmed:PNR083'),
(80, 'FLIGHT_REMINDER', 26, 'EMAIL', 'user26@gmail.com', '{"from":"Quy Nhơn","to":"TP. Hồ Chí Minh","seatNo":"3B","flightNo":"VU628","passengerName":"Võ Đức Vinh","departureTime":"2026-07-22 12:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR083'),
(81, 'BOOKING_CONFIRMED', 55, 'EMAIL', 'user55@gmail.com', '{"from":"Đà Nẵng","to":"TP. Hồ Chí Minh","amount":"1850000","seatNo":"11A","currency":"VND","flightNo":"QH378","bookingCode":"PNR084","passengerName":"Đặng Thị Sơn","departureTime":"2026-07-22 20:15:00"}', 'SENT', 0, NULL, '2026-07-17 18:22:00', '2026-07-17 18:22:00', 'booking_confirmed:PNR084'),
(82, 'FLIGHT_REMINDER', 55, 'EMAIL', 'user55@gmail.com', '{"from":"Đà Nẵng","to":"TP. Hồ Chí Minh","seatNo":"11A","flightNo":"QH378","passengerName":"Đặng Thị Sơn","departureTime":"2026-07-22 20:15:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR084'),
(83, 'BOOKING_CONFIRMED', 81, 'EMAIL', 'user81@gmail.com', '{"from":"Hà Nội","to":"Cam Ranh","amount":"1910000","seatNo":"15B","currency":"VND","flightNo":"QH600","bookingCode":"PNR085","passengerName":"Lê Kim Minh","departureTime":"2026-07-22 10:30:00"}', 'SENT', 0, NULL, '2026-07-18 09:40:00', '2026-07-18 09:40:00', 'booking_confirmed:PNR085'),
(84, 'BOOKING_CONFIRMED', 81, 'SMS', '098886650', '{"from":"Hà Nội","to":"Cam Ranh","seatNo":"15B","flightNo":"QH600","bookingCode":"PNR085","departureTime":"2026-07-22 10:30:00"}', 'SENT', 0, NULL, '2026-07-18 09:40:00', '2026-07-18 09:40:00', 'booking_confirmed_sms:PNR085'),
(85, 'FLIGHT_REMINDER', 81, 'EMAIL', 'user81@gmail.com', '{"from":"Hà Nội","to":"Cam Ranh","seatNo":"15B","flightNo":"QH600","passengerName":"Lê Kim Minh","departureTime":"2026-07-22 10:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR085'),
(86, 'BOOKING_CONFIRMED', 7, 'EMAIL', 'user7@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","amount":"2040000","seatNo":"10C","currency":"VND","flightNo":"VJ759","bookingCode":"PNR086","passengerName":"Nguyễn Đức Cường","departureTime":"2026-07-22 19:30:00"}', 'SENT', 0, NULL, '2026-07-21 10:33:00', '2026-07-21 10:33:00', 'booking_confirmed:PNR086'),
(87, 'FLIGHT_REMINDER', 7, 'EMAIL', 'user7@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","seatNo":"10C","flightNo":"VJ759","passengerName":"Nguyễn Đức Cường","departureTime":"2026-07-22 19:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR086'),
(88, 'BOOKING_CONFIRMED', 17, 'EMAIL', 'user17@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"2430000","seatNo":"19A","currency":"VND","flightNo":"VU423","bookingCode":"PNR087","passengerName":"Hoàng Quốc Dũng","departureTime":"2026-07-22 22:30:00"}', 'SENT', 0, NULL, '2026-07-21 18:35:00', '2026-07-21 18:35:00', 'booking_confirmed:PNR087'),
(89, 'FLIGHT_REMINDER', 17, 'EMAIL', 'user17@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","seatNo":"19A","flightNo":"VU423","passengerName":"Hoàng Quốc Dũng","departureTime":"2026-07-22 22:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR087'),
(90, 'BOOKING_CONFIRMED', 51, 'EMAIL', 'user51@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","amount":"1350000","seatNo":"26C","currency":"VND","flightNo":"QH618","bookingCode":"PNR088","passengerName":"Dương Thanh Lâm","departureTime":"2026-07-22 15:30:00"}', 'SENT', 0, NULL, '2026-07-18 13:40:00', '2026-07-18 13:40:00', 'booking_confirmed:PNR088'),
(91, 'FLIGHT_REMINDER', 51, 'EMAIL', 'user51@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","seatNo":"26C","flightNo":"QH618","passengerName":"Dương Thanh Lâm","departureTime":"2026-07-22 15:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR088'),
(92, 'BOOKING_CONFIRMED', 33, 'EMAIL', 'user33@gmail.com', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","amount":"2090000","seatNo":"19F","currency":"VND","flightNo":"VU264","bookingCode":"PNR089","passengerName":"Phan Xuân Tú","departureTime":"2026-07-22 22:45:00"}', 'SENT', 0, NULL, '2026-07-20 17:49:00', '2026-07-20 17:49:00', 'booking_confirmed:PNR089'),
(93, 'FLIGHT_REMINDER', 33, 'EMAIL', 'user33@gmail.com', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","seatNo":"19F","flightNo":"VU264","passengerName":"Phan Xuân Tú","departureTime":"2026-07-22 22:45:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR089'),
(94, 'BOOKING_CONFIRMED', 49, 'EMAIL', 'user49@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","amount":"2460000","seatNo":"28D","currency":"VND","flightNo":"VJ596","bookingCode":"PNR090","passengerName":"Vũ Thị Trang","departureTime":"2026-07-22 13:15:00"}', 'SENT', 0, NULL, '2026-07-20 09:19:00', '2026-07-20 09:19:00', 'booking_confirmed:PNR090'),
(95, 'BOOKING_CONFIRMED', 49, 'SMS', '098608753', '{"from":"Vinh","to":"TP. Hồ Chí Minh","seatNo":"28D","flightNo":"VJ596","bookingCode":"PNR090","departureTime":"2026-07-22 13:15:00"}', 'SENT', 0, NULL, '2026-07-20 09:19:00', '2026-07-20 09:19:00', 'booking_confirmed_sms:PNR090'),
(96, 'FLIGHT_REMINDER', 49, 'EMAIL', 'user49@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","seatNo":"28D","flightNo":"VJ596","passengerName":"Vũ Thị Trang","departureTime":"2026-07-22 13:15:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR090'),
(97, 'BOOKING_CONFIRMED', 88, 'EMAIL', 'user88@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hà Nội","amount":"1010000","seatNo":"20A","currency":"VND","flightNo":"QH460","bookingCode":"PNR091","passengerName":"Huỳnh Quốc An","departureTime":"2026-07-22 17:30:00"}', 'SENT', 0, NULL, '2026-07-20 15:38:00', '2026-07-20 15:38:00', 'booking_confirmed:PNR091'),
(98, 'FLIGHT_REMINDER', 88, 'EMAIL', 'user88@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hà Nội","seatNo":"20A","flightNo":"QH460","passengerName":"Huỳnh Quốc An","departureTime":"2026-07-22 17:30:00"}', 'SENT', 0, NULL, '2026-07-21 08:00:00', '2026-07-21 08:00:00', 'flight_reminder:PNR091'),
(99, 'BOOKING_CONFIRMED', 51, 'EMAIL', 'user51@gmail.com', '{"from":"Quy Nhơn","to":"Hà Nội","amount":"1220000","seatNo":"6F","currency":"VND","flightNo":"VJ152","bookingCode":"PNR092","passengerName":"Dương Thanh Lâm","departureTime":"2026-07-23 20:15:00"}', 'SENT', 0, NULL, '2026-07-19 16:23:00', '2026-07-19 16:23:00', 'booking_confirmed:PNR092'),
(100, 'BOOKING_CONFIRMED', 49, 'EMAIL', 'user49@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Quy Nhơn","amount":"1230000","seatNo":"22B","currency":"VND","flightNo":"VJ233","bookingCode":"PNR093","passengerName":"Vũ Thị Trang","departureTime":"2026-07-23 21:30:00"}', 'SENT', 0, NULL, '2026-07-22 15:38:00', '2026-07-22 15:38:00', 'booking_confirmed:PNR093'),
(101, 'BOOKING_CONFIRMED', 56, 'EMAIL', 'user56@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","amount":"1040000","seatNo":"17F","currency":"VND","flightNo":"QH157","bookingCode":"PNR094","passengerName":"Võ Xuân Linh","departureTime":"2026-07-23 10:00:00"}', 'SENT', 0, NULL, '2026-07-20 08:07:00', '2026-07-20 08:07:00', 'booking_confirmed:PNR094'),
(102, 'BOOKING_CONFIRMED', 48, 'EMAIL', 'user48@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"3690000","seatNo":"3D","currency":"VND","flightNo":"VN843","bookingCode":"PNR095","passengerName":"Phạm Văn Hải","departureTime":"2026-07-23 14:00:00"}', 'SENT', 0, NULL, '2026-07-21 09:06:00', '2026-07-21 09:06:00', 'booking_confirmed:PNR095'),
(103, 'BOOKING_CONFIRMED', 48, 'SMS', '091786814', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","seatNo":"3D","flightNo":"VN843","bookingCode":"PNR095","departureTime":"2026-07-23 14:00:00"}', 'SENT', 0, NULL, '2026-07-21 09:06:00', '2026-07-21 09:06:00', 'booking_confirmed_sms:PNR095'),
(104, 'BOOKING_CONFIRMED', 46, 'EMAIL', 'user46@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"2430000","seatNo":"8B","currency":"VND","flightNo":"QH910","bookingCode":"PNR096","passengerName":"Ngô Thanh Đạt","departureTime":"2026-07-23 21:00:00"}', 'SENT', 0, NULL, '2026-07-18 18:03:00', '2026-07-18 18:03:00', 'booking_confirmed:PNR096'),
(105, 'BOOKING_CONFIRMED', 97, 'EMAIL', 'user97@gmail.com', '{"from":"Hà Nội","to":"Cam Ranh","amount":"2370000","seatNo":"9F","currency":"VND","flightNo":"QH691","bookingCode":"PNR097","passengerName":"Phan Thị Hải","departureTime":"2026-07-23 21:00:00"}', 'SENT', 0, NULL, '2026-07-21 15:06:00', '2026-07-21 15:06:00', 'booking_confirmed:PNR097'),
(106, 'BOOKING_CONFIRMED', 54, 'EMAIL', 'user54@gmail.com', '{"from":"Phú Quốc","to":"Hà Nội","amount":"1920000","seatNo":"12E","currency":"VND","flightNo":"VU591","bookingCode":"PNR098","passengerName":"Lê Ngọc Hương","departureTime":"2026-07-23 11:00:00"}', 'SENT', 0, NULL, '2026-07-18 02:07:00', '2026-07-18 02:07:00', 'booking_confirmed:PNR098'),
(107, 'BOOKING_CONFIRMED', 90, 'EMAIL', 'user90@gmail.com', '{"from":"Phú Quốc","to":"TP. Hồ Chí Minh","amount":"1620000","seatNo":"28F","currency":"VND","flightNo":"VJ827","bookingCode":"PNR099","passengerName":"Hồ Xuân Vy","departureTime":"2026-07-23 19:45:00"}', 'SENT', 0, NULL, '2026-07-21 12:52:00', '2026-07-21 12:52:00', 'booking_confirmed:PNR099'),
(108, 'BOOKING_CONFIRMED', 24, 'EMAIL', 'user24@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","amount":"1160000","seatNo":"26E","currency":"VND","flightNo":"VN713","bookingCode":"PNR100","passengerName":"Hồ Thanh Hương","departureTime":"2026-07-24 12:45:00"}', 'SENT', 0, NULL, '2026-07-20 04:52:00', '2026-07-20 04:52:00', 'booking_confirmed:PNR100'),
(109, 'BOOKING_CONFIRMED', 24, 'SMS', '092473816', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","seatNo":"26E","flightNo":"VN713","bookingCode":"PNR100","departureTime":"2026-07-24 12:45:00"}', 'SENT', 0, NULL, '2026-07-20 04:52:00', '2026-07-20 04:52:00', 'booking_confirmed_sms:PNR100'),
(110, 'BOOKING_CONFIRMED', 48, 'EMAIL', 'user48@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Phú Quốc","amount":"2175000","seatNo":"3F","currency":"VND","flightNo":"VJ934","bookingCode":"PNR101","passengerName":"Phạm Văn Hải","departureTime":"2026-07-24 10:15:00"}', 'SENT', 0, NULL, '2026-07-19 09:23:00', '2026-07-19 09:23:00', 'booking_confirmed:PNR101'),
(111, 'BOOKING_CONFIRMED', 10, 'EMAIL', 'user10@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hà Nội","amount":"2030000","seatNo":"12B","currency":"VND","flightNo":"QH750","bookingCode":"PNR102","passengerName":"Hoàng Minh Minh","departureTime":"2026-07-24 12:30:00"}', 'SENT', 0, NULL, '2026-07-19 05:33:00', '2026-07-19 05:33:00', 'booking_confirmed:PNR102'),
(112, 'BOOKING_CONFIRMED', 88, 'EMAIL', 'user88@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Quy Nhơn","amount":"1650000","seatNo":"14F","currency":"VND","flightNo":"QH513","bookingCode":"PNR103","passengerName":"Huỳnh Quốc An","departureTime":"2026-07-24 20:15:00"}', 'SENT', 0, NULL, '2026-07-21 12:25:00', '2026-07-21 12:25:00', 'booking_confirmed:PNR103'),
(113, 'BOOKING_CONFIRMED', 74, 'EMAIL', 'user74@gmail.com', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","amount":"2260000","seatNo":"25D","currency":"VND","flightNo":"VU167","bookingCode":"PNR104","passengerName":"Hồ Hồng Khang","departureTime":"2026-07-24 20:30:00"}', 'SENT', 0, NULL, '2026-07-23 18:40:00', '2026-07-23 18:40:00', 'booking_confirmed:PNR104'),
(114, 'BOOKING_CONFIRMED', 67, 'EMAIL', 'user67@gmail.com', '{"from":"Đà Nẵng","to":"TP. Hồ Chí Minh","amount":"1690000","seatNo":"16D","currency":"VND","flightNo":"VJ784","bookingCode":"PNR105","passengerName":"Hồ Hồng Tuấn","departureTime":"2026-07-24 16:15:00"}', 'SENT', 0, NULL, '2026-07-21 06:20:00', '2026-07-21 06:20:00', 'booking_confirmed:PNR105'),
(115, 'BOOKING_CONFIRMED', 67, 'SMS', '091770039', '{"from":"Đà Nẵng","to":"TP. Hồ Chí Minh","seatNo":"16D","flightNo":"VJ784","bookingCode":"PNR105","departureTime":"2026-07-24 16:15:00"}', 'SENT', 0, NULL, '2026-07-21 06:20:00', '2026-07-21 06:20:00', 'booking_confirmed_sms:PNR105'),
(116, 'BOOKING_CONFIRMED', 76, 'EMAIL', 'user76@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","amount":"1270000","seatNo":"22E","currency":"VND","flightNo":"VJ141","bookingCode":"PNR106","passengerName":"Trần Trọng Tú","departureTime":"2026-07-24 10:00:00"}', 'SENT', 0, NULL, '2026-07-19 04:03:00', '2026-07-19 04:03:00', 'booking_confirmed:PNR106'),
(117, 'BOOKING_CONFIRMED', 32, 'EMAIL', 'user32@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Vinh","amount":"1140000","seatNo":"23A","currency":"VND","flightNo":"VJ615","bookingCode":"PNR107","passengerName":"Huỳnh Thị Phương","departureTime":"2026-07-24 16:00:00"}', 'SENT', 0, NULL, '2026-07-20 11:10:00', '2026-07-20 11:10:00', 'booking_confirmed:PNR107'),
(118, 'BOOKING_CONFIRMED', 63, 'EMAIL', 'user63@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Phú Quốc","amount":"2300000","seatNo":"7A","currency":"VND","flightNo":"VJ956","bookingCode":"PNR108","passengerName":"Bùi Hữu Vy","departureTime":"2026-07-25 21:30:00"}', 'SENT', 0, NULL, '2026-07-23 18:37:00', '2026-07-23 18:37:00', 'booking_confirmed:PNR108'),
(119, 'BOOKING_CONFIRMED', 6, 'EMAIL', 'user6@gmail.com', '{"from":"Đà Lạt","to":"TP. Hồ Chí Minh","amount":"2300000","seatNo":"24A","currency":"VND","flightNo":"QH529","bookingCode":"PNR109","passengerName":"Phan Thị Vinh","departureTime":"2026-07-25 16:00:00"}', 'SENT', 0, NULL, '2026-07-24 14:05:00', '2026-07-24 14:05:00', 'booking_confirmed:PNR109'),
(120, 'BOOKING_CONFIRMED', 38, 'EMAIL', 'user38@gmail.com', '{"from":"Hà Nội","to":"Huế","amount":"1500000","seatNo":"14F","currency":"VND","flightNo":"QH502","bookingCode":"PNR110","passengerName":"Nguyễn Kim Phúc","departureTime":"2026-07-25 12:30:00"}', 'SENT', 0, NULL, '2026-07-20 09:37:00', '2026-07-20 09:37:00', 'booking_confirmed:PNR110'),
(121, 'BOOKING_CONFIRMED', 38, 'SMS', '098490576', '{"from":"Hà Nội","to":"Huế","seatNo":"14F","flightNo":"QH502","bookingCode":"PNR110","departureTime":"2026-07-25 12:30:00"}', 'SENT', 0, NULL, '2026-07-20 09:37:00', '2026-07-20 09:37:00', 'booking_confirmed_sms:PNR110'),
(122, 'BOOKING_CONFIRMED', 16, 'EMAIL', 'user16@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","amount":"2340000","seatNo":"26E","currency":"VND","flightNo":"VN153","bookingCode":"PNR111","passengerName":"Hồ Hữu Huy","departureTime":"2026-07-25 12:15:00"}', 'SENT', 0, NULL, '2026-07-21 02:24:00', '2026-07-21 02:24:00', 'booking_confirmed:PNR111'),
(123, 'BOOKING_CONFIRMED', 19, 'EMAIL', 'user19@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","amount":"1500000","seatNo":"25E","currency":"VND","flightNo":"VU445","bookingCode":"PNR112","passengerName":"Đặng Xuân Hải","departureTime":"2026-07-25 21:15:00"}', 'SENT', 0, NULL, '2026-07-24 16:23:00', '2026-07-24 16:23:00', 'booking_confirmed:PNR112'),
(124, 'BOOKING_CONFIRMED', 16, 'EMAIL', 'user16@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Lạt","amount":"1290000","seatNo":"28A","currency":"VND","flightNo":"VJ854","bookingCode":"PNR113","passengerName":"Hồ Hữu Huy","departureTime":"2026-07-25 12:00:00"}', 'SENT', 0, NULL, '2026-07-21 11:03:00', '2026-07-21 11:03:00', 'booking_confirmed:PNR113'),
(125, 'BOOKING_CONFIRMED', 10, 'EMAIL', 'user10@gmail.com', '{"from":"Hà Nội","to":"Cần Thơ","amount":"2100000","seatNo":"19D","currency":"VND","flightNo":"VU684","bookingCode":"PNR114","passengerName":"Hoàng Minh Minh","departureTime":"2026-07-25 20:15:00"}', 'SENT', 0, NULL, '2026-07-20 17:24:00', '2026-07-20 17:24:00', 'booking_confirmed:PNR114'),
(126, 'BOOKING_CONFIRMED', 86, 'EMAIL', 'user86@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","amount":"1410000","seatNo":"18E","currency":"VND","flightNo":"VJ834","bookingCode":"PNR115","passengerName":"Bùi Đức Nam","departureTime":"2026-07-25 12:15:00"}', 'SENT', 0, NULL, '2026-07-20 02:25:00', '2026-07-20 02:25:00', 'booking_confirmed:PNR115'),
(127, 'BOOKING_CONFIRMED', 86, 'SMS', '096239189', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","seatNo":"18E","flightNo":"VJ834","bookingCode":"PNR115","departureTime":"2026-07-25 12:15:00"}', 'SENT', 0, NULL, '2026-07-20 02:25:00', '2026-07-20 02:25:00', 'booking_confirmed_sms:PNR115'),
(128, 'BOOKING_CONFIRMED', 48, 'EMAIL', 'user48@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","amount":"2110000","seatNo":"19F","currency":"VND","flightNo":"QH736","bookingCode":"PNR116","passengerName":"Phạm Văn Hải","departureTime":"2026-07-25 16:30:00"}', 'SENT', 0, NULL, '2026-07-23 08:40:00', '2026-07-23 08:40:00', 'booking_confirmed:PNR116'),
(129, 'BOOKING_CONFIRMED', 100, 'EMAIL', 'user100@gmail.com', '{"from":"Vinh","to":"TP. Hồ Chí Minh","amount":"1550000","seatNo":"19A","currency":"VND","flightNo":"VU349","bookingCode":"PNR117","passengerName":"Nguyễn Đức Giang","departureTime":"2026-07-26 13:45:00"}', 'SENT', 0, NULL, '2026-07-25 03:55:00', '2026-07-25 03:55:00', 'booking_confirmed:PNR117'),
(130, 'BOOKING_CONFIRMED', 37, 'EMAIL', 'user37@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Huế","amount":"2270000","seatNo":"23A","currency":"VND","flightNo":"VU586","bookingCode":"PNR118","passengerName":"Ngô Ngọc Hải","departureTime":"2026-07-26 11:30:00"}', 'SENT', 0, NULL, '2026-07-21 09:35:00', '2026-07-21 09:35:00', 'booking_confirmed:PNR118'),
(131, 'BOOKING_CONFIRMED', 26, 'EMAIL', 'user26@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"3405000","seatNo":"2B","currency":"VND","flightNo":"VU160","bookingCode":"PNR119","passengerName":"Võ Đức Vinh","departureTime":"2026-07-26 17:15:00"}', 'SENT', 0, NULL, '2026-07-21 15:23:00', '2026-07-21 15:23:00', 'booking_confirmed:PNR119'),
(132, 'BOOKING_CONFIRMED', 47, 'EMAIL', 'user47@gmail.com', '{"from":"Đà Lạt","to":"TP. Hồ Chí Minh","amount":"2130000","seatNo":"20E","currency":"VND","flightNo":"VN183","bookingCode":"PNR120","passengerName":"Hoàng Hồng Yến","departureTime":"2026-07-26 22:30:00"}', 'SENT', 0, NULL, '2026-07-22 17:38:00', '2026-07-22 17:38:00', 'booking_confirmed:PNR120'),
(133, 'BOOKING_CONFIRMED', 47, 'SMS', '097712388', '{"from":"Đà Lạt","to":"TP. Hồ Chí Minh","seatNo":"20E","flightNo":"VN183","bookingCode":"PNR120","departureTime":"2026-07-26 22:30:00"}', 'SENT', 0, NULL, '2026-07-22 17:38:00', '2026-07-22 17:38:00', 'booking_confirmed_sms:PNR120'),
(134, 'BOOKING_CONFIRMED', 20, 'EMAIL', 'user20@gmail.com', '{"from":"Hà Nội","to":"Quy Nhơn","amount":"1690000","seatNo":"8C","currency":"VND","flightNo":"VJ128","bookingCode":"PNR121","passengerName":"Bùi Minh Dũng","departureTime":"2026-07-26 11:00:00"}', 'SENT', 0, NULL, '2026-07-22 09:03:00', '2026-07-22 09:03:00', 'booking_confirmed:PNR121'),
(135, 'BOOKING_CONFIRMED', 91, 'EMAIL', 'user91@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","amount":"2380000","seatNo":"19E","currency":"VND","flightNo":"QH548","bookingCode":"PNR122","passengerName":"Vũ Thị Lâm","departureTime":"2026-07-26 19:45:00"}', 'SENT', 0, NULL, '2026-07-21 16:50:00', '2026-07-21 16:50:00', 'booking_confirmed:PNR122'),
(136, 'BOOKING_CONFIRMED', 61, 'EMAIL', 'user61@gmail.com', '{"from":"Huế","to":"TP. Hồ Chí Minh","amount":"2500000","seatNo":"17C","currency":"VND","flightNo":"VN974","bookingCode":"PNR123","passengerName":"Ngô Hữu Yến","departureTime":"2026-07-26 14:30:00"}', 'SENT', 0, NULL, '2026-07-22 11:35:00', '2026-07-22 11:35:00', 'booking_confirmed:PNR123'),
(137, 'BOOKING_CONFIRMED', 84, 'EMAIL', 'user84@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"1750000","seatNo":"4B","currency":"VND","flightNo":"QH292","bookingCode":"PNR124","passengerName":"Lê Xuân Tuấn","departureTime":"2026-07-26 13:30:00"}', 'SENT', 0, NULL, '2026-07-24 06:34:00', '2026-07-24 06:34:00', 'booking_confirmed:PNR124'),
(138, 'BOOKING_CONFIRMED', 97, 'EMAIL', 'user97@gmail.com', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","amount":"1395000","seatNo":"1A","currency":"VND","flightNo":"QH820","bookingCode":"PNR125","passengerName":"Phan Thị Hải","departureTime":"2026-07-27 15:30:00"}', 'SENT', 0, NULL, '2026-07-22 08:33:00', '2026-07-22 08:33:00', 'booking_confirmed:PNR125'),
(139, 'BOOKING_CONFIRMED', 97, 'SMS', '097738389', '{"from":"Hải Phòng","to":"TP. Hồ Chí Minh","seatNo":"1A","flightNo":"QH820","bookingCode":"PNR125","departureTime":"2026-07-27 15:30:00"}', 'SENT', 0, NULL, '2026-07-22 08:33:00', '2026-07-22 08:33:00', 'booking_confirmed_sms:PNR125'),
(140, 'BOOKING_CONFIRMED', 87, 'EMAIL', 'user87@gmail.com', '{"from":"Phú Quốc","to":"TP. Hồ Chí Minh","amount":"1130000","seatNo":"23B","currency":"VND","flightNo":"VN526","bookingCode":"PNR126","passengerName":"Huỳnh Thị Nam","departureTime":"2026-07-27 20:30:00"}', 'SENT', 0, NULL, '2026-07-26 16:33:00', '2026-07-26 16:33:00', 'booking_confirmed:PNR126'),
(141, 'BOOKING_CONFIRMED', 98, 'EMAIL', 'user98@gmail.com', '{"from":"Đà Lạt","to":"Hà Nội","amount":"1130000","seatNo":"16A","currency":"VND","flightNo":"QH202","bookingCode":"PNR127","passengerName":"Huỳnh Quốc Khang","departureTime":"2026-07-27 15:15:00"}', 'SENT', 0, NULL, '2026-07-25 05:22:00', '2026-07-25 05:22:00', 'booking_confirmed:PNR127'),
(142, 'BOOKING_CONFIRMED', 78, 'EMAIL', 'user78@gmail.com', '{"from":"Hà Nội","to":"Đà Nẵng","amount":"1810000","seatNo":"7B","currency":"VND","flightNo":"VJ241","bookingCode":"PNR128","passengerName":"Nguyễn Kim Giang","departureTime":"2026-07-27 08:00:00"}', 'SENT', 0, NULL, '2026-07-25 06:08:00', '2026-07-25 06:08:00', 'booking_confirmed:PNR128'),
(143, 'BOOKING_CONFIRMED', 36, 'EMAIL', 'user36@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","amount":"2130000","seatNo":"13E","currency":"VND","flightNo":"VN851","bookingCode":"PNR129","passengerName":"Ngô Đức Bình","departureTime":"2026-07-27 12:00:00"}', 'SENT', 0, NULL, '2026-07-26 05:07:00', '2026-07-26 05:07:00', 'booking_confirmed:PNR129'),
(144, 'BOOKING_CONFIRMED', 92, 'EMAIL', 'user92@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","amount":"1070000","seatNo":"4E","currency":"VND","flightNo":"QH745","bookingCode":"PNR130","passengerName":"Bùi Đức Tuấn","departureTime":"2026-07-27 10:15:00"}', 'SENT', 0, NULL, '2026-07-24 06:23:00', '2026-07-24 06:23:00', 'booking_confirmed:PNR130'),
(145, 'BOOKING_CONFIRMED', 92, 'SMS', '099885771', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","seatNo":"4E","flightNo":"QH745","bookingCode":"PNR130","departureTime":"2026-07-27 10:15:00"}', 'SENT', 0, NULL, '2026-07-24 06:23:00', '2026-07-24 06:23:00', 'booking_confirmed_sms:PNR130'),
(146, 'BOOKING_CONFIRMED', 38, 'EMAIL', 'user38@gmail.com', '{"from":"Phú Quốc","to":"Hà Nội","amount":"820000","seatNo":"23C","currency":"VND","flightNo":"VN551","bookingCode":"PNR131","passengerName":"Nguyễn Kim Phúc","departureTime":"2026-07-27 09:00:00"}', 'SENT', 0, NULL, '2026-07-25 06:04:00', '2026-07-25 06:04:00', 'booking_confirmed:PNR131'),
(147, 'BOOKING_CONFIRMED', 30, 'EMAIL', 'user30@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Nẵng","amount":"1370000","seatNo":"24B","currency":"VND","flightNo":"VU382","bookingCode":"PNR132","passengerName":"Nguyễn Ngọc Phương","departureTime":"2026-07-27 11:45:00"}', 'SENT', 0, NULL, '2026-07-22 01:50:00', '2026-07-22 01:50:00', 'booking_confirmed:PNR132'),
(148, 'BOOKING_CONFIRMED', 76, 'EMAIL', 'user76@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"2565000","seatNo":"2A","currency":"VND","flightNo":"QH698","bookingCode":"PNR133","passengerName":"Trần Trọng Tú","departureTime":"2026-07-28 21:15:00"}', 'SENT', 0, NULL, '2026-07-23 11:23:00', '2026-07-23 11:23:00', 'booking_confirmed:PNR133'),
(149, 'BOOKING_CONFIRMED', 32, 'EMAIL', 'user32@gmail.com', '{"from":"Phú Quốc","to":"TP. Hồ Chí Minh","amount":"1050000","seatNo":"4F","currency":"VND","flightNo":"VN987","bookingCode":"PNR134","passengerName":"Huỳnh Thị Phương","departureTime":"2026-07-28 08:30:00"}', 'SENT', 0, NULL, '2026-07-24 02:37:00', '2026-07-24 02:37:00', 'booking_confirmed:PNR134'),
(150, 'BOOKING_CONFIRMED', 51, 'EMAIL', 'user51@gmail.com', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","amount":"1240000","seatNo":"17D","currency":"VND","flightNo":"VU803","bookingCode":"PNR135","passengerName":"Dương Thanh Lâm","departureTime":"2026-07-28 19:00:00"}', 'SENT', 0, NULL, '2026-07-27 11:06:00', '2026-07-27 11:06:00', 'booking_confirmed:PNR135'),
(151, 'BOOKING_CONFIRMED', 51, 'SMS', '098972249', '{"from":"Cam Ranh","to":"TP. Hồ Chí Minh","seatNo":"17D","flightNo":"VU803","bookingCode":"PNR135","departureTime":"2026-07-28 19:00:00"}', 'SENT', 0, NULL, '2026-07-27 11:06:00', '2026-07-27 11:06:00', 'booking_confirmed_sms:PNR135'),
(152, 'BOOKING_CONFIRMED', 6, 'EMAIL', 'user6@gmail.com', '{"from":"Quy Nhơn","to":"TP. Hồ Chí Minh","amount":"2050000","seatNo":"17D","currency":"VND","flightNo":"QH971","bookingCode":"PNR136","passengerName":"Phan Thị Vinh","departureTime":"2026-07-28 09:30:00"}', 'SENT', 0, NULL, '2026-07-27 06:36:00', '2026-07-27 06:36:00', 'booking_confirmed:PNR136'),
(153, 'BOOKING_CONFIRMED', 94, 'EMAIL', 'user94@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Đà Lạt","amount":"1980000","seatNo":"28F","currency":"VND","flightNo":"VJ497","bookingCode":"PNR137","passengerName":"Phan Thị Tuấn","departureTime":"2026-07-28 07:45:00"}', 'SENT', 0, NULL, '2026-07-22 22:55:00', '2026-07-22 22:55:00', 'booking_confirmed:PNR137'),
(154, 'BOOKING_CONFIRMED', 87, 'EMAIL', 'user87@gmail.com', '{"from":"Hà Nội","to":"Huế","amount":"960000","seatNo":"28F","currency":"VND","flightNo":"VN390","bookingCode":"PNR138","passengerName":"Huỳnh Thị Nam","departureTime":"2026-07-28 06:30:00"}', 'SENT', 0, NULL, '2026-07-26 05:34:00', '2026-07-26 05:34:00', 'booking_confirmed:PNR138'),
(155, 'BOOKING_CONFIRMED', 65, 'EMAIL', 'user65@gmail.com', '{"from":"Đà Nẵng","to":"Hà Nội","amount":"1620000","seatNo":"28E","currency":"VND","flightNo":"VU248","bookingCode":"PNR139","passengerName":"Dương Trọng Khang","departureTime":"2026-07-28 15:45:00"}', 'SENT', 0, NULL, '2026-07-24 10:48:00', '2026-07-24 10:48:00', 'booking_confirmed:PNR139'),
(156, 'BOOKING_CONFIRMED', 96, 'EMAIL', 'user96@gmail.com', '{"from":"Hà Nội","to":"Phú Quốc","amount":"1410000","seatNo":"28A","currency":"VND","flightNo":"VJ163","bookingCode":"PNR140","passengerName":"Đặng Minh Khang","departureTime":"2026-07-28 16:15:00"}', 'SENT', 0, NULL, '2026-07-27 15:24:00', '2026-07-27 15:24:00', 'booking_confirmed:PNR140'),
(157, 'BOOKING_CONFIRMED', 96, 'SMS', '097992281', '{"from":"Hà Nội","to":"Phú Quốc","seatNo":"28A","flightNo":"VJ163","bookingCode":"PNR140","departureTime":"2026-07-28 16:15:00"}', 'SENT', 0, NULL, '2026-07-27 15:24:00', '2026-07-27 15:24:00', 'booking_confirmed_sms:PNR140'),
(158, 'BOOKING_CONFIRMED', 13, 'EMAIL', 'user13@gmail.com', '{"from":"Phú Quốc","to":"TP. Hồ Chí Minh","amount":"1515000","seatNo":"2F","currency":"VND","flightNo":"VJ645","bookingCode":"PNR141","passengerName":"Ngô Hồng Minh","departureTime":"2026-07-28 21:45:00"}', 'SENT', 0, NULL, '2026-07-26 15:50:00', '2026-07-26 15:50:00', 'booking_confirmed:PNR141'),
(159, 'BOOKING_CONFIRMED', 44, 'EMAIL', 'user44@gmail.com', '{"from":"Vinh","to":"Hà Nội","amount":"1420000","seatNo":"15C","currency":"VND","flightNo":"VJ875","bookingCode":"PNR142","passengerName":"Đặng Ngọc Huy","departureTime":"2026-07-29 16:15:00"}', 'SENT', 0, NULL, '2026-07-24 09:25:00', '2026-07-24 09:25:00', 'booking_confirmed:PNR142'),
(160, 'BOOKING_CONFIRMED', 7, 'EMAIL', 'user7@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Cam Ranh","amount":"3495000","seatNo":"2A","currency":"VND","flightNo":"VU267","bookingCode":"PNR143","passengerName":"Nguyễn Đức Cường","departureTime":"2026-07-29 22:00:00"}', 'SENT', 0, NULL, '2026-07-27 20:07:00', '2026-07-27 20:07:00', 'booking_confirmed:PNR143'),
(161, 'BOOKING_CONFIRMED', 14, 'EMAIL', 'user14@gmail.com', '{"from":"Vinh","to":"Hà Nội","amount":"2180000","seatNo":"20F","currency":"VND","flightNo":"VU274","bookingCode":"PNR144","passengerName":"Huỳnh Văn Đạt","departureTime":"2026-07-29 17:30:00"}', 'SENT', 0, NULL, '2026-07-24 14:38:00', '2026-07-24 14:38:00', 'booking_confirmed:PNR144'),
(162, 'BOOKING_CONFIRMED', 94, 'EMAIL', 'user94@gmail.com', '{"from":"Huế","to":"TP. Hồ Chí Minh","amount":"2470000","seatNo":"5A","currency":"VND","flightNo":"VN450","bookingCode":"PNR145","passengerName":"Phan Thị Tuấn","departureTime":"2026-07-29 11:45:00"}', 'SENT', 0, NULL, '2026-07-28 09:54:00', '2026-07-28 09:54:00', 'booking_confirmed:PNR145'),
(163, 'BOOKING_CONFIRMED', 94, 'SMS', '091088359', '{"from":"Huế","to":"TP. Hồ Chí Minh","seatNo":"5A","flightNo":"VN450","bookingCode":"PNR145","departureTime":"2026-07-29 11:45:00"}', 'SENT', 0, NULL, '2026-07-28 09:54:00', '2026-07-28 09:54:00', 'booking_confirmed_sms:PNR145'),
(164, 'BOOKING_CONFIRMED', 44, 'EMAIL', 'user44@gmail.com', '{"from":"TP. Hồ Chí Minh","to":"Hải Phòng","amount":"1120000","seatNo":"6F","currency":"VND","flightNo":"VJ749","bookingCode":"PNR146","passengerName":"Đặng Ngọc Huy","departureTime":"2026-07-29 22:30:00"}', 'SENT', 0, NULL, '2026-07-28 13:34:00', '2026-07-28 13:34:00', 'booking_confirmed:PNR146'),
(165, 'BOOKING_CONFIRMED', 77, 'EMAIL', 'user77@gmail.com', '{"from":"Hà Nội","to":"Quy Nhơn","amount":"3000000","seatNo":"2F","currency":"VND","flightNo":"VJ337","bookingCode":"PNR147","passengerName":"Dương Kim Bình","departureTime":"2026-07-29 22:30:00"}', 'SENT', 0, NULL, '2026-07-26 14:38:00', '2026-07-26 14:38:00', 'booking_confirmed:PNR147'),
(166, 'BOOKING_CONFIRMED', 34, 'EMAIL', 'user34@gmail.com', '{"from":"Hà Nội","to":"Đà Lạt","amount":"2410000","seatNo":"17E","currency":"VND","flightNo":"VJ406","bookingCode":"PNR148","passengerName":"Hoàng Trọng Sơn","departureTime":"2026-07-29 14:15:00"}', 'SENT', 0, NULL, '2026-07-27 10:22:00', '2026-07-27 10:22:00', 'booking_confirmed:PNR148'),
(167, 'BOOKING_CONFIRMED', 30, 'EMAIL', 'user30@gmail.com', '{"from":"Quy Nhơn","to":"TP. Hồ Chí Minh","amount":"2505000","seatNo":"1D","currency":"VND","flightNo":"VN926","bookingCode":"PNR149","passengerName":"Nguyễn Ngọc Phương","departureTime":"2026-07-29 13:30:00"}', 'SENT', 0, NULL, '2026-07-24 10:40:00', '2026-07-24 10:40:00', 'booking_confirmed:PNR149'),
(168, 'BOOKING_CONFIRMED', 59, 'EMAIL', 'user59@gmail.com', '{"from":"Quy Nhơn","to":"Hà Nội","amount":"2115000","seatNo":"2E","currency":"VND","flightNo":"VJ305","bookingCode":"PNR150","passengerName":"Vũ Ngọc Giang","departureTime":"2026-07-30 07:00:00"}', 'SENT', 0, NULL, '2026-07-25 06:06:00', '2026-07-25 06:06:00', 'booking_confirmed:PNR150'),
(169, 'BOOKING_CONFIRMED', 59, 'SMS', '095260408', '{"from":"Quy Nhơn","to":"Hà Nội","seatNo":"2E","flightNo":"VJ305","bookingCode":"PNR150","departureTime":"2026-07-30 07:00:00"}', 'SENT', 0, NULL, '2026-07-25 06:06:00', '2026-07-25 06:06:00', 'booking_confirmed_sms:PNR150');

INSERT INTO flight_reminder_log (booking_id, flight_id, sent_at) VALUES
(83, 84, '2026-07-21 08:00:00'),
(84, 85, '2026-07-21 08:00:00'),
(85, 86, '2026-07-21 08:00:00'),
(86, 87, '2026-07-21 08:00:00'),
(87, 88, '2026-07-21 08:00:00'),
(88, 89, '2026-07-21 08:00:00'),
(89, 90, '2026-07-21 08:00:00'),
(90, 91, '2026-07-21 08:00:00'),
(91, 92, '2026-07-21 08:00:00');

SELECT setval('notification_id_seq', (SELECT MAX(id) FROM notification));
COMMIT;
