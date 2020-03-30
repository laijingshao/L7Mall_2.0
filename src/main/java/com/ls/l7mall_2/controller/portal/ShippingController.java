package com.ls.l7mall_2.controller.portal;

import com.ls.l7mall_2.entity.Shipping;
import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-30-9:31
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    
    @Autowired
    private ShippingService shippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ResponseEntity add(HttpSession session, Shipping shipping){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return shippingService.addShipping(user.getId(),shipping);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ResponseEntity delete(HttpSession session, int shippingId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return shippingService.deleteShipping(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ResponseEntity update(HttpSession session, Shipping shipping){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return shippingService.updateShipping(user.getId(),shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ResponseEntity select(HttpSession session, Integer shippingId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return shippingService.selectShipping(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return shippingService.listShipping(user.getId(),pageNum,pageSize);
    }
}
