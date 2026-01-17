package GUI;

import Entity.Patient;
import FileIO.PatientFileHandler;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EyeHospitalManagementPage extends JFrame {
    private PatientFileHandler fileHandler;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField serialNumberField;
    private JTextField nameField;
    private JTextField ageField;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    
    public EyeHospitalManagementPage() {
        fileHandler = new PatientFileHandler();
        initializeGUI();
        loadPatientsToTable();
    }
    
    private void initializeGUI() {
        setTitle("Eye Hospital Management System");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Patient Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);

        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "Patient Details",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        // Table
        String[] columnNames = {"Serial No", "Name", "Age"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 12));
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        patientTable.getTableHeader().setBackground(new Color(176, 196, 222));
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() != -1) {
                fillFormFromSelectedRow();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        return leftPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            "Patient Information",
            0,
            0,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel serialLabel = new JLabel("Serial Number:");
        serialLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(serialLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        serialNumberField = new JTextField(15);
        serialNumberField.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(serialNumberField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(ageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ageField = new JTextField(15);
        ageField.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(ageField, gbc);
        
        rightPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = createStyledButton("Add");
        updateButton = createStyledButton("Update");
        removeButton = createStyledButton("Remove");
        
        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        removeButton.addActionListener(e -> removePatient());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(135, 206, 235)); 
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.BLACK); 
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void addPatient() {
        try {
            String serialNumber = serialNumberField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            
            if (serialNumber.isEmpty()) {
                throw new IllegalArgumentException("Serial number cannot be empty!");
            }
            
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty!");
            }
            
            if (ageText.isEmpty()) {
                throw new IllegalArgumentException("Age cannot be empty!");
            }
            
            int age = Integer.parseInt(ageText);
            
            if (age <= 0 || age > 150) {
                throw new IllegalArgumentException("Age must be between 1 and 150!");
            }

            Patient patient = new Patient(serialNumber, name, age);
            fileHandler.savePatient(patient);
            loadPatientsToTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this,
                "Patient added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Age must be a valid number!",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePatient() {
        try {
            int selectedRow = patientTable.getSelectedRow();
            
            if (selectedRow == -1) {
                throw new IllegalArgumentException("Please select a patient from the table to update!");
            }
 
            String originalSerialNumber = (String) tableModel.getValueAt(selectedRow, 0);
            String newSerialNumber = serialNumberField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            
            if (newSerialNumber.isEmpty()) {
                throw new IllegalArgumentException("Serial number cannot be empty!");
            }
            
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty!");
            }
            
            if (ageText.isEmpty()) {
                throw new IllegalArgumentException("Age cannot be empty!");
            }
            
            int age = Integer.parseInt(ageText);
            
            if (age <= 0 || age > 150) {
                throw new IllegalArgumentException("Age must be between 1 and 150!");
            }

            Patient updatedPatient = new Patient(newSerialNumber, name, age);
            fileHandler.updatePatient(originalSerialNumber, updatedPatient);

            loadPatientsToTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this,
                "Patient updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Age must be a valid number!",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removePatient() {
        try {
            int selectedRow = patientTable.getSelectedRow();
            
            if (selectedRow == -1) {
                throw new IllegalArgumentException("Please select a patient from the table to remove!");
            }
            
            String serialNumber = (String) tableModel.getValueAt(selectedRow, 0);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove patient:\n" + 
                "Serial: " + serialNumber + "\nName: " + name + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                fileHandler.removePatient(serialNumber);
                loadPatientsToTable();
                clearForm();
                
                JOptionPane.showMessageDialog(this,
                    "Patient removed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Selection Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPatientsToTable() {
        try {
            tableModel.setRowCount(0);
            List<Patient> patients = fileHandler.loadAllPatients();
            
            for (Patient patient : patients) {
                Object[] row = {
                    patient.getSerialNumber(),
                    patient.getName(),
                    patient.getAge()
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading patients: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fillFormFromSelectedRow() {
        try {
            int selectedRow = patientTable.getSelectedRow();
            
            if (selectedRow != -1) {
                serialNumberField.setText((String) tableModel.getValueAt(selectedRow, 0));
                nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                ageField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error filling form: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        serialNumberField.setText("");
        nameField.setText("");
        ageField.setText("");
        patientTable.clearSelection();
    }
}