package garlicbears.quiz.domain.common.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
