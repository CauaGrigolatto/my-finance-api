package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.sql.Connection;
import java.util.List;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;

public class TransactionDAO extends BasicDAO<Transaction> {

	public TransactionDAO(Connection conn) throws Throwable {
		super(conn);
		
	}

	@Override
	public void save(Transaction entity) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(Transaction entity) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Transaction findById(Integer id) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> findByFilter() throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	
}
