package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ErrorFieldDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PutTransactionCommand extends AbstractJsonCommand {
	
	private TransactionDAO transactionDAO;
	
	public PutTransactionCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			String json = toJson(request);
			
			Transaction transaction = gson.fromJson(json, Transaction.class);
			transaction.setId(id);
			
			Transaction transactionToUpdate = transactionDAO.findById(id);

			ResponseDTO<Transaction> responseDTO;
			
			if (transactionToUpdate == null) {
				responseDTO = new ResponseDTO<Transaction>(
					HttpStatus.SC_NOT_FOUND,
					"Could not update transaction. Please, check if it still exists.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			else {
				List<ErrorFieldDTO> errors = new LinkedList<ErrorFieldDTO>();
				
				if (transaction.getValue() == null) {
					ErrorFieldDTO error = new ErrorFieldDTO("value", "Value cannot be empty.");
					errors.add(error);
				}
				
				if (transaction.getType() == null) {
					ErrorFieldDTO error = new ErrorFieldDTO("type", "Type cannot be empty.");
					errors.add(error);
				}
				
				if (! errors.isEmpty()) {
					transactionDAO.rollback();

					responseDTO = new ResponseDTO<Transaction>(
						HttpStatus.SC_BAD_REQUEST,
						"Request validation failed.",
						null,
						errors
					);
					
					response.setStatus(HttpStatus.SC_BAD_REQUEST);
				}
				else {
					transactionDAO.update(transaction);
					log.info("Transaction updated successfully", transaction);
					transactionDAO.commit();

					responseDTO = new ResponseDTO<Transaction>(
						HttpStatus.SC_OK,
						"Transaction updated successfully.",
						transaction,
						errors
					);
					
					response.setStatus(HttpStatus.SC_OK);
				}
			}
						
			String responnseJson = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
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
