package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	    String description = request.getParameter("description");
	    if (StringUtils.isNotBlank(description)) {
	        filter.setDescription(description.trim());
	    }

	    String valueStr = request.getParameter("value");
	    if (StringUtils.isNotBlank(valueStr)) {
	        try {
	            BigDecimal value = new BigDecimal(valueStr);
	            filter.setValue(value);
	        } catch (NumberFormatException ex) {
	            filter.setValue(null);
	        }
	    }

	    int month = NumberUtils.toInt(request.getParameter("month"), -1);
	    filter.setMonth(month);

	    int year = NumberUtils.toInt(request.getParameter("year"), -1);
	    filter.setYear(year);

	    int categoryId = NumberUtils.toInt(request.getParameter("category"), -1);
	    filter.setCategoryId(categoryId);

	    String typeStr = request.getParameter("type");
	    if (StringUtils.isNotBlank(typeStr)) {
	        try {
	            TransactionType type = TransactionType.valueOf(typeStr);
	            filter.setType(type);
	        } catch (IllegalArgumentException ex) {
	            filter.setType(null);
	        }
	    }

	    int page = NumberUtils.toInt(request.getParameter("page"), 1);
	    if (page < 1) {
	        page = 1;
	    }

	    int pageSize = NumberUtils.toInt(request.getParameter("size"), 2);
	    if (pageSize < 1) {
	        pageSize = 10;
	    }

	    int offset = (page - 1) * pageSize;

	    filter.setLimit(pageSize);
	    filter.setOffset(offset);

	    return filter;
	}
}
