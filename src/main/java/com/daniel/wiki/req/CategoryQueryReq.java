package com.daniel.wiki.req;

/*
request类，参数可以为id, name, pageNumber(在父类里), pageSize(在父类里)
 */

public class CategoryQueryReq extends PageReq{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}