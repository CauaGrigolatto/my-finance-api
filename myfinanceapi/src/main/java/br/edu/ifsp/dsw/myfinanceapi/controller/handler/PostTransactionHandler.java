package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.PostTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostTransactionHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
		boolean isPath = "/transaction".equals(request.getPathInfo());
		boolean isJson = "application/json".equalsIgnoreCase(request.getContentType());

		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;

		return isPost && isPath && isJson && hasBody;
	}

	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new PostTransactionCommand().execute(request, response);
	}
}
