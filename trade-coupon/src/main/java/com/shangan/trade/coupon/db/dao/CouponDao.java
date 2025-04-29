package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.Coupon;

public interface CouponDao {
    /**
     * 新增一张券
     * @param coupon
     * @return
     */
    boolean insertCoupon(Coupon coupon);
}
