package com.ls.l7mall_2.mapper;

import com.ls.l7mall_2.entity.PayInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    int insert(PayInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    int insertSelective(PayInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    PayInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PayInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_pay_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PayInfo record);
}