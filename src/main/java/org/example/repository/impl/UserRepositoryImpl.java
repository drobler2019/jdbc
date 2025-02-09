package org.example.repository.impl;

import org.example.configuration.PoolDataSource;
import org.example.constant.UtilDataSource;
import org.example.entities.User;
import org.example.repository.UserRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Collection<User> findAll() {
        var users = new ArrayList<User>();
        try (var connection = this.getConnection(); var statement = connection.createStatement();
             var resultSet = statement.executeQuery("SELECT * FROM usuarios")) {
            while (resultSet.next()) {
                var user = getUser(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        try (var connection = this.getConnection();
             var preparedStatement = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    user = this.getUser(resultSet);
                }
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User save(User user) {
        var sql = "INSERT INTO usuarios (username,password,email) VALUES (?,?,?)";
        if (UtilDataSource.hasId(user.id())) {
            sql = "UPDATE usuarios SET username =? ,password =?, email =? WHERE id =?";
        }
        try (var connection = this.getConnection(); var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());
            if (UtilDataSource.hasId(user.id())) {
                preparedStatement.setLong(4, user.id());
            }
            preparedStatement.executeUpdate();
            System.out.println("usuario registrado con éxito!");

            var id = 0L;

            if (user.id() == null) {
                try (var resultSet = preparedStatement.getGeneratedKeys()) {
                    id = resultSet.getLong(1);
                }
            }

            return new User(id, user.username(), user.password(), user.email());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = this.getConnection();
             var preparedStatement = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("usuario eliminado con éxito!");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return PoolDataSource.getConnection();
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"), resultSet.getString("username"),
                resultSet.getString("password"), resultSet.getString("email"));
    }

}
