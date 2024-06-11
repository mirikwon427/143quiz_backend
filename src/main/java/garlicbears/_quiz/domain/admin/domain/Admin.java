package garlicbears._quiz.domain.admin.domain;

import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("'active'")
    private Active active;
}
