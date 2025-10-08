import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TravelerScreen extends JFrame {
    private JTextField nameField, startLocationField, endLocationField;
    private JButton submitBtn, backBtn;
    private JProgressBar progressBar;

    public TravelerScreen() {
        setTitle("Traveler Registration - CrowdShipping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {
        // Initialize text fields with preferred size
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));

        startLocationField = new JTextField();
        startLocationField.setPreferredSize(new Dimension(250, 30));

        endLocationField = new JTextField();
        endLocationField.setPreferredSize(new Dimension(250, 30));

        submitBtn = new JButton("Register as Traveler");
        backBtn = new JButton("← Back to Main");

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setString("Processing...");
        progressBar.setStringPainted(true);

        // Style buttons
        submitBtn.setBackground(new Color(34, 139, 34));
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setPreferredSize(new Dimension(200, 40));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.BLACK);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(150, 35));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style text fields
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(fieldFont);
        startLocationField.setFont(fieldFont);
        endLocationField.setFont(fieldFont);

        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        startLocationField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        endLocationField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Traveler Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Instructions
        JLabel instructionLabel = new JLabel("<html><center>Please enter your travel details below.<br>" +
                "We'll find the coordinates automatically and match you with senders.</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(105, 105, 105));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(instructionLabel, gbc);

        // Name field Label and input
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Start location Label and input
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("From (Start Location):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(startLocationField, gbc);

        // End location Label and input
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("To (End Location):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(endLocationField, gbc);

        // Progress bar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(progressBar, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(submitBtn);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupEventHandlers() {
        submitBtn.addActionListener(e -> handleSubmit());
        backBtn.addActionListener(e -> {
            new MainScreen();
            dispose();
        });

        // Add placeholder text behavior
        addPlaceholderText(nameField, "Enter your full name");
        addPlaceholderText(startLocationField, "e.g., Marina Beach, Chennai");
        addPlaceholderText(endLocationField, "e.g., Phoenix Mall, Chennai");
    }

    private void addPlaceholderText(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String startLocation = startLocationField.getText().trim();
        String endLocation = endLocationField.getText().trim();

        // Validate input
        if (name.isEmpty() || name.equals("Enter your full name") ||
                startLocation.isEmpty() || startLocation.equals("e.g., Marina Beach, Chennai") ||
                endLocation.isEmpty() || endLocation.equals("e.g., Phoenix Mall, Chennai")) {

            JOptionPane.showMessageDialog(this,
                    "Please fill all fields with valid information!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Disable submit button and show progress
        submitBtn.setEnabled(false);
        progressBar.setVisible(true);

        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                publish("Getting coordinates for start location...");

                // Get coordinates for locations
                double[] startCoords = LocationService.getCoordinates(startLocation);
                if (startCoords == null) {
                    publish("Error: Could not find start location coordinates");
                    return false;
                }

                publish("Getting coordinates for end location...");
                double[] endCoords = LocationService.getCoordinates(endLocation);
                if (endCoords == null) {
                    publish("Error: Could not find end location coordinates");
                    return false;
                }

                publish("Saving traveler information to database...");

                // Save to database
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO travelers (name, start_location, start_lat, start_lng, end_location, end_lat, end_lng) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, startLocation);
                        pstmt.setDouble(3, startCoords[0]);
                        pstmt.setDouble(4, startCoords[1]);
                        pstmt.setString(5, endLocation);
                        pstmt.setDouble(6, endCoords[0]);
                        pstmt.setDouble(7, endCoords[1]);

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            publish("Traveler information saved successfully!");
                            return true;
                        }
                    }
                } catch (SQLException ex) {
                    publish("Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }

                return false;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                String lastMessage = chunks.get(chunks.size() - 1);
                progressBar.setString(lastMessage);
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                submitBtn.setEnabled(true);

                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(TravelerScreen.this,
                                "Registration successful!\nYou will be notified when matches are found.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear fields
                        clearForm();

                        // Check for matches
                        MatchingService.findMatches();
                    } else {
                        JOptionPane.showMessageDialog(TravelerScreen.this,
                                "Registration failed. Please check your input and try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(TravelerScreen.this,
                            "An unexpected error occurred: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void clearForm() {
        nameField.setText("Enter your full name");
        nameField.setForeground(Color.GRAY);
        startLocationField.setText("e.g., Marina Beach, Chennai");
        startLocationField.setForeground(Color.GRAY);
        endLocationField.setText("e.g., Phoenix Mall, Chennai");
        endLocationField.setForeground(Color.GRAY);
    }
}
