package garlicbears.quiz.domain.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_seq")
	private Long roleId;

	@Column(name = "role_name", nullable = false)
	private String roleName;

	public Role() {

	}
	public Role(String roleName) {
		this.roleName = roleName;
	}
	public Role(long roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}


}
