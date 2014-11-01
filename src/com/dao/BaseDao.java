package com.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.model.QueryModel;
import com.model.SqlModel;
import com.util.LoggerUtil;
import com.util.ModelUtil;

public abstract class BaseDao<T> {
    protected final static Logger log = LoggerUtil.getLogger();

    protected final static QueryRunner QRY_RUN = new QueryRunner();

    protected String tableName;
    private ResultSetHandler<T> resultSetHandler;
    private ResultSetHandler<List<T>> resultListSetHandler;

    @SuppressWarnings("unchecked")
    protected Class<T> getModelClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private ResultSetHandler<Integer> countRsHandler = new ResultSetHandler<Integer>() {
        public Integer handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    };
    private ResultSetHandler<Long> longRsHandler = new ResultSetHandler<Long>() {
        public Long handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0l;
        }
    };
    private ResultSetHandler<Object> objectRsHandler = new ResultSetHandler<Object>() {
        public Object handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        }
    };
    private ResultSetHandler<List<Long>> longRsListHandler = new ResultSetHandler<List<Long>>() {

        @Override
        public List<Long> handle(ResultSet rs) throws SQLException {
            List<Long> longList = new ArrayList<Long>();
            while (rs.next()) {
                longList.add(rs.getLong(1));
            }
            return longList;
        }
    };

    public BaseDao(String tableName) {
        this.tableName = tableName;

        Class<T> modelCl = getModelClass();
        resultSetHandler = new BeanHandler<T>(modelCl, new BasicRowProcessor(new MyBeanProcessor()));
        resultListSetHandler = new BeanListHandler<T>(modelCl, new BasicRowProcessor(new MyBeanProcessor()));
    }

    // 抽象方法
    protected abstract Connection getCon() throws SQLException;

    protected abstract void closeCon(Connection connection) throws SQLException;

    protected abstract int update(String sql, Object... params);

    protected abstract int batchUpdate(String sql, Object[][] params);

    protected final int getCount(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, countRsHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return 0;
        }
    }

    protected final long getLongThrowException(String sql, Object... params) throws SQLException {
        Connection connection = getCon();
        try {
            return QRY_RUN.query(connection, sql, longRsHandler, params);
        } finally {
            if (connection.getAutoCommit()) {
                closeCon(connection);
            }
        }
    }

    protected final long getLong(String sql, Object... params) {
        try {
            return getLongThrowException(sql, params);
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return 0;
        }
    }

    protected final Object getObject(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, objectRsHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final List<Long> getLongList(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, longRsListHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final Map<String, Object> getMap(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, new MapHandler(), params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final List<Map<String, Object>> getMapList(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, new MapListHandler(), params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final <V> V getOne(String sql, Class<V> modelCl, Object... params) {
        try {
            Connection connection = getCon();
            try {
                ResultSetHandler<V> resultSetHandler = new BeanHandler<V>(modelCl, new BasicRowProcessor(new MyBeanProcessor()));
                return QRY_RUN.query(connection, sql, resultSetHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final T getOne(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, resultSetHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final List<T> getList(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.query(connection, sql, resultListSetHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    protected final <TT> List<TT> getList(String sql, Class<TT> modelType, Object... params) {
        try {
            Connection connection = getCon();
            try {
                ResultSetHandler<List<TT>> resultListSetHandler = new BeanListHandler<TT>(modelType, new BasicRowProcessor(
                        new MyBeanProcessor()));
                return QRY_RUN.query(connection, sql, resultListSetHandler, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("查询出错,sql:" + sql, e);
            return null;
        }
    }

    /* 公共方法 */

    public boolean exists(QueryModel queryModel) {
        String sql = "select 1 from " + tableName + " " + queryModel.getNoOrderQueryStr();
        return getLong(sql, queryModel.getParams()) > 0;
    }

    protected List<T> findList(String key, Object value) {
        return getList("select * from " + tableName + " where " + key + "=?", value);
    }

    protected List<T> findAllList() {
        return getList("select * from " + tableName);
    }

    protected List<T> findListBySql(String queryStr) {
        return getList("select * from " + tableName + " where " + queryStr);
    }

    public boolean delete(Object id) {
        return delete("id", id);
    }

    public boolean delete(String key, Object value) {
        String sql = "delete from " + tableName + " where " + key + "=?";
        return update(sql, value) > 0;
    }

    /**
     * 通过id进行保存或更新
     */
    public boolean update(T modelBean, String... ids) {
        if (modelBean != null) {
            if (ids.length == 0) {
                Object id = ModelUtil.getModelId(modelBean);
                if (id == null) {
                    log.warn("更新失败，模型id为空!", new Exception());
                    return false;
                }
            }
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<String, Object> objMap = getSqlObjMap(modelBean, false, fieldMap);
            SqlModel sqlModel = getUpdateSqlModel(objMap, ids);
            if (fieldMap != null && !fieldMap.isEmpty()) {
                SqlModel sqlQueryModel = getPkSqlModel(objMap, fieldMap, ids);// 原来的值
                T oldModelBean = getOne(sqlQueryModel.getSql(), sqlQueryModel.getParams());
                updateIndexFieldContrast(modelBean, oldModelBean, fieldMap);// 修改原来的值
            }
            return update(sqlModel.getSql(), sqlModel.getParams()) > 0;
        }
        return false;
    }

    public boolean updateAllField(T modelBean, String... ids) {
        if (modelBean != null) {
            if (ids.length == 0) {
                Object id = ModelUtil.getModelId(modelBean);
                if (id == null) {
                    log.warn("更新失败，模型id为空!", new Exception());
                    return false;
                }
            }
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<String, Object> objMap = getSqlObjMap(modelBean, true, fieldMap);
            SqlModel sqlModel = getUpdateSqlModel(objMap, ids);
            if (fieldMap != null && !fieldMap.isEmpty()) {
                SqlModel sqlQueryModel = getPkSqlModel(objMap, fieldMap, ids);// 原来的值
                T oldModelBean = getOne(sqlQueryModel.getSql(), sqlQueryModel.getParams());
                updateIndexFieldContrast(modelBean, oldModelBean, fieldMap);// 修改原来的值
            }
            return update(sqlModel.getSql(), sqlModel.getParams()) > 0;
        }
        return false;
    }

    public int update(List<T> modelBeans, String... ids) {
        if (modelBeans != null && !modelBeans.isEmpty()) {
            Object[][] params = new Object[modelBeans.size()][];
            String sql = null;
            int i = 0;
            for (T modelBean : modelBeans) {
                Map<String, Field> fieldMap = new HashMap<String, Field>();
                Map<String, Object> objMap = getSqlObjMap(modelBean, false, fieldMap);
                SqlModel sqlModel = getUpdateSqlModel(objMap, ids);
                params[i++] = sqlModel.getParams();
                if (sql == null) {
                    sql = sqlModel.getSql();
                }
                if (fieldMap != null && !fieldMap.isEmpty()) {
                    SqlModel sqlQueryModel = getPkSqlModel(objMap, fieldMap, ids);// 原来的值
                    T oldModelBean = getOne(sqlQueryModel.getSql(), sqlQueryModel.getParams());
                    updateIndexFieldContrast(modelBean, oldModelBean, fieldMap);// 修改原来的值
                }
            }
            return batchUpdate(sql, params);
        }
        return 0;
    }

    /**
     * 通过id进行保存或更新
     */
    public boolean updateByMap(Map<String, Object> modelMap, String... ids) {
        if (modelMap == null) {
            log.warn("更新失败，模型为空!", new Exception());
            return false;
        }
        if (ids.length == 0) {
            Object id = modelMap.get("id");
            if (id == null) {
                log.warn("更新失败，模型id为空!", new Exception());
                return false;
            }
        }
        SqlModel sqlModel = getUpdateSqlModel(modelMap, ids);
        return update(sqlModel.getSql(), sqlModel.getParams()) > 0;
    }

    public boolean save(T modelBean) {
        if (modelBean != null) {
            SqlModel sqlModel = getInsertSqlModel(modelBean, false);
            return update(sqlModel.getSql(), sqlModel.getParams()) > 0;
        }
        return false;
    }

    public int save(List<T> modelBeans) {
        if (modelBeans != null && !modelBeans.isEmpty()) {
            Object[][] params = new Object[modelBeans.size()][];
            String sql = null;
            int i = 0;
            for (T modelBean : modelBeans) {
                SqlModel sqlModel = getInsertSqlModel(modelBean, true);
                params[i++] = sqlModel.getParams();
                if (sql == null) {
                    sql = sqlModel.getSql();
                }
            }
            return batchUpdate(sql, params);
        }
        return 0;
    }

    private boolean isSupportedType(Class<?> fieldType) {
        return fieldType.isPrimitive() || Number.class.isAssignableFrom(fieldType) || fieldType.equals(String.class)
                || fieldType.equals(Date.class) || fieldType.equals(Timestamp.class) || fieldType.equals(byte[].class);
    }

    protected SqlModel getInsertSqlModel(T modelBean, boolean isAll) {
        Map<String, Object> objMap = getSqlObjMap(modelBean, isAll, null);
        SqlModel sqlModel = new SqlModel();
        String keySql = "", insertValueSql = "";
        for (Entry<String, Object> e : objMap.entrySet()) {
            Object value = e.getValue();
            keySql += "," + e.getKey();
            insertValueSql += ",?";
            sqlModel.addParam(value);
        }
        sqlModel.setSql("insert into " + tableName + "(" + keySql.substring(1) + ") values(" + insertValueSql.substring(1) + ")");
        return sqlModel;
    }

    // fileMap 用于存放需要记录图片、附件索引的字段
    private Map<String, Object> getSqlObjMap(T modelBean, boolean isAll, Map<String, Field> fileMap) {
        Map<String, Object> objMap = new HashMap<String, Object>();
        Map<String, Field> fieldMap = ModelUtil.getFieldMap(modelBean.getClass());
        for (Entry<String, Field> e : fieldMap.entrySet()) {
            Field field = e.getValue();
            // 判断是否为private属性
            if (field.getModifiers() == 2) {
                field.setAccessible(true);
                try {
                    updateIndexField(modelBean, field, fileMap);
                    Object value = field.get(modelBean);
                    if (isAll || value != null) {
                        Class<?> type = field.getType();
                        // 只添加基本类型
                        if (isSupportedType(type)) {
                            objMap.put(e.getKey(), value);
                        } else {
                            log.debug("sql类型过滤，对象类型:" + modelBean.getClass() + ",属性名称:" + e.getKey() + ",属性类型:" + type);
                        }
                    }
                } catch (Exception e1) {
                    log.error("", e1);
                }
            }
        }
        return objMap;
    }

    // 更新图片、附件索引
    protected abstract void updateIndexField(T modelBean, Field field, Map<String, Field> fileMap) throws IllegalArgumentException,
            IllegalAccessException;

    // 与原来数据对比更新图片、附件索引
    protected abstract void updateIndexFieldContrast(T modelBean, T OldmodelBean, Map<String, Field> fieldMap);

    private SqlModel getUpdateSqlModel(Map<String, Object> objMap, String... ids) {
        SqlModel sqlModel = new SqlModel();
        String setSql = "";
        for (Entry<String, Object> e : objMap.entrySet()) {
            Object value = e.getValue();
            setSql += "," + e.getKey() + "=?";
            sqlModel.addParam(value);
        }
        if (ids.length > 0) {
            setSql += " where ";
            for (int i = 0; i < ids.length; i++) {
                if (i != 0) {
                    setSql += " and ";
                }
                setSql += ids[i] + "=?";
                sqlModel.addParam(objMap.get(ids[i]));
            }
        } else {
            setSql += " where id=?";
            sqlModel.addParam(objMap.get("id"));
        }
        sqlModel.setSql("update " + tableName + " set " + setSql.substring(1));
        return sqlModel;
    }

    private SqlModel getPkSqlModel(Map<String, Object> objMap, Map<String, Field> fieldMap, String... ids) {
        SqlModel sqlModel = new SqlModel();
        String setSql = "";
        if (ids.length > 0) {
            setSql += " WHERE ";
            for (int i = 0; i < ids.length; i++) {
                if (i != 0) {
                    setSql += " and ";
                }
                setSql += ids[i] + "=?";
                sqlModel.addParam(objMap.get(ids[i]));
            }
        } else {
            setSql += " WHERE id=?";
            sqlModel.addParam(objMap.get("id"));
        }
        sqlModel.setSql("SELECT " + StringUtils.join(fieldMap.keySet(), ",") + " FROM " + tableName + setSql);
        return sqlModel;
    }

    public List<Map<String, Object>> getMapList(QueryModel queryModel) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();
        return getMapList(sql, queryModel.getParams());
    }

    public List<Long> getLongList(QueryModel queryModel) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getNoOrderQueryStr();
        return getLongList(sql, queryModel.getParams());
    }

    public List<T> getList(QueryModel queryModel) {
        String sql = "select " + queryModel.getSelectFields() + " from " + tableName + " " + queryModel.getOrderQueryStr();
        return getList(sql, queryModel.getParams());
    }

    public final long getMaxId() {
        String sql = " select Max(id) from " + tableName;
        return getLong(sql);
    }

    public final long getCount() {
        String sql = " select count(*) from " + tableName;
        return getLong(sql);
    }
}
