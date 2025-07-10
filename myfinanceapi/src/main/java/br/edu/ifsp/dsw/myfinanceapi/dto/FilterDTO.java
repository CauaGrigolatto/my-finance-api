package br.edu.ifsp.dsw.myfinanceapi.dto;

public interface FilterDTO {
	void setOffset(int offset);
	int getOffset();
	void setLimit(int limit);
	int getLimit();
	int getPage();
	void setPage(int page);
	String buildWhere(boolean isCount);
}
