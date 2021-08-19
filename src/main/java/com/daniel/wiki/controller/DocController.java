package com.daniel.wiki.controller;

import com.daniel.wiki.req.DocQueryReq;
import com.daniel.wiki.req.DocSaveReq;
import com.daniel.wiki.resp.DocQueryResp;
import com.daniel.wiki.resp.CommonResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;


    @GetMapping("/all")
    //将repuest封装成 DocReq类
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    //将repuest封装成 DocReq类
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    //将repuest封装成 DocReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    //根据id删除,需要传入id
    @DeleteMapping("/delete/{id}")
    //将repuest封装成 DocReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        docService.delete(id);
        return resp;
    }
}
