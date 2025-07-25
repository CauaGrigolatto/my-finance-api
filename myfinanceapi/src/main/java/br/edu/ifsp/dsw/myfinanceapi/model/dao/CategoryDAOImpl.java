package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.edu.ifsp.dsw.myfinanceapi.dto.CategoryFilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.dto.FilterDTO;
import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;

public class CategoryDAOImpl extends BasicDAO<Category> {
	
	public CategoryDAOImpl(Connection conn) throws Throwable {
		super(conn);
	}

	@Override
	public void save(Category category) throws Throwable {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO category(title) VALUES (?) ");
			PreparedStatement ps = conn.prepareStatement(sb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, category.getTitle());
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
				Integer id = rs.getInt(1);
				category.setId(id);
			}
		}
		catch(Throwable t) {
			log.error("Error on saving category");
			throw t;
		}
	}
	
	@Override
	public boolean update(Category category) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE category SET ");
			sql.append("title = ? ");
//			sql.append(❤️)
			sql.append("WHERE category_id = ? ");
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, category.getTitle());
			ps.setInt(2, category.getId());
			
			int rows = ps.executeUpdate();
			
			return rows > 0;
		}
		catch(Throwable t) {
			log.error("Error on updating category");
			throw t;
		}
	}

	@Override
	public boolean delete(Category category) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM category WHERE category_id = ? ");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, category.getId());
			int rows = ps.executeUpdate();
			return rows > 0;
		}
		catch(Throwable t) {
			log.error("Error on deleting category");
			throw t;
		}
	}

	@Override
	public Category findById(Integer id) throws Throwable {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.category_id AS categoryId, ");
			sql.append("c.title AS title ");
			sql.append("FROM category c ");
			sql.append("WHERE c.category_id = ? ");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Category category = null;
			
			if (rs.next()) {
				category = buildEntity(rs);
			}
			
			return category;
		}
		catch(Throwable t) {
			log.error("Error on finding category by ID");
			throw t;
		}
	}

	@Override
	public List<Category> findByFilter(FilterDTO filter) throws Throwable {
		try {
			CategoryFilterDTO categoryFilterDTO = (CategoryFilterDTO) filter;
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT c.category_id AS categoryId, ");
			sql.append("c.title AS title ");
			sql.append("FROM category c ");
			sql.append(filter.buildWhere(false));
			
			int index = 1;
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (StringUtils.isNotBlank(categoryFilterDTO.getTitle())) {
			    ps.setString(index++, "%" + categoryFilterDTO.getTitle() + "%");
			}
			
			if (! categoryFilterDTO.isUnpaged()) {				
				ps.setInt(index++, categoryFilterDTO.getLimit());
				ps.setInt(index++, categoryFilterDTO.getOffset());
			}
			
			ResultSet rs = ps.executeQuery();
			
			List<Category> categories = new LinkedList<Category>();
			
			while (rs.next()) {
				Category category = buildEntity(rs);
				categories.add(category);
			}
			
			return categories;
		}
		catch(Throwable t) {
			log.error("Error on finding by filter");
			throw t;
		}
	}
	
	@Override
	public long count(FilterDTO filter) throws Throwable {
		try {
			CategoryFilterDTO categoryFilterDTO = (CategoryFilterDTO) filter;
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(c.category_id) AS result ");
			sql.append("FROM category c ");
			sql.append(filter.buildWhere(true));
			
			int index = 1;
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			
			if (StringUtils.isNotBlank(categoryFilterDTO.getTitle())) {
			    ps.setString(index++, "%" + categoryFilterDTO.getTitle() + "%");
			}
			
			ResultSet rs = ps.executeQuery();
			
			long result = 0;
			
			if (rs.next()) {
				result = rs.getLong("result");
			}
			
			return result;
		}
		catch(Throwable t) {
			log.error("Error on counting by filter");
			throw t;
		}
	}

	@Override
	protected Category buildEntity(ResultSet resultSet) throws Throwable {
		Category category = new Category();
		Integer categoryId = resultSet.getInt("categoryId");
		String title = resultSet.getString("title");
		category.setId(categoryId);
		category.setTitle(title);
		return category;
	}
}
