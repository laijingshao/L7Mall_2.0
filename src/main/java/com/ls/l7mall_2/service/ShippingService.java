package com.ls.l7mall_2.service;

import com.github.pagehelper.PageInfo;
import com.ls.l7mall_2.entity.Shipping;
import com.ls.l7mall_2.global.ResponseEntity;

/**
 * @author laijs
 * @date 2020-3-30-9:32
 */
public interface ShippingService {
    public ResponseEntity addShipping(Integer userId, Shipping shipping);
    public ResponseEntity deleteShipping(Integer userId,Integer shippingId);
    public ResponseEntity updateShipping(Integer userId,Shipping shipping);
    public ResponseEntity selectShipping(Integer userId,Integer shippingId);
    public ResponseEntity<PageInfo> listShipping(Integer userId, Integer pageNum, Integer pageSize);
}
