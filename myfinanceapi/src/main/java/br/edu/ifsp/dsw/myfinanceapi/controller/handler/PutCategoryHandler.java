package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.PutCategoryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PutCategoryHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isPost = "PUT".equalsIgnoreCase(request.getMethod());
		boolean isPath = "/category".equals(request.getPathInfo());
		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;
		return isPost && isPath && hasBody;
	}

	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new PutCategoryCommand().execute(request, response);
	}
}
