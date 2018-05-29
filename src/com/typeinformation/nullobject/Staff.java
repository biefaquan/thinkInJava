package com.typeinformation.nullobject;

import java.util.ArrayList;

/**
 * 空对象 P342
 * @author bfq
 *
 */
public class Staff extends ArrayList<Position> {
	public void add(String title, Person person) {
		add(new Position(title, person));
	}
	public void add(String... titles) {
		for(String title : titles) {
			add(new Position(title));
		}
	}
	public Staff(String... titles) {
		add(titles);
	}
	public boolean positionAvailable(String title) {
		for(Position position : this) {
			if(position.getTitle().equals(title) && position.getPerson() == Person.NULL) {
				return true;
			}
		}
		return false;
	}
	public void fillPosition(String title, Person hire) {
		for(Position position : this) {
			if(position.getTitle().equals(title) && position.getPerson() == Person.NULL) {
				position.setPerson(hire);
				return;
			}
		}
		throw new RuntimeException("Position " + title +" not available");
	}
	public static void main(String[] args) {
		Staff staff = new Staff("President", "CTO", 
				"Marketing Mananger", "Product Manager", 
				"Project Lead", "Software Engineer",
				"Software Engineer", "Test Engineer",
				"Technical Writer");
		staff.fillPosition("President", new Person("Me", "Last", "the Burbs"));
		staff.fillPosition("Project Lead", new Person("Janet", "Planner", "The Burbs"));
		if(staff.positionAvailable("Software Engineer")) {
			staff.fillPosition("Software Engineer", new Person("Bob", "Coder", "Bright Light City"));
		}
		System.out.println(staff);
	}
}
