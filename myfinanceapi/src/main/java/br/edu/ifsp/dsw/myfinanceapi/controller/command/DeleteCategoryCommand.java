package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
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
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			Category category = new Category(id);
			boolean deletedSuccessfully = categoryDAO.delete(category);
			
			ResponseDTO<Category> responseDTO;
			
			if (deletedSuccessfully) {
				categoryDAO.commit();
				log.info("Category deleted successfully", category);
				response.setStatus(HttpStatus.SC_NO_CONTENT);
			}
			else {
				categoryDAO.rollback();
				responseDTO = new ResponseDTO<Category>(
					HttpStatus.SC_NOT_FOUND,
					"Category with ID " + id + " not found.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
				
				String responnseJson = gson.toJson(responseDTO);
				response.setContentType("application/json");
				response.getWriter().write(responnseJson);
			}
		}
		catch(Throwable t) {
			categoryDAO.rollback();
			log.error("Error on category delete");
			
			ResponseDTO<Transaction> responseDTO = new ResponseDTO<Transaction>(
				HttpStatus.SC_INTERNAL_SERVER_ERROR,
				"An error occurred while processing your request.",
				null,
				null
			);
			
			String responnseJson = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			throw t;
		}
	}
}
