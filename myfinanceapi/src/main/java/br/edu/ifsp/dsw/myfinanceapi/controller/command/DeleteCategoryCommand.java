package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteCategoryCommand extends AbstractJsonCommand {
	
	private CategoryDAO categoryDAO;
	
	public DeleteCategoryCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String idStr = request.getParameter("id");
			Integer id = Integer.parseInt(idStr);
			categoryDAO.delete(new Category(id));
			log.info("Category deleted successfully");
			categoryDAO.commit();
			response.setStatus(HttpStatus.SC_NO_CONTENT);
		}
		catch(Throwable t) {
			categoryDAO.rollback();
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on deleting category");
			throw t;
		}
	}
}
