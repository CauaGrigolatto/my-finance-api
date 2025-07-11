package br.edu.ifsp.dsw.myfinanceapi.dto;

import org.apache.commons.lang3.StringUtils;

public class CategoryFilterDTO implements FilterDTO {
	private String title;
	private int limit;
	private int offset;
	private int page;
	private boolean unpaged;
	
	public CategoryFilterDTO() {
	
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public int getPage() {
		return page;
	}
	
	public boolean isUnpaged() {
		return unpaged;
	}

	public void setUnpaged(boolean unpaged) {
		this.unpaged = unpaged;
	}

	@Override
	public String buildWhere(boolean isCount) {
		StringBuilder where = new StringBuilder();
		where.append("WHERE 1 = 1");

		if (StringUtils.isNotBlank(title)) {
			where.append(" AND c.title LIKE ?");
		}
		
		if (!isCount && !unpaged) {
			where.append(" LIMIT ? OFFSET ?");			
		}

		return where.toString();
	}
}
