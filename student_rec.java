import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class student_rec extends JFrame {

    // GUI Components
    JTextField txtRoll, txtName, txtSection;
    JButton btnEnter, btnFind, btnUpdate, btnDelete;

    // JDBC
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    // Constructor
    public student_rec() {
        setTitle("Student Record System");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblTitle = new JLabel("Student Records");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(100, 10, 250, 30);
        add(lblTitle);

        JLabel lblRoll = new JLabel("Roll No:");
        lblRoll.setBounds(40, 60, 100, 25);
        add(lblRoll);

        txtRoll = new JTextField();
        txtRoll.setBounds(150, 60, 180, 25);
        add(txtRoll);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(40, 100, 100, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(150, 100, 180, 25);
        add(txtName);

        JLabel lblSection = new JLabel("Section:");
        lblSection.setBounds(40, 140, 100, 25);
        add(lblSection);

        txtSection = new JTextField();
        txtSection.setBounds(150, 140, 180, 25);
        add(txtSection);

        btnEnter = new JButton("Enter");
        btnEnter.setBounds(30, 200, 80, 30);
        add(btnEnter);

        btnFind = new JButton("Find");
        btnFind.setBounds(120, 200, 80, 30);
        add(btnFind);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(210, 200, 80, 30);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(300, 200, 80, 30);
        add(btnDelete);

        // MySQL Connection
        connect();

        // Button Listeners
        btnEnter.addActionListener(e -> insertRecord());
        btnFind.addActionListener(e -> findRecord());
        btnUpdate.addActionListener(e -> updateRecord());
        btnDelete.addActionListener(e -> deleteRecord());

        setVisible(true);
    }

    // Connect to MySQL
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/sakshi", "root", "root");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Connection Error: " + ex.getMessage());
        }
    }

    // Insert Record
    public void insertRecord() {
        try {
            int roll = Integer.parseInt(txtRoll.getText());
            String name = txtName.getText();
            String section = txtSection.getText();

            pst = con.prepareStatement("INSERT INTO student(roll, name, section) VALUES (?, ?, ?)");
            pst.setInt(1, roll);
            pst.setString(2, name);
            pst.setString(3, section);

            int result = pst.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Record Added");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Insert Failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Find Record
    public void findRecord() {
        try {
            int roll = Integer.parseInt(txtRoll.getText());

            pst = con.prepareStatement("SELECT * FROM student WHERE roll = ?");
            pst.setInt(1, roll);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtSection.setText(rs.getString("section"));
            } else {
                JOptionPane.showMessageDialog(this, "No Record Found");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Update Record
    public void updateRecord() {
        try {
            int roll = Integer.parseInt(txtRoll.getText());
            String name = txtName.getText();
            String section = txtSection.getText();

            pst = con.prepareStatement("UPDATE student SET name = ?, section = ? WHERE roll = ?");
            pst.setString(1, name);
            pst.setString(2, section);
            pst.setInt(3, roll);

            int result = pst.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Record Updated");
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Delete Record
    public void deleteRecord() {
        try {
            int roll = Integer.parseInt(txtRoll.getText());

            pst = con.prepareStatement("DELETE FROM student WHERE roll = ?");
            pst.setInt(1, roll);

            int result = pst.executeUpdate();
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Record Deleted");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Delete Failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Clear Input Fields
    public void clearFields() {
        txtRoll.setText("");
        txtName.setText("");
        txtSection.setText("");
    }

    public static void main(String[] args) {
        new student_rec();
    }
}
