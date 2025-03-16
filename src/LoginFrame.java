import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    // Admin and Student Accounts
    private static final Map<String, String> adminAccounts = new HashMap<>();
    private static final Map<String, String> studentAccounts = new HashMap<>();

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        // Set background color
        getContentPane().setBackground(new Color(18, 18, 18));

        adminAccounts.put("admin", "admin123"); // Default admin credentials

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setBackground(new Color(40, 40, 40));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(40, 40, 40));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(29, 185, 84)); // Spotify green
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        add(loginButton, gbc);

        gbc.gridy = 3;
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, gbc);

        LoginHandler loginHandle = new LoginHandler();

        loginButton.addActionListener(loginHandle);
        usernameField.addActionListener(loginHandle);
        passwordField.addActionListener(loginHandle);

        pack();
    }

    class LoginHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (adminAccounts.containsKey(username) && adminAccounts.get(username).equals(password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Welcome, Admin!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> new EnrollmentFrame().setVisible(true));
                dispose();
            } else if (studentAccounts.containsKey(username) && studentAccounts.get(username).equals(password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Welcome, " + username + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> new EnrollmentFrame().setVisible(true));
                dispose();
            } else {
                statusLabel.setText("Invalid credentials. Try again.");
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void addStudentAccount(String username, String password) {
        studentAccounts.put(username, password);
    }
}