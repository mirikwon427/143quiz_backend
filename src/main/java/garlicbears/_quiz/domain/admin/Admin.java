package garlicbears._quiz.domain.admin;

import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_seq")
    private long adminId;

    @Column(name = "admin_email", nullable = false, unique = true, length = 200)
    private String adminEmail;

    @Column(name = "admin_password", nullable = false, length = 100)
    private String adminPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_active")
    private Active active;
}
