package com.ls.l7mall_2.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.order_no
     *
     * @mbggenerated
     */
    private Long orderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.user_id
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.shipping_id
     *
     * @mbggenerated
     */
    private Integer shippingId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.payment
     *
     * @mbggenerated
     */
    private BigDecimal payment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.payment_type
     *
     * @mbggenerated
     */
    private Integer paymentType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.postage
     *
     * @mbggenerated
     */
    private Integer postage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.payment_time
     *
     * @mbggenerated
     */
    private Date paymentTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.send_time
     *
     * @mbggenerated
     */
    private Date sendTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.close_time
     *
     * @mbggenerated
     */
    private Date closeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column l7mall_order.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order
     *
     * @mbggenerated
     */
    public Order(Integer id, Long orderNo, Integer userId, Integer shippingId, BigDecimal payment, Integer paymentType, Integer postage, Integer status, Date paymentTime, Date sendTime, Date endTime, Date closeTime, Date createTime, Date updateTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.userId = userId;
        this.shippingId = shippingId;
        this.payment = payment;
        this.paymentType = paymentType;
        this.postage = postage;
        this.status = status;
        this.paymentTime = paymentTime;
        this.sendTime = sendTime;
        this.endTime = endTime;
        this.closeTime = closeTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table l7mall_order
     *
     * @mbggenerated
     */
    public Order() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.id
     *
     * @return the value of l7mall_order.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.id
     *
     * @param id the value for l7mall_order.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.order_no
     *
     * @return the value of l7mall_order.order_no
     *
     * @mbggenerated
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.order_no
     *
     * @param orderNo the value for l7mall_order.order_no
     *
     * @mbggenerated
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.user_id
     *
     * @return the value of l7mall_order.user_id
     *
     * @mbggenerated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.user_id
     *
     * @param userId the value for l7mall_order.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.shipping_id
     *
     * @return the value of l7mall_order.shipping_id
     *
     * @mbggenerated
     */
    public Integer getShippingId() {
        return shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.shipping_id
     *
     * @param shippingId the value for l7mall_order.shipping_id
     *
     * @mbggenerated
     */
    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.payment
     *
     * @return the value of l7mall_order.payment
     *
     * @mbggenerated
     */
    public BigDecimal getPayment() {
        return payment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.payment
     *
     * @param payment the value for l7mall_order.payment
     *
     * @mbggenerated
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.payment_type
     *
     * @return the value of l7mall_order.payment_type
     *
     * @mbggenerated
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.payment_type
     *
     * @param paymentType the value for l7mall_order.payment_type
     *
     * @mbggenerated
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.postage
     *
     * @return the value of l7mall_order.postage
     *
     * @mbggenerated
     */
    public Integer getPostage() {
        return postage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.postage
     *
     * @param postage the value for l7mall_order.postage
     *
     * @mbggenerated
     */
    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.status
     *
     * @return the value of l7mall_order.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.status
     *
     * @param status the value for l7mall_order.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.payment_time
     *
     * @return the value of l7mall_order.payment_time
     *
     * @mbggenerated
     */
    public Date getPaymentTime() {
        return paymentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.payment_time
     *
     * @param paymentTime the value for l7mall_order.payment_time
     *
     * @mbggenerated
     */
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.send_time
     *
     * @return the value of l7mall_order.send_time
     *
     * @mbggenerated
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.send_time
     *
     * @param sendTime the value for l7mall_order.send_time
     *
     * @mbggenerated
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.end_time
     *
     * @return the value of l7mall_order.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.end_time
     *
     * @param endTime the value for l7mall_order.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.close_time
     *
     * @return the value of l7mall_order.close_time
     *
     * @mbggenerated
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.close_time
     *
     * @param closeTime the value for l7mall_order.close_time
     *
     * @mbggenerated
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.create_time
     *
     * @return the value of l7mall_order.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.create_time
     *
     * @param createTime the value for l7mall_order.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column l7mall_order.update_time
     *
     * @return the value of l7mall_order.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column l7mall_order.update_time
     *
     * @param updateTime the value for l7mall_order.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Order(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public Order(Long orderNo, Integer userId, Integer shippingId, BigDecimal payment, Integer paymentType, Integer postage, Integer status) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.shippingId = shippingId;
        this.payment = payment;
        this.paymentType = paymentType;
        this.postage = postage;
        this.status = status;
    }
}