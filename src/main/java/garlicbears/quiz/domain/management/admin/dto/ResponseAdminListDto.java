package garlicbears.quiz.domain.management.admin.dto;

import java.util.List;

public class ResponseAdminListDto {
	private String sort;
	private int pageNumber;
	private int pageSize;
	private int totalPage;
	private long totalCount;
	private List<ResponseAdminDto> admins;

	public ResponseAdminListDto(String sort, int pageNumber, int pageSize, int totalPage, long totalCount,
		List<ResponseAdminDto> admins) {
		this.sort = sort;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.admins = admins;
	}

	public String getSort() {
		return sort;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public List<ResponseAdminDto> getAdmins() {
		return admins;
	}
}
