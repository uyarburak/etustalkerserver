package com.okapi.stalker.executers;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;
import com.okapi.stalker.data.storage.model.Interval;
import com.okapi.stalker.data.storage.model.Section;
import com.okapi.stalker.data.storage.model.Student;

public abstract class Matchable {
	Map<String, Student> students;
	Map<String, Department> departments;
	Map<String, Section> sections;
	Map<String, Course> courses;
	Map<String, Instructor> instructors;
	Set<Interval> intervals;
	
	public Matchable() {
		students = new HashMap<String, Student>();
		departments = new HashMap<String, Department>();
		sections = new HashMap<String, Section>();
		courses = new HashMap<String, Course>();
		instructors = new HashMap<String, Instructor>();
		intervals = new HashSet<Interval>();
	}
	public void printSizes(){
		System.out.println("--------------------------------------");
		System.out.println(this.getClass().getName());
		System.out.println("course: " + courses.size());
		System.out.println("department: " + departments.size());
		System.out.println("student: " + students.size());
		System.out.println("instructor: " + instructors.size());
		System.out.println("section: " + sections.size());
		System.out.println("interval: " + intervals.size());
		System.out.println("---------------------------------------");
	}
}
