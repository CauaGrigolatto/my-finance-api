package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.CategoryFilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetCategoryListCommand extends AbstractJsonCommand {
	
	private CategoryDAO categoryDAO;
	
	public GetCategoryListCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAO(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			CategoryFilterDTO filter = toCategoryFilterDTO(request);
			List<Category> categories = categoryDAO.findByFilter(filter);
			
			log.info("Categories consulted successfully");
			
			String json = gson.toJson(categories);
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			log.error("Error on finding all categories");
			throw t;
		}
		finally {			
			categoryDAO.rollback();
		}
	}
	
	private CategoryFilterDTO toCategoryFilterDTO(HttpServletRequest request) {
		CategoryFilterDTO filter = new CategoryFilterDTO();

	    String title = request.getParameter("title");
	    if (StringUtils.isNotBlank(title)) {
	        filter.setTitle(title.trim());
	    }
	    
	    return filter;
	}
}
