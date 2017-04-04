package com.okapi.stalker.data.storage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.DynamicUpdate;

@SuppressWarnings("serial")
@Entity(name="STUDENTS")
@DynamicUpdate(value=true)
public class Student implements Serializable{
	@Id
	@Column(name="ID")
	private String id;

	@Column(name="NAME")
	private String name;

	@Column(name="MAIL")
	private String mail;
	
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private Department department;

	@Column(name="DEPARTMENT_2_ID")
	private String department2;

	@Column(name="YEAR")
	private Integer year;

	@Column(name="SEX")
	private Character gender;

	@Column(name="IMAGE_URL")
	private String image;

	@Column(name="ACTIVE")
	private Boolean active;

//	@ElementCollection
//	private Set<Friend> friends;
	@ManyToMany(mappedBy="students", fetch = FetchType.EAGER)
	private Set<Section> sections;

	public Student() {
//		friends = new HashSet<Friend>();
		sections = new HashSet<Section>();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		if(this.department == null)
			this.department = department;
//		else if(!this.department.equals(department))
//			this.department2 = department;
		else
			return;
		department.addStudent(this);
	}

	public String getDepartment2() {
		return department2;
	}

	public void setDepartment2(String department2) {
		this.department2 = department2;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

//	public boolean addFriend(Student friend){
//		return friends.add(new Friend(this, friend));
//	}
//	public Set<Friend> getFriends() {
//		return friends;
//	}
	public boolean addSection(Section section){
		if(sections.add(section)){
			section.addStudent(this);
			return true;
		}
		return false;
	}
	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

//	public void setFriends(Set<Friend> friends) {
//		this.friends = friends;
//	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Student){
			Student other = (Student)obj;
			return other.id.equals(id) && other.year.equals(year);
		}
		return false;
	}
	@Override
	public String toString() {
		return String.format("Student Name: %s\nMail: %s\nDepartment: %s\nYear: %s\nSex: %c\nImage: %s\nActive: %s",
				this.name, this.mail, this.department, this.year, this.gender, this.image, this.active);
	}
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	public void reset(){
		sections = new HashSet<Section>();
	}
	
	
	
}