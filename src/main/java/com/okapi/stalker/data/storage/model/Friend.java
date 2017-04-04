package com.okapi.stalker.data.storage.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity(name="FRIENDS")
public class Friend{
	@Id
	@Column(name="ID")
	private Integer id;
	@OneToOne
	@JoinColumn(name="OWNER_ID")
	private Student owner;
	@OneToOne
	@JoinColumn(name="OWNED_ID")
	private Student owned;
	@Column(name="SINCE")
	private Timestamp since;
	
	
	public Friend(Student owner, Student owned){
		this.owner = owner;
		this.owned = owned;
		since = new Timestamp(System.currentTimeMillis());
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Student getOwner() {
		return owner;
	}
	public void setOwner(Student owner) {
		this.owner = owner;
	}
	public Student getOwned() {
		return owned;
	}
	public void setOwned(Student owned) {
		this.owned = owned;
	}
	public Timestamp getSince() {
		return since;
	}
	public void setSince(Timestamp since) {
		this.since = since;
	}
	
	/**
	 * Two friendship is the same if and only if the owners and the owned are the same.
	 * @param obj Object to compare this Friend object
	 * @return true if two objects are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Friend){
			return this.owner.equals(((Friend) obj).getOwner()) && this.owned.equals(((Friend) obj).getOwned());
		}
		return false;
	}
	
	@Override
	public String toString(){
		return String.format("Friendship ID: %d\nOwner Student Name: %s\nOwned Student Name %s\nSince: %s",
				this.id, this.owner.getName(), this.owned.getName(), since.toString());
	}
	
	
}