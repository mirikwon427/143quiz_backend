package garlicbears.quiz.domain.management.admin.dto;

import garlicbears.quiz.domain.common.entity.Active;

public class ResponseAdminDto {
	private long adminId;
	private String adminEmail;
	private String adminActive;

	public ResponseAdminDto(long adminId, String adminEmail, Active adminActive) {
		this.adminId = adminId;
		this.adminEmail = adminEmail;
		this.adminActive = adminActive.name();
	}

	public long getAdminId() {
		return adminId;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public String getAdminActive() {
		return adminActive;
	}
}
