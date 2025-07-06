package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import org.apache.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractHandler implements Handler {
	private Handler next;

	@Override
	public void setNext(Handler next) {
		this.next = next;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		if (canHandle(request)) {
			proccess(request, response);
		}
		else if (next != null) {
			next.handle(request, response);
		}
		else {
			response.sendError(HttpStatus.SC_NOT_FOUND, "Endpoint not found.");
		}
	}
	
	protected abstract boolean canHandle(HttpServletRequest request);
	protected abstract void proccess(HttpServletRequest request, HttpServletResponse response) throws Throwable;
}
