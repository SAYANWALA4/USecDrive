import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainGUI {

    private JFrame frame;
    private JTextField inputField;
    private JTextField outputField;
    private JTextField keyField;
    private JPanel mainButtonsPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI window = new MainGUI();
            window.frame.setVisible(true);
        });
    }

    public MainGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("USecDrive");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Main buttons panel
        mainButtonsPanel = createMainButtonsPanel();
        mainPanel.add(mainButtonsPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);

        JLabel programNameLabel = new JLabel("USecDrive");
        programNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        programNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(programNameLabel);

        JLabel descriptionLabel = new JLabel("Encrypt and decrypt data for secure cloud or server storage.");
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(descriptionLabel);

        return headerPanel;
    }

    private JPanel createMainButtonsPanel() {
        JPanel mainButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton encryptButton = createStyledButton("Encrypt");
        encryptButton.addActionListener(e -> showEncryptPanel());
        mainButtonsPanel.add(encryptButton);

        JButton decryptButton = createStyledButton("Decrypt");
        decryptButton.addActionListener(e -> showDecryptPanel());
        mainButtonsPanel.add(decryptButton);

        return mainButtonsPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    private void showEncryptPanel() {
        JPanel encryptPanel = createEncryptionPanel();

        // Initialize inputField and outputField
        inputField = new JTextField();
        outputField = new JTextField();

        JButton executeButton = createStyledButton("Execute");
        executeButton.addActionListener(e -> {
            try {
                SecretKey secretKey = Encrypt_Decrypt.generateAESKey();
                String encodedKey = Encrypt_Decrypt.bytesToHex(secretKey.getEncoded());
                Encrypt_Decrypt encryptDecrypt = new Encrypt_Decrypt(inputField.getText(), outputField.getText(), encodedKey, 1);

                saveKeyToFile(encodedKey);

                encryptDecrypt.encyptAll();
                JOptionPane.showMessageDialog(frame, "Encryption completed successfully!");
            } catch (Exception ex) {
                handleException(ex, "encryption");
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> setMainPanel(mainButtonsPanel));

        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> browseForFiles(inputField));

        encryptPanel.add(new JLabel("Enter input path:"));
        encryptPanel.add(inputField);

        encryptPanel.add(new JLabel("Enter output path:"));
        encryptPanel.add(outputField);

        encryptPanel.add(executeButton);
        encryptPanel.add(backButton);
        encryptPanel.add(browseButton);

        setMainPanel(encryptPanel);
    }

    private JPanel createEncryptionPanel() {
        return new JPanel(new GridLayout(4, 1));
    }

    private void showDecryptPanel() {
        JPanel decryptPanel = createDecryptionPanel();

        // Initialize inputField, outputField, and keyField
        inputField = new JTextField();
        outputField = new JTextField();
        keyField = new JTextField();

        JButton executeButton = createStyledButton("Execute");
        executeButton.addActionListener(e -> {
            try {
                String key = getKeyFromFile();
                Encrypt_Decrypt encryptDecrypt = new Encrypt_Decrypt(inputField.getText(), outputField.getText(), key, 2);

                encryptDecrypt.deencyptAll();
                JOptionPane.showMessageDialog(frame, "Decryption completed successfully!");
            } catch (Exception ex) {
                handleException(ex, "decryption");
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> setMainPanel(mainButtonsPanel));

        JButton browseButton = createStyledButton("Browse");
        browseButton.addActionListener(e -> browseForFiles(inputField));

        decryptPanel.add(new JLabel("Enter AES key:"));
        decryptPanel.add(keyField);

        decryptPanel.add(new JLabel("Enter input path:"));
        decryptPanel.add(inputField);

        decryptPanel.add(new JLabel("Enter output path:"));
        decryptPanel.add(outputField);

        decryptPanel.add(executeButton);
        decryptPanel.add(backButton);
        decryptPanel.add(browseButton);

        setMainPanel(decryptPanel);
    }

    private JPanel createDecryptionPanel() {
        return new JPanel(new GridLayout(5, 1));
    }

    private void setMainPanel(JPanel panel) {
        Container contentPane = frame.getContentPane();
        contentPane.removeAll();

        // Main buttons panel should be added only once
        contentPane.add(mainButtonsPanel, BorderLayout.CENTER);

        // Ensure inputField, outputField, and keyField are set to null
        inputField = null;
        outputField = null;
        keyField = null;

        contentPane.add(panel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
        frame.pack();
    }

    private void browseForFiles(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    private void saveKeyToFile(String key) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Get the document folder path
        String documentFolderPath = System.getProperty("user.home") + File.separator + "Documents";

        // Create the file path
        String filePath = documentFolderPath + File.separator + "key.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(key + " Date:" + date + " Time:" + time);
            JOptionPane.showMessageDialog(frame, "Key saved to: " + filePath);
        } catch (IOException e) {
            handleException(e, "saving key to file");
        }
    }

    private String getKeyFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))) {
            return reader.readLine();
        }
    }

    private void handleException(Exception ex, String operation) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error during " + operation + ".", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
