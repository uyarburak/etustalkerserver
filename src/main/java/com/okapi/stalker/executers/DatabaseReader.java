package com.okapi.stalker.executers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.common.collect.Sets;
import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;
import com.okapi.stalker.data.storage.model.Interval;
import com.okapi.stalker.data.storage.model.Section;
import com.okapi.stalker.data.storage.model.Student;

public class DatabaseReader extends Matchable {

	public static void main(String[] args) {
		DatabaseReader db = new DatabaseReader();
		db.fetchAllData();
	}
	public boolean read(){
		try{
			fetchAllData();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private void fetchAllData(){
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Course> dbCourseList = session.createCriteria(Course.class).list();
		for (Course course : dbCourseList) {
			courses.put(course.getCode(), course);
		}
		List<Department> dbDepartmentList = session.createCriteria(Department.class).list();
		for (Department department : dbDepartmentList) {
			departments.put(department.getName(), department);
		}
		List<Student> dbStudentList = session.createCriteria(Student.class).list();
		for (Student student : dbStudentList) {
			students.put(student.getId(), student);
		}
		List<Instructor> dbInstructorList = session.createCriteria(Instructor.class).list();
		for (Instructor instructor : dbInstructorList) {
			instructors.put(instructor.getName(), instructor);
		}
		List<Section> dbSectionList = session.createCriteria(Section.class).list();
		for (Section section : dbSectionList) {
			sections.put(section.getCourse().getCode()+section.getSectionNo(), section);
		}
		List<Interval> dbIntervalList = session.createCriteria(Interval.class).list();
		intervals = Sets.newHashSet(dbIntervalList);
		session.getTransaction().commit();
		session.close();
		System.out.println("course: " + dbCourseList.size());
		System.out.println("department: " + dbDepartmentList.size());
		System.out.println("student: " + dbStudentList.size());
		System.out.println("instructor: " + dbInstructorList.size());
		System.out.println("section: " + dbSectionList.size());
		System.out.println("interval: " + dbIntervalList.size());
	}
}
