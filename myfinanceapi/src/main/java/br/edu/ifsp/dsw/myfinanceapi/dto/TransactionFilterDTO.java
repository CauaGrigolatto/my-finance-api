package br.edu.ifsp.dsw.myfinanceapi.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;

public class TransactionFilterDTO implements FilterDTO {
	private String description;
	private BigDecimal value;
	private Integer month;
	private Integer year;
	private TransactionType type;
	private Integer categoryId;
	private int limit;
	private int offset;
	private int page;
	private boolean unpaged;

	public TransactionFilterDTO() {
		this.month = -1;
		this.year = -1;
		this.categoryId = -1;
		this.limit = 10;
		this.offset = 0;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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

		if (StringUtils.isNotBlank(description)) {
			where.append(" AND t.description LIKE ?");
		}

		if (value != null) {
			where.append(" AND t.value = ?");
		}

		if (month != null && month != -1) {
			where.append(" AND MONTH(t.due_date) = ?");
		}

		if (year != null && year != -1) {
			where.append(" AND YEAR(t.due_date) = ?");
		}

		if (type != null) {
			where.append(" AND t.type = ?");
		}

		if (categoryId != null && categoryId != -1) {
			where.append(" AND t.category_id = ?");
		}
				
		if (! isCount) {
			where.append(" ORDER BY t.transaction_id DESC");	
			where.append(" LIMIT ? OFFSET ?");			
		}

		return where.toString();
	}
}
