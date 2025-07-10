package br.edu.ifsp.dsw.myfinanceapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ifsp.dsw.myfinanceapi.controller.handler.Handler;
import br.edu.ifsp.dsw.myfinanceapi.controller.handler.HandlerFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class FrontController extends HttpServlet {
	
	private static final Logger log = LoggerFactory.getLogger(FrontController.class);
	
	private Handler chain;
	
	@Override
	public void init() {
		try {
			chain = HandlerFactory.chain();
		}
		catch(Throwable t) {
			log.error("Error on init chain", t);
		}
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		try {
			chain.handle(req, resp);
		}
		catch(Throwable t) {
			log.error("Error on service", t);
		}
	}
}
