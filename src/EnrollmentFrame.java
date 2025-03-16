import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class EnrollmentFrame extends JFrame {
    private JTextField nameField, ageField;
    private JComboBox<String> studentTypeBox, yearLevelBox;
    private JTextArea outputArea;
    private static List<String> enrolledStudents = new ArrayList<>();
    private static Map<String, String[]> subjects = new HashMap<>();
    private static Map<String, String> studentPasswords = new HashMap<>();
    private static Map<String, String> studentYearLevels = new HashMap<>();
    private static Map<String, String> studentUsernames = new HashMap<>();

    public EnrollmentFrame() {
        setTitle("University of Cebu - Enrollment System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeSubjects();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Enroll Student", createEnrollmentPanel());
        tabbedPane.add("Student List", createStudentListPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentTypeBox = new JComboBox<>(new String[]{"New Student", "Old Student"});
        nameField = new JTextField(15);
        ageField = new JTextField(3);
        yearLevelBox = new JComboBox<>(new String[]{"First Year", "Second Year", "Third Year", "Fourth Year"});
        JButton enrollButton = new JButton("Enroll");
        JButton clearButton = new JButton("Clear Fields");
        JButton loginButton = new JButton("Student Login");
        JButton logoutButton = new JButton("Logout"); // Add logout button
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Student Type:"), gbc);
        gbc.gridx = 1;
        panel.add(studentTypeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Year Level:"), gbc);
        gbc.gridx = 1;
        panel.add(yearLevelBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(enrollButton, gbc);
        gbc.gridx = 1;
        panel.add(clearButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(logoutButton, gbc); // Add logout button to the layout

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(scrollPane, gbc);

        enrollButton.addActionListener(new EnrollmentHandler());
        clearButton.addActionListener(e -> clearFields());
        loginButton.addActionListener(e -> showStudentLoginDialog());
        logoutButton.addActionListener(e -> logout());

        return panel;
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea studentListArea = new JTextArea(15, 40);
        studentListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(studentListArea);
        JButton refreshButton = new JButton("Refresh List");

        refreshButton.addActionListener(e -> {
            studentListArea.setText("Enrolled Students:\n" + String.join("\n", enrolledStudents));
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void initializeSubjects() {
        subjects.put("First Year", new String[]{"Communication Arts", "Mathematics", "Programming 1"});
        subjects.put("Second Year", new String[]{"Object-Oriented Programming", "Data Structures"});
        subjects.put("Third Year", new String[]{"Database Systems", "Networking"});
        subjects.put("Fourth Year", new String[]{"Software Engineering", "Cybersecurity"});
    }

    class EnrollmentHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String yearLevel = ((String) yearLevelBox.getSelectedItem()).trim();

            if (name.isEmpty() || ageText.isEmpty()) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                if (age < 16 || age > 60) {
                    JOptionPane.showMessageDialog(EnrollmentFrame.this, "Invalid age. Must be between 16 and 60.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username = generateUsername(name);
            String password = UUID.randomUUID().toString().substring(0, 8);

            enrolledStudents.add(name);
            studentUsernames.put(name, username);
            studentPasswords.put(username, password);
            studentYearLevels.put(username, yearLevel);

            outputArea.setText("Enrollment Successful!\nName: " + name +
                    "\nAge: " + ageText +
                    "\nYear Level: " + yearLevel +
                    "\n Enrolled Subjects: " + getSubjects(yearLevel) +
                    "\nGenerated Username: " + username +
                    "\nGenerated Password: " + password);
        }

        private String getSubjects(String yearLevel) {
            String[] subjectList = subjects.getOrDefault(yearLevel, new String[]{"No subjects found."});
            StringBuilder sb = new StringBuilder();
            for (String subject : subjectList) {
                sb.append("- ").append(subject).append("\n");
            }
            return sb.toString();
        }
    }

    private String generateUsername(String name) {
        // Remove spaces, convert to lowercase, and add a random number
        String baseName = name.toLowerCase().replaceAll("\\s+", "");
        // Limit to 8 characters if too long
        if (baseName.length() > 8) {
            baseName = baseName.substring(0, 8);
        }
        // Add a random 3-digit number to ensure uniqueness
        int randomNum = 100 + (int)(Math.random() * 900);
        return baseName + randomNum;
    }

    private void showStudentLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Student Login", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridBagLayout());
        loginDialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameInput = new JTextField(15);
        JPasswordField passwordInput = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginDialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginDialog.add(usernameInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginDialog.add(passwordInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginDialog.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameInput.getText().trim();
            String password = new String(passwordInput.getPassword());

            if (studentPasswords.containsKey(username) && studentPasswords.get(username).equals(password)) {
                // Find the name associated with this username
                String studentName = "";
                for (Map.Entry<String, String> entry : studentUsernames.entrySet()) {
                    if (entry.getValue().equals(username)) {
                        studentName = entry.getKey();
                        break;
                    }
                }

                if (!studentName.isEmpty() && enrolledStudents.contains(studentName)) {
                    new StudentDashboard(studentName, studentYearLevels.get(username));
                    loginDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Student record not found!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        usernameInput.addActionListener(e -> loginButton.doClick());
        passwordInput.addActionListener(e -> loginButton.doClick());

        loginDialog.setVisible(true);
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        studentTypeBox.setSelectedIndex(0);
        yearLevelBox.setSelectedIndex(0);
        outputArea.setText("");
    }

    private class StudentDashboard extends JFrame {
        public StudentDashboard(String name, String yearLevel) {
            setTitle("Student Dashboard - " + name);
            setSize(400, 300);
            setLayout(new BorderLayout());

            if (!enrolledStudents.contains(name)) {
                JOptionPane.showMessageDialog(this, "You are not officially enrolled!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            String[] enrolledSubjects = subjects.getOrDefault(yearLevel, new String[]{"No subjects available"});
            JTextArea subjectArea = new JTextArea("Enrolled Subjects:\n" + String.join("\n", enrolledSubjects));
            subjectArea.setEditable(false);
            add(new JScrollPane(subjectArea), BorderLayout.CENTER);

            JButton logoutButton = new JButton("Logout");
            logoutButton.addActionListener(e -> {
                dispose();
                EnrollmentFrame.this.dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            });
            add(logoutButton, BorderLayout.SOUTH);

            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
