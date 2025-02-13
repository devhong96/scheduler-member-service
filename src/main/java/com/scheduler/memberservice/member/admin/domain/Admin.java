package com.scheduler.memberservice.member.admin.domain;

import com.scheduler.memberservice.member.common.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.scheduler.memberservice.member.admin.dto.AdminInfoRequest.EditEmailRequest;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "admin")
@NoArgsConstructor(access = PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String adminId;

    private String name;

    private String username;

    private String password;

    @Column(columnDefinition = "varchar(255) default '이메일을 입력해 주세요'")
    private String email;

    @Enumerated(STRING)
    private RoleType roleType;

    public void updatePassword(PasswordEncoder passwordEncoder, String newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public void updateEmail(EditEmailRequest editEmailRequest) {
        this.email = editEmailRequest.getEmail();
    }

    public static Admin create(String username, String password, String email, PasswordEncoder passwordEncoder) {
        Admin admin = new Admin();
        admin.username = username;
        admin.password = passwordEncoder.encode(password);
        admin.adminId = UUID.randomUUID().toString();
        admin.email = email;
        admin.name = UUID.randomUUID().toString();
        admin.roleType = RoleType.ADMIN;
        return admin;

    }
}
