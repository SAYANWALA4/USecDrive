import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class Main {
	
	public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        // Create a KeyGenerator object for AES (copied from internet)
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        // Generate a secret key
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }

    // Helper method to convert bytes to hexadecimal representation (copied from internet)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter path:");
		int userchoice;
		String passWord = null;
		String inputPath = scan.nextLine();

		System.out.println("Output path (Prevent Dataloss use seprate folders):");
		String outPath = scan.nextLine();

		System.out.println("Enter Option: 1.Encrypt, 2.Decrypt");
		userchoice = scan.nextInt();
		//password 
		if (userchoice == 1) {
		 try {
	            // Generate a key
	            SecretKey secretKey = generateAESKey();

	            // Print the key in encoded format
	            byte[] encodedKey = secretKey.getEncoded();
	            System.out.println("Encoded Key: " + bytesToHex(encodedKey));
	            passWord = bytesToHex(encodedKey);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
		}
		else if (userchoice == 2) {
			System.out.println("Enter your previously generated AES code:");
			passWord = scan.nextLine();
			passWord = scan.nextLine();
		}
		Encrypt_Decrypt jaspreet = new Encrypt_Decrypt(inputPath, outPath, passWord,userchoice);

	}

}