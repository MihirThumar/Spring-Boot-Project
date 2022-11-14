package com.school.model;

public class Student {
	
	private int sId;
	private int rollNO;
	private String name;
	private String lastName;
	private String standard;
	
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getRollNO() {
		return rollNO;
	}
	public void setRollNO(int rollNO) {
		this.rollNO = rollNO;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Student() {
		super();
	}
	
	@Override
	public String toString() {
		return " {sId:" + sId + ", rollNO=" + rollNO + ", name=" + name + ", lastName=" + lastName
				+ ", standard=" + standard + "}";
	}
	
}
