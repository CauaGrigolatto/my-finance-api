package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.DeleteCategoryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteCategoryHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isDelete = request.getMethod().equals("DELETE");
		
		String pathInfo = request.getPathInfo();
		
		boolean isValidPath = pathInfo != null && pathInfo.matches("^/category/\\d+$");
		
		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;
		
		return isDelete && isValidPath && hasBody;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new DeleteCategoryCommand().execute(request, response);
	}
}
