package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import br.edu.ifsp.dsw.myfinanceapi.dto.FilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.TransactionFilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;

public class TransactionDAOImpl extends BasicDAO<Transaction> implements TransactionDAO {

	public TransactionDAOImpl(Connection conn) throws Throwable {
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
			ps.setObject(5, transaction.getCategory() != null ? transaction.getCategory().getId() : null, Types.INTEGER);
			
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
				Integer id = rs.getInt(1);
				transaction.setId(id);
			}
		}
		catch(Throwable t) {
			log.error("Error on transaction saving"); 
			throw t;
		}
	}
	
	@Override
	public boolean update(Transaction transaction) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE transaction SET ");
			sql.append("description = ?, ");
			sql.append("value = ?, ");
			sql.append("type = ?, ");
			sql.append("due_date = ?, ");
			sql.append("category_id = ? ");
			sql.append("WHERE transaction_id = ? ");
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, transaction.getDescription());
			ps.setBigDecimal(2, transaction.getValue());
			ps.setString(3, transaction.getType().toString());
			ps.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));
			ps.setObject(5, transaction.getCategory() != null ? transaction.getCategory().getId() : null, Types.INTEGER);
			ps.setInt(6, transaction.getId());
			
			int rows = ps.executeUpdate();
			
			return rows > 0;
		}
		catch(Throwable t) {
			log.error("Error on updating transaction");
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
			log.error("Error on deleting transaction");
			throw t;
		}
	}

	@Override
	public Transaction findById(Integer id) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.transaction_id AS transactionId, ");
			sql.append("t.description AS description, ");
			sql.append("t.value AS value, ");
			sql.append("t.type AS type, ");
			sql.append("t.due_date AS dueDate, ");
			sql.append("t.category_id AS categoryId ");
			sql.append("FROM transaction t ");
			sql.append("WHERE t.transaction_id = ? ");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Transaction transaction = null;
			
			if (rs.next()) {				
				transaction = buildEntity(rs);
			}
			
			return transaction;
		}
		catch(Throwable t) {
			log.error("Error on finding transaction by id");
			throw t;
		}
	}

	@Override
	public List<Transaction> findByFilter(FilterDTO filterDTO) throws Throwable {
		try {
			TransactionFilterDTO transactionFilterDTO = (TransactionFilterDTO) filterDTO;

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t.transaction_id AS transactionId, ");
			sql.append("t.description AS description, ");
			sql.append("t.value AS value, ");
			sql.append("t.type AS type, ");
			sql.append("t.due_date AS dueDate, ");
			sql.append("t.category_id AS categoryId ");
			sql.append("FROM transaction t ");
			sql.append(transactionFilterDTO.buildWhere(false));
			
			int index = 1;
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (StringUtils.isNotBlank(transactionFilterDTO.getDescription())) {
			    ps.setString(index++, "%" + transactionFilterDTO.getDescription() + "%");
			}

			if (transactionFilterDTO.getValue() != null) {
			    ps.setBigDecimal(index++, transactionFilterDTO.getValue());
			}

			if (transactionFilterDTO.getMonth() != null && transactionFilterDTO.getMonth() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getMonth());
			}

			if (transactionFilterDTO.getYear() != null && transactionFilterDTO.getYear() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getYear());
			}

			if (transactionFilterDTO.getType() != null) {
			    ps.setString(index++, transactionFilterDTO.getType().name());
			}

			if (transactionFilterDTO.getCategoryId() != null && transactionFilterDTO.getCategoryId() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getCategoryId());
			}

			ps.setInt(index++, transactionFilterDTO.getLimit());

			ps.setInt(index++, transactionFilterDTO.getOffset());

			ResultSet rs = ps.executeQuery();
			
			List<Transaction> transactions = new LinkedList<Transaction>();
			
			while (rs.next()) {
				Transaction transaction = buildEntity(rs);
				transactions.add(transaction);
			}
			
			return transactions;
		}
		catch(Throwable t) {
			log.error("Error on finding by filter");
			throw t;
		}
	}
	
	@Override
	public long count(FilterDTO filterDTO) throws Throwable {
		try {
			TransactionFilterDTO transactionFilterDTO = (TransactionFilterDTO) filterDTO;

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(t.transaction_id) AS result ");
			sql.append("FROM transaction t ");
			sql.append(transactionFilterDTO.buildWhere(true));
			
			int index = 1;
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (StringUtils.isNotBlank(transactionFilterDTO.getDescription())) {
			    ps.setString(index++, "%" + transactionFilterDTO.getDescription() + "%");
			}

			if (transactionFilterDTO.getValue() != null) {
			    ps.setBigDecimal(index++, transactionFilterDTO.getValue());
			}

			if (transactionFilterDTO.getMonth() != null && transactionFilterDTO.getMonth() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getMonth());
			}

			if (transactionFilterDTO.getYear() != null && transactionFilterDTO.getYear() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getYear());
			}

			if (transactionFilterDTO.getType() != null) {
			    ps.setString(index++, transactionFilterDTO.getType().name());
			}

			if (transactionFilterDTO.getCategoryId() != null && transactionFilterDTO.getCategoryId() != -1) {
			    ps.setInt(index++, transactionFilterDTO.getCategoryId());
			}

			ResultSet rs = ps.executeQuery();
			
			long result = 0;
			
			if (rs.next()) {
				result = rs.getLong("result");
			}
			
			return result;
		}
		catch(Throwable t) {
			log.error("Error on counting by filter");
			throw t;
		}
	}

	@Override
	protected Transaction buildEntity(ResultSet resultSet) throws Throwable {
		Integer transactionId = resultSet.getInt("transactionId");
		String description = resultSet.getString("description");
		BigDecimal value = resultSet.getBigDecimal("value");
		TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
		Date dueDate = resultSet.getDate("dueDate");
		Integer categoryId = resultSet.getInt("categoryId");
		
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setDescription(description);
		transaction.setValue(value);
		transaction.setType(type);
		transaction.setDueDate(dueDate);
		transaction.setCategory(new Category(categoryId));
		
		return transaction;
	}

	@Override
	public BigDecimal sumExpenses(Category category) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT SUM(t.value) as expensesSum ");
			sql.append("FROM transaction t ");
			sql.append("WHERE t.type = 'EXPENSE' ");
			
			if (category != null) {
				sql.append("AND t.category_id = ? ");
			}
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (category != null) {
				ps.setInt(1, category.getId());
			}
			
			ResultSet rs = ps.executeQuery();
			
			BigDecimal expensesSum = BigDecimal.ZERO;
			
			if (rs.next()) {
				expensesSum = Optional.ofNullable(rs.getBigDecimal("expensesSum")).orElse(BigDecimal.ZERO);
			}
			
			return expensesSum.multiply(BigDecimal.valueOf(-1));
		}
		catch(Throwable t) {
			log.error("Error on adding expenses");
			throw t;
		}
	}

	@Override
	public BigDecimal sumRevenues(Category category) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT SUM(t.value) as revenuesSum ");
			sql.append("FROM transaction t ");
			sql.append("WHERE t.type = 'REVENUE' ");
			
			if (category != null) {
				sql.append("AND t.category_id = ? ");
			}
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (category != null) {
				ps.setInt(1, category.getId());
			}
			
			ResultSet rs = ps.executeQuery();
			
			BigDecimal revenuesSum = BigDecimal.ZERO;
			
			if (rs.next()) {
				revenuesSum = Optional.ofNullable(rs.getBigDecimal("revenuesSum")).orElse(BigDecimal.ZERO);;
			}
			
			return revenuesSum;
		}
		catch(Throwable t) {
			log.error("Error on adding revenues");
			throw t;
		}
	}

	@Override
	public BigDecimal getBalance() throws Throwable {
		try {
			BigDecimal revenues = sumRevenues(null);
			BigDecimal expenses = sumExpenses(null);
			return revenues.add(expenses);
		}
		catch(Throwable t) {
			log.error("Error on getting balance");
			throw t;
		}
	}
}
