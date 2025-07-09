package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.ErrorFieldDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostCategoryCommand extends AbstractJsonCommand {
	
	private CategoryDAOImpl categoryDAO;
	
	public PostCategoryCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAOImpl(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			String json = toJson(request);
			Category category = gson.fromJson(json, Category.class);
			
			ResponseDTO<Category> responseDTO;
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
				categoryDAO.save(category);
				log.info("Category created successfully", category);
				categoryDAO.commit();
				
				responseDTO = new ResponseDTO<Category>(
					HttpStatus.SC_CREATED,
					"Category registered successfully.",
					category,
					errors
				);
				
				response.setStatus(HttpStatus.SC_CREATED);
			}
			
			String responnseJson = gson.toJson(responseDTO);
			response.setContentType("application/json");
			response.getWriter().write(responnseJson);
		}
		catch(Throwable t) {
			categoryDAO.rollback();
			log.error("Error on category creation");
			
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
