package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.Coupon;

import java.util.List;

public interface CouponDao {
    /**
     * 新增一张券
     * @param coupon
     * @return
     */
    boolean insertCoupon(Coupon coupon);

    /**
     *  查询用的优惠券
     * @param userId
     * @return
     */
    List<Coupon> queryUserCoupons(long userId);
}
