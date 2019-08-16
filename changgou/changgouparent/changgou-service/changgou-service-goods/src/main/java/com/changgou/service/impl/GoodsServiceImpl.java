package com.changgou.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.changgou.dao.*;
import com.changgou.entity.IdWorker;
import com.changgou.goods.pojo.*;
import com.changgou.service.AlbumService;
import com.changgou.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

/****
 * @Author:shenkunlin
 * @Description:Album业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired(required = false)
    private SpuMapper spuMapper;
    @Autowired(required = false)
    private SkuMapper skuMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;
    @Autowired(required = false)
    private BrandMapper brandMapper;
    @Override
    public void add(Goods goods) {
        Spu spu = goods.getSpu();
        spu.setId(idWorker.nextId());
spuMapper.insert(spu);

        List<Sku> skuList = goods.getSkuList();
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());


        // 创建商品日期
        Date date = new Date();
        for (Sku sku : skuList) {
            if (StringUtils.isEmpty(sku.getSpec())){
                        sku.setSpec("{}");
            }
       sku.setSpuId(spu.getId());
            sku.setId(idWorker.nextId());

        //  sku 的name 属性 是拼接起来的

        String name=spu.getName();
            Map<String,String> map = JSON.parseObject(sku.getSpec(),Map.class);
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                name+=" "+stringStringEntry.getKey();
            }
            sku.setName(name);
            sku.setId(idWorker.nextId());
        sku.setCreateTime(date);
        sku.setUpdateTime(date);
        sku.setCategoryId(spu.getCategory3Id());
        sku.setCategoryName(category.getName());
        sku.setBrandName(brand.getName());
        skuMapper.insertSelective(sku);
        }
    }
}
