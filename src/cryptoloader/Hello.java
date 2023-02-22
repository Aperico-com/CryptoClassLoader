/*
 * Created on Mar 5, 2004
 * Copy right 2003 AET Data Consulting, all rights reserved
 * Author: Andreas Toresäter
 **/
package cryptoloader;

import javax.swing.JDialog;

/**
 * @author Andreas E Toresäter
 * Copyright 2003 AET Data Consulting, all rights reserved.
*/
public class Hello implements CryptClass{

	public Hello(){
		
		System.out.println("hi");
		//HelloDep2 dep2 = new HelloDep2(this);
		//dep2.useHello();
		
	}
	
	
	
	public void madeit(){
		System.out.println("you made it");
		javax.swing.JDialog d = new javax.swing.JDialog();
		d.setVisible(true);
	}
	
	public static void main(String[] args){
		new Hello();
	}
	
}
