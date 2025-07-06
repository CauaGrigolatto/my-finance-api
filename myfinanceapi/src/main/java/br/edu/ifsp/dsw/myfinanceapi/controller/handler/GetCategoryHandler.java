package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.GetCategoryCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetCategoryHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isGet = request.getMethod().equals("GET");
		boolean isPath = request.getPathInfo().equals("/category");
		boolean hasId = request.getParameter("id") != null;
		return  isGet && isPath && hasId;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new GetCategoryCommand().execute(request, response);
	}
}
