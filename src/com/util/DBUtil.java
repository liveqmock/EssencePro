package com.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DBUtil {
    /**
     * 用PreparedStatement给DB的参数赋值
     * 
     * @author : 玄承勇
     * @datetime: 2014-5-2 上午10:35:16
     * @param:
     * @param:
     * @return ： void
     */
    public static void setVal(int idx, int type, ResultSet rs, PreparedStatement pstmt) throws SQLException {
        switch (type) {
        case Types.TINYINT:
        case Types.SMALLINT:
            pstmt.setShort(idx, rs.getShort(idx));
            break;
        case Types.INTEGER:
            pstmt.setInt(idx, rs.getInt(idx));
            break;
        case Types.BIGINT:
            pstmt.setLong(idx, rs.getLong(idx));
            break;
        case Types.BOOLEAN:
            pstmt.setBoolean(idx, rs.getBoolean(idx));
            break;
        case Types.CHAR:
        case Types.VARCHAR:
            pstmt.setString(idx, rs.getString(idx));
            break;
        case Types.DATE:
            pstmt.setDate(idx, rs.getDate(idx));
            break;
        case Types.DECIMAL:
            pstmt.setBigDecimal(idx, rs.getBigDecimal(idx));
            break;
        case Types.DOUBLE:
            pstmt.setDouble(idx, rs.getDouble(idx));
            break;
        case Types.FLOAT:
            pstmt.setFloat(idx, rs.getFloat(idx));
            break;
        case Types.NUMERIC:
            pstmt.setFloat(idx, rs.getFloat(idx));
            break;
        case Types.REAL:
            pstmt.setFloat(idx, rs.getFloat(idx));
            break;
        case Types.TIME:
            pstmt.setTime(idx, rs.getTime(idx));
            break;
        case Types.TIMESTAMP:
            pstmt.setTimestamp(idx, rs.getTimestamp(idx));
            break;
        default:
            pstmt.setString(idx, rs.getString(idx));
        }
    }

}
