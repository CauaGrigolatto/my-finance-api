package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetRevenuesSumCommand extends AbstractJsonCommand {
	
	private TransactionDAOImpl transactionDAO;
	
	public GetRevenuesSumCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAOImpl(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			ResponseDTO<BigDecimal> responseDTO;
			
			BigDecimal revenues = transactionDAO.sumRevenues();
			
			responseDTO = new ResponseDTO<BigDecimal>(
				HttpStatus.SC_OK,
				"Revenues added successfully.",
				revenues,
				null
			);
			
			log.info("Revenues added successfully", revenues);
			response.setStatus(HttpStatus.SC_OK);
			
			String json = gson.toJson(responseDTO);
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
			
			String responnseJson = gson.toJson(responseDTO);
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
