package com.model;

public class CheckerModel {

    private String key;
    private String value;
    private String showMessage;
    private int maxlen;
    private int minlen;
    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getMinlen() {
        return minlen;
    }

    public void setMinlen(int minlen) {
        this.minlen = minlen;
    }

    public CheckerModel() {
        super();
    }

    public CheckerModel(String key, String value, String showMessage) {
        super();
        this.key = key;
        this.value = value;
        this.showMessage = showMessage;
    }

    public CheckerModel(String key, String value, String showMessage, int maxlen) {
        super();
        this.key = key;
        this.value = value;
        this.showMessage = showMessage;
        this.maxlen = maxlen;
    }

    public CheckerModel(String key, String value, String showMessage, int minlen, int maxlen) {
        super();
        this.key = key;
        this.value = value;
        this.showMessage = showMessage;
        this.maxlen = maxlen;
        this.minlen = minlen;
    }

    public CheckerModel(String key, String value, String showMessage, String pattern) {
        super();
        this.key = key;
        this.value = value;
        this.showMessage = showMessage;
        this.pattern = pattern;
    }
    
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public int getMaxlen() {
        return maxlen;
    }

    public void setMaxlen(int maxlen) {
        this.maxlen = maxlen;
    }

}
