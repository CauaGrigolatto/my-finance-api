package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.model.dao.TransactionDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionCommand extends AbstractJsonCommand {
	
	private TransactionDAO transactionDAO;
	
	public DeleteTransactionCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.transactionDAO = new TransactionDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String json = toJson(request);
			Transaction transaction = gson.fromJson(json, Transaction.class);
			transactionDAO.delete(transaction);
			log.info("Transaction deleted successfully", transaction);
			transactionDAO.commit();
			response.setStatus(HttpStatus.SC_NO_CONTENT);
		}
		catch(Throwable t) {
			transactionDAO.rollback();
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on deleting transaction");
			throw t;
		}
	}
}
