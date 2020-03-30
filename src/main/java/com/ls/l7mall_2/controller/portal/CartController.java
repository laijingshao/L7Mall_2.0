package com.ls.l7mall_2.controller.portal;

import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.CartService;
import com.ls.l7mall_2.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-30-9:00
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private CartService cartService;

    // 购物车List列表
    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity<CartVo> list(HttpSession session){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.list(user.getId());
    }

    // 购物车添加商品
    @RequestMapping("add.do")
    @ResponseBody
    public ResponseEntity<CartVo> add(HttpSession session,Integer productId,Integer count){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.add(user.getId(),productId,count);
    }

    // 更新购物车某个产品的数量
    @RequestMapping("update.do")
    @ResponseBody
    public ResponseEntity<CartVo> update(HttpSession session,Integer productId,Integer count){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.update(user.getId(),productId,count);
    }

    // 移除购物车某个产品
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ResponseEntity<CartVo> deleteProduct(HttpSession session, String productIds){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.deleteProduct(user.getId(),productIds);
    }

    // 单选
    @RequestMapping("select.do")
    @ResponseBody
    public ResponseEntity<CartVo> select(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.selectOrUnselect(user.getId(),productId,Const.Cart.CHECKED);
    }

    // 取消单选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ResponseEntity<CartVo> unSelect(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.selectOrUnselect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    // 全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ResponseEntity<CartVo> selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.selectOrUnselect(user.getId(),null,Const.Cart.CHECKED);
    }

    // 取消全选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ResponseEntity<CartVo> unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        return cartService.selectOrUnselect(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    // 查询在购物车里的产品数量
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ResponseEntity<Integer> getCartProductCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenSuccess(0);
        }
        return cartService.getCartProductCount(user.getId());
    }
    
}
