package com.model;

import java.util.ArrayList;
import java.util.List;

public class SqlModel {
    private String sql;
    private List<Object> params = new ArrayList<Object>();

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public void addParam(Object param) {
        if (param != null) {
            if (param.getClass().equals(java.util.Date.class)) {
                param = new java.sql.Timestamp(((java.util.Date) param).getTime());
            }
        }
        params.add(param);
    }

    public void addParams(List<Object> params) {
        params.addAll(params);
    }

}
