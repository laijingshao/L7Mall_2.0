package com.ls.l7mall_2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.ls.l7mall_2.entity.Shipping;
import com.ls.l7mall_2.global.ResponseCode;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.mapper.ShippingMapper;
import com.ls.l7mall_2.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author laijs
 * @date 2020-3-30-9:33
 */
@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
    
    @Autowired
    private ShippingMapper shippingMapper;
    
    @Override
    public ResponseEntity addShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.insert(shipping);
        if(count > 0){
            HashMap map = Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return ResponseEntity.responesWhenSuccess("新建地址成功",map);
        }
        return ResponseEntity.responesWhenError("新建地址失败");
    }

    @Override
    public ResponseEntity deleteShipping(Integer userId, Integer shippingId) {
        if(shippingId == null){
            return ResponseEntity.responesWhenError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
        int count = shippingMapper.deleteByIdAndUserId(userId, shippingId);
        if(count > 0){
            return ResponseEntity.responesWhenSuccess("删除地址成功");
        }
        return ResponseEntity.responesWhenError("删除地址失败");
    }

    @Override
    public ResponseEntity updateShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.updateByShipping(shipping);
        if(count > 0){
            return ResponseEntity.responesWhenSuccess("更新地址成功");
        }
        return ResponseEntity.responesWhenError("更新地址失败");
    }

    @Override
    public ResponseEntity selectShipping(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByIdAndUserId(userId, shippingId);
        if(shipping == null){
            return ResponseEntity.responesWhenError("获取地址详情失败");
        }
        return ResponseEntity.responesWhenSuccess("获取地址详情成功",shipping);
    }

    @Override
    public ResponseEntity<PageInfo> listShipping(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ResponseEntity.responesWhenSuccess("获取地址详情成功",pageInfo);
    }
    
}
