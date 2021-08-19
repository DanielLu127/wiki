package com.daniel.wiki.service;

import com.daniel.wiki.domain.Doc;
import com.daniel.wiki.domain.DocExample;
import com.daniel.wiki.mapper.DocMapper;
import com.daniel.wiki.req.DocQueryReq;
import com.daniel.wiki.req.DocSaveReq;
import com.daniel.wiki.resp.DocQueryResp;
import com.daniel.wiki.resp.PageResp;
import com.daniel.wiki.util.CopyUtil;
import com.daniel.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public List<DocQueryResp> all() {

        //设置criteria，帮助查询
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        //通过docMapper Interface操作数据库，取出数据, 类型为一个list
        List<Doc> docList = docMapper.selectByExample(docExample);

        //将转Doc类型转为DocResp类型
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {

        //设置criteria，帮助查询
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        //设置分页查询,用doclist得到pageinfo
        //这句话只对下面代码遇到的第一个sql查询有效
        //表示查询第几页,每页多少行
        PageHelper.startPage(req.getPage(), req.getSize());
        //通过docMapper Interface操作数据库，取出数据, 类型为一个list
        List<Doc> docList = docMapper.selectByExample(docExample);
        //得到list后进行分页
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);

        //写日志用{}，不用+
        LOG.info("Total rows: {}", pageInfo.getTotal());
        LOG.info("Total pages:{}", pageInfo.getPages());


        //将转Doc类型转为DocResp类型
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        //创建返回值，类型为pageResp
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            Long id = snowFlake.nextId();
            LOG.info("This is" + id.toString());
            doc.setId(id);
            docMapper.insert(doc);
        }
        else {
            //更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }
}
