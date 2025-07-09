package br.edu.ifsp.dsw.myfinanceapi.dto;

public interface FilterDTO {
	void setPage(int page);
	int getPage();
	String buildWhere(boolean isCount);
}
