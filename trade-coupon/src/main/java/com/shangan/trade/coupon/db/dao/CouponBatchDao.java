package com.shangan.trade.coupon.db.dao;

import com.shangan.trade.coupon.db.model.CouponBatch;

public interface CouponBatchDao {
    /**
     * 插入一个券批次
     *
     * @param couponBatch
     * @return
     */
    boolean insertCouponBatch(CouponBatch couponBatch);

    /**
     * 根据ID删除对应的券批次
     *
     * @param id
     * @return
     */
    boolean deleteCouponBatchById(long id);

    /**
     * 根据ID查询对应的券批次
     *
     * @param id
     * @return
     */
    CouponBatch queryCouponBatchById(long id);

    /**
     * 修改对应的券批次
     *
     * @param couponBatch
     * @return
     */
    boolean updateCouponBatch(CouponBatch couponBatch);
}
