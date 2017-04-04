package com.okapi.stalker.data.storage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity(name="INSTRUCTORS")
public class Instructor implements Serializable{
	// SO FAR SO GOOD with this class
	@Id @GeneratedValue
	@Column(name="ID")
	private Integer id;
	@Column(name="NAME")
	private String name;
	@Column(name="MAIL")
	private String mail;
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private Department department;
	@Column(name="SEX")
	private Character gender;
	@Column(name="OFFICE")
	private String office;
	@Column(name="WEBSITE")
	private String website;
	@Column(name="IMAGE_URL")
	private String image;
	@Column(name="TEL")
	private String tel;

	public String getLab() {
		return lab;
	}
	public void setLab(String lab) {
		this.lab = lab;
	}

	@Column(name="LAB_URL")
	private String lab;

	@OneToMany(mappedBy="instructor")
	private Set<Section> sections;

	public Instructor() {
		sections = new HashSet<Section>();
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
		this.department = department;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	public boolean addSection(Section section){
		return sections.add(section);
	}
	
	
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Instructor){
			Instructor other = (Instructor) obj;
			String insName = other.getName().replaceAll("\\.", "");
			if(isSubSequence(insName.toLowerCase(), this.name.toLowerCase())){
				this.mail = other.getMail();
				this.department	= other.getDepartment();
				this.office = other.getOffice();
				this.website = other.getWebsite();
				this.image = other.getImage();
				return true;
			}
		}
		return false;
	}

	private boolean isSubSequence(String s1, String s2){

		// Base Cases
		if (s1.isEmpty()) return true;
		if (s2.isEmpty()) return false;

		// If last characters of two strings are matching
		if(s1.charAt(s1.length()-1) == s2.charAt(s2.length()-1))
			return isSubSequence(s1.substring(0,  s1.length()-1), s2.substring(0,  s2.length()-1)); 

		// If last characters are not matching
		return isSubSequence(s1, s2.substring(0,  s2.length()-1)); 
	}

	@Override
	public String toString() {
		return String.format("Instructor Name: %s\nMail: %s\nDepartment: %s\nSex: %c\nOffice: %s\nWebsite: %s\nImage: %s",
				this.name, this.mail, this.department, this.gender, this.office, this.website, this.image);
	}





}