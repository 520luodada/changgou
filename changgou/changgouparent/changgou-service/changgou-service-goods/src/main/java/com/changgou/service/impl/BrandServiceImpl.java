package com.changgou.service.impl;

import com.changgou.dao.BrandMpper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired(required = false)
    private BrandMpper brandMpper;
    @Override
    public List<Brand> findAll() {
        return brandMpper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMpper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand,Integer id) {
        if (brand!=null){
            brandMpper.updateByPrimaryKeySelective(brand);
        }
    }

    @Override
    public void addBrand(Brand brand) {
        brandMpper.insertSelective(brand);
    }

    @Override
    public void deleteByid(Integer id) {
        brandMpper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Brand> serach(Integer page,Integer size) {
        PageHelper.startPage(page, size);
        List<Brand> brands = brandMpper.selectAll();
        return new PageInfo<>(brands);
    }
}
