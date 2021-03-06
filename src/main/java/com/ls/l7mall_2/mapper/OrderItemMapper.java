package com.ls.l7mall_2.mapper;

import com.ls.l7mall_2.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    int insert(OrderItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    int insertSelective(OrderItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OrderItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order_item
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OrderItem record);

    public void batchInsert(@Param("orderItems") List<OrderItem> orderItems);

    public List<OrderItem> selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    public List<OrderItem> selectByOrderNo( @Param("orderNo") Long orderNo);
}