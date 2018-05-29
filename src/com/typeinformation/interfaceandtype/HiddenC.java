package com.typeinformation.interfaceandtype;
/**
 * 接口与类型信息 P347
 * @author bfq
 *
 */
class C implements A {
	@Override
	public void f() {
		System.out.println("public C.f()");
	}
	public void g() {
		System.out.println("public C.g()");
	}
	void u() {
		System.out.println("protected C.v()");
	}
	protected void w() {
		System.out.println("private C.w()");
	}
}

public class HiddenC {
	public static A makeA() {
		return new C();
	}
}
