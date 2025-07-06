package br.edu.ifsp.dsw.myfinanceapi.controller;

import java.io.IOException;
import java.sql.Connection;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test-connection")
public class ConnectionTest extends HttpServlet {
	
	private static final Logger log = LoggerFactory.getLogger(ConnectionTest.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Connection conn = null;
			conn = ConnectionFactory.getConnection();
			resp.setStatus(HttpStatus.SC_OK);
			log.info("Connection:", conn);			
		}
		catch(Throwable t) {
			resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on testing connection", t);
		}
	}
}
