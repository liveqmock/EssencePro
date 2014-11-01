package com.model;

public class User {
	private Integer id;

	private String bm;

	private String name;

	private String role;

	private String address;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm == null ? null : bm.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

}