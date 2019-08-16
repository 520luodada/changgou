package com.changgou;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/findAll")
    public Result<List<Brand>> findAll(){
        List<Brand> all = brandService.findAll();
        return new Result<>(true, StatusCode.ACCESSERROR,"查询成功",all);
    }

    @GetMapping("/findById/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id){
        Brand byId = brandService.findById(id);

        return new Result<>(true, StatusCode.ACCESSERROR,"查询成功",byId);
    }
    @PutMapping("/addBrand")
    public Result addBrand(@RequestBody Brand brand){
      brandService.addBrand(brand);

        return new Result<>(true, StatusCode.ACCESSERROR,"新增成功");
    }
   @PutMapping("/updata/{id}")
    public Result update(@RequestBody Brand brand,@PathVariable(value = "id") Integer id){
      brand.setId(id);
        brandService.update(brand,id);

        return new Result<>(true, StatusCode.ACCESSERROR,"更新成功");
    }
    @DeleteMapping("/deleteById/{id}")
    public Result uodate(@PathVariable(value = "id")Integer id
    ){
        brandService.deleteByid(id);

        return new Result<>(true, StatusCode.ACCESSERROR,"删除成功");
    }
    //分页查询
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> search(@PathVariable(value = "page")Integer page,@PathVariable(value = "size")Integer size
    ){
        PageInfo<Brand> serach = brandService.serach(page, size);

        return new Result<PageInfo<Brand>>(true, StatusCode.ACCESSERROR,"分页查询成功",serach);
    }

}
