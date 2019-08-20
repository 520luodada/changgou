package com.changgou.service;

import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:Sku业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SkuService {

    // 修改库存

    void updateSkuWeightByIdForSku(Sku sku);

    void updateSkuWeightById(Integer weight,long SkuId);

    //  显示某spu 的商品 列表


    List<Sku> listSku(long spuID);
    /***
     * Sku多条件分页查询
     * @param sku
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(Sku sku, int page, int size);

    /***
     * Sku分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(int page, int size);

    /***
     * Sku多条件搜索方法
     * @param sku
     * @return
     */
    List<Sku> findList(Sku sku);

    /***
     * 删除Sku
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Sku数据
     * @param sku
     */
    void update(Sku sku);

    /***
     * 新增Sku
     * @param sku
     */
    void add(Sku sku);

    /**
     * 根据ID查询Sku
     * @param id
     * @return
     */
     Sku findById(Long id);

    /***
     * 查询所有Sku
     * @return
     */
    List<Sku> findAll();


    List<Sku> skuList(String status,Integer start,Integer end);
}
