package com.generics.coffee;
/**
 * 泛型接口 P358
 * @author bfq
 *
 */
public class Coffee {
	private static long counter = 0;
	private final long id = counter++;
	public String toString() {
		return getClass().getSimpleName() + " " + id;
	}
}
