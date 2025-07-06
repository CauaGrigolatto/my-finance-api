package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;

public class TransactionDAO extends BasicDAO<Transaction> {

	public TransactionDAO(Connection conn) throws Throwable {
		super(conn);
		
	}

	@Override
	public void save(Transaction transaction) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO transaction (description, value, type, due_date, category_id) VALUES ");
			sql.append("(?,?,?,?,?) ");
			PreparedStatement ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, transaction.getDescription());
			ps.setBigDecimal(2, transaction.getValue());
			ps.setString(3, transaction.getType().toString());
			ps.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));
			ps.setInt(5, transaction.getCategory().getId());
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
				Integer id = rs.getInt(1);
				transaction.setId(id);
			}
		}
		catch(Throwable t) {
			log.error("Error on transaction saving", t); 
			throw t;
		}
	}

	@Override
	public boolean delete(Transaction transaction) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM transaction WHERE transaction_id = ? ");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, transaction.getId());
			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Throwable t) {
			log.error("Error on deleting transaction", t);
			throw t;
		}
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
