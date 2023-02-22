/*
 * Created on Mar 5, 2004
 * Copy right 2003 AET Data Consulting, all rights reserved
 * Author: Andreas Toresäter
 **/
package cryptoloader;

import java.io.*;
import java.net.*;


import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.Attributes;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.Attributes;
import java.io.IOException;

/**
 * @author Andreas E Toresäter
 * Copyright 2003 AET Data Consulting, all rights reserved.
*/
public class Loader extends URLClassLoader {

	String secret; // my license string
	Decrypter dc;
	public static String cryptExtension = ".crypt";

	public Loader(URL url, String sec) {
		super(new URL[] { url });
		this.secret = sec;
		this.dc  = new Decrypter(this.secret); // checks license string 
	}
	
	public void invokeClass(String name, String[] args)
	throws ClassNotFoundException,
		   NoSuchMethodException,
		   InvocationTargetException
	{
	Class c = findClass(name);
	Method m = c.getMethod("main", new Class[] { args.getClass() });
	m.setAccessible(true);
	int mods = m.getModifiers();
	if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
		!Modifier.isPublic(mods)) {
		throw new NoSuchMethodException("main");
	}
	try {
		m.invoke(null, new Object[] { args });
	} catch (IllegalAccessException e) {
		// This should not happen, as we have disabled access checks
	}
	}


/*
public Class loadClass(String name){
	byte[] b = loadClassData(name);
	Class c = defineClass(name, b, 0, b.length);
	resolveClass(c);
			return c;
}

*/
	public Class findClass(String name) {
		byte[] b = loadClassData(name);
		return defineClass(name, b, 0, b.length);
	}

	private byte[] loadClassData(String name) {
		//	   load the class data from the crypted file
		name = name.replace('.', '/'); // packagename to filesystem conversion
		String filename = name + cryptExtension;
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
		//Loader loader2 = new Loader("testing");
		CryptoClassLoader ccl = new CryptoClassLoader("testing");
		
		URL[] urls = new URL[1];
		try {
			urls[0] = new URL("jar", "", "http://www.aetdata.com/cryptan.jar" + "!/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	
		Loader loader=null;
		String[] arg = new String[0];

			loader =
				new Loader(urls[0], "testing"); 
			try {
				try {
					loader.invokeClass("cryptoloader.Hello", arg);
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
/*
				try {
					loader.loadClass("cryptoloader.Hello", true).newInstance();
					
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
*/				

			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
/*		
		try {
			//Hello	hello = (Hello)loader.loadClass("cryptoloader.Hello").newInstance();
			loader.loadClass("cryptoloader.Hello").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	/*
		Loader loader = new Loader("testing");

		try {
			loader.loadClass("cryptan", true);
				//Hello	hello = (Hello)loader.loadClass("cryptoloader.Hello", true).newInstance();
				Hello hello = (Hello)ClassLoader.getSystemClassLoader().loadClass("cryptoloader.Hello").newInstance();
				HelloDep dep = new HelloDep(hello);
					dep.useHello();
				} catch (Exception e) {
				}

/*
		try {
		Hello	hello = (Hello)loader.loadClass("cryptoloader.Hello", true).newInstance();
		HelloDep dep = new HelloDep(hello);
			dep.useHello();
		} catch (Exception e) {
		}
*/
		
	}

}
