package br.edu.ifsp.dsw.myfinanceapi.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;

public class Transaction {
	private Integer id;
	private String description;
	private BigDecimal value;
	private TransactionType type;
	private Date dueDate;
	private Category category;
	
	public Transaction() {}
	
	public Transaction(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
