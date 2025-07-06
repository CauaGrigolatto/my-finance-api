package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.GetTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && request.getPathInfo().equals("/transaction");
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new GetTransactionCommand().execute(request, response);
	}
}
