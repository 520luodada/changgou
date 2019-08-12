package com.changgou.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {
    // 查询所有
    List<Brand> findAll();

    // 根据id 查询一个
    Brand findById(Integer id);
    // 更新
    void update(Brand brand,Integer id);
    //增加
    void addBrand(Brand brand);
    // 删除
    void deleteByid(Integer id);

    // 分页查询
    PageInfo<Brand> serach(Integer page,Integer size);
}
