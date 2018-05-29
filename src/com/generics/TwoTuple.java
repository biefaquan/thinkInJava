package com.generics;
/**
 * 泛型  P354
 * @author bfq
 *
 */
public class TwoTuple<A,B> {
	public final A first;
	public final B second;
	public TwoTuple(A a, B b) {
		first = a;
		second = b;
	}
	@Override
	public String toString() {
		return "(" + first + "," + second +")";
	}
}
