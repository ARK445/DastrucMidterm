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
    protected static List<String> enrolledStudents = new ArrayList<>();
    protected static Map<String, String[]> subjects = new HashMap<>();
    protected static Map<String, String> studentPasswords = new HashMap<>();
    protected static Map<String, String> studentYearLevels = new HashMap<>();
    protected static Map<String, String> studentUsernames = new HashMap<>();
    protected static Map<String, Map<String, Double>> studentGrades = new HashMap<>();

    // Add a reference to the studentBox in the Grades Panel
    private JComboBox<String> studentBox;

    // Track if the user is an admin or guest
    private boolean isAdmin;

    public EnrollmentFrame(boolean isAdmin) {
        this.isAdmin = isAdmin; // Set admin/guest mode
        setTitle("University of Cebu - Enrollment System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(18, 18, 18));

        initializeSubjects();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(40, 40, 40));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.add("Enroll Student", createEnrollmentPanel());
        tabbedPane.add("Student List", createStudentListPanel());
        tabbedPane.add("Grades", createGradesPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        // Disable certain features for guest users
        if (!isAdmin) {
            disableAdminFeatures();
        }
    }

    // Disable admin-only features for guest users
    private void disableAdminFeatures() {
        nameField.setEditable(false);
        ageField.setEditable(false);
        studentTypeBox.setEnabled(false);
        yearLevelBox.setEnabled(false);
        outputArea.setText("Guest users cannot enroll students.");
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 18, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentTypeBox = new JComboBox<>(new String[]{"New Student", "Old Student"});
        studentTypeBox.setBackground(new Color(40, 40, 40));
        studentTypeBox.setForeground(Color.WHITE);
        nameField = new JTextField(15);
        nameField.setBackground(new Color(40, 40, 40));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        ageField = new JTextField(3);
        ageField.setBackground(new Color(40, 40, 40));
        ageField.setForeground(Color.WHITE);
        ageField.setCaretColor(Color.WHITE);
        yearLevelBox = new JComboBox<>(new String[]{"First Year", "Second Year", "Third Year", "Fourth Year"});
        yearLevelBox.setBackground(new Color(40, 40, 40));
        yearLevelBox.setForeground(Color.WHITE);
        JButton enrollButton = new JButton("Enroll");
        enrollButton.setBackground(new Color(29, 185, 84)); // Spotify green
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setFocusPainted(false);
        JButton clearButton = new JButton("Clear Fields");
        clearButton.setBackground(new Color(29, 185, 84)); // Spotify green
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        JButton loginButton = new JButton("Student Login");
        loginButton.setBackground(new Color(29, 185, 84)); // Spotify green
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(29, 185, 84)); // Spotify green
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        outputArea = new JTextArea(10, 40);
        outputArea.setBackground(new Color(40, 40, 40));
        outputArea.setForeground(Color.WHITE);
        outputArea.setCaretColor(Color.WHITE);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBackground(new Color(40, 40, 40));

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel studentTypeLabel = new JLabel("Select Student Type:");
        studentTypeLabel.setForeground(Color.WHITE);
        panel.add(studentTypeLabel, gbc);
        gbc.gridx = 1;
        panel.add(studentTypeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(Color.WHITE);
        panel.add(ageLabel, gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel yearLevelLabel = new JLabel("Year Level:");
        yearLevelLabel.setForeground(Color.WHITE);
        panel.add(yearLevelLabel, gbc);
        gbc.gridx = 1;
        panel.add(yearLevelBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(enrollButton, gbc);
        gbc.gridx = 1;
        panel.add(clearButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(logoutButton, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(scrollPane, gbc);

        enrollButton.addActionListener(new EnrollmentHandler());
        clearButton.addActionListener(e -> clearFields());
        loginButton.addActionListener(e -> showStudentLoginDialog());
        logoutButton.addActionListener(e -> logout());

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(18, 18, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dropdown for selecting a student
        studentBox = new JComboBox<>(); // Initialize the studentBox
        studentBox.setBackground(new Color(40, 40, 40));
        studentBox.setForeground(Color.WHITE);

        // Update the student dropdown with enrolled students
        updateStudentBox(studentBox);

        // Dropdown for selecting a subject
        JComboBox<String> subjectBox = new JComboBox<>();
        subjectBox.setBackground(new Color(40, 40, 40));
        subjectBox.setForeground(Color.WHITE);

        // Text field for entering a grade
        JTextField gradeField = new JTextField(5);
        gradeField.setBackground(new Color(40, 40, 40));
        gradeField.setForeground(Color.WHITE);
        gradeField.setCaretColor(Color.WHITE);

        // Button to apply the grade
        JButton applyGradeButton = new JButton("Apply Grade");
        applyGradeButton.setBackground(new Color(29, 185, 84)); // Spotify green
        applyGradeButton.setForeground(Color.WHITE);
        applyGradeButton.setFocusPainted(false);

        // Text area to display grades
        JTextArea gradesArea = new JTextArea(10, 30);
        gradesArea.setBackground(new Color(40, 40, 40));
        gradesArea.setForeground(Color.WHITE);
        gradesArea.setCaretColor(Color.WHITE);
        gradesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gradesArea);
        scrollPane.setBackground(new Color(40, 40, 40));

        // Event listener for student selection
        studentBox.addActionListener(e -> {
            String selectedStudent = (String) studentBox.getSelectedItem();
            if (selectedStudent != null) {
                // Get the year level of the selected student
                String yearLevel = studentYearLevels.get(studentUsernames.get(selectedStudent));
                if (yearLevel != null) {
                    // Load subjects for the selected student's year level
                    subjectBox.setModel(new DefaultComboBoxModel<>(subjects.get(yearLevel)));
                }
                // Display the grades for the selected student
                gradesArea.setText("Grades for " + selectedStudent + ":\n" + getGradesForStudent(studentUsernames.get(selectedStudent)));
            }
        });

        // Event listener for applying a grade
        applyGradeButton.addActionListener(e -> {
            String selectedStudent = (String) studentBox.getSelectedItem();
            String selectedSubject = (String) subjectBox.getSelectedItem();
            String gradeText = gradeField.getText().trim();

            // Validate input
            if (selectedStudent == null || selectedSubject == null || gradeText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double grade = Double.parseDouble(gradeText);
                // Validate grade range (1.0 to 5.0)
                if (grade < 1.0 || grade > 5.0) {
                    JOptionPane.showMessageDialog(this, "Grade must be between 1.0 and 5.0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the grade for the selected student and subject
                String username = studentUsernames.get(selectedStudent);
                studentGrades.computeIfAbsent(username, k -> new HashMap<>()).put(selectedSubject, grade);

                // Update the grades display
                gradesArea.setText("Grades for " + selectedStudent + ":\n" + getGradesForStudent(username));
                gradeField.setText(""); // Clear the grade field
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid grade.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the panel
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel studentLabel = new JLabel("Select Student:");
        studentLabel.setForeground(Color.WHITE);
        panel.add(studentLabel, gbc);
        gbc.gridx = 1;
        panel.add(studentBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setForeground(Color.WHITE);
        panel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        panel.add(subjectBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel gradeLabel = new JLabel("Enter Grade (1.0–5.0):");
        gradeLabel.setForeground(Color.WHITE);
        panel.add(gradeLabel, gbc);
        gbc.gridx = 1;
        panel.add(gradeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(applyGradeButton, gbc);

        gbc.gridy = 4;
        panel.add(scrollPane, gbc);

        return panel;
    }

    // Helper method to update the student dropdown
    private void updateStudentBox(JComboBox<String> studentBox) {
        studentBox.removeAllItems(); // Clear existing items
        for (String student : enrolledStudents) {
            studentBox.addItem(student); // Add enrolled students
        }
    }

    // Method to update the studentBox dropdown dynamically
    public void refreshStudentBox() {
        if (studentBox != null) {
            updateStudentBox(studentBox);
        }
    }

    // Make this method static to fix the bug
    public static String getGradesForStudent(String username) {
        Map<String, Double> grades = studentGrades.getOrDefault(username, new HashMap<>());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : grades.entrySet()) {
            double grade = entry.getValue();
            String status = (grade <= 3.0) ? "Pass" : "Fail"; // Determine pass/fail status
            sb.append(entry.getKey()).append(": ").append(grade).append(" (").append(status).append(")\n");
        }
        return sb.toString();
    }

    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));
        JTextArea studentListArea = new JTextArea(15, 40);
        studentListArea.setBackground(new Color(40, 40, 40));
        studentListArea.setForeground(Color.WHITE);
        studentListArea.setCaretColor(Color.WHITE);
        studentListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(studentListArea);
        scrollPane.setBackground(new Color(40, 40, 40));
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setBackground(new Color(29, 185, 84)); // Spotify green
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> {
            studentListArea.setText("Enrolled Students:\n" + String.join("\n", enrolledStudents));
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void initializeSubjects() {
        // First Year Subjects (First Semester Only)
        subjects.put("First Year", new String[]{
                "ENGL 100 - Communication Arts",
                "SOCIO 102 - Gender and Society",
                "MATH 100 - College Mathematics",
                "PSYCH 101 - Understanding the Self",
                "CC-INTCOM11 - Introduction to Computing",
                "CC-COMPROG11 - Computer Programming 1",
                "PE 101 - Movement Competency Training (PATHFit 1)",
                "NSTP 101 - National Service Training Program 1"
        });

        // Second Year Subjects (First Semester Only)
        subjects.put("Second Year", new String[]{
                "SOCIO 101 - The Contemporary World",
                "RIZAL 101 - Life, Works & Writings of Dr. Jose Rizal",
                "CC-DIGILOG21 - Digital Logic Design",
                "IT-OOPROG21 - Object Oriented Programming",
                "IT-SAD21 - System Analysis & Design",
                "CC-ACCTG21 - Accounting for IT",
                "CC-TWRITE21 - Technical Writing & Presentation Skills in IT",
                "PE 103 - Sports and Dance (PATHFit 3)"
        });

        // Third Year Subjects (First Semester Only)
        subjects.put("Third Year", new String[]{
                "IT-IMDBSYS31 - Information Management (DB Sys. 1)",
                "IT-NETWORK31 - Computer Networks",
                "IT-TESTQUA31 - Testing & Quality Assurance",
                "CC-HCI31 - Human Computer Interaction",
                "CC-RESCOM31 - Methods of Research in Computing",
                "IT-EL_ _ _ _ _ _ - IT Elective 1",
                "IT-SYSADMN32 - Systems Administration & Maintenance"
        });

        // Fourth Year Subjects (First Semester Only)
        subjects.put("Fourth Year", new String[]{
                "IT-CPSTONE30 - Capstone Project 1",
                "CC-PROFIS10 - Professional Issues in Computing",
                "LIT 11 - Literatures of the World",
                "IT-EL_ _ _ _ _ _ - IT Elective 3",
                "IT-FRE_ _ _ _ _ - Free Elective 4"
        });
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
            LoginFrame.addStudentAccount(username, password);

            outputArea.setText("Enrollment Successful!\nName: " + name +
                    "\nAge: " + ageText +
                    "\nYear Level: " + yearLevel +
                    "\nEnrolled Subjects: " + getSubjects(yearLevel) +
                    "\nGenerated Username: " + username +
                    "\nGenerated Password: " + password);

            // Refresh the studentBox dropdown in the Grades Panel
            refreshStudentBox();
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
        String baseName = name.toLowerCase().replaceAll("\\s+", "");
        if (baseName.length() > 8) {
            baseName = baseName.substring(0, 8);
        }
        int randomNum = 100 + (int)(Math.random() * 900);
        return baseName + randomNum;
    }

    private void showStudentLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Student Login", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridBagLayout());
        loginDialog.setLocationRelativeTo(this);
        loginDialog.getContentPane().setBackground(new Color(18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameInput = new JTextField(15);
        usernameInput.setBackground(new Color(40, 40, 40));
        usernameInput.setForeground(Color.WHITE);
        usernameInput.setCaretColor(Color.WHITE);
        JPasswordField passwordInput = new JPasswordField(15);
        passwordInput.setBackground(new Color(40, 40, 40));
        passwordInput.setForeground(Color.WHITE);
        passwordInput.setCaretColor(Color.WHITE);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(29, 185, 84)); // Spotify green
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        loginDialog.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginDialog.add(usernameInput, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        loginDialog.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginDialog.add(passwordInput, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        loginDialog.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameInput.getText().trim();
            String password = new String(passwordInput.getPassword());

            if (studentPasswords.containsKey(username) && studentPasswords.get(username).equals(password)) {
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

    public static class StudentDashboard extends JFrame {
        public StudentDashboard(String name, String yearLevel) {
            setTitle("Student Dashboard - " + name);
            setSize(400, 300);
            setLayout(new BorderLayout());
            getContentPane().setBackground(new Color(18, 18, 18));

            if (!enrolledStudents.contains(name)) {
                JOptionPane.showMessageDialog(this, "You are not officially enrolled!", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            String[] enrolledSubjects = subjects.getOrDefault(yearLevel, new String[]{"No subjects available"});
            String grades = EnrollmentFrame.getGradesForStudent(studentUsernames.get(name)); // Use static method
            JTextArea subjectArea = new JTextArea("Enrolled Subjects:\n" + String.join("\n", enrolledSubjects) + "\n\nGrades:\n" + grades);
            subjectArea.setBackground(new Color(40, 40, 40));
            subjectArea.setForeground(Color.WHITE);
            subjectArea.setCaretColor(Color.WHITE);
            subjectArea.setEditable(false);
            add(new JScrollPane(subjectArea), BorderLayout.CENTER);

            JButton logoutButton = new JButton("Logout");
            logoutButton.setBackground(new Color(29, 185, 84)); // Spotify green
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFocusPainted(false);
            logoutButton.addActionListener(e -> {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            });
            add(logoutButton, BorderLayout.SOUTH);

            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}