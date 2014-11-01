package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class QueryModel {

    private String selectFields = "*";
    private String condition = "";// 查询条件
    private String order = "";// 排序条件

    private List<Object> params = new ArrayList<Object>();

    public QueryModel() {
    }

    public QueryModel(String selectFields) {
        this.selectFields = selectFields;
    }

    private Object formatValue(Object value) {
        if (value.getClass() == java.util.Date.class) {
            return new java.sql.Date(((Date) value).getTime());
        } else {
            return value;
        }
    }

    public QueryModel(String fieldName, Object value) {
        addEq(fieldName, value);
    }

    public void addEq(String fieldName, Object value) {
        if (value == null) {
            condition += " and " + fieldName + " is null";
        } else {
            condition += " and " + fieldName + "=?";
            params.add(formatValue(value));
        }
    }

    public void addUnEq(String fieldName, Object value) {
        if (value == null) {
            condition += " and " + fieldName + " is not null";
        } else {
            condition += " and " + fieldName + "!=?";
            params.add(formatValue(value));
        }
    }

    public static void main(String[] args) {
        QueryModel queryModel = new QueryModel();

        QueryModel queryModel1 = new QueryModel();
        queryModel1.addGe("userId", 1);

        QueryModel queryModel2 = new QueryModel();
        queryModel2.addLe("userId", 2);

        queryModel.addOrMultiCondition(queryModel1, queryModel2);

        queryModel.addEq("id", 3);
        System.out.println(queryModel.getOrderQueryStr());
        System.out.println(queryModel.params);
    }

    // 大于
    public void addGt(String fieldName, Object value) {
        condition += " and " + fieldName + ">?";
        params.add(formatValue(value));
    }

    public void addGe(String fieldName, Object value) {
        condition += " and " + fieldName + ">=?";
        params.add(formatValue(value));
    }

    // 小于
    public void addLt(String fieldName, Object value) {
        condition += " and " + fieldName + "<?";
        params.add(formatValue(value));
    }

    public void addLe(String fieldName, Object value) {
        condition += " and " + fieldName + "<=?";
        params.add(formatValue(value));
    }

    public void addLeftLike(String fieldName, String value) {
        condition += " and " + fieldName + " like '" + value + "%'";
    }

    public void addLike(String fieldName, String value) {
        condition += " and " + fieldName + " like '%" + value + "%'";
    }

    public void addRegion(String fieldName, Object value1, Object value2) {
        condition += " and " + fieldName + " between ? and ?";
        params.add(formatValue(value1));
        params.add(formatValue(value2));
    }

    public void addInCondition(String fieldName, String subCondition) {
        condition += " and " + fieldName + " in (" + subCondition + ")";
    }

    public void addInSet(String fieldName, Object[] values) {
        addInSet(fieldName, values, true);
    }

    public void addInSet(String fieldName, Object[] values, boolean isContained) {
        String str = "";
        for (Object value : values) {
            str += ",'" + value + "'";
        }
        if (isContained) {
            condition += " and " + fieldName + " in (" + str.substring(1) + ")";
        } else {
            condition += " and " + fieldName + " not in (" + str.substring(1) + ")";
        }
    }

    public void addInSet(String fieldName, Set<?> values) {
        addInSet(fieldName, values, true);
    }

    public void addInSet(String fieldName, Set<?> values, boolean isContained) {
        addInSet(fieldName, values.toArray(), isContained);
    }

    public void addCondition(String condition) {
        this.condition += " and " + condition;
    }

    public void addMultiCondition(boolean isOr, QueryModel... queryModels) {
        if (isOr) {
            addOrMultiCondition(queryModels);
        } else {
            addAndMultiCondition(queryModels);
        }
    }

    public void addAndMultiCondition(QueryModel... queryModels) {
        for (QueryModel tempQueryModel : queryModels) {
            String temp = tempQueryModel.getCondition();
            if (temp != null && !temp.isEmpty()) {
                condition += " and " + temp;
                params.addAll(Arrays.asList(tempQueryModel.getParams()));
            }
        }
    }

    public void addOrMultiCondition(QueryModel... queryModels) {
        String tempCondition = "";
        for (QueryModel tempQueryModel : queryModels) {
            String temp = tempQueryModel.getCondition();
            if (temp != null && !temp.isEmpty()) {
                tempCondition += " or (" + temp + ")";
                params.addAll(Arrays.asList(tempQueryModel.getParams()));
            }
        }
        condition += " and (" + tempCondition.substring(4) + ")";
    }

    public void setOrder(String fieldName, boolean isDesc) {
        order = " order by " + fieldName;
        if (isDesc) {
            order += " desc";
        }
    }

    private String getCondition() {
        if (condition.isEmpty()) {
            return "";
        }
        return condition.substring(4);
    }

    public String getNoOrderQueryStr() {
        if (condition.isEmpty()) {
            return "";
        }
        return "where" + condition.substring(4);
    }

    public String getOrderQueryStr() {
        return getNoOrderQueryStr() + order;
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public String getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(String selectFields) {
        this.selectFields = selectFields;
    }

    /**
     * 添加查询添加sql,以and开头
     * 
     * @return
     * @author 玄承勇
     * @date 2014-6-30 上午9:35:30
     */
    public String getNoOrderQueryStrCondition() {
        if (condition.isEmpty()) {
            return "";
        }
        return " and " + condition.substring(4);
    }
}
