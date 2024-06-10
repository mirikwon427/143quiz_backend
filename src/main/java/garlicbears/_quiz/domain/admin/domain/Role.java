package garlicbears._quiz.domain.admin.domain;

import garlicbears._quiz.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_seq")
    private long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

}
