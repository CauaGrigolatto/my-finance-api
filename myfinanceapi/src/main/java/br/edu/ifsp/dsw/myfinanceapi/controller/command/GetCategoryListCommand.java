package br.edu.ifsp.dsw.myfinanceapi.controller.command;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;

import br.edu.ifsp.dsw.myfinanceapi.dto.CategoryFilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.PaginatedResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.ResponseDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.dao.CategoryDAOImpl;
import br.edu.ifsp.dsw.myfinanceapi.model.database.ConnectionFactory;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetCategoryListCommand extends AbstractJsonCommand {
	
	private CategoryDAOImpl categoryDAO;
	
	public GetCategoryListCommand() throws Throwable {
		super();
		Connection conn = ConnectionFactory.getConnection();
		this.categoryDAO = new CategoryDAOImpl(conn);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		try {
			CategoryFilterDTO filter = toCategoryFilterDTO(request);
			
			List<Category> categories = categoryDAO.findByFilter(filter);
			long count = categoryDAO.count(filter);
			
			PaginatedResponseDTO<Category> responseDTO = new PaginatedResponseDTO<Category>();
			responseDTO.setStatus(HttpStatus.SC_OK);
			responseDTO.setData(categories);
			responseDTO.setPage(filter.getPage());
			responseDTO.setPageSize(filter.getLimit());
			responseDTO.setTotalItems(count);
			responseDTO.setTotalPages((int) Math.ceilDiv(count, filter.getLimit()));
			
			log.info("Categories consulted successfully");
			
			String json = gson.toJson(responseDTO);
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write(json);
		}
		catch(Throwable t) {
			log.error("Error on getting category list");
			
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
	
	private CategoryFilterDTO toCategoryFilterDTO(HttpServletRequest request) {
		CategoryFilterDTO filter = new CategoryFilterDTO();

	    String title = request.getParameter("title");
	    if (StringUtils.isNotBlank(title)) {
	        filter.setTitle(title.trim());
	    }
	    
	    int page = NumberUtils.toInt(request.getParameter("page"), 1);
	    if (page < 1) {
	        page = 1;
	    }

	    int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 2);
	    if (pageSize < 1) {
	        pageSize = 10;
	    }

	    int offset = (page - 1) * pageSize;

	    filter.setPage(page);
	    filter.setLimit(pageSize);
	    filter.setOffset(offset);
	    
	    return filter;
	}
}
