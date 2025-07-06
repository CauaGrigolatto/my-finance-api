package br.edu.ifsp.dsw.myfinanceapi.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;

public class TransactionFilterDTO implements FilterDTO {
	public String description;
	public BigDecimal value;
	public int month;
	public int year;
	public TransactionType type;
	public int categoryId;
	
	@Override
	public String buildWhere() {
		StringBuilder where = new StringBuilder();
		where.append("WHERE 1 = 1 ");
		
		if (StringUtils.isNotBlank(description)) {
			where.append(" AND t.description LIKE '%" + description + "%'");
		}
		
		if (value != null) {
			where.append(" AND t.value = " + value);
		}
		
		if (month != -1) {
			where.append(" AND MONTH(t.due_date) = " + month);
		}
		
		if (year != -1) {
			where.append(" AND YEAR(t.due_date) = " + year);
		}
		
		if (type != null) {
			where.append(" AND t.type = '" + type + "'");
		}
		
		if (categoryId != -1) {
			where.append(" AND t.category_id = " + categoryId);
		}
		
		return where.toString();
	}

}