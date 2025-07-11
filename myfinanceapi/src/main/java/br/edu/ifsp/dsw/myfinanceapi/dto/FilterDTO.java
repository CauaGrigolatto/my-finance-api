package br.edu.ifsp.dsw.myfinanceapi.dto;

public interface FilterDTO {
	void setOffset(int offset);
	int getOffset();
	void setLimit(int limit);
	int getLimit();
	int getPage();
	void setPage(int page);
	boolean isUnpaged();
	void setUnpaged(boolean unpaged);
	String buildWhere(boolean isCount);
}
