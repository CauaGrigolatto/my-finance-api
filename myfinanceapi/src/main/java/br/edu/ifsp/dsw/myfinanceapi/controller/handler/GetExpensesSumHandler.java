package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import br.edu.ifsp.dsw.myfinanceapi.controller.command.GetExpensesSumCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetExpensesSumHandler extends AbstractHandler {
	@Override
	protected boolean canHandle(HttpServletRequest request) {
		boolean isGet = request.getMethod().equals("GET");
		
		String pathInfo = request.getPathInfo();
		
		boolean isValidPath = pathInfo != null && (pathInfo.matches("^/transaction/expenses-sum$") || pathInfo.matches("^/transaction/expenses-sum/\\d+$"));
		
		boolean hasBody = request.getContentLength() > 0 || request.getContentLength() == -1;
		
		return  isGet && isValidPath && hasBody;
	}
	
	@Override
	protected void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		new GetExpensesSumCommand().execute(request, response);
	}
}
