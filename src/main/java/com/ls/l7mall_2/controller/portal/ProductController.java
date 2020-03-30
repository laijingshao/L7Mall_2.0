package com.ls.l7mall_2.controller.portal;

import com.github.pagehelper.PageInfo;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.ProductService;
import com.ls.l7mall_2.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laijs
 * @date 2020-3-29-12:22
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseEntity<ProductDetailVo> getDetail(Integer productId){
        return productService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value ="categoryId",required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.searchProductListByKeyWordAndCategoryId(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
