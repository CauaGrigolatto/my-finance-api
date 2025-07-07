package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.PostCategoryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCategoryHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
		boolean isPath = "/category".equals(request.getPathInfo());
		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;

		return isPost && isPath && hasBody;
	}

	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new PostCategoryCommand().execute(request, response);
	}
}
