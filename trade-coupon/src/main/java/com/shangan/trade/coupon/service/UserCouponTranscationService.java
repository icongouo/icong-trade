package com.shangan.trade.coupon.service;

/**
 * 优惠券的TCC接口
 */
public interface UserCouponTranscationService {
    /**
     * 冻结优惠券
     * @param userId
     * @param couponId
     * @param orderId
     * @return
     */
    boolean tryCoupon(long userId,long couponId,long orderId);

    boolean cancleCoupon(long userId,long couponId,long orderId);

    boolean commitCoupon(long userId,long couponId,long orderId);
}
