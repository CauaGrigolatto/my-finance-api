package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ErrorFieldDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAO;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
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
			String[] parts = request.getPathInfo().split("/");
			String idStr = parts[2];
			Integer id = Integer.valueOf(idStr);
			
			String json = toJson(request);
			
			Category category = gson.fromJson(json, Category.class);
			category.setId(id);
			
			Category categoryToUpdate = categoryDAO.findById(id);
			
			ResponseDTO<Category> responseDTO;
			
			if (categoryToUpdate == null) {
				categoryDAO.rollback();
				
				responseDTO = new ResponseDTO<Category>(
					HttpStatus.SC_NOT_FOUND,
					"Could not update category. Please, check if it still exists.",
					null,
					null
				);
				
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			else {
				List<ErrorFieldDTO> errors = new LinkedList<ErrorFieldDTO>();
			
				if (category.getTitle() == null) {
					ErrorFieldDTO error = new ErrorFieldDTO("title", "Title cannot be empty.");
					errors.add(error);
				}
				
				if (! errors.isEmpty()) {
					categoryDAO.rollback();
					
					responseDTO = new ResponseDTO<Category>(
						HttpStatus.SC_BAD_REQUEST,
						"Request validation failed.",
						null,
						errors
					);
					
					response.setStatus(HttpStatus.SC_BAD_REQUEST);
				}
				else {
					categoryDAO.update(category);
					log.info("Category updated successfully", category);
					categoryDAO.commit();
					
					responseDTO = new ResponseDTO<Category>(
						HttpStatus.SC_OK,
						"Category updated successfully.",
						category,
						null
					);
					
					response.setStatus(HttpStatus.SC_OK);
				}
			}
			
			String responnseJson = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
		}
		catch(Throwable t) {
			categoryDAO.rollback();
			log.error("Error on category update");
			
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
