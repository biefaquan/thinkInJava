package com.java;

public class demo1 {
	public static void main(String[] args) {
		finaltest t = new finaltest();
		t.setA("dd");
		final finaltest ss = t;
		System.out.println(ss.getA());
		t.setA("aa");
		System.out.println(ss.getA());
		
	}
} 

class finaltest {
	private String a;
	
	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
	
}
