package garlicbears._quiz.domain.admin.entity;

import garlicbears._quiz.global.entity.BaseTimeEntity;
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
