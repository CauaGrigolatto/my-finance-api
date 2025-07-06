package br.edu.ifsp.dsw.myfinanceapi.model.database;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {
	private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private static Connection conn;
	
	public static Connection getConnection() throws Throwable {
		try {			
			if (conn == null || conn.isClosed()) {
				createConnection();
			}
			return conn;
		}
		catch(Throwable t) {
			log.error("Error on getting connection", t);
			throw t;
		}		
	}
	
	private static void createConnection() throws Throwable {
		try {
			InitialContext context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/myfinanceapi");
			conn = ds.getConnection();			
		}
		catch(Throwable t) {
			log.error("Error on creating connection", t);
			throw t;
		}
	}
}
