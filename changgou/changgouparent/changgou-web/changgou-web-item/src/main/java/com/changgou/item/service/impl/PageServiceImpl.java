package com.changgou.item.service.impl;
import	java.io.Writer;
import	java.io.PrintWriter;
import	java.io.File;
import java.util.HashMap;
import java.util.List;
import	java.util.Map;


import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
public class PageServiceImpl implements PageService {
    @Autowired(required = false)
    private TemplateEngine templateEngine;
    @Autowired
    private CategoryFeign categoryFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;
    @Value("${pagepath}")
    private String pagepath;
    @Override
    public void creatHtml(Long spuid) {

        try {
            Map<String, Object> map=getDateModel(spuid);
        Context context = new Context();
        context.setVariables(map);
        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();   //目录为空  创建

        }
        File dest = new File(dir, spuid + ".html");
        PrintWriter printWriter = new PrintWriter(dest, "utf-8");
            templateEngine.process("item",context,printWriter);
    }catch (Exception e ){
            e.printStackTrace();
        }
    }


    private Map<String, Object> getDateModel(Long id) {
Map<String, Object> map= new HashMap<>();
            // 获取商品信息
        Spu spu = spuFeign.findById(id).getData();
        map.put("spu", spu);
        // 商品库存
        Sku sku = new Sku();
        sku.setId(spu.getId());
        Result<List<Sku>> list = skuFeign.findList(sku);
        List<Sku> skuList = list.getData();
        map.put("skuList", skuList);

        // 商品分类
        Result<Category> category1 = categoryFeign.findById(spu.getCategory1Id());
        Result<Category> category2 = categoryFeign.findById(spu.getCategory2Id());
        Result<Category> category3 = categoryFeign.findById(spu.getCategory3Id());
        Category categorydata1 = category1.getData();
        Category categorydata2 = category2.getData();
        Category categorydata3 = category3.getData();
        map.put("category1", categorydata1);
        map.put("category2", categorydata2);
        map.put("category3", categorydata3);


        //商品图片
        String images = spu.getImages();
        String[] image = images.split(",");
        map.put("images",image);


        // 商品规格
        Map<String,String> specMap = JSON.parseObject(spu.getSpecItems(), Map.class);
        map.put("specificationList", specMap);
        return map;
    }
}
