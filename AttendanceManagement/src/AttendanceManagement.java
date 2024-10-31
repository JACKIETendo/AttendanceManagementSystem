import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttendanceManagement extends JFrame implements ActionListener {
    private JTextField studentNameField;
    private JComboBox<String> statusComboBox;
    private JButton submitButton;
    private Connection connection;

    public AttendanceManagement() {
        // Set up the frame
        setTitle("Attendance Management System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // Create components
        JLabel studentNameLabel = new JLabel("Student Name:");
        studentNameField = new JTextField();
        JLabel statusLabel = new JLabel("Status:");
        statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        submitButton = new JButton("Submit");

        // Add action listener
        submitButton.addActionListener(this);

        // Add components to the frame
        add(studentNameLabel);
        add(studentNameField);
        add(statusLabel);
        add(statusComboBox);
        add(new JLabel()); // Empty cell
        add(submitButton);

        // Setup database connection
        setupDatabaseConnection();
    }

    //private void setupDatabaseConnection() {
        //try {
            // Load MySQL JDBC Driver
            //Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to the database
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_management?useSSL=false&serverTimezone=UTC", "root", "");
        //} catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        //}
    //}

    private void setupDatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to the database with a specific timezone parameter
            String url = "jdbc:mysql://localhost:3306/attendance_management?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = ""; // Update with your MySQL password if needed
            connection = DriverManager.getConnection(url, user, password);

            // Confirm the connection was successful
            JOptionPane.showMessageDialog(this, "Connected to the database successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String studentName = studentNameField.getText();
        String status = (String) statusComboBox.getSelectedItem();

        if (studentName.isEmpty() || status == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert attendance record into the database
        try {
            String query = "INSERT INTO attendance (student_name, date, status) VALUES (?, CURDATE(), ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, status);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance recorded successfully!");
            studentNameField.setText("");
            statusComboBox.setSelectedIndex(0);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog (this, "Failed to record attendance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AttendanceManagement attendanceManagement = new AttendanceManagement();
            attendanceManagement.setVisible(true);
        });
    }
}