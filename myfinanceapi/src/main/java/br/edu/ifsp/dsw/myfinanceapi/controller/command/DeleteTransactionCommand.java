package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionCommand extends AbstractJsonCommand {
	
	private TransactionDAOImpl transactionDAO;
	
	public DeleteTransactionCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAOImpl(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			Transaction transaction = new Transaction(id);
			boolean deletedSuccessfully = transactionDAO.delete(transaction);
			
			ResponseDTO<Transaction> responseDTO;
			
			if (deletedSuccessfully) {
				transactionDAO.commit();
				log.info("Transaction deleted successfully", transaction);
				response.setStatus(HttpStatus.SC_NO_CONTENT);
			}
			else {
				transactionDAO.rollback();
				responseDTO = new ResponseDTO<Transaction>(
					HttpStatus.SC_NOT_FOUND,
					"Transaction with ID " + id + " not found.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
				
				String responnseJson = gson.toJson(responseDTO);
				response.setContentType("application/json");
				response.getWriter().write(responnseJson);
			}
			
		}
		catch(Throwable t) {
			transactionDAO.rollback();
			log.error("Error on transaction update");
			
			ResponseDTO<Transaction> responseDTO = new ResponseDTO<Transaction>(
				HttpStatus.SC_INTERNAL_SERVER_ERROR,
				"An error occurred while processing your request.",
				null,
				null
			);
			
			String responnseJson = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			throw t;
		}
	}
}
