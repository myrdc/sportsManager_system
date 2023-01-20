package com.gdut.boot.handler.reflect.property;

import java.util.Iterator;

/**
 * @author JLHWASX
 * @Description 分词器
 * @verdion
 * @date 2022/3/2712:14
 */

public class WordTokenizer implements Iterator<WordTokenizer> {

    /**
     * 如果是users[0].user.name
     * 1. name: users
     * 2. indexedName: users[0]
     * 3. index: 0
     * 4. children: user.name
     */
    private String name;
    private final String indexedName;
    private String index;
    private final String children;

    /**
     * 构造器, 对这几个参数进行初始化
     *
     * @param fullname users[0].user.name类型的数据
     */
    public WordTokenizer(String fullname) {
        //.的下标
        int indexPoint = fullname.indexOf(".");
        if (indexPoint > -1) {
            //首先是users[0]
            name = fullname.substring(0, indexPoint);
            //children = user.name
            children = fullname.substring(indexPoint + 1);
        } else {
            //没有., 证明只有一个属性
            name = fullname;
            children = null;
        }
        //下面再来求indexedName和index, indexedName = users[0]
        indexedName = name;
        indexPoint = fullname.indexOf("[");
        if (indexPoint > -1) {
            //证明有数组
            index = name.substring(indexPoint + 1, name.length() - 1);
        } else {
            name = name.substring(0, indexPoint);
        }
    }

    public String getName() {
        return name;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getIndex() {
        return index;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public WordTokenizer next() {
        return new WordTokenizer(children);
    }
}
