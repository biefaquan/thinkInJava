package com.typeinformation;

class Candy {
	static {System.out.println("Loading Candy");}
}
class Gum {
	static {System.out.println("Loading Gum");}
}
class Cookie {
	static {System.out.println("Loading Cookie");}
}
public class SweetShop {
	public static void main(String[] args) {
		System.out.println("inside main");
		new Candy();
		try {
			Class.forName("com.typeinformation.Gum"); //需要把路径名填写完整。
		} catch (Exception e) {
			System.out.println("Couldn't find Gum");
		}
		System.out.println("After Class.forName(\"Gum\")");
		new Cookie();
		System.out.println("After creating Cookie");
	}
}
