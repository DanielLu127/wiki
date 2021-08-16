package com.daniel.wiki.controller;

import com.daniel.wiki.req.EbookQueryReq;
import com.daniel.wiki.req.EbookSaveReq;
import com.daniel.wiki.resp.CommonResp;
import com.daniel.wiki.resp.EbookResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.service.EbookQueryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookQueryService ebookService;

    @GetMapping("/list")
    //将repuest封装成 EbookReq类
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    //将repuest封装成 EbookReq类
    //注意要加RequestBody才能以json形式接收参数
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
