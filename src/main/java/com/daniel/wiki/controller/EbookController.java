package com.daniel.wiki.controller;

import com.daniel.wiki.req.EbookQueryReq;
import com.daniel.wiki.req.EbookSaveReq;
import com.daniel.wiki.resp.CommonResp;
import com.daniel.wiki.resp.EbookQueryResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    //将repuest封装成 EbookReq类
    public CommonResp list(@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    //将repuest封装成 EbookReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    //根据id删除,需要传入id
    @DeleteMapping("/delete/{id}")
    //将repuest封装成 EbookReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
