package com.okapi.stalker.data.storage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity(name="SECTIONS")
public class Section implements Serializable{
	@Id @GeneratedValue
	@Column(name="ID")
	private Integer id;
	@Column(name="SECTION_NO")
	private Integer sectionNo;
	@Column(name="SIZE")
	private Integer size;
	@ManyToOne
	@JoinColumn(name="COURSE_ID")
	private Course course;
	@ManyToOne
	@JoinColumn(name="INSTRUCTOR_ID")
	private Instructor instructor;
	
	@OneToMany(mappedBy="section")
	private Set<Interval> intervals;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="ENROLLMENTS",
	joinColumns=@JoinColumn(name="SECTION_ID"),
	inverseJoinColumns=@JoinColumn(name="STUDENT_ID")
	)
	private Set<Student> students;

	public Section() {
		intervals = new HashSet<Interval>();
		students = new HashSet<Student>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(Integer sectionNo) {
		this.sectionNo = sectionNo;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
		instructor.addSection(this);
	}

	public Set<Interval> getIntervals() {
		return intervals;
	}

	public void setIntervals(Set<Interval> intervals) {
		this.intervals = intervals;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
	public boolean addStudent(Student student){
		return students.add(student);
	}
	
	public boolean addInterval(Interval interval){
		return intervals.add(interval);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Section){
			return this.course.getCode().equals(((Section) obj).getCourse().getCode()) && this.sectionNo == ((Section) obj).getSectionNo();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.course.getCode()+this.sectionNo).hashCode();
	}

	@Override
	public String toString() {
		return String.format("Section Code: %s-%d\nSize: %d\nInstructor Name: %s",
				this.course.getCode(), this.sectionNo, this.size, this.instructor.getName());
	}
	
	
	
	
}