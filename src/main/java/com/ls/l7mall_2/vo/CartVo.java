package com.ls.l7mall_2.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author laijs
 * @date 2020-3-30-9:02
 */
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    public CartVo(List<CartProductVo> cartProductVoList, boolean allChecked, BigDecimal cartTotalPrice) {
        this.cartProductVoList = cartProductVoList;
        this.allChecked = allChecked;
        this.cartTotalPrice = cartTotalPrice;
    }

    public CartVo() {
    }

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
