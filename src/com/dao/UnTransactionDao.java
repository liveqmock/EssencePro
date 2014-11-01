package com.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class UnTransactionDao<T> extends BaseDao<T> {

    private DataSource DATA_SOURCE;

    public UnTransactionDao(DataSource dataSource, String tableName) {
        super(tableName);
        DATA_SOURCE = dataSource;
    }

    protected Connection getCon() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    protected void closeCon(Connection con) throws SQLException {
        con.close();
    }

    protected int update(String sql, Object... params) {
        try {
            Connection connection = getCon();
            try {
                return QRY_RUN.update(connection, sql, params);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("修改出错,sql:" + sql, e);
            return 0;
        }
    }

    private String getSubString(String str){
        if (str.length()>120){
           return str.substring(0,120);
        }
        return str;
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
            try {
                int[] counts = QRY_RUN.batch(connection, sql, params);
                return getTotal(counts);
            } finally {
                if (connection.getAutoCommit()) {
                    closeCon(connection);
                }
            }
        } catch (SQLException e) {
            log.error("批量修改出错,sql:" + sql, e);
            return 0;
        }
    }

}
