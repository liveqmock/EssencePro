package com.model;

public class ExeclBean {

	String name;

	Integer leng = 15;

	String getterMethod;

	public ExeclBean(String name, String getterMethod) {
		super();
		this.name = name;
		this.getterMethod = getterMethod;
	}

	public ExeclBean() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLeng() {
		return leng;
	}

	public void setLeng(Integer leng) {
		this.leng = leng;
	}

	public String getGetterMethod() {
		return getterMethod;
	}

	public void setGetterMethod(String getterMethod) {
		this.getterMethod = getterMethod;
	}

}
