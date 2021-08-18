package com.daniel.wiki.req;

/*
request类，参数可以为id, name, pageNumber(在父类里), pageSize(在父类里)
 */

public class EbookQueryReq extends PageReq{

    private Long id;

    private String name; //Spring自动映射，只要名字和http://localhost:8082/ebook/list?name=Spring这里的参数名字一样

    private Long categoryId2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    @Override
    public String toString() {
        return "EbookQueryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId2='" + categoryId2 + '\'' +
                "} " + super.toString();
    }
}