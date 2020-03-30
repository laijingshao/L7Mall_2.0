package com.ls.l7mall_2.controller.backend;

import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-30-10:39
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private OrderService orderService;

    //订单列表
    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){

            return orderService.listManageOrder(pageNum,pageSize);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    //按订单号查询
    @RequestMapping("search.do")
    @ResponseBody
    public ResponseEntity search(HttpSession session, Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){

            return orderService.search(orderNo,pageNum,pageSize);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 订单详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseEntity detail(HttpSession session, Long orderNo) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){

            return orderService.detail(orderNo);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 订单发货
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ResponseEntity sendGoods(HttpSession session, Long orderNo) {
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){

            return orderService.sendGoods(orderNo);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }
}
