package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.math.BigDecimal;

public interface TransactionDAO {
	BigDecimal sumExpenses() throws Throwable;
	BigDecimal sumRevenues() throws Throwable;
	BigDecimal getBalance() throws Throwable;
}
