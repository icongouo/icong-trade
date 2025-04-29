package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.db.model.CouponRule;
import com.shangan.trade.coupon.exceptions.BizException;
import com.shangan.trade.coupon.service.CouponSendService;
import com.shangan.trade.coupon.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private RedisLock redisLock;

    @Override
    public boolean sendUserCouponSynWithLock(long batchId, long userId) {
        String lockkey = "sendUserCoupon";
        String requestId = UUID.randomUUID().toString();
        try {
            //去获取分布式锁，并判断是否获取成功
            if (redisLock.tryGetLock(lockkey, requestId, 1000)) {
                return sendUserCouponSyn(batchId, userId);
            }
        } catch (Exception e) {
            log.error("sendUserCouponSynWithLock error batchId={} userId={}", batchId, userId, e);
            throw new BizException(e.getMessage());
        } finally {
            // 执行完后必须要释放锁
            redisLock.releaseLock(lockkey, requestId);
        }
        return false;
    }

    /**
     * 同步发券
     *
     * @param batchId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean sendUserCouponSyn(long batchId, long userId) {
        //1.查询批次信息
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(batchId);
        //2.判断该batchId，对应的券批次信息是否存储在
        if (couponBatch == null) {
            //业务校验失败时，抛出自定义业务异常
            log.error("couponBatch is not exist batchId={} userId={}", batchId, userId);
            throw new BizException("券批次信息不存在");
        }
        //3.判断该批次的券余量是否大于0
        if (couponBatch.getAvailableCount() <= 0) {
            log.error("couponBatch totalCount is not enough batchId={} userId={}", batchId, userId);
            throw new BizException("券批次余量不足");
        }
        //4.更新券余量和券发送数量
        boolean updateSendCouponBatchRes = couponBatchDao.updateSendCouponBatchCount(couponBatch.getId());
        log.info("updateSendCouponBatchCount batchId={} userId={}", batchId, userId);
        if (!updateSendCouponBatchRes) {
            log.error("couponBatch updateSendCouponBatchRes error batchId={} userId={}", batchId, userId);
            throw new BizException("更新券券批次数量失败");
        }
        //5.优惠券表中新增该用户优惠券记录
        Coupon coupon = createCoupon(couponBatch, userId);
        boolean insertCouponRes = couponDao.insertCoupon(coupon);
        if (!insertCouponRes) {
            log.error("couponBatch insertCoupon error batchId={} userId={}", batchId, userId);
            throw new BizException("优惠券表中新增该用户优惠券记录失败");
        }
        log.info("sendUserCouponSyn success coupon info: {}", JSON.toJSONString(coupon));
        return true;
    }

    public Coupon createCoupon(CouponBatch couponBatch, long userId) {
        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setBatchId(couponBatch.getId());
        //收到的时间用当前系统的时间
        coupon.setReceivedTime(new Date());
        String rule = couponBatch.getRule();
        if (StringUtils.isEmpty(rule)) {
            log.error("couponBatch rule is empty batchId={} userId={}", couponBatch.getId(), userId);
            throw new BizException("券批次规则为空");
        }
        /*
         * 将JSON中的rule规则，转化成CouponRule对象
         */
        CouponRule couponRule = JSON.parseObject(rule, CouponRule.class);
        //设置到期时间
        coupon.setValidateTime(couponRule.getEndTime());
        coupon.setCouponName(couponBatch.getCouponName());
        //默认为1 有效
        coupon.setStatus(1);
        coupon.setCreateTime(new Date());
        return coupon;
    }
}
