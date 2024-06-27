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
	//스프링 시큐리티는 기본적으로 ROLE_ 접두사를 사용하여 권한을 처리한다.
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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
