package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.TransactionFilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import br.edu.ifsp.dsw.myfinanceapi.model.enums.TransactionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionListCommand extends AbstractJsonCommand {
	
	private TransactionDAO transactionDAO;
	private CategoryDAO categoryDAO;
	
	public GetTransactionListCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAO(conn);
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			TransactionFilterDTO filter = toTransactionFilterDTO(request);
			List<Transaction> transactions = transactionDAO.findByFilter(filter);
			
			for (Transaction transaction : transactions) {
				Category category = categoryDAO.findById(transaction.getCategory().getId());
				transaction.setCategory(category);
			}
			
			log.info("Transactions consulted successfully");
			String json = gson.toJson(transactions);
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on finding all transactions");
			throw t;
		}
		finally {			
			transactionDAO.rollback();
		}
	}
	
	private TransactionFilterDTO toTransactionFilterDTO(HttpServletRequest request) {
		TransactionFilterDTO filter = new TransactionFilterDTO();
		
		
		filter.description = request.getParameter("description");
		
		BigDecimal value = null;
		
		try {
			value = new BigDecimal(request.getParameter("value"));
		}
		catch(Throwable t) {
			value = null;
		}
		
		filter.value = value;
		filter.month = NumberUtils.toInt(request.getParameter("month"), -1);
		filter.year = NumberUtils.toInt(request.getParameter("year"), -1);
		filter.categoryId = NumberUtils.toInt(request.getParameter("category"), -1);
		
		TransactionType type = null;
		
		try {
			type = TransactionType.valueOf(request.getParameter("type"));
		}
		catch (Throwable t) {
			type = null;
		}
		
		filter.type = type;
		
		return filter;
	}
}
