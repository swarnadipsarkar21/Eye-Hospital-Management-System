import GUI.EyeHospitalManagementPage;
import javax.swing.*;

public class Start {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                EyeHospitalManagementPage frame = new EyeHospitalManagementPage();
                frame.setVisible(true);
                System.out.println("Eye Hospital Management System started successfully!");
            } catch (RuntimeException e) {
                System.err.println("Error starting application: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}