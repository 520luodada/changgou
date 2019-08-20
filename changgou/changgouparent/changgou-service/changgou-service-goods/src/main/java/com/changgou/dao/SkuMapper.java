package com.changgou.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
@Select(value = "SELECT * from tb_sku WHERE spu_id=#{spuID}")
    List<Sku> listSku(long spuID);


@Select(value = "UPDATE tb_sku SET weight=#{weight} WHERE id=#{id}")
  void   updateSkuWeightById(Integer weight,long id);


    @Select("select * from tb_sku where status=#{status} limit #{start},#{end}")
    List<Sku> selectByPage(String status,Integer start,Integer end);
}
