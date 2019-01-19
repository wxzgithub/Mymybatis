package com.wxz.learn.sqlSession;

import com.wxz.learn.executor.Executor;
import com.wxz.learn.executor.MyExecutor;

import java.lang.reflect.Proxy;

public class MySqlsession {

    private static Executor excutor = new MyExecutor();

    private static MyConfiguration myConfiguration = new MyConfiguration();
    public <T> T selectOne(String statement, Object object){
        return excutor.query(statement, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clas){
        return  (T) Proxy.newProxyInstance(clas.getClassLoader(), new Class[]{clas},
                new MyMapperProxy(this, myConfiguration));
    }
}
