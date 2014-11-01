package com.model;

public class AreaProbModel {
    //地区概率模型
    String id;
    String name;
    int  prob;//数值越大概率越高
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
    public int getProb() {
        return prob;
    }
    public void setProb(int prob) {
        this.prob = prob;
    }
    

}
