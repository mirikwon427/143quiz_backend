package garlicbears.quiz.domain.management.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RequestChangeRoleDto {
	@NotNull
	long adminId;
	@NotNull
	String roleName;

	public String getRoleName() {
		return roleName;
	}

	public long getAdminId(){
		return adminId;
	}

	public RequestChangeRoleDto(){

	}

	public RequestChangeRoleDto(long adminId, String roleName) {
		this.adminId = adminId;
		this.roleName = roleName;
	}
}
