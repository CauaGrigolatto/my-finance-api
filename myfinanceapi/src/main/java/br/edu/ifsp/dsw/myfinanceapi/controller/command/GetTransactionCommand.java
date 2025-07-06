package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionCommand extends AbstractJsonCommand {
	
	private TransactionDAO transactionDAO;
	private CategoryDAO categoryDAO;
	
	public GetTransactionCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAO(conn);
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String idStr = request.getParameter("id");
			Integer id = Integer.parseInt(idStr);
			
			Transaction transaction = transactionDAO.findById(id);
			
			if (transaction == null) {
				response.setStatus(HttpStatus.SC_NOT_FOUND);
				return;
			}
			
			Category category = categoryDAO.findById(transaction.getCategory().getId());
			
			transaction.setCategory(category);
			
			log.info("Transaction consulted successfully", transaction);
			String json = gson.toJson(transaction);
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on finding transaction by ID");
			throw t;
		}
		finally {			
			transactionDAO.rollback();
		}
	}
}
