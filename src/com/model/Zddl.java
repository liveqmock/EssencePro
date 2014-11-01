package com.model;

import java.util.List;

public class Zddl {

	@Override
	public String toString() {
		return "Zddl [bm=" + bm + ", children=" + children + ", id=" + id
				+ ", name=" + name + "]";
	}

	private String name;
	
	private String bm;
	
	private Integer id;
	
	private List<Zd> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Zd> getChildren() {
		return children;
	}

	public void setChildren(List<Zd> children) {
		this.children = children;
	}

	public Zddl(){}
	
	public Zddl(String bm){
		this.bm = bm;
	}
	
	public Zddl(String name,String bm){
		this.name = name;
		this.bm= bm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bm == null) ? 0 : bm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zddl other = (Zddl) obj;
		if (bm == null) {
			if (other.bm != null)
				return false;
		} else if (!bm.equals(other.bm))
			return false;
		return true;
	}
}
