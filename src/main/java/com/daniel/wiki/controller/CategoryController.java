package com.daniel.wiki.controller;

import com.daniel.wiki.req.CategoryQueryReq;
import com.daniel.wiki.req.CategorySaveReq;
import com.daniel.wiki.resp.CategoryQueryResp;
import com.daniel.wiki.resp.CommonResp;
import com.daniel.wiki.resp.CategoryQueryResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.service.CategoryService;
import com.daniel.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    @GetMapping("/all")
    //将repuest封装成 CategoryReq类
    public CommonResp all() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    //将repuest封装成 CategoryReq类
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    //将repuest封装成 CategoryReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    //根据id删除,需要传入id
    @DeleteMapping("/delete/{id}")
    //将repuest封装成 CategoryReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
