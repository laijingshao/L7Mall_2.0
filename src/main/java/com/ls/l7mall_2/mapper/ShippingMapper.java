package com.ls.l7mall_2.mapper;

import com.ls.l7mall_2.entity.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    int insert(Shipping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    int insertSelective(Shipping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    Shipping selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Shipping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_shipping
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Shipping record);
    public int deleteByIdAndUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);
    public int updateByShipping(Shipping shipping);
    public Shipping selectByIdAndUserId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);
    public List<Shipping> selectByUserId(Integer userId);
    
}