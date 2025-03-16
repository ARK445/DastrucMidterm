import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> userAccounts;

    public LoginFrame() {
        setTitle("Login - University of Cebu Enrollment System");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        userAccounts = new HashMap<>();
        userAccounts.put("admin", "1234"); // Example credentials

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 1; gbc.gridy = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(new LoginHandler());
    }

    private class LoginHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!");
                dispose();
                new EnrollmentFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
