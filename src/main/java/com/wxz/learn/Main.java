package com.wxz.learn;

import com.wxz.learn.dao.EmpMapper;
import com.wxz.learn.bean.Employee;
import com.wxz.learn.sqlSession.MySqlsession;

public class Main {

    public static void main(String[] args) {
        MySqlsession mySqlsession = new MySqlsession();
        EmpMapper empMapper = mySqlsession.getMapper(EmpMapper.class);
        Employee employee = (Employee) empMapper.getStuById(1);
        System.out.println(employee.toString());
    }
}
