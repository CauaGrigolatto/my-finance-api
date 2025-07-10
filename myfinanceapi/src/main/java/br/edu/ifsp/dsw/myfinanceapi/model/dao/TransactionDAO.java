package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.math.BigDecimal;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;

public interface TransactionDAO {
	BigDecimal sumExpenses(Category category) throws Throwable;
	BigDecimal sumRevenues(Category category) throws Throwable;
	BigDecimal getBalance() throws Throwable;
}
