package com.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.model.DataKeyStatic;
import com.model.PageNavigationBean;
import com.model.QueryModel;
import com.model.SqlModel;

public abstract class MySqlBaseDao<T> extends TransactionDao<T> {

    public MySqlBaseDao(String key, String tableName) {
        super(key, tableName);
    }

    public MySqlBaseDao(String tableName) {
        super(DataKeyStatic.CREDIT_MYSQL_DATA_SOURE_KEY, tableName);
    }

    public boolean exists(String key, Object value) {
        return getCount("select 1 from " + tableName + " where " + key + "=? limit 1", value) > 0;
    }

    public T findByUuid(String uuid) {
        return getOne("select * from " + tableName + " where uuid=? limit 1", uuid);
    }

    public long getMax(String table, String col) {
        return getLong("select max(" + col + ") from " + table);
    }

    public T findById(String key, Object value) {
        return getOne("select * from " + tableName + " where " + key + "=? limit 1", value);
    }

    /**
     * sql和参数
     * 
     * @param sql
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     * @author 玄承勇
     * @date 2014-6-30 下午3:03:34
     */
    protected PageNavigationBean<T> getPageNavigationBean(String sql, int pageNum, int pageSize, Object... params) {
        PageNavigationBean<T> pageBean = new PageNavigationBean<T>();
        pageBean.setCurrentPage(pageNum);
        pageBean.setPageSize(pageSize);
        String countSql = "select count(1) from (" + sql + ")";
        int count = getCount(countSql, params);

        pageBean.setTotalCount(count);

        int startRowNum = pageBean.getCurrentPoint();

        List<T> list = getList(sql + " limit " + startRowNum + "," + pageSize, params);
        pageBean.setResultList(list);
        return pageBean;
    }

    public Map<String, Object> getMap(QueryModel queryModel) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getNoOrderQueryStr();
        return getMap(sql, queryModel.getParams());
    }

    private Object updateAndReturnId(String sql, Object... params) {
        try {
            Connection connection = getCon();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Object o : params) {
                    ps.setObject(i, o);
                    i++;
                }
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getObject(1) : null;
            } finally {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("修改出错,sql:" + sql, e);
            throw new RuntimeException(e);
        }
    }

    public Object saveAndReturnId(T modelBean) {
        if (modelBean != null) {
            SqlModel sqlModel = getInsertSqlModel(modelBean, false);
            return updateAndReturnId(sqlModel.getSql(), sqlModel.getParams());
        }
        return null;
    }

    public T getOne(QueryModel queryModel) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getNoOrderQueryStr() + " limit 1";
        return getOne(sql, queryModel.getParams());
    }

    public List<T> getList(QueryModel queryModel, int size) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr() + " limit "
                + size;
        return getList(sql, queryModel.getParams());
    }

    public List<T> getListBySql(QueryModel queryModel, String condition, int size) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " where " + condition + " limit " + size;
        ;
        return getList(sql, queryModel.getParams());
    }

    /**
     * queryModel
     * 
     * @param queryModel
     * @param pageNum
     * @param pageSize
     * @return
     * @author 玄承勇
     * @date 2014-6-30 下午3:03:53
     */
    public PageNavigationBean<T> getPageNavigationBean(QueryModel queryModel, int pageNum, int pageSize) {
        PageNavigationBean<T> pageBean = new PageNavigationBean<T>();
        pageBean.setCurrentPage(pageNum);
        pageBean.setPageSize(pageSize);

        String countSql = "select count(1) from " + tableName + " " + queryModel.getNoOrderQueryStr();

        int count = getCount(countSql, queryModel.getParams());

        pageBean.setTotalCount(count);

        int startRowNum = pageBean.getCurrentPoint() - 1;

        String pageBeanSql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr()
                + " limit " + startRowNum + "," + pageSize;

        List<T> list = getList(pageBeanSql, queryModel.getParams());
        pageBean.setResultList(list);
        return pageBean;
    }

    @Override
    protected void updateIndexField(T modelBean, Field field, Map<String, Field> fileMap) throws IllegalArgumentException,
            IllegalAccessException {
    }

    @Override
    protected void updateIndexFieldContrast(T modelBean, T OldmodelBean, Map<String, Field> fieldMap) {
    }
}
