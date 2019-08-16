package com.changgou;

import com.changgou.goods.pojo.Album;
import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AlbumService {
    // 多条件分夜查询
    PageInfo<Album> pageinfo(Album album,Integer page,Integer size);
    // 无条件分夜查询
    PageInfo<Album> listpage(Integer page,Integer size);
List<Album> listAlbum(Album album);
    Album findById(Integer id);
    void  addAlbum(Album album);
    void deleteById(Integer id);
    // 通过相册名删除相册
    void  deleBytitle(String title);
    void updateById(Album album);
    List<Album> findAll();



}
