package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.DeleteCategoryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteCategoryHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isDelete = request.getMethod().equals("DELETE");
		boolean isPath = request.getPathInfo().equals("/category");
		boolean hasId = request.getParameter("id") != null;
		return isDelete && isPath && hasId;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new DeleteCategoryCommand().execute(request, response);
	}
}
