package br.edu.ifsp.dsw.myfinanceapi.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import br.edu.ifsp.dsw.myfinanceapi.model.entity.Category;

public class CategoryDAO extends BasicDAO<Category> {
	
	public CategoryDAO(Connection conn) throws Throwable {
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
				Integer id = rs.getInt("category_id");
				category.setId(id);
			}
		}
		catch(Throwable t) {
			log.error("Error on saving category", t);
			throw t;
		}
	}

	@Override
	public boolean delete(Category entity) throws Throwable {
		// TODO Auto-generated method stub
		return false;
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
	public List<Category> findByFilter() throws Throwable {
		// TODO Auto-generated method stub
		return null;
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
