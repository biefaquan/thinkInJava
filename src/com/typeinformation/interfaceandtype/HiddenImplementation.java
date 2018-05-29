package com.typeinformation.interfaceandtype;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * 接口与类型信息 P347
 * @author bfq
 *
 */
public class HiddenImplementation {
	public static void main(String[] args) throws Exception {
		A a = HiddenC.makeA();
		a.f();
		System.out.println(a.getClass().getName());
//		if(a instanceof C) {
//			C c = (C)a;
//			c.g();
//		}
		callHiddenMethod(a, "g");
		callHiddenMethod(a, "u");
		callHiddenMethod(a, "v");
		callHiddenMethod(a, "w");
	}
	static void callHiddenMethod(Object a, String methodName) throws Exception {
		Method g = a.getClass().getDeclaredMethod(methodName);
		g.setAccessible(true);
		g.invoke(a);
	}
}
