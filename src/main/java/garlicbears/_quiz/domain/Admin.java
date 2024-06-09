package garlicbears._quiz.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_seq")
    private long adminId;

    @Column(name = "admin_email", nullable = false, unique = true, length = 200)
    private String adminEmail;

    @Column(name = "admin_password", nullable = false, length = 100)
    private String adminPassword;

    //  BaseTimeEntity
    //  Active
}
