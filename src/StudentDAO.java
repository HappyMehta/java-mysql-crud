import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StudentDAO {
    private Connection connection;

    public StudentDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/student_sql", "root", "Admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertStudent(String firstName, String lastName, int age, String major) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Students (first_name, last_name, age, major) VALUES (?, ?, ?, ?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, major);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel getAllStudents() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Student ID", "First Name", "Last Name", "Age", "Major", "Actions"}, 0);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Students");
            while (resultSet.next()) {
                int id = resultSet.getInt("student_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                String major = resultSet.getString("major");
                model.addRow(new Object[]{id, firstName, lastName, age, major, "Update/Delete"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public void updateStudent(int id, String firstName, String lastName, int age, String major) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Students SET first_name=?, last_name=?, age=?, major=? WHERE student_id=?");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setString(4, major);
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Students WHERE student_id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
