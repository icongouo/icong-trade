package com.shangan.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.model.CouponBatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponBatchTest {
    @Resource
    private CouponBatchDao couponBatchDao;

    @Test
    public void insertCouponBatchTest() {
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setBatchName("优惠券批次测试");
        couponBatch.setCouponName("满100减20优惠券");
        couponBatch.setCouponType(1);
        couponBatch.setRule("rule");
        couponBatch.setTotalCount(100L);
        couponBatch.setAvailableCount(100L);
        couponBatch.setUsedCount(20L);
        couponBatch.setAssignCount(10L);
        couponBatch.setGrantType(1);
        couponBatch.setCreateTime(new Date());
        couponBatch.setStatus(1);
        boolean insertResult = couponBatchDao.insertCouponBatch(couponBatch);
        System.out.println(insertResult);
    }

    @Test
    public void deleteGoodsTest() {
        boolean deleteResult = couponBatchDao.deleteCouponBatchById(1);
        System.out.println(deleteResult);
    }

    @Test
    public void queryGoodsTest() {
        CouponBatch goods = couponBatchDao.queryCouponBatchById(2);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(2);
        couponBatch.setBatchName(couponBatch.getBatchName() + " update");
        couponBatchDao.updateCouponBatch(couponBatch);
    }
}
