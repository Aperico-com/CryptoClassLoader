/*
 * Created on Mar 5, 2004
 * Copy right 2003 AET Data Consulting, all rights reserved
 * Author: Andreas Toresäter
 **/
package cryptoloader;

/**
 * @author Andreas E Toresäter
 * Copyright 2003 AET Data Consulting, all rights reserved.
*/
public class HelloDep {
	
	CryptClass hello;
	
		public HelloDep(CryptClass hello){
			this.hello = hello;
		}
		
		public void useHello(){
			hello.madeit();
		}

}
