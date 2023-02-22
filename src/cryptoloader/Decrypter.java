package cryptoloader;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 AET Data Consulting</p>
 * <p>Company: </p>
 * @author Andreas E Toresäter
 * @version 1.0
 */

import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

class Decrypter {

	private char[] secret;
	private String algorithm = "PBEWithMD5AndDES";

	public Decrypter(String secret) {
		this.secret = secret.toCharArray();
	}

	public void setSecret(String secret) {
		this.secret = secret.toCharArray();
	}

	// Encrypts an bytearray with MD5 and DES
	public void encrypt(InputStream in, OutputStream out) {
		//byte[] ciphertext = null;

/*
		File file = new File(filename);
		byte[] filebytes = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			filebytes = new byte[(int) file.length()];
			fis.read(filebytes);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] clearText = filebytes;
*/
		// Salt
		byte[] salt =
			{
				(byte) 0x2f,
				(byte) 0x28,
				(byte) 0x35,
				(byte) 0x8f,
				(byte) 0x74,
				(byte) 0x2e,
				(byte) 0x4f,
				(byte) 0x12 };

		// Iteration count
		int count = 20;
		/*
				try {
					// Create PBE parameter set
					PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
		
					PBEKeySpec pbeKeySpec = new PBEKeySpec(this.secret);
					SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algorithm);
					SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
		
					// Create PBE Cipher
					Cipher pbeCipher = Cipher.getInstance(algorithm);
		
					// Initialize PBE Cipher with key and parameters
					pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
		
					// Encrypt the cleartext
					ciphertext = pbeCipher.doFinal(clearText);
		
				} catch (InvalidKeyException ike) {
				} catch (java.security.spec.InvalidKeySpecException e) {
				} catch (NoSuchAlgorithmException nsae) {
				} catch (IllegalBlockSizeException ibse) {
				} catch (InvalidAlgorithmParameterException ibse) {
				} catch (NoSuchPaddingException ee) {
				} catch (BadPaddingException eee) {
				}
		*/
		try {
			
			byte[] buf = new byte[1024];
			
			//			Create PBE parameter set
			PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
			
			PBEKeySpec pbeKeySpec = new PBEKeySpec(this.secret);
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algorithm);
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
			
			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance(algorithm);
			
			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
			
			// Bytes written to out will be encrypted
			out = new CipherOutputStream(out, pbeCipher);
			
			// Read in the cleartext bytes and write to out to encrypt
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0) {
				out.write(buf, 0, numRead);
			}
			out.close();
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//return ciphertext;

	}

	public byte[] decrypt(byte[] cleartext) {
		byte[] ciphertext = null;
		byte[] clearText = cleartext;

		// Salt
		byte[] salt =
			{
				(byte) 0x2f,
				(byte) 0x28,
				(byte) 0x35,
				(byte) 0x8f,
				(byte) 0x74,
				(byte) 0x2e,
				(byte) 0x4f,
				(byte) 0x12 };

		// Iteration count
		int count = 20;

		try {
			// Create PBE parameter set
			PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);

			PBEKeySpec pbeKeySpec = new PBEKeySpec(secret);
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algorithm);
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance(algorithm);

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

			// Encrypt the cleartext
			ciphertext = pbeCipher.doFinal(clearText);

		} catch (InvalidKeyException ike) {
		} catch (java.security.spec.InvalidKeySpecException e) {
		} catch (NoSuchAlgorithmException nsae) {
		} catch (IllegalBlockSizeException ibse) {
		} catch (InvalidAlgorithmParameterException ibse) {
		} catch (NoSuchPaddingException ee) {
		} catch (BadPaddingException eee) {
		}

		//System.out.println(new String(ciphertext));

		return ciphertext;

	}


}
