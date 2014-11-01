package com.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.model.DataKeyStatic;
import com.model.PageNavigationBean;
import com.model.QueryModel;

public abstract class OracleBaseDao<T> extends TransactionDao<T> {

    public OracleBaseDao(String key, String tableName) {
        super(key, tableName);
    }

    public OracleBaseDao(String tableName) {
        super(DataKeyStatic.CREDIT_ORACLE_DATA_SOURE_KEY, tableName);
    }

    /* 公共方法 */
    public boolean exists(Object id) {
        return exists("id", id);
    }

    public boolean exists(String key, Object value) {
        return getCount("select 1 from " + tableName + " where rownum=1 and " + key + "=?", value) > 0;
    }

    public T findByUuid(String uuid) {
        return getOne("select * from " + tableName + " where rownum=1 and uuid=?", uuid);
    }

    public T findById(Object id) {
        return findById("id", id);
    }

    public T findById(String key, Object value) {
        return getOne("select * from " + tableName + " where rownum=1 and " + key + "=?", value);
    }

    protected int updateThrowException(String sql, Object... params) throws SQLException {
        Connection connection = getCon();
        try {
            return QRY_RUN.update(connection, sql, params);
        } finally {
            if (connection.getAutoCommit()) {
                closeCon(connection);
            }
        }
    }

    protected PageNavigationBean<T> getPageNavigationBean(String sql, int pageNum, int pageSize, Object... params) {
        PageNavigationBean<T> pageBean = new PageNavigationBean<T>();
        pageBean.setCurrentPage(pageNum);
        pageBean.setPageSize(pageSize);
        String countSql = "select count(1) from (" + sql + ")";
        int count = getCount(countSql, params);

        pageBean.setTotalCount(count);

        int startRowNum = pageBean.getCurrentPoint();
        int endRowNum = startRowNum + pageSize;

        sql = "SELECT * FROM (SELECT TT.*, ROWNUM AS ROWNO FROM (" + sql + ") TT WHERE ROWNUM <" + endRowNum
                + ") TABLE_ALIAS where TABLE_ALIAS.rowno >=" + startRowNum;
        List<T> list = getList(sql, params);
        pageBean.setResultList(list);
        return pageBean;
    }

    public Map<String, Object> getMap(QueryModel queryModel) {
        queryModel.addEq("rownum", 1);
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();
        return getMap(sql, queryModel.getParams());
    }

    public T getOne(QueryModel queryModel) {
        queryModel.addEq("rownum", 1);
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();
        return getOne(sql, queryModel.getParams());
    }

    public List<T> getList(QueryModel queryModel, int size) {
        String sql = "select * from (select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr()
                + ") where rownum<=" + size;
        return getList(sql, queryModel.getParams());
    }

    public List<T> getTopList(String sql, int rownum) {
        String pageSql = "select * from (" + sql + ") where rownum<= ?";
        return super.getList(pageSql, rownum);
    }

    public PageNavigationBean<T> getPageNavigationBean(QueryModel queryModel, int pageNum, int pageSize) {
        return getPageNavigationBean(queryModel, pageNum, pageSize, true);
    }

    public PageNavigationBean<T> getPageNavigationBeanNoOrder(QueryModel queryModel, int pageNum, int pageSize) {
        return getPageNavigationBean(queryModel, pageNum, pageSize, false);
    }

    public <TT> PageNavigationBean<TT> getPageNavigationBean(QueryModel queryModel, int pageNum, int pageSize, Class<TT> modelType) {
        PageNavigationBean<TT> pageBean = new PageNavigationBean<TT>();
        pageBean.setCurrentPage(pageNum);
        pageBean.setPageSize(pageSize);

        String countSql = "select count(1) from " + tableName + " " + queryModel.getNoOrderQueryStr();

        int count = getCount(countSql, queryModel.getParams());

        pageBean.setTotalCount(count);

        int startRowNum = pageBean.getCurrentPoint();
        int endRowNum = startRowNum + pageSize;

        String pageBeanSql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();

        pageBeanSql = "SELECT * FROM (SELECT TT.*, ROWNUM AS ROWNO FROM (" + pageBeanSql + ") TT WHERE ROWNUM <" + endRowNum
                + ") TABLE_ALIAS where TABLE_ALIAS.rowno >=" + startRowNum;

        List<TT> list = getList(pageBeanSql, modelType, queryModel.getParams());
        pageBean.setResultList(list);
        return pageBean;
    }

    private PageNavigationBean<T> getPageNavigationBean(QueryModel queryModel, int pageNum, int pageSize, boolean isOrder) {
        PageNavigationBean<T> pageBean = new PageNavigationBean<T>();
        pageBean.setCurrentPage(pageNum);
        pageBean.setPageSize(pageSize);

        String countSql = "select count(1) from " + tableName + " " + queryModel.getNoOrderQueryStr();

        int count = getCount(countSql, queryModel.getParams());

        pageBean.setTotalCount(count);

        int startRowNum = pageBean.getCurrentPoint();
        int endRowNum = startRowNum + pageSize;

        String pageBeanSql = "select " + queryModel.getSelectFields() + " from " + tableName;
        if (isOrder) {
            pageBeanSql += " " + queryModel.getOrderQueryStr();
        } else {
            pageBeanSql += " " + queryModel.getNoOrderQueryStr();
        }

        pageBeanSql = "SELECT * FROM (SELECT TT.*, ROWNUM AS ROWNO FROM (" + pageBeanSql + ") TT WHERE ROWNUM <" + endRowNum
                + ") TABLE_ALIAS where TABLE_ALIAS.rowno >=" + startRowNum;

        List<T> list = getList(pageBeanSql, queryModel.getParams());
        pageBean.setResultList(list);
        return pageBean;
    }

    public List<T> getList(QueryModel queryModel, int startNum, int endNum) {
        String pageBeanSql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();
        pageBeanSql = "SELECT * FROM (SELECT TT.*, ROWNUM AS ROWNO FROM (" + pageBeanSql + ") TT WHERE ROWNUM <" + endNum
                + ") TABLE_ALIAS where TABLE_ALIAS.rowno >=" + startNum;

        return getList(pageBeanSql, queryModel.getParams());
    }

    public int batchDelete(QueryModel queryModel) {
        String sql = "delete from " + tableName + " " + queryModel.getOrderQueryStr();
        return update(sql, queryModel.getParams());
    }
}
