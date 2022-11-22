package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user " +
                "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                " name VARCHAR(32), " +
                " lastname VARCHAR(32), " +
                " age BIT(8), " +
                " PRIMARY KEY ( id ))";
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e1) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS user";
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e1) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO user (name, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        List<User> userList = null;
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            try {
                ResultSet resultSet = statement.executeQuery(sql);
                connection.commit();
                userList = new ArrayList<>(10);
                while (resultSet.next()) {
                    User resultUser = new User();
                    resultUser.setId(resultSet.getLong("id"));
                    resultUser.setName(resultSet.getString("name"));
                    resultUser.setLastName(resultSet.getString("lastname"));
                    resultUser.setAge(resultSet.getByte("age"));
                    userList.add(resultUser);
                }
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM user";
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
