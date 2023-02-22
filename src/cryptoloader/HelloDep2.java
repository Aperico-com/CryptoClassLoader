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
public class HelloDep2 {
	
	Hello hello;
	
		public HelloDep2(Hello hello){
			this.hello = hello;
		}
		
		public void useHello(){
			System.out.println("Test av icke crypterad class");
		}

}
