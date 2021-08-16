package com.daniel.wiki.req;

/*
request类，参数可以为id, name, pageNumber(在父类里), pageSize(在父类里)
 */

public class EbookReq extends PageReq{
    private Long id;

    private String name; //Spring自动映射，只要名字和http://localhost:8082/ebook/list?name=Spring这里的参数名字一样

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append("]");
        return sb.toString();
    }
}