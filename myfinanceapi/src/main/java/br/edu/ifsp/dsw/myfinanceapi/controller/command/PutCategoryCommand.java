package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PutCategoryCommand extends AbstractJsonCommand {
	
	private CategoryDAO categoryDAO;
	
	public PutCategoryCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String json = toJson(request);
			Category category = gson.fromJson(json, Category.class);
			categoryDAO.update(category);
			log.info("Category updated successfully");
			categoryDAO.commit();
			response.setStatus(HttpStatus.SC_OK);
		}
		catch(Throwable t) {
			categoryDAO.rollback();
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on category update");
			throw t;
		}
	}
}
