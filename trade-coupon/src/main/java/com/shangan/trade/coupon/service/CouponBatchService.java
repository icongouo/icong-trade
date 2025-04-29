package com.shangan.trade.coupon.service;

import com.shangan.trade.coupon.db.model.CouponBatch;

public interface CouponBatchService {

    /**
     * 新增一个券批次
     *
     * @param couponBatch
     * @return
     */
    boolean insertCouponBatch(CouponBatch couponBatch);
}
