DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS student_payment;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS name_outbox_event;

-- 리프레시 토큰 테이블
CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expiry_date DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);


CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id VARCHAR(255),
    name VARCHAR(255),
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255) DEFAULT '이메일을 입력해 주세요',
    role_type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE teacher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255),
    teacher_name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    role_type VARCHAR(255),
    approved BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(255) NOT NULL UNIQUE,
    teacher_id VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    student_phone_number VARCHAR(255) NOT NULL,
    student_parent_phone_number VARCHAR(255) NOT NULL,
    student_address VARCHAR(255) NOT NULL,
    student_detailed_address VARCHAR(255) NOT NULL,
    role_type VARCHAR(255),
    approved BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE student_payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(255) NOT NULL,
    tuition_amount INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 이름 변경 아웃박스 이벤트 테이블
CREATE TABLE name_outbox_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(255),
    member_id VARCHAR(255),
    old_name VARCHAR(255),
    new_name VARCHAR(255),
    processed BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

