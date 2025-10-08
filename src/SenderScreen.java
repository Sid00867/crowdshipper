import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SenderScreen extends JFrame {
    private JTextField nameField, startLocationField, endLocationField;
    private JTextArea packageDetailsArea;
    private JButton submitBtn, backBtn;
    private JProgressBar progressBar;

    public SenderScreen() {
        setTitle("Sender Registration - CrowdShipping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setVisible(true);
    }

    private void initializeComponents() {

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));

        startLocationField = new JTextField();
        startLocationField.setPreferredSize(new Dimension(250, 30));

        endLocationField = new JTextField();
        endLocationField.setPreferredSize(new Dimension(250, 30));

        packageDetailsArea = new JTextArea(3, 25);
        packageDetailsArea.setLineWrap(true);
        packageDetailsArea.setWrapStyleWord(true);
        packageDetailsArea.setPreferredSize(new Dimension(250, 80));

        submitBtn = new JButton("Register Package Request");
        backBtn = new JButton("← Back to Main");

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setString("Processing...");
        progressBar.setStringPainted(true);

        // Style buttons
        submitBtn.setBackground(new Color(220, 20, 60));
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setPreferredSize(new Dimension(220, 40));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.BLACK);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(150, 35));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style text fields and area
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(fieldFont);
        startLocationField.setFont(fieldFont);
        endLocationField.setFont(fieldFont);
        packageDetailsArea.setFont(fieldFont);

        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        startLocationField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        endLocationField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        packageDetailsArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 248, 240));
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 20, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Sender Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 248, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Instructions
        JLabel instructionLabel = new JLabel("<html><center>Please enter your package delivery details below.<br>" +
                "We'll match you with available travelers on similar routes.</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(105, 105, 105));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(instructionLabel, gbc);

        // Name field label and input
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

        // Package details label and textarea
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Package Details:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        formPanel.add(new JScrollPane(packageDetailsArea), gbc);

        // Pickup location label and input
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Pickup Location:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(startLocationField, gbc);

        // Delivery location label and input
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Delivery Location:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(endLocationField, gbc);

        // Progress bar
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(progressBar, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 248, 240));
        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(submitBtn);

        gbc.gridy = 6;
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
        addPlaceholderText(startLocationField, "e.g., Anna Nagar, Chennai");
        addPlaceholderText(endLocationField, "e.g., T. Nagar, Chennai");
        packageDetailsArea.setText("Describe your package (size, weight, contents)...");
        packageDetailsArea.setForeground(Color.GRAY);

        packageDetailsArea.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (packageDetailsArea.getText().equals("Describe your package (size, weight, contents)...")) {
                    packageDetailsArea.setText("");
                    packageDetailsArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (packageDetailsArea.getText().isEmpty()) {
                    packageDetailsArea.setText("Describe your package (size, weight, contents)...");
                    packageDetailsArea.setForeground(Color.GRAY);
                }
            }
        });
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
        String packageDetails = packageDetailsArea.getText().trim();
        String startLocation = startLocationField.getText().trim();
        String endLocation = endLocationField.getText().trim();

        // Validate input
        if (name.isEmpty() || name.equals("Enter your full name") ||
                packageDetails.isEmpty() || packageDetails.equals("Describe your package (size, weight, contents)...")
                ||
                startLocation.isEmpty() || startLocation.equals("e.g., Anna Nagar, Chennai") ||
                endLocation.isEmpty() || endLocation.equals("e.g., T. Nagar, Chennai")) {

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
                publish("Getting coordinates for pickup location...");

                // Get coordinates for locations
                double[] startCoords = LocationService.getCoordinates(startLocation);
                if (startCoords == null) {
                    publish("Error: Could not find pickup location coordinates");
                    return false;
                }

                publish("Getting coordinates for delivery location...");
                double[] endCoords = LocationService.getCoordinates(endLocation);
                if (endCoords == null) {
                    publish("Error: Could not find delivery location coordinates");
                    return false;
                }

                publish("Saving sender request to database...");

                // Save to database
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "INSERT INTO senders (name, start_location, start_lat, start_lng, end_location, end_lat, end_lng) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name + " [" + packageDetails + "]"); // Store package details with name
                        pstmt.setString(2, startLocation);
                        pstmt.setDouble(3, startCoords[0]);
                        pstmt.setDouble(4, startCoords[1]);
                        pstmt.setString(5, endLocation);
                        pstmt.setDouble(6, endCoords[0]);
                        pstmt.setDouble(7, endCoords[1]);

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            publish("Sender request saved successfully!");
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
                        JOptionPane.showMessageDialog(SenderScreen.this,
                                "Package request submitted successfully!\nYou will be contacted when a traveler match is found.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear form
                        clearForm();

                        // Check for matches
                        MatchingService.findMatches();
                    } else {
                        JOptionPane.showMessageDialog(SenderScreen.this,
                                "Request submission failed. Please check your input and try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SenderScreen.this,
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
        packageDetailsArea.setText("Describe your package (size, weight, contents)...");
        packageDetailsArea.setForeground(Color.GRAY);
        startLocationField.setText("e.g., Anna Nagar, Chennai");
        startLocationField.setForeground(Color.GRAY);
        endLocationField.setText("e.g., T. Nagar, Chennai");
        endLocationField.setForeground(Color.GRAY);
    }
}
