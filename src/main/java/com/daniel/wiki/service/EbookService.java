package com.daniel.wiki.service;

import com.daniel.wiki.domain.Ebook;
import com.daniel.wiki.domain.EbookExample;
import com.daniel.wiki.mapper.EbookMapper;
import com.daniel.wiki.req.EbookReq;
import com.daniel.wiki.resp.EbookResp;
import com.daniel.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        //设置criteria
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        //通过ebookMapper Interface操作数据库，取出数据
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //返回 EbookResp类型
        List<EbookResp> respList = new ArrayList<>();
        //将转Ebook类型转为EbookResp类型
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        //或者使用下面代码
       /* for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            //ebookResp.setId(ebook.getId());
            BeanUtils.copyProperties(ebook, ebookResp);
            respList.add(ebookResp);
        }*/


        return list;
    }
}
