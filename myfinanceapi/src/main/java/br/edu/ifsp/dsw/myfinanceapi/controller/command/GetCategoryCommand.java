package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetCategoryCommand extends AbstractJsonCommand {
	
	private CategoryDAO categoryDAO;
	
	public GetCategoryCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String idStr = request.getParameter("id");
			Integer id = Integer.parseInt(idStr);
			
			Category category = categoryDAO.findById(id);
			
			if (category == null) {
				response.setStatus(HttpStatus.SC_NOT_FOUND);
				return;
			}
			
			log.info("Category consulted successfully", category);
			
			String json = gson.toJson(category);
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on finding category by ID");
			throw t;
		}
		finally {			
			categoryDAO.rollback();
		}
	}
}
