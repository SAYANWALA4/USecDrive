import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypt_Decrypt {

    // Class variables to store input file path, output file path, and secret key
    
	private String inputFilePath;
    private String outputFilePath;
    private String secretKey;
    
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        // Create a KeyGenerator object for AES (copied from internet)
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        // Generate a secret key
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }

    // Helper method to convert bytes to hexadecimal representation (copied from internet)
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
    
    // Constructor to initialize class variables
    public Encrypt_Decrypt(String inputFilePath, String outputFilePath, String secretKey, int userchoice) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.secretKey = secretKey;
        noworlater(userchoice);
    }

    // Method to perform encryption or decryption based on the parameter 'yes'
    // 1 for encryption, 2 for decryption
    public void noworlater(int yes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
        if (yes == 1) {
            encyptAll();
            System.out.println("Encrypted successfully");
        }
        if (yes == 2) {
        	deencyptAll();
            System.out.println("Decrypted successfully");
        }
    }
    
    public void encyptAll() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
    	// Specify the path of the folder you want to list files from
        String folderPath = inputFilePath;
        String outFolderPath = outputFilePath;
        File folder = new File(folderPath);

        // Check if the specified path is a directory
        if (folder.isDirectory()) {
            // Get the list of files in the folder
            File[] files = folder.listFiles();
            // Check if the folder is not empty
            if (files != null) {
                for (File file : files) {
                  inputFilePath = folderPath+"\\"+file.getName();
                  outputFilePath = outFolderPath+"\\"+file.getName();
                  encryptFile();
                }
            } 
            else {
                System.out.println("\u001B[31m"+"The folder is empty."+"\u001B[0m");
            }
        } 
        else {
            System.out.println("The specified path is not a directory.");
        }
    }
    
    public void deencyptAll() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
    	// Specify the path of the folder you want to list files from
        String folderPath = inputFilePath;
        String outFolderPath = outputFilePath;
        File folder = new File(folderPath);

        // Check if the specified path is a directory
        if (folder.isDirectory()) {
            // Get the list of files in the folder
            File[] files = folder.listFiles();
            // Check if the folder is not empty
            if (files != null) {
                for (File file : files) {
                  inputFilePath = folderPath+"\\"+file.getName();
                  outputFilePath = outFolderPath+"\\"+file.getName();
                  decryptFile();
                }
            } 
            else {
                System.out.println("\u001B[31m"+"The folder is empty."+"\u001B[0m");
            }
        } 
        else {
            System.out.println("The specified path is not a directory.");
        }
    }
    
    // Algorithm used for encryption and decryption (AES)
    private static final String ALGORITHM = "AES";

    // Method to encrypt the content of a file
    public void encryptFile() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create a cipher instance using the AES algorithm
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // Generate a secret key from the provided key bytes using AES
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        // Initialize the cipher for encryption using the generated key
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Try-with-resources block to automatically close the input and output streams
        try (InputStream inputStream = new FileInputStream(inputFilePath);
             OutputStream outputStream = new FileOutputStream(outputFilePath)) {

            byte[] inputBytes = new byte[2048];
            int bytesRead;

            // Read data from the input stream, update the cipher, and write to the output stream
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                outputStream.write(outputBytes);
            }

            // Finalize the encryption process and write the remaining data
            byte[] outputBytes = cipher.doFinal();
            outputStream.write(outputBytes);
        }
    }

    // Method to decrypt the content of a file
    public void decryptFile() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create a cipher instance using the AES algorithm
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // Generate a secret key from the provided key bytes using AES
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        // Initialize the cipher for decryption using the generated key
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Try-with-resources block to automatically close the input and output streams
        try (InputStream inputStream = new FileInputStream(inputFilePath);
             OutputStream outputStream = new FileOutputStream(outputFilePath)) {

            byte[] inputBytes = new byte[2048];
            int bytesRead;

            // Read data from the input stream, update the cipher, and write to the output stream
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                outputStream.write(outputBytes);
            }

            // Finalize the decryption process and write the remaining data
            byte[] outputBytes = cipher.doFinal();
            outputStream.write(outputBytes);
        }
    }


}