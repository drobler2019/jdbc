package org.example.repository.impl;

import org.example.constant.UtilDataSource;
import org.example.entities.Category;
import org.example.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    private Connection connection;

    public CategoryRepositoryImpl() {}

    public CategoryRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Category> findAll() {
        var categories = new ArrayList<Category>();
        try (var statement = this.connection.createStatement();
             var resultSet = statement.executeQuery("SELECT * FROM categorias")) {

            while (resultSet.next())
                categories.add(this.getCategory(resultSet));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    @Override
    public Optional<Category> findById(Long id) {
        Optional<Category> categoryOptional = Optional.empty();
        try (var statement = this.connection.prepareStatement("SELECT * FROM categorias WHERE id =?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryOptional = Optional.of(new Category(resultSet.getLong(1), resultSet.getString(2)));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categoryOptional;
    }

    @Override
    public Category save(Category category) {
        String sql;

        if (UtilDataSource.hasId(category.id())) {
            sql = "UPDATE categorias SET nombre =? WHERE id=?";
        } else {
            sql = "INSERT INTO categorias (nombre) VALUES(?)";
        }

        try (var statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.nombre());
            if (UtilDataSource.hasId(category.id())) {
                statement.setLong(2, category.id());
            }
            statement.executeUpdate();

            var id = 0L;

            if (category.id() == null) {
                try (var rs = statement.getGeneratedKeys()) {
                    if (rs.next())
                        id = rs.getLong(1);
                }
            }

            return new Category(id, category.nombre());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var statement = this.connection.prepareStatement("DELETE FROM categorias WHERE id =?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Category getCategory(ResultSet resultSet) throws SQLException {
        return new Category(resultSet.getLong(1), resultSet.getString(2));
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
