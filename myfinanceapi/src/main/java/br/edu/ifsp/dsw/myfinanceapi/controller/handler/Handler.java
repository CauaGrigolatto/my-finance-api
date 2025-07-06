package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {
	void setNext(Handler handler);
	void handle(HttpServletRequest request, HttpServletResponse response) throws Throwable;
}
