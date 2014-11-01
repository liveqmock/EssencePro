package com.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;

public class TransactionDao<T> extends BaseDao<T> {

    private ThreadLocal<Connection> CONNECTION_THREAD_LOCAL;
    private DataSource DATA_SOURCE;

    public TransactionDao(String key, String tableName) {
        super(tableName);
        DATA_SOURCE = DataSourceFactory.getDataSource(key);
        CONNECTION_THREAD_LOCAL = DataSourceFactory.getThreadLocalConnection(key);
    }

    // 开始事务
    public void starTransaction() throws SQLException {
        Connection con = getConnection();
        con.setAutoCommit(false);
    }

    // 提交事务
    public void commitTransaction() throws SQLException {
        Connection con = CONNECTION_THREAD_LOCAL.get();
        if (con != null) {
            con.commit();
            con.setAutoCommit(true);
        } else {
            log.error("事务提交失败，连接为空！");
        }
    }

    // 回滚事务
    public boolean rollbackTransaction() {
        Connection con = CONNECTION_THREAD_LOCAL.get();
        if (con != null) {
            try {
                log.info("回滚事务");
                con.rollback();
                con.setAutoCommit(true);
                closeConnection(con);
                return true;
            } catch (SQLException e) {
                log.error("事务回滚失败！", e);
            }
        } else {
            log.error("事务回滚失败，连接为空！");
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection != null && !connection.isClosed()) {
            return connection;
        } else {
            connection = DATA_SOURCE.getConnection();
            CONNECTION_THREAD_LOCAL.set(connection);
            return connection;
        }
    }

    private void closeConnection(Connection con) throws SQLException {
        con.close();
        CONNECTION_THREAD_LOCAL.remove();
    }

    protected Connection getCon() throws SQLException {
        return getConnection();
    }

    protected void closeCon(Connection con) throws SQLException {
        closeConnection(con);
    }

    protected int update(String sql, Object... params) {
        try {
            Connection connection = getCon();
            if (connection.getAutoCommit()) {
                try {
                    return QRY_RUN.update(connection, sql, params);
                } finally {
                    closeCon(connection);
                }
            } else {
                try {
                    return QRY_RUN.update(connection, sql, params);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            log.error("修改出错,sql:" + sql, e);
            return 0;
        }
    }

    private int getTotal(int[] counts) {
        if (counts == null || counts.length == 0) {
            return 0;
        }
        int total = 0;
        for (int count : counts) {
            if (count != 0) {
                total++;
            }
        }
        return total;
    }

    protected int batchUpdate(String sql, Object[][] params) {
        try {
            Connection connection = getCon();
            if (connection.getAutoCommit()) {
                try {
                    int[] counts = QRY_RUN.batch(connection, sql, params);
                    return getTotal(counts);
                } finally {
                    closeCon(connection);
                }
            } else {
                try {
                    int[] counts = QRY_RUN.batch(connection, sql, params);
                    return getTotal(counts);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (SQLException e) {
            log.error("批量更新修改出错,sql:" + sql, e);
            return 0;
        }
    }

    @Override
    protected void updateIndexField(T modelBean, Field field, Map<String, Field> fileMap) throws IllegalArgumentException,
            IllegalAccessException {

    }

    @Override
    protected void updateIndexFieldContrast(T modelBean, T OldmodelBean, Map<String, Field> fieldMap) {

    }

}
