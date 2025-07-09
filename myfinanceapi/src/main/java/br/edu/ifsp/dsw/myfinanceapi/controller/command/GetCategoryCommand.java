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
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			ResponseDTO<Category> responseDTO;
			Category category = categoryDAO.findById(id);
			
			if (category == null) {
				responseDTO = new ResponseDTO<Category>(
					HttpStatus.SC_NOT_FOUND,
					"Category with ID " + id + " not found.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			else {
				responseDTO = new ResponseDTO<Category>(
					HttpStatus.SC_OK,
					null,
					category,
					null
				);
				
				log.info("Category consulted successfully", category);
				response.setStatus(HttpStatus.SC_OK);
			}
			
			String json = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			log.error("Error on retrieving transactions");
			
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
		finally {			
			categoryDAO.rollback();
		}
	}
}
