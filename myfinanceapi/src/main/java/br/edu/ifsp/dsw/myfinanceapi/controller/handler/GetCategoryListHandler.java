package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.GetCategoryListCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetCategoryListHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isGet = request.getMethod().equals("GET");
		boolean isValidPath = request.getPathInfo().equals("/category");
		return isGet && isValidPath;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new GetCategoryListCommand().execute(request, response);
	}
}
