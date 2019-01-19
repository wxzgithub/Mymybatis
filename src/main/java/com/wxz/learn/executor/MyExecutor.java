package com.wxz.learn.executor;

import com.wxz.learn.bean.Employee;
import com.wxz.learn.sqlSession.MyConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyExecutor implements Executor {

    private static MyConfiguration xmlConfig = new MyConfiguration();

    private Connection getConnnection() {
        Connection con = xmlConfig.build("config.xml");
        return con;
    }

    @Override
    public <T> T query(String statement, Object parameter) {
        Connection connection = getConnnection();
        ResultSet resultSet = null;
        PreparedStatement pre = null;
        try {
            pre = connection.prepareStatement(statement);
            pre.setString(1, parameter.toString());
            resultSet = pre.executeQuery();
            Employee employee = new Employee();
            while (resultSet.next()) {
                employee.setId(resultSet.getString(1));
                employee.setName(resultSet.getString(2));
                employee.setEmail(resultSet.getString(3));
            }
            return (T) employee;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResurce(connection, resultSet, pre);
        }
        return null;
    }

    private void closeResurce(Connection connection, ResultSet resultSet, PreparedStatement pre) {
        try {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != pre) {
                pre.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
