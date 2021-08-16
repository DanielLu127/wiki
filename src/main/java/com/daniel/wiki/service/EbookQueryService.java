package com.daniel.wiki.service;

import com.daniel.wiki.domain.Ebook;
import com.daniel.wiki.domain.EbookExample;
import com.daniel.wiki.mapper.EbookMapper;
import com.daniel.wiki.req.EbookQueryReq;
import com.daniel.wiki.req.EbookSaveReq;
import com.daniel.wiki.resp.EbookResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookQueryService.class);

    @Resource
    private EbookMapper ebookMapper;

    public PageResp<EbookResp> list(EbookQueryReq req) {

        //设置criteria，帮助查询
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        //动态sql,模糊查询
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }



        //设置分页查询,用ebooklist得到pageinfo
        //这句话只对下面代码遇到的第一个sql查询有效
        //表示查询第几页,每页多少行
        PageHelper.startPage(req.getPage(), req.getSize());
        //通过ebookMapper Interface操作数据库，取出数据, 类型为一个list
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //得到list后进行分页
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);

        //写日志用{}，不用+
        LOG.info("Total rows: {}", pageInfo.getTotal());
        LOG.info("Total pages:{}", pageInfo.getPages());


        //将转Ebook类型转为EbookResp类型
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);

        //创建返回值，类型为pageResp
        PageResp<EbookResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            ebookMapper.insert(ebook);
        }
        else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
