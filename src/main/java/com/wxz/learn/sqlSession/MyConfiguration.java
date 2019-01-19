package com.wxz.learn.sqlSession;

import com.wxz.learn.config.Function;
import com.wxz.learn.config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyConfiguration {
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    public Connection build(String resource) {
        try {
            InputStream stream = loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return getDbConnection(root);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("get config xml failed.");
        }
    }

    public Connection getDbConnection(Element node) {
        if (node.getName().equals("database")) {
            String driverClassName = null;
            String url = null;
            String username = null;
            String password = null;
            for (Object item : node.elements()) {
                Element i = (Element) item;
                String value = getValue(i);
                String name = i.attributeValue("name");
                if (null == name || null == value) {
                    throw new RuntimeException("database connect info exception.");
                }
                switch (name) {
                    case "url":
                        url = value;
                        break;
                    case "username":
                        username = value;
                        break;
                    case "password":
                        password = value;
                        break;
                    case "driverClassName":
                        driverClassName = value;
                        break;
                    default:
                        throw new RuntimeException("获取数据库配置失败");
                }
            }
            Connection connection = null;
            try {
                Class.forName(driverClassName);
                //适配高版本mysql数据库
                url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=false";
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        } else {
            throw new RuntimeException("database config exception.");
        }
    }

    private String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    public MapperBean readMapper(String path) {
        MapperBean mapperBean = new MapperBean();
        InputStream stream = loader.getResourceAsStream(path);
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(stream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        mapperBean.setInterfaceName(root.attributeValue("nameSpace").trim());
        List<Function> list = new ArrayList<Function>();
        for (Iterator rootIter = root.elementIterator(); rootIter.hasNext(); ) {
            Function fun = new Function();
            Element e = (Element) rootIter.next();
            String sqlType = e.getName().trim();
            String funName = e.attributeValue("id").trim();
            String sql = e.getText().trim();
            String resultType = e.attributeValue("resultType");
            fun.setFuncName(funName);
            fun.setSql(sql);
            fun.setSqlType(sqlType);
            Object newInstance = null;
            try {
                newInstance = Class.forName(resultType).newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
            }
            fun.setResultType(newInstance);
            list.add(fun);
            mapperBean.setList(list);
        }
        return mapperBean;
    }
}
