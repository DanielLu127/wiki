package com.daniel.wiki.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class PageReq {
    @NotNull (message = "page number cannot be null")
    private int page;

    @NotNull(message = "size number cannot be null")
    @Max(value = 100, message = "max rows cannot exceed 100")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}