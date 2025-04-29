package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.Coupon;

public interface CouponMapper {
    int insert(Coupon record);

    int insertSelective(Coupon record);
}