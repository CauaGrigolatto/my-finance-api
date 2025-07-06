package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

public abstract class AbstractJsonCommand implements Command {
	
	protected static final Logger log = LoggerFactory.getLogger(AbstractJsonCommand.class);
	
	protected Gson gson;
	
	protected AbstractJsonCommand() {
		gson = new Gson();
	}
	
	protected String toJson(HttpServletRequest request) throws Throwable {
		StringBuilder jsonBuffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		
		while ((line = reader.readLine()) != null) {
			jsonBuffer.append(line);
		}
		
		return jsonBuffer.toString();
	}
}
