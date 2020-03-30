package com.ls.l7mall_2.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ls.l7mall_2.entity.Category;
import com.ls.l7mall_2.entity.Product;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseCode;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.mapper.CategoryMapper;
import com.ls.l7mall_2.mapper.ProductMapper;
import com.ls.l7mall_2.service.CategoryService;
import com.ls.l7mall_2.service.ProductService;
import com.ls.l7mall_2.util.DateTimeUtil;
import com.ls.l7mall_2.vo.ProductDetailVo;
import com.ls.l7mall_2.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laijs
 * @date 2020-3-29-12:24
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
   
    @Autowired
    private ProductMapper  productMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private CategoryService categoryService;
   
    @Override
    public ResponseEntity saveOrUpdateProduct(Product product) {
        // 判断是否为空
        if(product == null){
            return ResponseEntity.responesWhenError("参数有误，无法保存产品信息");
        }
        // 将首张子图设置为主图
        if(StringUtils.isNotBlank(product.getSubImages())){
            String[] images = product.getSubImages().split(",");
            if(images.length > 0){
                product.setMainImage(images[0]);
            }
        }
        // 新增OR更新产品
        if(product.getId() == null){
            // 新增
            int resultCount = productMapper.insert(product);
            if(resultCount > 0){
                return ResponseEntity.responesWhenSuccess("新增产品信息成功");
            }
            return ResponseEntity.responesWhenError("新增产品信息失败");
        } else {
            // 更新
            int rowCount = productMapper.updateByPrimaryKey(product);
            if(rowCount > 0){
                return ResponseEntity.responesWhenSuccess("更新产品信息成功");
            }
            return ResponseEntity.responesWhenError("更新产品信息失败");
        }
    }

    @Override
    public ResponseEntity<String> setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null){
            return ResponseEntity.responesWhenError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
        Product product = new Product(productId,status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ResponseEntity.responesWhenSuccess("更新产品状态成功");
        }
        return ResponseEntity.responesWhenError("更新产品状态失败");
    }

    @Override
    public ResponseEntity<ProductDetailVo> manageProductDetail(Integer productId) {
        if(productId == null){
            return ResponseEntity.responesWhenError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
        // 根据id获取产品
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ResponseEntity.responesWhenError("产品已下架或删除");
        }
        // 当响应信息和产品信息相关，但有所区分时，通过创建value object，VO类来封装数据 ——>ProductDetailVo
        // 提供一个方法将POJO转化为VO，在此调用
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ResponseEntity.responesWhenSuccess(productDetailVo);
    }

    @Value("${ftp.server.http.prefix}")
    private String ImageHost;
    
    // Product ---> ProductDetailVo
    public ProductDetailVo assembleProductDetailVo(Product product){
        // 从POJO类中继承而来、且不需加工的属性，通过构造器赋值给VO类
        ProductDetailVo vo = new ProductDetailVo(product.getId(), product.getCategoryId(), product.getName(), product.getSubtitle(), product.getMainImage(),
                product.getSubImages(), product.getDetail(), product.getPrice(), product.getStock(), product.getStatus());

        // POJO中的createTime和updateTime属性，在VO类中是String，创建一个用于处理时间格式的工具类DateTimeUtil
        String createTime = DateTimeUtil.dateToStr(product.getCreateTime());
        String updateTime = DateTimeUtil.dateToStr(product.getUpdateTime());
        vo.setCreateTime(createTime);
        vo.setUpdateTime(updateTime);

        // 图片前缀imageHost属性需要在配置文件中配置
        vo.setImageHost(ImageHost);

        // 父类parentCategoryId属性的设置
        Integer categoryId = product.getCategoryId();
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category == null){
            vo.setParentCategoryId(0);
        } else {
            vo.setParentCategoryId(category.getParentId());
        }

        return vo;
    }

    @Override
    public ResponseEntity<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        Page<Object> pageHelper = PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectAll();
        ArrayList<ProductListVo> vos = new ArrayList<>();
        for (Product product : products) {
            ProductListVo vo = assembleProductListVo(product);
            vos.add(vo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(vos);
        return ResponseEntity.responesWhenSuccess(pageInfo);

    }

    // product  ---> ProductListVo
    public ProductListVo assembleProductListVo(Product product){
        ProductListVo vo = new ProductListVo(product.getId(), product.getCategoryId(), product.getName(), product.getSubtitle(), product.getMainImage(), product.getPrice(), product.getStatus());
        vo.setImageHost(ImageHost);
        return vo;
    }

    @Override
    public ResponseEntity<PageInfo> searchProductList(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        Page<Object> pageHelper = PageHelper.startPage(pageNum, pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectByIdAndName(productName, productId);
        ArrayList<ProductListVo> vos = new ArrayList<>();
        for (Product product : products) {
            ProductListVo vo = assembleProductListVo(product);
            vos.add(vo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(vos);
        return ResponseEntity.responesWhenSuccess(pageInfo);
    }

    @Override
    public ResponseEntity<ProductDetailVo> getProductDetail(Integer productId) {
        if(productId == null){
            return ResponseEntity.responesWhenError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
        // 根据id获取产品
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ResponseEntity.responesWhenError("产品已下架或删除");
        }
        if(product.getStatus() != Const.ProductStatus.ON_SALE.getCode()){
            return ResponseEntity.responesWhenError("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ResponseEntity.responesWhenSuccess(productDetailVo);
    }

    @Override
    public ResponseEntity<PageInfo> searchProductListByKeyWordAndCategoryId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        if(keyword.isBlank() && categoryId == null){
            return ResponseEntity.responesWhenError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            // 无该分类且关键字为空，返回空结果集
            if(category == null && keyword.isBlank()){
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> list = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(list);
                return ResponseEntity.responesWhenSuccess(pageInfo);
            }
            // 有当前分类，递归当前分类的所有子分类
            categoryIdList = categoryService.getDeepChildrenCategoryById(categoryId).getData();

        }
        if(!keyword.isBlank()){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 设置分页的排序
        if(!orderBy.isBlank()){
            String[] orderByArray = orderBy.split("_");
            PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
        }
        // 持久层操作
        List<Product> products = productMapper.selectByNameAndCategoryIds(keyword, categoryIdList);
        List<ProductListVo> vos = Lists.newArrayList();
        for (Product product : products) {
            ProductListVo vo = assembleProductListVo(product);
            vos.add(vo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(vos);
        return ResponseEntity.responesWhenSuccess(pageInfo);
    }
    
}
