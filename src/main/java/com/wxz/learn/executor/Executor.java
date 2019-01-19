package com.wxz.learn.executor;

public interface Executor {
    <T> T query(String statement, Object parameter);
}
