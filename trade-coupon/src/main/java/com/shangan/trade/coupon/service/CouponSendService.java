package com.shangan.trade.coupon.service;

/**
 * 发送优惠券服务
 */
public interface CouponSendService {

    /**
     * 同步发券
     *
     * @param batchId
     * @param userId
     * @return
     */
    boolean sendUserCouponSyn(long batchId, long userId);


    /**
     * 同步发券(使用了分布式锁)
     *
     * @param batchId
     * @param userId
     * @return
     */
    boolean sendUserCouponSynWithLock(long batchId, long userId);
}
