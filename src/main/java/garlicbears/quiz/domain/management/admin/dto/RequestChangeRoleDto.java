package garlicbears.quiz.domain.management.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RequestChangeRoleDto {
	@NotNull
	long adminId;
	@NotNull
	String role;

	public String getRole() {
		return role;
	}

	public long getAdminId(){
		return adminId;
	}

	public RequestChangeRoleDto(){

	}

	public RequestChangeRoleDto(long adminId, String role) {
		this.adminId = adminId;
		this.role = role;
	}
}
