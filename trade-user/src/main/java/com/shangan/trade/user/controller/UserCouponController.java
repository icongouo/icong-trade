package com.shangan.trade.user.controller;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.db.model.CouponRule;
import com.shangan.trade.coupon.service.CouponQueryService;
import com.shangan.trade.coupon.utils.RedisWorker;
import com.shangan.trade.user.vo.CouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class UserCouponController {

    @Autowired
    private CouponQueryService  couponQueryService;

    @Autowired
    private RedisWorker redisWorker;

    /**
     * 跳转到用户的优惠券列表页
     * @param userId
     * @return
     */
    @RequestMapping("/coupon/list/{userId}")
    public ModelAndView userCouponList(@PathVariable("userId") long userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("coupon_list");

        List<Coupon> coupons = couponQueryService.queryUsrCoupons(userId);

        List<CouponVO> notUsedList = new ArrayList<>();
        List<CouponVO> usedList = new ArrayList<>();
        List<CouponVO> expiredList = new ArrayList<>();

        for (Coupon coupon : coupons) {
            CouponVO couponVo = createCouponVo(coupon);
            if (coupon.getStatus() == 0) {
                notUsedList.add(couponVo);
            } else if (coupon.getStatus() == 1) {
                usedList.add(couponVo);
            } else if (coupon.getStatus() == 2) {
                expiredList.add(couponVo);
            }
        }
        modelAndView.addObject("notUsedList", notUsedList);
        modelAndView.addObject("usedList", usedList);
        modelAndView.addObject("expiredList", expiredList);
        return modelAndView;
    }

    public CouponVO createCouponVo(Coupon coupon) {
        CouponVO couponVO = new CouponVO();
        String batchRule = redisWorker.getValueByKey("couponBatchRule:" + coupon.getBatchId());
        /*
         * 将JSON中的rule规则，转化成CouponRule对象
         */
        CouponRule couponRule = JSON.parseObject(batchRule, CouponRule.class);

        if (couponRule.getCouponType() == 1) {
            //满减 券
            couponVO.setThresholdAmountStr("满" + couponRule.getThresholdAmount() + "元可用");
        }

        couponVO.setDiscountAmount(couponRule.getDiscountAmount());
        Date startTime = couponRule.getStartTime();
        Date endTime = couponRule.getEndTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeStr = dateFormat.format(startTime);
        String endTimeStr = dateFormat.format(endTime);
        couponVO.setStartTimeToEndTime(startTimeStr+"~"+endTimeStr);
        couponVO.setCouponName(coupon.getCouponName());
        return couponVO;
    }
}
