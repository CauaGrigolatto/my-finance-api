package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ifsp.dsw.myfinanceapi.dto.FilterDTO;

public abstract class BasicDAO<E> {
	
	protected static final Logger log = LoggerFactory.getLogger(BasicDAO.class);
	
	protected Connection conn;
	
	protected BasicDAO(Connection conn) throws Throwable {
		setConnection(conn);
	}
	
	public abstract void save(E entity) throws Throwable;
	public abstract boolean update(E entity) throws Throwable;
	public abstract boolean delete(E entity) throws Throwable;
	public abstract E findById(Integer id) throws Throwable;
	public abstract List<E> findByFilter(FilterDTO filter) throws Throwable;
	protected abstract E buildEntity(ResultSet resultSet) throws Throwable;
	
	public void commit() throws Throwable {
		try {
			conn.commit();
			conn.close();
		}
		catch(Throwable t) {
			log.error("Error on commiting transaction");
			throw t;
		}
	}
	
	public void rollback() throws Throwable {
		try {
			conn.rollback();
			conn.close();
		}
		catch(Throwable t) {
			log.error("Error on rolling back transaction");
			throw t;
		}
	}
	
	private void setConnection(Connection conn) throws Throwable {
		try {
			if (conn == null) {
				throw new Throwable("Connection cannot be null");
			}
			conn.setAutoCommit(false);
			this.conn = conn;
			
		}
		catch(Throwable t) {
			log.error("Error on setting connection");
			throw t;
		}
	}
}
