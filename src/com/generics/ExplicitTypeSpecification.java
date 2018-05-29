package com.generics;

import java.util.*;

import com.typeinformation.nullobject.Person;
import com.util.New;

public class ExplicitTypeSpecification {
	static void f(Map<Person, List<Pet>> petPeople) {}
	public static void main(String[] args) {
		f(New.<Person, List<Pet>>map());
	}
}
class Pet{}