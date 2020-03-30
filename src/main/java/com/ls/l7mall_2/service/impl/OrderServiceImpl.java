package com.ls.l7mall_2.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ls.l7mall_2.config.AlipayConfig;
import com.ls.l7mall_2.entity.*;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.mapper.*;
import com.ls.l7mall_2.service.OrderService;
import com.ls.l7mall_2.util.BigDecimalUtils;
import com.ls.l7mall_2.util.DateTimeUtil;
import com.ls.l7mall_2.util.FTPServerUtils;
import com.ls.l7mall_2.util.PropertiesUtil;
import com.ls.l7mall_2.vo.OrderItemVo;
import com.ls.l7mall_2.vo.OrderProductVo;
import com.ls.l7mall_2.vo.OrderVo;
import com.ls.l7mall_2.vo.ShippingVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author laijs
 * @date 2020-3-30-9:50
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private AlipayConfig alipayConfig;
    
    // portal

    // 创建新订单
    public ResponseEntity createOrder(Integer userId, Integer shippingId) {
        // 根据用户id查找checked状态为1的购物车
        List<Cart> carts = cartMapper.selectCartCheckedByUserId(userId);
        // 找到购物车中各个订单详情
        ResponseEntity responseEntity = this.assembleOrderItem(userId, carts);
        if (!responseEntity.isSuccess()) {
            return responseEntity;
        }
        List<OrderItem> orderItems = (List<OrderItem>) responseEntity.getData();
        if (orderItems == null) {
            return ResponseEntity.responesWhenError("购物车为空");
        }
        // 计算订单的总价
        BigDecimal payment = this.getOrderTotalPrice(orderItems);
        // 生成order
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return ResponseEntity.responesWhenError("创建订单失败");
        }
        // 将生成的订单号赋值给orderItems中的每一个orderItem
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        // 将orderItem中的数据批量插入数据库中
        orderItemMapper.batchInsert(orderItems);
        // 订单生成后，需要更改产品的库存数量，
        for (OrderItem orderItem : orderItems) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        // 并删除购物车的数据
        for (Cart cart : carts) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }

        // 封装需要返回给前端的数据
        OrderVo orderVo = this.assembleOrderVo(order, orderItems);
        return ResponseEntity.responesWhenSuccess(orderVo);
    }

    // 获取订单的商品信息
    public ResponseEntity getOrderCartProduct(Integer userId) {
        // 根据用户id查找checked状态为1的购物车
        List<Cart> carts = cartMapper.selectCartCheckedByUserId(userId);
        // 找到购物车中各个订单详情
        ResponseEntity responseEntity = this.assembleOrderItem(userId, carts);
        if (!responseEntity.isSuccess()) {
            return responseEntity;
        }
        List<OrderItem> orderItems = (List<OrderItem>) responseEntity.getData();
        if (orderItems == null) {
            return ResponseEntity.responesWhenError("购物车为空");
        }
        // 计算订单的总价
        BigDecimal payment = this.getOrderTotalPrice(orderItems);
        ArrayList<OrderItemVo> orderItemVos = Lists.newArrayList();
        // OrderItem ---> OrderItemVo
        for (OrderItem orderItem : orderItems) {
            OrderItemVo orderItemVo = new OrderItemVo(orderItem.getOrderNo(), orderItem.getProductId(), orderItem.getProductName(), orderItem.getProductImage(), orderItem.getCurrentUnitPrice(),
                    orderItem.getQuantity(), orderItem.getTotalPrice(), DateTimeUtil.dateToStr(orderItem.getCreateTime()));
            orderItemVos.add(orderItemVo);
        }
        OrderProductVo orderProductVo = new OrderProductVo(orderItemVos, payment, PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return ResponseEntity.responesWhenSuccess(orderProductVo);

    }

    // 取消订单
    public ResponseEntity cancelOrder(Integer userId, Long orderNo) {
        // 获取订单
        Order order = orderMapper.selectOrderByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ResponseEntity.responesWhenError("订单不存在");
        }
        // 判断是否已经发货
        if (order.getStatus() != Const.OrderStatus.NO_PAY.getCode()) {
            return ResponseEntity.responesWhenError("订单已发货，无法取消");
        }
        Order updateOrder = new Order(order.getId(), Const.OrderStatus.CANCELED.getCode());
        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (rowCount > 0) {
            // 更新库存信息
            List<OrderItem> orderItems = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
            for (OrderItem orderItem : orderItems) {
                Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
                product.setStock(product.getStock() + orderItem.getQuantity());
                productMapper.updateByPrimaryKeySelective(product);
            }
            return ResponseEntity.responesWhenSuccess("取消订单成功");
        }
        return ResponseEntity.responesWhenError("取消订单失败");
    }

    // 获取订单列表
    public ResponseEntity listOrder(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectOrderByUserId(userId);
        ArrayList<OrderVo> orderVos = Lists.newArrayList();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemMapper.selectByUserIdAndOrderNo(userId, order.getOrderNo());
            OrderVo orderVo = this.assembleOrderVo(order, orderItems);
            orderVos.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo(orders);
        pageInfo.setList(orderVos);
        return ResponseEntity.responesWhenSuccess(pageInfo);
    }

    // 获取订单详情
    public ResponseEntity orderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOrderByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItems = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItems);
            return ResponseEntity.responesWhenSuccess(orderVo);
        }
        return ResponseEntity.responesWhenError("没有找到该订单");
    }

    // List<Cart> carts ---> ArrayList<OrderItem> orderItems
    public ResponseEntity assembleOrderItem(Integer userId, List<Cart> carts) {
        if (carts.isEmpty()) {
            return ResponseEntity.responesWhenError("购物车为空");
        }
        // 遍历该用户的购物车，获取其中的product信息并封装到orderItem中
        ArrayList<OrderItem> orderItems = Lists.newArrayList();
        for (Cart cart : carts) {
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            // 判断product的状态和库存信息
            if (product.getStatus() != Const.ProductStatus.ON_SALE.getCode()) {
                return ResponseEntity.responesWhenError("产品" + product.getName() + "已下架");
            }
            if (product.getStock() < cart.getQuantity()) {
                return ResponseEntity.responesWhenError("产品" + product.getName() + "库存不足");
            }
            OrderItem orderItem = new OrderItem(userId, product.getId(), product.getName(), product.getMainImage(), product.getPrice(),
                    cart.getQuantity(), BigDecimalUtils.multiply(cart.getQuantity(), product.getPrice().doubleValue()));
            orderItems.add(orderItem);
        }
        return ResponseEntity.responesWhenSuccess(orderItems);
    }

    // getOrderTotalPrice
    public BigDecimal getOrderTotalPrice(List<OrderItem> orderItems) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItems) {
            payment = BigDecimalUtils.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    // assembleOrder
    public Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        long orderNo = System.currentTimeMillis() + new Random().nextInt(100);
        Order order = new Order(orderNo, userId, shippingId, payment, Const.PaymentType.ONLINE_PAY.getCode(), 0, Const.OrderStatus.NO_PAY.getCode());
        int rowCount = orderMapper.insert(order);
        if (rowCount == 0) {
            return null;
        }
        return order;
    }

    // Order ---> OrderVo
    public OrderVo assembleOrderVo(Order order, List<OrderItem> orderItems) {
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        ShippingVo shippingVo = this.assembleShippingVo(shipping);
        List<OrderItemVo> orderItemVos = this.assembleOrderItemVo(orderItems);
        OrderVo orderVo = new OrderVo(order.getOrderNo(), order.getPayment(), order.getPaymentType(), Const.PaymentType.codeOf(order.getPaymentType()).getValue(),
                order.getPostage(), order.getStatus(), Const.OrderStatus.codeOf(order.getStatus()).getValue(), DateTimeUtil.dateToStr(order.getPaymentTime()),
                DateTimeUtil.dateToStr(order.getSendTime()), DateTimeUtil.dateToStr(order.getEndTime()), DateTimeUtil.dateToStr(order.getCloseTime()),
                DateTimeUtil.dateToStr(order.getCreateTime()), orderItemVos, PropertiesUtil.getProperty("ftp.server.http.prefix"), shipping.getId(),
                shipping.getReceiverName(), shippingVo);
        return orderVo;
    }

    // OrderItem ---> OrderItemVo
    public List<OrderItemVo> assembleOrderItemVo(List<OrderItem> orderItems) {
        ArrayList<OrderItemVo> orderItemVos = Lists.newArrayList();
        // OrderItems ---> OrderItemVos
        for (OrderItem orderItem : orderItems) {
            OrderItemVo orderItemVo = new OrderItemVo(orderItem.getOrderNo(), orderItem.getProductId(), orderItem.getProductName(), orderItem.getProductImage(), orderItem.getCurrentUnitPrice(),
                    orderItem.getQuantity(), orderItem.getTotalPrice(), DateTimeUtil.dateToStr(orderItem.getCreateTime()));
            orderItemVos.add(orderItemVo);
        }
        return orderItemVos;
    }

    // Shipping ---> ShippingVo
    public ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo(shipping.getReceiverName(), shipping.getReceiverPhone(), shipping.getReceiverMobile(), shipping.getReceiverProvince(),
                shipping.getReceiverCity(), shipping.getReceiverDistrict(), shipping.getReceiverAddress(), shipping.getReceiverZip());
        return shippingVo;
    }



    // 订单列表
    public ResponseEntity listManageOrder(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectAll();
        ArrayList<OrderVo> orderVos = Lists.newArrayList();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
            OrderVo orderVo = this.assembleOrderVo(order, orderItems);
            orderVos.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo(orders);
        pageInfo.setList(orderVos);
        return ResponseEntity.responesWhenSuccess(pageInfo);
    }

    // 按订单号查询(可以拓展多条件的查询)
    public ResponseEntity<PageInfo> search(Long orderNo,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Order order = orderMapper.selectOrderByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItems);

            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVo));
            return ResponseEntity.responesWhenSuccess(pageInfo);
        }
        return ResponseEntity.responesWhenError("订单不存在");
    }

    // 订单详情
    public ResponseEntity detail(Long orderNo) {
        Order order = orderMapper.selectOrderByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItems);
            return ResponseEntity.responesWhenSuccess(orderVo);
        }
        return ResponseEntity.responesWhenError("订单不存在");
    }

    // 订单发货
    public ResponseEntity sendGoods(Long orderNo) {
        Order order = orderMapper.selectOrderByOrderNo(orderNo);
        if (order != null) {
            if(order.getStatus() == Const.OrderStatus.PAID.getCode()){
                order.setStatus(Const.OrderStatus.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ResponseEntity.responesWhenSuccess("发货成功");
            }
            return ResponseEntity.responesWhenError("发货失败");
        }
        return ResponseEntity.responesWhenError("订单不存在");
    }

    // 支付模块    

    public ResponseEntity pay(Integer userId, Long orderNo, String path) throws AlipayApiException {
        HashMap map = Maps.newHashMap();
        Order order = orderMapper.selectOrderByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ResponseEntity.responesWhenError("用户无该订单");
        }
        map.put("orderNo", order.getOrderNo().toString());
        
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppID(), alipayConfig.getMerchantPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipayPublicKey(), alipayConfig.getSignType());

        

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("L7Mall扫码支付，订单号：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

//        // 商品明细列表，需填写购买商品详细信息，
//        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
//        List<OrderItem> orderItems = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
//        for (OrderItem orderItem : orderItems) {
//            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
//                    BigDecimalUtils.multiply(orderItem.getCurrentUnitPrice().doubleValue(), 100).longValue(), orderItem.getQuantity());
//            goodsDetailList.add(goods);
//        }
//        *//*//*/ 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
//        // 创建好一个商品后添加至商品明细列表
//        goodsDetailList.add(goods1);
//        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);*//*
//
//        // 创建扫码支付请求builder，设置请求参数
//        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
//                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
//                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
//                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
//                .setTimeoutExpress(timeoutExpress)
//                // 回调函数访问路径
//                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
//                .setGoodsDetailList(goodsDetailList);
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
        
        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setUndiscountableAmount(undiscountableAmount);
        model.setSellerId(sellerId);
        model.setBody(body);
        model.setOperatorId(operatorId);
        model.setStoreId(storeId);
        model.setTimeoutExpress(timeoutExpress);
        
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        
        AlipayTradePrecreateResponse alipayResponse = alipayClient.execute(alipayRequest);
        if(alipayResponse.isSuccess()){
            logger.info("支付宝预下单成功: )");

            // 将生成的二维码保存到FTP服务器中

            // 创建目录
            File file = new File(path);
            if (!file.exists()) {
                file.setExecutable(true);
                file.mkdirs();
            }
            // 需要修改为运行机器上的路径
            String filePath = String.format(path + "/qr-%s.png", alipayResponse.getOutTradeNo());
            String fileName = String.format("/qr-%s.png", alipayResponse.getOutTradeNo());
            ZxingUtils.getQRCodeImge(alipayResponse.getQrCode(), 256, filePath);
            // 上传文件
            File targetFile = new File(path, fileName);
            try {
                FTPServerUtils.upload(Lists.newArrayList(targetFile));
            } catch (IOException e) {
                logger.error("上传二维码异常", e);
            }
            logger.info("filePath:" + filePath);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
            map.put("url", url);
            return ResponseEntity.responesWhenSuccess(map);
        } else {
            logger.error("支付宝预下单失败!!!");
            return ResponseEntity.responesWhenError("支付宝预下单失败!!!");
        }
        
    }

    // 处理支付宝的回调
    public ResponseEntity aliCallback(Map<String, String> params) {
        // 从回调中获取订单号、交易号、交易状态码
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        // 根据订单号获取订单
        Order order = orderMapper.selectOrderByOrderNo(orderNo);
        if (order == null) {
            return ResponseEntity.responesWhenError("非本商场订单");
        }
        // 根据订单状态判断支付宝是否重复回调了
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
            return ResponseEntity.responesWhenSuccess("支付宝重复回调");
        }
        // 根据交易状态判断是否交易成功
        if (Const.TradeStatus.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            // 交易成功--修改订单内容
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatus.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        // 将交易详情持久化
        PayInfo payInfo = new PayInfo(order.getUserId(), order.getOrderNo(), Const.payPlatformEnum.ALIPAY.getCode(), tradeNo, tradeStatus);
        payInfoMapper.insert(payInfo);
        return ResponseEntity.responesWhenSuccess();

    }

    // 查询订单的支付状态
    public ResponseEntity queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOrderByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ResponseEntity.responesWhenError("用户无该订单");
        }
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
            return ResponseEntity.responesWhenSuccess();
        }
        return ResponseEntity.responesWhenError();
    }


}
