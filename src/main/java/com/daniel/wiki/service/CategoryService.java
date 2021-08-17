package com.daniel.wiki.service;

import com.daniel.wiki.domain.Category;
import com.daniel.wiki.domain.CategoryExample;
import com.daniel.wiki.mapper.CategoryMapper;
import com.daniel.wiki.req.CategoryQueryReq;
import com.daniel.wiki.req.CategorySaveReq;
import com.daniel.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {

        //设置criteria，帮助查询
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        //设置分页查询,用categorylist得到pageinfo
        //这句话只对下面代码遇到的第一个sql查询有效
        //表示查询第几页,每页多少行
        PageHelper.startPage(req.getPage(), req.getSize());
        //通过categoryMapper Interface操作数据库，取出数据, 类型为一个list
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //得到list后进行分页
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);

        //写日志用{}，不用+
        LOG.info("Total rows: {}", pageInfo.getTotal());
        LOG.info("Total pages:{}", pageInfo.getPages());


        //将转Category类型转为CategoryResp类型
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        //创建返回值，类型为pageResp
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            Long id = snowFlake.nextId();
            LOG.info("This is" + id.toString());
            category.setId(id);
            categoryMapper.insert(category);
        }
        else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
