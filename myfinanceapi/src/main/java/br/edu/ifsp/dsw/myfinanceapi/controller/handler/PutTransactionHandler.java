package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.PutTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PutTransactionHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isPost = "PUT".equalsIgnoreCase(request.getMethod());
		
		String pathInfo = request.getPathInfo();
		
		boolean isValidPath = pathInfo != null && pathInfo.matches("^/transaction/\\d+$");
		
	    boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;
		
		return isPost && isValidPath && hasBody;
	}

	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new PutTransactionCommand().execute(request, response);
	}
}
