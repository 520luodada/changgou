package com.changgou.service;

import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/****
 * @Author:shenkunlin
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService {


// 下架  商品
    void pull(long spuId);
// 商品上架版本一
void auditStatusByspuId(long spuId);
    // 商品上架
    void auditStatus(long spuId,String status);




    /***
     * // 以及  select *from tb_spu from categray1_id
     * Spu多条件分页查询
     * @param
     * @param
     * @param
     * @return
     */



    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();

    // 批量上架
    void putmany(long[] ids);
    // 批量下架
    void pullmany(long[] ids);


    //删除商品 逻辑删除  把isdelete设置为1
void logicdelete(long spuId);
    //删除商品 应用删除  调用deletef方法
    void delete(long spuId);

    void restore(long spuId);

}
