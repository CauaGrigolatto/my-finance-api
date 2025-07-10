package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.DeleteTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isDelete = request.getMethod().equals("DELETE");
		
		String pathInfo = request.getPathInfo();
		
		boolean isValidPath = pathInfo != null && pathInfo.matches("^/transaction/\\d+$");
		
		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;
		
		return isDelete && isValidPath && hasBody;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new DeleteTransactionCommand().execute(request, response);
	}
}
