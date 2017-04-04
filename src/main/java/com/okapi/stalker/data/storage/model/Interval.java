package com.okapi.stalker.data.storage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity(name="INTERVALS")
public class Interval implements Serializable{	
	@Id @GeneratedValue
	@Column(name="ID")
	private Integer id;

	@Column(name="DAY")
	private Integer day;

	@Column(name="HOUR")
	private Integer hour;
	
	@Column(name="ROOM")
	private String room;

	@ManyToOne
	@JoinColumn(name="SECTION_ID")
	private Section section;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Interval))
			return false;
		Interval other = (Interval) obj;
		return other.day.equals(this.day) && other.hour.equals(this.hour) && other.room.equals(this.room) && other.section.getCourse().getCode().equals(this.getSection().getCourse().getCode());
	}
	@Override
	public int hashCode(){
	    return toString().hashCode();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.day);
		builder.append(this.hour);
		builder.append(this.room);
		builder.append(this.section.getCourse().getCode());
		builder.append(this.section.getSectionNo());
		return builder.toString();
	}
	
	
	
}