package com.okapi.stalker.data.storage.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity(name="COURSES")
public class Course implements Serializable{
	@Id
	@Column(name="CODE")
	private String code;
	@Column(name="TITLE")
	private String title;
	@Column(name="ACTIVE")
	private boolean active;

	@OneToMany(mappedBy="course", fetch = FetchType.EAGER)
	private Set<Section> sections;
	
	public Course() {
		sections = new HashSet<Section>();
	}
	
	public boolean addSection(Section section){
		section.setCourse(this);
		if(sections.add(section)){
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Course)
			return this.code.equals(((Course) obj).getCode());
		return false;
	}

	@Override
	public String toString() {
		return String.format("Course Title: %s, Code: %s, Status: %s"
				, this.title, this.code, active ? "ACTIVE" : "DEACTIVE");
	}


}