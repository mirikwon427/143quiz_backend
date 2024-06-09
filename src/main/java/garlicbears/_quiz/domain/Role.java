package garlicbears._quiz.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_seq")
    private long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    //  BaseTimeEntity

}
