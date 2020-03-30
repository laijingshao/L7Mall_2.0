package com.ls.l7mall_2.service;

import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.vo.CartVo;

/**
 * @author laijs
 * @date 2020-3-30-9:03
 */
public interface CartService {
    public ResponseEntity<CartVo> list(Integer userId);
    public ResponseEntity<CartVo> add(Integer userId,Integer productId,Integer count);
    public ResponseEntity<CartVo> update(Integer userId,Integer productId,Integer count);
    public ResponseEntity<CartVo> deleteProduct(Integer userId,String productIds);
    public ResponseEntity<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer checked);
    public ResponseEntity<Integer> getCartProductCount(Integer userId);
}
