package com.ls.l7mall_2.mapper;

import com.ls.l7mall_2.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    int insert(Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    int insertSelective(Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_product
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Product record);
    public List<Product> selectAll();
    public List<Product> selectByIdAndName(@Param("productName") String productName, @Param("productId") Integer productId);
    public List<Product> selectByNameAndCategoryIds(@Param("productName") String productName, @Param("categoryIdList")List<Integer> categoryIdList);
}