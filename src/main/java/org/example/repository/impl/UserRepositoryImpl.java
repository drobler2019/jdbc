package org.example.repository.impl;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.User;
import org.example.repository.UserRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;

    public UserRepositoryImpl() throws SQLException {
        this.connection = ConnectionDataSource.getSingleton();
    }

    @Override
    public Collection<User> findAll() {
        var users = new ArrayList<User>();
        try (var statement = this.connection.createStatement();
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
        try (var preparedStatement = this.connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
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
    public void save(User user) {
        var sql = "INSERT INTO usuarios (username,password,email) VALUES (?,?,?)";
        if (this.hasId(user)) {
            sql = "UPDATE usuarios SET username =? ,password =?, email =? WHERE id =?";
        }
        try (var preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());
            if (this.hasId(user)) {
                preparedStatement.setLong(4, user.id());
            }
            preparedStatement.executeUpdate();
            System.out.println("usuario registrado con éxito!");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var preparedStatement = this.connection.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("usuario eliminado con éxito!");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"), resultSet.getString("username"),
                resultSet.getString("password"), resultSet.getString("email"));
    }

    private boolean hasId(User user) {
        return user.id() != null && user.id() != 0;
    }
}
