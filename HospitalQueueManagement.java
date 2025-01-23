import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class HospitalQueueManagement {
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField, ageField, sexField, addressField, mobileField, waitingTimeField;
    private JComboBox<String> severityComboBox, urgencyComboBox;
    private JButton addPatientButton, viewQueueButton, treatPatientButton, loginSubmitButton;
    private ArrayList<Patient> patientQueue;  // Store patient data in an ArrayList

    // File to save treated patients
    private static final String TREATED_PATIENTS_FILE = "treated_patients.txt";

    public HospitalQueueManagement() {
        frame = new JFrame("Hospital Queue Management");
        panel = new JPanel();
        patientQueue = new ArrayList<>();

        // Initialize the main frame
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Set background color for the panel to light blue
        panel.setBackground(new Color(173, 216, 230)); // Light Blue
        panel.setLayout(new BorderLayout());

        // Show the login page initially
        showLoginPage();

        frame.add(panel);
        frame.setVisible(true);
    }

    // Show login page
    private void showLoginPage() {
        panel.removeAll();

        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60)); // Large font size
        titleLabel.setForeground(Color.WHITE);

        JLabel usernameLabel = new JLabel("Username:", JLabel.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Increased font size
        usernameLabel.setForeground(Color.WHITE);

        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 30)); // Increased font size for input field

        JLabel passwordLabel = new JLabel("Password:", JLabel.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Increased font size
        passwordLabel.setForeground(Color.WHITE);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 30)); // Increased font size for input field

        loginSubmitButton = new JButton("Login");
        loginSubmitButton.setFont(new Font("Arial", Font.BOLD, 40)); // Increased font size
        loginSubmitButton.setBackground(new Color(0, 115, 230)); // Light Blue
        loginSubmitButton.setForeground(Color.WHITE);

        loginSubmitButton.addActionListener(e -> {
            // Validate login credentials
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("adminpass")) {
                panel.removeAll();
                showHospitalManagementScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add padding around elements

        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);
        gbc.gridy = 2;
        panel.add(usernameField, gbc);

        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);
        gbc.gridy = 4;
        panel.add(passwordField, gbc);

        gbc.gridy = 5;
        panel.add(loginSubmitButton, gbc);

        frame.revalidate();
        frame.repaint();
    }

    private void showHospitalManagementScreen() {
        panel.removeAll();
        panel.setBackground(new Color(173, 216, 230)); // Light Blue background
    
        JLabel titleLabel = new JLabel("Hospital Queue Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Increased font size for title
        titleLabel.setForeground(Color.WHITE);
    
        // Set up input fields with increased font sizes
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 22));
        nameField.setBackground(Color.WHITE);
    
        ageField = new JTextField(3);
        ageField.setFont(new Font("Arial", Font.PLAIN, 22));
        ageField.setBackground(Color.WHITE);
    
        sexField = new JTextField(10);
        sexField.setFont(new Font("Arial", Font.PLAIN, 22));
        sexField.setBackground(Color.WHITE);
    
        addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 22));
        addressField.setBackground(Color.WHITE);
    
        mobileField = new JTextField(10);
        mobileField.setFont(new Font("Arial", Font.PLAIN, 22));
        mobileField.setBackground(Color.WHITE);
    
        waitingTimeField = new JTextField(5);
        waitingTimeField.setFont(new Font("Arial", Font.PLAIN, 22));
        waitingTimeField.setBackground(Color.WHITE);
    
        severityComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        urgencyComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
    
        addPatientButton = new JButton("Add Patient");
        addPatientButton.setFont(new Font("Arial", Font.BOLD, 20));
        addPatientButton.setBackground(new Color(0, 115, 230)); // Light Blue
        addPatientButton.setForeground(Color.WHITE);
        addPatientButton.addActionListener(e -> addPatient());
    
        viewQueueButton = new JButton("View Queue");
        viewQueueButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewQueueButton.setBackground(new Color(0, 115, 230)); // Light Blue
        viewQueueButton.setForeground(Color.WHITE);
        viewQueueButton.addActionListener(e -> viewQueue());
    
        treatPatientButton = new JButton("Treat Patient");
        treatPatientButton.setFont(new Font("Arial", Font.BOLD, 20));
        treatPatientButton.setBackground(new Color(0, 115, 230)); // Light Blue
        treatPatientButton.setForeground(Color.WHITE);
        treatPatientButton.addActionListener(e -> treatPatient());
    
        // Set up the form panel with increased font size for labels and fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(new Color(173, 216, 230)); // Light Blue background
        formPanel.setForeground(Color.WHITE);
    
        // Set larger font size for labels and fields
        Font labelFont = new Font("Arial", Font.PLAIN, 26); // Larger font size for labels
        Font fieldFont = new Font("Arial", Font.PLAIN, 22); // Larger font size for input fields
    
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        nameField.setFont(fieldFont);
    
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        ageField.setFont(fieldFont);
    
        JLabel sexLabel = new JLabel("Sex (Male/Female/Other):");
        sexLabel.setFont(labelFont);
        formPanel.add(sexLabel);
        formPanel.add(sexField);
        sexField.setFont(fieldFont);
    
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        addressField.setFont(fieldFont);
    
        JLabel mobileLabel = new JLabel("Mobile No:");
        mobileLabel.setFont(labelFont);
        formPanel.add(mobileLabel);
        formPanel.add(mobileField);
        mobileField.setFont(fieldFont);
    
        JLabel severityLabel = new JLabel("Severity (1-10):");
        severityLabel.setFont(labelFont);
        formPanel.add(severityLabel);
        formPanel.add(severityComboBox);
        severityComboBox.setFont(fieldFont);
    
        JLabel urgencyLabel = new JLabel("Urgency (1-10):");
        urgencyLabel.setFont(labelFont);
        formPanel.add(urgencyLabel);
        formPanel.add(urgencyComboBox);
        urgencyComboBox.setFont(fieldFont);
    
        JLabel waitingTimeLabel = new JLabel("Waiting Time (mins):");
        waitingTimeLabel.setFont(labelFont);
        formPanel.add(waitingTimeLabel);
        formPanel.add(waitingTimeField);
        waitingTimeField.setFont(fieldFont);
    
        // Set up the button panel with action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230)); // Light Blue background
        buttonPanel.add(addPatientButton);
        buttonPanel.add(viewQueueButton);
        buttonPanel.add(treatPatientButton);
    
        // Add all components to the main panel
        panel.setLayout(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        frame.revalidate();
        frame.repaint();
    }
    
    

    private void addPatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String sex = sexField.getText();
        String address = addressField.getText();
        long mobileNo = Long.parseLong(mobileField.getText());
        int severity = Integer.parseInt((String) severityComboBox.getSelectedItem());
        int urgency = Integer.parseInt((String) urgencyComboBox.getSelectedItem());
        int waitingTime = Integer.parseInt(waitingTimeField.getText());

        if (name.isEmpty() || sex.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Patient newPatient = new Patient(name, age, sex, address, mobileNo, severity, urgency, waitingTime);
        patientQueue.add(newPatient);

        // Clear fields after adding patient
        nameField.setText("");
        ageField.setText("");
        sexField.setText("");
        addressField.setText("");
        mobileField.setText("");
        waitingTimeField.setText("");

        JOptionPane.showMessageDialog(frame, "Patient Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewQueue() {
        String[] columnNames = {"Name", "Age", "Sex", "Address", "Severity", "Urgency", "Waiting Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add patient data to the table
        for (Patient patient : patientQueue) {
            model.addRow(new Object[]{
                patient.name, patient.age, patient.sex, patient.address, patient.severity, patient.urgency, patient.waitingTime
            });
        }

        JTable patientTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        JFrame queueFrame = new JFrame("Patient Queue");
        queueFrame.setSize(600, 400);
        queueFrame.setLocationRelativeTo(null);
        queueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        queueFrame.add(scrollPane);
        queueFrame.setVisible(true);
    }

    private void treatPatient() {
        if (!patientQueue.isEmpty()) {
            Patient patientToTreat = patientQueue.remove(0);
            JOptionPane.showMessageDialog(frame, "Treating Patient: " + patientToTreat.name, "Success", JOptionPane.INFORMATION_MESSAGE);
            saveTreatedPatient(patientToTreat);  // Save treated patient to file
        } else {
            JOptionPane.showMessageDialog(frame, "No patients in the queue!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTreatedPatient(Patient patient) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TREATED_PATIENTS_FILE, true))) {
            writer.write("Patient ID: " + patient.id + ", Name: " + patient.name + ", Severity: " + patient.severity + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HospitalQueueManagement();
    }

    class Patient {
        static int idCounter = 1;
        int id;
        String name;
        int age;
        String sex;
        String address;
        long mobileNo;
        int severity;
        int urgency;
        int waitingTime;

        public Patient(String name, int age, String sex, String address, long mobileNo, int severity, int urgency, int waitingTime) {
            this.id = idCounter++;
            this.name = name;
            this.age = age;
            this.sex = sex;
            this.address = address;
            this.mobileNo = mobileNo;
            this.severity = severity;
            this.urgency = urgency;
            this.waitingTime = waitingTime;
        }
    }
}