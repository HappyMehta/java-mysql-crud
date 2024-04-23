import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentUI extends JFrame {
    private JTextField firstNameField, lastNameField, ageField, majorField;
    private DefaultTableModel tableModel;
    private JTable table;
    private StudentDAO studentDAO;

    public StudentUI() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize DAO
        studentDAO = new StudentDAO();

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        ageField = new JTextField();
        majorField = new JTextField();
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Major:"));
        formPanel.add(majorField);

        // Button to insert data
        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String major = majorField.getText();
                studentDAO.insertStudent(firstName, lastName, age, major);
                refreshTable();
            }
        });
        formPanel.add(createButton);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = studentDAO.getAllStudents();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button column renderer and editor
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        //update
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
        tablePanel.add(refreshButton, BorderLayout.SOUTH);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private JButton updateButton;
        private JButton deleteButton;
    
        public ButtonRenderer() {
            setLayout(new BorderLayout());
            updateButton = new JButton("Update");
            deleteButton = new JButton("Delete");
            add(updateButton, BorderLayout.WEST);
            add(deleteButton, BorderLayout.EAST);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton updateButton;
        private JButton deleteButton;
    
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
    
            updateButton = new JButton("Update");
            deleteButton = new JButton("Delete");
    
            panel = new JPanel(new GridLayout(1, 2));
            panel.add(updateButton);
            panel.add(deleteButton);
    
            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = table.getEditingRow();
                    if (row != -1) {
                        int id = (int) table.getValueAt(row, 0); // Assuming student_id is in column 0
                        String firstName = (String) table.getValueAt(row, 1);
                        String lastName = (String) table.getValueAt(row, 2);
                        // int age = (int) table.getValueAt(row, 3);
                        int age = Integer.parseInt(table.getValueAt(row, 3).toString());
                        String major = (String) table.getValueAt(row, 4);
        
                        // Update the database
                        studentDAO.updateStudent(id, firstName, lastName, age, major);
        
                        // Refresh the table
                        refreshTable();
                    }
                }
            });
    
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = table.getEditingRow();
                    int id = (int) table.getValueAt(row, 0); // Assuming student_id is in column 0
    
                    // Delete the row from the database
                    studentDAO.deleteStudent(id);
    
                    // Refresh the table
                    refreshTable();
                }
            });
        }
    
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == table.getColumnCount() - 1) {
                return panel; // Return the panel with buttons for the last column
            } else {
                return super.getTableCellEditorComponent(table, value, isSelected, row, column); // Return default editor for other columns
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        DefaultTableModel newTableModel = studentDAO.getAllStudents();
        table.setModel(newTableModel);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new ButtonEditor(new JCheckBox()));
    }



    public static void main(String[] args) {
        // StudentUI studentUI = new StudentUI();
        new StudentUI();
    }
}
