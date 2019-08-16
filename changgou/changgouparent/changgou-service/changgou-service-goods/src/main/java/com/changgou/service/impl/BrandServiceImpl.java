package com.changgou.service.impl;


import com.changgou.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * @Author:shenkunlin
 * @Description:Brand业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired(required = false)
    private BrandMapper brandMapper;
    @Autowired(required = false)
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired(required = false)
    private TemplateMapper templateMapper;
    @Autowired(required = false)
    private SpecMapper specMapper;
    @Autowired(required = false)
    private CategoryMapper categoryMapper;
    @Autowired(required = false)
    private ParaMapper paraMapper;



    @Override
    public Map<String, Object> all(Integer categoryId) {
        Map<String, Object> map=new HashMap<>();
        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        //  获取商品品牌
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(category.getId());
        List<CategoryBrand> listcategoryBrand = categoryBrandMapper.select(categoryBrand);
            List<Brand> brandList=new ArrayList<>();
        for (CategoryBrand brand : listcategoryBrand) {
            Brand brand1 = brandMapper.selectByPrimaryKey(brand.getBrandId());
            brandList.add(brand1);
        }
        map.put("brandList",brandList);

        // 获取该类商品下的模板

        Template template = templateMapper.selectByPrimaryKey(category.getTemplateId());
        map.put("template",template);

        // 加载该商品的规格信息
        Spec spec = new Spec();
        spec.setTemplateId(template.getId());
        List<Spec> specList = specMapper.select(spec);

        map.put("specList",specList);

        Para para = new Para();
        para.setTemplateId(template.getId());
        List<Para> paraList = paraMapper.select(para);
        map.put("paraList",paraList);


        return map;
    }

    @Override
    public List<Brand> listBrand(Integer categoryId) {
        CategoryBrand categoryBrand=new CategoryBrand();
        categoryBrand.setCategoryId(categoryId);
        List<CategoryBrand> select = categoryBrandMapper.select(categoryBrand);
        List<Brand> list=new ArrayList<>();
        for (CategoryBrand categoryBrandbrand : select) {
      list.add(brandMapper.selectByPrimaryKey(categoryBrandbrand.getBrandId()));
        }
        return list;
    }

    /**
     * Brand条件+分页查询
     * @param brand 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Brand> findPage(Brand brand, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(brand);
        //执行搜索
        return new PageInfo<Brand>(brandMapper.selectByExample(example));
    }

    /**
     * Brand分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Brand>(brandMapper.selectAll());
    }

    /**
     * Brand条件查询
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand){
        //构建查询条件
        Example example = createExample(brand);
        //根据构建的条件查询数据
        return brandMapper.selectByExample(example);
    }


    /**
     * Brand构建查询对象
     * @param brand
     * @return
     */
    public Example createExample(Brand brand){
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(brand!=null){
            // 品牌id
            if(!StringUtils.isEmpty(brand.getId())){
                    criteria.andEqualTo("id",brand.getId());
            }
            // 品牌名称
            if(!StringUtils.isEmpty(brand.getName())){
                    criteria.andLike("name","%"+brand.getName()+"%");
            }
            // 品牌图片地址
            if(!StringUtils.isEmpty(brand.getImage())){
                    criteria.andEqualTo("image",brand.getImage());
            }
            // 品牌的首字母
            if(!StringUtils.isEmpty(brand.getLetter())){
                    criteria.andEqualTo("letter",brand.getLetter());
            }
            // 排序
            if(!StringUtils.isEmpty(brand.getSeq())){
                    criteria.andEqualTo("seq",brand.getSeq());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Brand
     * @param brand
     */
    @Override
    public void update(Brand brand){
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 增加Brand
     * @param brand
     */
    @Override
    public void add(Brand brand){
        brandMapper.insert(brand);
    }

    /**
     * 根据ID查询Brand
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id){
        return  brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Brand全部数据
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }
}
