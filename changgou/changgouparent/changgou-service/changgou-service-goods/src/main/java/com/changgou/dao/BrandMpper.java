package com.changgou.dao;

import com.changgou.goods.pojo.Brand;
import tk.mybatis.mapper.common.Mapper;
//mapper 基于mybatis自带的一个借口  包含了 增删改查。。
public interface BrandMpper extends Mapper<Brand> {

}
