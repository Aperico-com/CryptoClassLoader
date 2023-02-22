/*
 * Created on Mar 5, 2004
 * Copy right 2003 AET Data Consulting, all rights reserved
 * Author: Andreas Toresäter
 **/
package cryptoloader;

import java.io.*;
import java.net.*;

/**
 * @author Andreas E Toresäter
 * Copyright 2003 AET Data Consulting, all rights reserved.
*/
public class CryptoClassLoader extends ClassLoader {

	String secret; 
	Decrypter dc;
	public static String cryptExtension = ".crypt";

	public CryptoClassLoader(String sec) {
		this.secret = sec;
		this.dc = new Decrypter(this.secret);
	}

	public Class loadClass(String name, boolean resolve)
		throws ClassNotFoundException {
		Class c = null;

		// First, check if the class has already been loaded
		if (name.equalsIgnoreCase("cryptoloader.Hello"))
			c = findClass(name);
		else {
			c = findLoadedClass(name);
			if (c == null) {
				try {
					ClassLoader parent = ClassLoader.getSystemClassLoader();
					if (parent != null) 
						c = parent.loadClass(name);
				} catch (ClassNotFoundException e) {
					// If still not found, then call findClass in order
					// to find the class.
					c = findClass(name);
				}
			}
		}

		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	public Class findClass(String name) {
		byte[] b = loadClassData(name);
		return defineClass(name, b, 0, b.length);
	}

	private byte[] loadClassData(String name) {
		//	   load the class data from the crypted file
		name = name.replace('.', '/'); // packagename to filesystem conversion
		String filename = name + cryptExtension;

		System.out.println("Filename: " + filename);
		InputStream stream = getResourceAsStream(filename);
		//if (Debug.DEBUG)
		//Debug.log(this, "found: " + stream);

		byte[] classBytes = null;
		BufferedInputStream instream = null;
		try {
			instream = new BufferedInputStream(stream);

			int len = instream.available();
			byte[] inblock = new byte[len];
			instream.read(inblock, 0, len);

			//	   now decrypt it!
			//	   extract key from license string lic and use it to decrypt the class data
			dc.setSecret(secret);
			classBytes = dc.decrypt(inblock);

		} catch (IOException e) {
			//if (Debug.DEBUG)
			//Debug.log(this, "error during crypted class loading operation");
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (Exception e) {
			}
		}

		return (classBytes);
	}

	public static void main(String[] args) {
		CryptoClassLoader loader = new CryptoClassLoader("testing");

		try {
			
			Class c =  loader.loadClass("cryptoloader.Hello", true);
		System.out.println("Class: "+ c.getClass());
		System.out.println("ClassLoader: "+ c.getClassLoader());
		System.out.println("Method 0: "+ c.getMethods()[0]);
		System.out.println("Modifiers: "+ c.getModifiers());
		System.out.println("package: "+ c.getPackage());
		
		CryptClass hello = (CryptClass)c.newInstance();
			System.out.println("Class: "+ hello.getClass());

			hello.madeit();
			HelloDep dep = new HelloDep(hello);
			dep.useHello();
		

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
