INSERT INTO admin (
    admin_id, name, username, password, email,
    role_type, created_at, last_modified_at
) VALUES (
             'ADM001', 'System Admin', 'admin_user', 'encodedAdminPwd',
             'admin@example.com', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
         );


INSERT INTO teacher (
    teacher_id, username, teacher_name, password, email,
    role_type, approved, created_at, last_modified_at
) VALUES
      (
          'TCH001', 'lee_teacher', 'Lee Minho', 'encodedPwd1',
          'lee@example.com', 'TEACHER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
      ),
      (
          'TCH002', 'kim_teacher', 'Kim Hana', 'encodedPwd2',
          'kim@example.com', 'TEACHER', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
      );

INSERT INTO student (
    student_id, teacher_id, username, password, student_name,
    email, student_phone_number, student_parent_phone_number,
    student_address, student_detailed_address, role_type, approved
) VALUES
      (
          'STU001', 'TCH001', 'lee_student', 'securePass123', 'John Doe',
          'lee_student@example.com', '010-1234-5678', '010-9876-5432',
          'Seoul, Gangnam-gu', 'Apt 101, ABC Tower', 'STUDENT', TRUE
      ),
      (
          'STU002', 'TCH002', 'kim_student', 'pass456Secure', 'Jane Smith',
          'kim_student@example.com', '010-8765-4321', '010-1234-5678',
          'Busan, Haeundae-gu', 'Unit 305, XYZ Villa', 'STUDENT', FALSE
      );

