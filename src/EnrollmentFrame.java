import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentFrame extends JFrame {
    private JTextField nameField, ageField;
    private JComboBox<String> studentTypeBox, yearLevelBox;
    private JTextArea outputArea;
    private List<String> enrolledStudents;
    private Map<String, String[]> subjects;

    public EnrollmentFrame() {
        setTitle("University of Cebu - Enrollment System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        enrolledStudents = new ArrayList<>();
        initializeSubjects();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Enroll Student", createEnrollmentPanel());
        tabbedPane.add("Student List", createStudentListPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentTypeBox = new JComboBox<>(new String[]{"New Student", "Old Student"});
        nameField = new JTextField(15);
        ageField = new JTextField(3);
        yearLevelBox = new JComboBox<>(new String[]{"First Year", "Second Year", "Third Year"});
        JButton enrollButton = new JButton("Enroll");
        JButton clearButton = new JButton("Clear Fields");
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Student Type:"), gbc);
        gbc.gridx = 1;
        panel.add(studentTypeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Year Level:"), gbc);
        gbc.gridx = 1;
        panel.add(yearLevelBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(enrollButton, gbc);
        gbc.gridx = 1;
        panel.add(clearButton, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(scrollPane, gbc);

        enrollButton.addActionListener(new EnrollmentHandler());
        clearButton.addActionListener(e -> clearFields());

        return panel;
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
        subjects = new HashMap<>();
        subjects.put("First Year", new String[]{"Communication Arts", "Mathematics", "Programming 1"});
        subjects.put("Second Year", new String[]{"Object-Oriented Programming", "Data Structures"});
        subjects.put("Third Year", new String[]{"Database Systems", "Networking"});
    }

    class EnrollmentHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String yearLevel = (String) yearLevelBox.getSelectedItem();

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

            enrolledStudents.add(name + " - " + yearLevel);
            outputArea.setText("Enrollment Successful!\n" + "Name: " + name + "\nAge: " + ageText + "\nYear Level: " + yearLevel + "\nSubjects:\n" + getSubjects(yearLevel));
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

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        studentTypeBox.setSelectedIndex(0);
        yearLevelBox.setSelectedIndex(0);
        outputArea.setText("");
    }
}
