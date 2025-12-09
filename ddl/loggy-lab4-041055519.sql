-- 1) Create a database for Loggy
CREATE DATABASE IF NOT EXISTS loggydb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2) Create a dedicated user (optional, but good practice)
CREATE USER IF NOT EXISTS 'loggyuser'@'localhost' IDENTIFIED BY 'loggypass';

GRANT ALL PRIVILEGES ON loggydb.* TO 'loggyuser'@'localhost';
FLUSH PRIVILEGES;

-- 3) Use the new database
USE loggydb;

-- 4) Create the logs table
CREATE TABLE IF NOT EXISTS logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(60) NOT NULL,
    content VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
