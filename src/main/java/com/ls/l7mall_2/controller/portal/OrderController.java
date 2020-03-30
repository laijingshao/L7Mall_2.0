package com.ls.l7mall_2.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.ls.l7mall_2.config.AlipayConfig;
import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author laijs
 * @date 2020-3-30-9:48
 */
@Controller
@RequestMapping("/order/")
public class OrderController {
    public static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private AlipayConfig alipayConfig;

    // 创建订单
    @RequestMapping("create.do")
    @ResponseBody
    public ResponseEntity create(HttpSession session, Integer shippingId) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return orderService.createOrder(user.getId(), shippingId);
    }

    // 获取订单的商品信息
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ResponseEntity getOrderCartProduct(HttpSession session) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    // 取消订单
    @RequestMapping("cancel.do")
    @ResponseBody
    public ResponseEntity cancel(HttpSession session,Long orderNo) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return orderService.cancelOrder(user.getId(),orderNo);
    }

    //订单列表
    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return orderService.listOrder(user.getId(),pageNum,pageSize);
    }

    //订单列表
    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseEntity detail(HttpSession session, Long orderNo) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return orderService.orderDetail(user.getId(),orderNo);
    }


    // 扫码支付
    @RequestMapping("pay.do")
    @ResponseBody
    public ResponseEntity pay(HttpSession session, Long orderNo, HttpServletRequest request) throws AlipayApiException {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        String path = request.getServletContext().getRealPath("upload");
        return orderService.pay(user.getId(), orderNo, path);
    }

    // 支付宝回调
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        // 创建一个map用于保存回调请求参数（修改形式后）
        HashMap<String, String> params = Maps.newHashMap();
        // 获取回调请求中的参数
        Map<String, String[]> requestParams = request.getParameterMap();
        // 使用迭代器进行遍历
        Iterator<String> iter = requestParams.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            // 将map中一个key对应的value封装为一个String字符串
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 修改value的形式后，存在自定义的map中
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        // ☆☆☆☆☆☆验签（验证是否为支付宝发的回调请求）、避免重复通知
        params.remove("sign_type");
        try {
            boolean rsaCheckV2 = AlipaySignature.rsaCheckV2(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            // 验证失败
            if (!rsaCheckV2) {
                return ResponseEntity.responesWhenError("非法请求,验证不通过,恶意请求我们将交由网警处理");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }
        // 验证成功，调用业务方法处理回调中的数据
        ResponseEntity responseEntity = orderService.aliCallback(params);
        if(responseEntity.isSuccess()){
            // 处理成功
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        // 处理失败
        return Const.AlipayCallback.RESPONSE_FAILED;

    }

    // 查询支付状态
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ResponseEntity<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        ResponseEntity responseEntity = orderService.queryOrderPayStatus(user.getId(), orderNo);
        if(responseEntity.isSuccess()){
            return ResponseEntity.responesWhenSuccess(true);
        }
        return ResponseEntity.responesWhenSuccess(false);
    }
    
    
}
