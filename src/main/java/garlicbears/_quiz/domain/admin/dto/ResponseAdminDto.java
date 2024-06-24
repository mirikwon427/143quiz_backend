package garlicbears._quiz.domain.admin.dto;

public class ResponseAdminDto {
	private long adminId;
	private String adminEmail;
	private String adminActive;

	public ResponseAdminDto(long adminId, String adminEmail, String adminActive) {
		this.adminId = adminId;
		this.adminEmail = adminEmail;
		this.adminActive = adminActive;
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
