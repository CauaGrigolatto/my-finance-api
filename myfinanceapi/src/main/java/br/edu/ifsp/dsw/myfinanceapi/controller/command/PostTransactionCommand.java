package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostTransactionCommand extends AbstractJsonCommand {
	
	public PostTransactionCommand() {
		super();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {			
			String json = toJson(request);
			Transaction transaction = gson.fromJson(json, Transaction.class);
			log.info("Transaction created successfully", transaction);
		}
		catch(Throwable t) {
			log.error("Error on transaction creation", t);
		}
	}
}
