package com.wxz.learn.sqlSession;

import com.wxz.learn.config.Function;
import com.wxz.learn.config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MyMapperProxy implements InvocationHandler {
    private MyConfiguration myConfiguration;
    private MySqlsession mySqlsession;

    public MyMapperProxy(MySqlsession mySqlsession, MyConfiguration myConfiguration){
        this.myConfiguration = myConfiguration;
        this.mySqlsession = mySqlsession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean = myConfiguration.readMapper("UserMapper.xml");
        if(!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return null;
        }
        List<Function> list = mapperBean.getList();
        if (null != list && 0 !=  list.size()){
            for (Function function : list){
                if(method.getName().equals(function.getFuncName())){
                    return mySqlsession.selectOne(function.getSql(),String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}
