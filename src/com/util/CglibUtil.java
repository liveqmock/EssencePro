package com.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.log4j.Logger;

import com.annotation.Annotation.Transaction;
import com.dao.DataSourceFactory;

public class CglibUtil implements MethodInterceptor {

    // 此处的用法为注解(推荐使用)，与TransactionDao 中的 startTranscation等不冲突，也可以单独使用，但不推荐使用
    private static ThreadLocal<byte[]> transactionThreadLocal = new ThreadLocal<byte[]>();
    private static CglibUtil cglibUtils = new CglibUtil();
    protected final static Logger log = LoggerUtil.getLogger();

    public static Object getInstance(Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());

        // 回调方法
        enhancer.setCallback(cglibUtils);
        // 创建代理对象
        return enhancer.create();
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Exception {
        Transaction transcation = method.getAnnotation(Transaction.class);
        boolean isTransaction = transcation != null;// 是否需要开始事务
        boolean isInTransaction = transactionThreadLocal.get() != null;// 是否已经处在事务中
        if (isTransaction && !isInTransaction) {
            String dataKey = transcation.value();
            DataSource dataSource = DataSourceFactory.getDataSource(dataKey);
            ThreadLocal<Connection> connectionThreadLocal = DataSourceFactory.getThreadLocalConnection(dataKey);
            Connection connection = getConnection(connectionThreadLocal, dataSource);
            try {
                transactionThreadLocal.set(new byte[0]);
                connection.setAutoCommit(false);// 事务开始
                Object result = proxy.invokeSuper(obj, args);
                commitTransaction(connection);// 提交事务
                return result;
            } catch (Exception e) {
                rollbackTransaction(connection, connectionThreadLocal);
                throw e;
            } catch (Throwable e) {
                rollbackTransaction(connection, connectionThreadLocal);
                throw new Exception(e);
            } finally {
                transactionThreadLocal.remove();// 事务结束
            }
        } else {
            try { // 执行方法
                return proxy.invokeSuper(obj, args);
            } catch (Throwable e) {
                throw new Exception(e);
            }
        }
    }

    // 获取连接
    private Connection getConnection(ThreadLocal<Connection> connectionThreadLocal, DataSource dataSource) throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if (connection != null && !connection.isClosed()) {
            return connection;
        } else {
            connection = dataSource.getConnection();
            connectionThreadLocal.set(connection);
            return connection;
        }
    }

    // 提交事务
    private void commitTransaction(Connection con) throws SQLException {
        if (con != null) {
            con.commit();
            con.setAutoCommit(true);
        } else {
            log.error("事务提交失败，连接为空！");
        }
    }

    // 回滚事务
    private void rollbackTransaction(Connection con, ThreadLocal<Connection> connectionThreadLocal) throws SQLException {
        if (con != null) {
            log.info("回滚事务");
            con.rollback();// 事务回滚
            con.setAutoCommit(true);
            con.close();
            connectionThreadLocal.remove();
        } else {
            log.error("事务回滚失败，连接为空！");
        }
    }
}