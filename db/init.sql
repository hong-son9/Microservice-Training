-- ============================================================
-- Shoes Microservices — DB initialization
-- Tao 5 schema rieng cho 5 service.
-- Chay 1 lan tren MySQL 8 truoc khi start cac service.
-- ============================================================

CREATE DATABASE IF NOT EXISTS identity_db   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS product_db    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS cart_db       CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS order_db      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS promotion_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- (Optional) Tao user rieng cho ung dung, han che quyen cho root
-- CREATE USER IF NOT EXISTS 'shoes_app'@'localhost' IDENTIFIED BY 'shoes_pwd';
-- GRANT ALL PRIVILEGES ON identity_db.*   TO 'shoes_app'@'localhost';
-- GRANT ALL PRIVILEGES ON product_db.*    TO 'shoes_app'@'localhost';
-- GRANT ALL PRIVILEGES ON cart_db.*       TO 'shoes_app'@'localhost';
-- GRANT ALL PRIVILEGES ON order_db.*      TO 'shoes_app'@'localhost';
-- GRANT ALL PRIVILEGES ON promotion_db.*  TO 'shoes_app'@'localhost';
-- FLUSH PRIVILEGES;

SHOW DATABASES LIKE '%_db';
