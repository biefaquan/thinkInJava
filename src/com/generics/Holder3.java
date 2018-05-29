package com.generics;
/**
 * 泛型 P354
 * @author bfq
 *
 * @param <T>
 */
public class Holder3<T> {
	private T a;
	public Holder3(T a) {
		this.a = a;
	}
	public void set(T a) {
		this.a = a;
	}
	public T get() {
		return a;
	}
	public static void main(String[] args) {
		Holder3<Automobile> h3 = new Holder3<Automobile>(new Automobile());
		//h3.set("Not an Automobile"); //Error
		//h3.set(1);    //Error   
	}
}
