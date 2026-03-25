CREATE DATABASE IF NOT EXISTS regalia DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE regalia;

-- 1. 장비 그룹
CREATE TABLE device_group (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. 장비 (모니터링 대상)
CREATE TABLE device (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id            BIGINT,
    name                VARCHAR(100) NOT NULL,
    url                 VARCHAR(500) NOT NULL,
    description         VARCHAR(255),
    pos_x               DOUBLE DEFAULT 0,
    pos_y               DOUBLE DEFAULT 0,
    check_interval_sec  INT DEFAULT 300,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_device_group FOREIGN KEY (group_id) REFERENCES device_group(id) ON DELETE SET NULL
);

-- 3. 토폴로지 연결선 (source_id < target_id 보장 + 중복 방지)
CREATE TABLE device_link (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_id   BIGINT NOT NULL,
    target_id   BIGINT NOT NULL,
    label       VARCHAR(100),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_link_source FOREIGN KEY (source_id) REFERENCES device(id) ON DELETE CASCADE,
    CONSTRAINT fk_link_target FOREIGN KEY (target_id) REFERENCES device(id) ON DELETE CASCADE,
    CONSTRAINT chk_link_order CHECK (source_id < target_id),
    UNIQUE KEY uq_device_link (source_id, target_id)
);

-- 4. 장비 현재 상태
CREATE TABLE device_status (
    device_id           BIGINT PRIMARY KEY,
    current_status      ENUM('GREEN','YELLOW','RED') NOT NULL DEFAULT 'GREEN',
    last_response_time  INT,
    consecutive_fails   INT DEFAULT 0,
    last_checked_at     DATETIME,
    CONSTRAINT fk_status_device FOREIGN KEY (device_id) REFERENCES device(id) ON DELETE CASCADE
);

-- 5. 체크 이력 로그 (30일 보관)
CREATE TABLE check_log (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id       BIGINT NOT NULL,
    status          ENUM('GREEN','YELLOW','RED') NOT NULL,
    response_time   INT,
    status_code     INT,
    is_success      TINYINT(1) NOT NULL DEFAULT 0,
    checked_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_device FOREIGN KEY (device_id) REFERENCES device(id) ON DELETE CASCADE,
    INDEX idx_device_checked (device_id, checked_at),
    INDEX idx_checked_at (checked_at)
);