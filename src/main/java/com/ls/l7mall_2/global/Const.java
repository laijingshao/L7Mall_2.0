package com.ls.l7mall_2.global;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author laijs
 * @date 2020-3-28-22:13
 */
public class Const {
    // 登录时session的key值
    public static final String CURRENT_USER = "currentUser";

    // 校验用户名和email时的type
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    // 用户等级
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    // 产品状态
    public enum ProductStatus{
        ON_SALE(1,"在线");
        private int code;
        private String value;

        ProductStatus(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    // 分页的升降序
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    // 购物车
    public interface Cart{
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";       // 库存充足
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS"; // 库存不足
    }

    // 订单状态
    public enum OrderStatus{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");

        private Integer code;
        private String value;

        OrderStatus(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        // 提供一个方法，根据code获取订单状态
        public static OrderStatus codeOf(Integer code){
            for (OrderStatus orderStatus : values()) {
                if(orderStatus.getCode() == code){
                    return orderStatus;
                }
            }
            throw new RuntimeException("没有相应的订单状态");
        }
    }

    // 交易状态(用于和支付宝回调的交易状态比较，从支付宝文档中获取)
    public interface TradeStatus{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
    }

    // 交易平台
    public enum payPlatformEnum{
        ALIPAY(1,"支付宝"),
        WECHATPAY(0,"微信");

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        payPlatformEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        private Integer code;
        private String value;

    }

    // 支付宝回调的处理结果
    public interface AlipayCallback{
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    // 交易类型
    public enum PaymentType{
        ONLINE_PAY(1,"在线支付");

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        PaymentType(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        private Integer code;
        private String value;

        // 提供一个方法，根据code获取交易类型
        public static PaymentType codeOf(int code){
            for(PaymentType paymentType : values()){
                if(paymentType.getCode() == code){
                    return paymentType;
                }
            }
            throw new RuntimeException("没有相应的交易类型");
        }

    }
}
