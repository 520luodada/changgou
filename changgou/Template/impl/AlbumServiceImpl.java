package com.changgou.impl;

import com.changgou.AlbumDao;
import com.changgou.goods.pojo.Album;
import com.changgou.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {
@Autowired(required = false)
private AlbumDao ablumDao;
    @Override
    public PageInfo<Album> pageinfo(Album album, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        // 构建查询对象
        Example example = createExample(album);

     //   List<Album> albumList = ablumDao.select(album);

        List<Album> albumList = ablumDao.selectByExample(example);


        return new PageInfo<>(albumList);
    }

    @Override
    public PageInfo<Album>listpage(Integer page, Integer size) {
        PageHelper.startPage(page,size);


        return new PageInfo<Album>(ablumDao.selectAll());
    }

    @Override
    public List<Album> listAlbum(Album album) {
        Example example=createExample(album);
        List<Album> albumList = ablumDao.selectByExample(album);
        return albumList;
    }

    @Override
    public Album findById(Integer id) {
        return ablumDao.selectByPrimaryKey(id);
    }

    @Override
    public void addAlbum(Album album) {
        ablumDao.insert(album);
    }

    @Override
    public void deleteById(Integer id) {
        ablumDao.deleteByPrimaryKey(id);

    }

    @Override
    public void deleBytitle(String title) {
        Example example=createExampleBybject(title);
        ablumDao.deleteByExample(example);
    }

    @Override
    public void updateById(Album album) {

        ablumDao.updateByPrimaryKeySelective(album);
    }

    @Override
    public List<Album> findAll() {
        return ablumDao.selectAll();
    }


    public Example createExample(Album album){
        Example example=new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album!=null){
            if (!StringUtils.isEmpty(album.getId())){
                criteria.andEqualTo("id",album.getId());
            }if (!StringUtils.isEmpty(album.getImage())){
                criteria.andEqualTo("image",album.getImage());
            }if (!StringUtils.isEmpty(album.getImageItems())){
                criteria.andEqualTo("image_items",album.getImageItems());
            }if (!StringUtils.isEmpty(album.getTitle())){
                criteria.andLike("title","%"+album.getTitle()+"%");
            }

        }
        return example;

    }

    public Example createExampleBybject(Object title){
        Example example=new Example(Object.class);
        Example.Criteria criteria = example.createCriteria();
        if (title!=null){
            if (!StringUtils.isEmpty(title)){
                criteria.andEqualTo("title",title);
            }
        }
        return example;

    }
}
