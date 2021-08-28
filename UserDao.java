package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY =
            "SELECT * FROM users where id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? where id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";
    private static final String SHOW_ALL_USERS_QUERY =
            "SELECT * FROM users";


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User createUser(User user) {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("Successfully added user to database");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static User read(int userId) {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                System.out.println(user);
                return user;
            } else {
                System.out.println("User id is empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void update(User user) {
        try(Connection connection = DbUtil.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1,user.getUserName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,hashPassword(user.getPassword()));
            preparedStatement.setInt(4,user.getId());
            preparedStatement.executeUpdate();
            System.out.println("User information updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void delete(int userId) {
        try(Connection connection = DbUtil.connect()) {
            PreparedStatement preStat =
                    connection.prepareStatement(DELETE_USER_QUERY);
            preStat.setInt(1,userId);
            preStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static User[] findAll() {
        try (Connection conn = DbUtil.connect()) {
            User[] users = new User[0];
            PreparedStatement statement = conn.prepareStatement(SHOW_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users = addToArray(user, users);
            }
            for (User u : users) {
                System.out.println(u);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static User[] addToArray(User user, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = user;
        return tmpUsers;
    }











}
