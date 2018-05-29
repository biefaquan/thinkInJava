package com.generics;
/**
 * 泛型 P353
 * @author bfq
 *
 */
class Automobile {}

public class Holder1 {
	private Automobile a;
	public Holder1(Automobile a) {
		this.a = a;
	}
	Automobile get() {
		return a;
	}
}
