package br.edu.ifsp.dsw.myfinanceapi.dto;

import org.apache.commons.lang3.StringUtils;

public class CategoryFilterDTO implements FilterDTO {
	private String title;
	
	public CategoryFilterDTO() {
	
	}

	@Override
	public String buildWhere(boolean isCount) {
		StringBuilder where = new StringBuilder();
		where.append("WHERE 1 = 1");

		if (StringUtils.isNotBlank(title)) {
			where.append(" AND c.title LIKE ?");
		}

		return where.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setPage(int page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPage() {
		// TODO Auto-generated method stub
		return 0;
	}
}
