package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.DeleteTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("DELETE") && request.getPathInfo().equals("/transaction") && request.getParameter("id") != null;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new DeleteTransactionCommand().execute(request, response);
	}
}
