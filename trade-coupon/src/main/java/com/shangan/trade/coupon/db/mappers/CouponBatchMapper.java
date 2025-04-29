package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.CouponBatch;

public interface CouponBatchMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponBatch record);

    int insertSelective(CouponBatch record);

    CouponBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponBatch record);

    int updateByPrimaryKey(CouponBatch record);
}