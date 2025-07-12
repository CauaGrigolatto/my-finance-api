package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.Date;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.adapter.DateAdapter;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionCommand extends AbstractJsonCommand {
	
	private TransactionDAOImpl transactionDAO;
	private CategoryDAOImpl categoryDAO;
	
	public GetTransactionCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAOImpl(conn);
		this.categoryDAO = new CategoryDAOImpl(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			ResponseDTO<Transaction> responseDTO;
			Transaction transaction = transactionDAO.findById(id);
			
			if (transaction == null) {
				responseDTO = new ResponseDTO<Transaction>(
					HttpStatus.SC_NOT_FOUND,
					"Transaction with ID " + id + " not found.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			else {
				if (transaction.getCategory() != null) {
					Category category = categoryDAO.findById(transaction.getCategory().getId());					
					transaction.setCategory(category);
				}
				
				responseDTO = new ResponseDTO<Transaction>(
					HttpStatus.SC_OK,
					null,
					transaction,
					null
				);
				
				log.info("Transaction consulted successfully", transaction);
				response.setStatus(HttpStatus.SC_OK);
			}
			
			Gson transactionSerializer = new GsonBuilder()
			        .registerTypeAdapter(java.util.Date.class, new DateAdapter())
			        .registerTypeAdapter(java.sql.Date.class, new DateAdapter())
			        .create();
						
			String json = transactionSerializer.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			log.error("Error on retrieving transactions");
			
			ResponseDTO<Transaction> responseDTO = new ResponseDTO<Transaction>(
				HttpStatus.SC_INTERNAL_SERVER_ERROR,
				"An error occurred while processing your request.",
				null,
				null
			);
			
			Gson transactionSerializer = new GsonBuilder()
			        .registerTypeAdapter(Date.class, new DateAdapter())
			        .create();
			
			String responnseJson = transactionSerializer.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			throw t;
		}
		finally {			
			transactionDAO.rollback();
		}
	}
}
