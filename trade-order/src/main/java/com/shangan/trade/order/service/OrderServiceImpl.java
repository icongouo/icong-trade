package com.shangan.trade.order.service;

import com.shangan.trade.coupon.service.UserCouponTranscationService;
import com.shangan.trade.goods.service.GoodsTranscationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GoodsTranscationService goodsTranscationService;

    @Autowired
    private UserCouponTranscationService userCouponTranscationService;

    @Override
    public void createOrder(long userId, long couponId, long orderId) {
        //1.try 先锁定库存
        boolean tryGoodsStockResult = goodsTranscationService.tryGoodsStock();

        //2.try 锁定优惠
        boolean tryCouponReuslt = userCouponTranscationService.tryCoupon(userId, couponId, orderId);

        if (!tryGoodsStockResult || !tryCouponReuslt) {
            log.error("roback tryGoodsStockResult:{},tryCouponReuslt:{}", tryGoodsStockResult, tryCouponReuslt);
            goodsTranscationService.cancleGoodsStock();
            userCouponTranscationService.cancleCoupon(userId, couponId, orderId);
        }

        log.info("order create success waiting for pay.....");
    }

    @Override
    public void paySuccess(long userId, long couponId, long orderId) {
        goodsTranscationService.commitGoodsStock();
        userCouponTranscationService.commitCoupon(userId, couponId, orderId);
    }

    @Override
    public void closeOrder(long userId, long couponId, long orderId) {
        //关闭订单时候，要把前面所有try的资源都回滚掉
        goodsTranscationService.cancleGoodsStock();
        userCouponTranscationService.cancleCoupon(userId, couponId, orderId);
    }
}
