package com.changgou;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @GetMapping("/findAll")
    public Result<List<Album>> findAll(){
        List<Album> all = albumService.findAll();

        return new Result<>(true, StatusCode.ACCESSERROR,"查询成功",all);
    }

    @GetMapping("/findById/{id}")
    public Result<Album> findById(@PathVariable(value = "id") Integer id){
        Album byId = albumService.findById(id);
        return new Result<>(true, StatusCode.ACCESSERROR,"查询成功",byId);
    }
    @PutMapping("/addAlbum")
    public Result addAlbum(@RequestBody Album album){
        albumService.addAlbum(album);

        return new Result<>(true, StatusCode.ACCESSERROR,"新增成功");
    }
   @PutMapping("/updata/{id}")
    public Result update(@RequestBody Album album,@PathVariable(value = "id") long id){
     album.setId(id);
       albumService.updateById(album);

        return new Result<>(true, StatusCode.ACCESSERROR,"更新成功");
    }
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable(value = "id")Integer id
    ){
        albumService.deleteById(id);

        return new Result<>(true, StatusCode.ACCESSERROR,"删除成功");
    }
    //分页查询
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Album>> search(@PathVariable(value = "page")Integer page,@PathVariable(value = "size")Integer size
    ){
        PageInfo<Album> listpage = albumService.listpage(page, size);

        return new Result<PageInfo<Album>>(true, StatusCode.ACCESSERROR,"分页查询成功",listpage);
    }
    @PostMapping("/PageInfo/{page}/{size}")
    public Result<PageInfo<Album>> PageInfo(@RequestBody(required = false) Album album,@PathVariable(value = "page")Integer page,@PathVariable(value = "size")Integer size
    ){
        PageInfo<Album> pageinfo = albumService.pageinfo(album, page, size);

        return new Result<PageInfo<Album>>(true, StatusCode.ACCESSERROR,"分页查询成功",pageinfo);
    }


}
