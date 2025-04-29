package com.shangan.trade.admin.controller;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.db.model.CouponRule;
import com.shangan.trade.coupon.service.CouponBatchService;
import com.shangan.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    private CouponBatchService couponBatchService;

    @Autowired
    private CouponSendService couponSendService;

    /**
     * 跳转到主页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 跳转券批次信息添加
     *
     * @return
     */
    @RequestMapping("/addCouponBatch")
    public String addGoods() {
        return "add_coupon_batch";
    }

    /**
     * 跳转到优惠券批次列表页
     *
     * @return
     */
    @RequestMapping("/couponBatchList")
    public String couponBatchList(Map<String, Object> resultMap) {
        List<CouponBatch> couponBatchList = couponBatchService.queryCouponBatchList();
        resultMap.put("couponBatchList", couponBatchList);
        return "coupon_batch_list";
    }

    /**
     * 添加优惠券批次信息
     *
     * @return
     */
    @RequestMapping("/addCouponBatchAction")
    public String addCouponBatchAction(@RequestParam("batchName") String batchName,
                                       @RequestParam("couponName") String couponName,
                                       @RequestParam("couponType") int couponType,
                                       @RequestParam("grantType") int grantType,
                                       @RequestParam("totalCount") long totalCount,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime,
                                       @RequestParam("thresholdAmount") int thresholdAmount,
                                       @RequestParam("discountAmount") int discountAmount,
                                       Map<String, Object> resultMap) {
        try {

            CouponBatch couponBatch = new CouponBatch();
            couponBatch.setBatchName(batchName);
            couponBatch.setCouponName(couponName);
            couponBatch.setCouponType(couponType);
            couponBatch.setGrantType(grantType);
            couponBatch.setTotalCount(totalCount);
            couponBatch.setAvailableCount(totalCount);
            //默认状态有效
            couponBatch.setStatus(1);
            couponBatch.setUsedCount(0L);
            couponBatch.setAssignCount(0L);
            couponBatch.setCreateTime(new Date());

            /*
             * 创建优惠券规则对象
             */
            CouponRule couponRule = new CouponRule();
            couponRule.setCouponType(couponType);
            couponRule.setGrantType(grantType);
            //获取到的startTime时间格式  2022-10-05T22:51
            startTime = startTime.substring(0, 10) + " " + startTime.substring(11);
            endTime = endTime.substring(0, 10) + " " + endTime.substring(11);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            couponRule.setStartTime(format.parse(startTime));
            couponRule.setEndTime(format.parse(endTime));
            couponRule.setThresholdAmount(thresholdAmount);
            couponRule.setDiscountAmount(discountAmount);
            //转为JSON格式
            couponBatch.setRule(JSON.toJSONString(couponRule));

            couponBatchService.insertCouponBatch(couponBatch);
            log.info("addCouponBatchAction success  couponBatch:{}", JSON.toJSONString(couponRule));
            //跳转到券批次列表
            return "coupon_batch_list";
        } catch (Exception e) {
            log.error("addCouponBatchAction error", e);
            //跳转到异常提示页面
            return "error_page";
        }
    }


    /**
     * 发放优惠券给用户
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/sendSyn/{batchId}/{userId}")
    @ResponseBody
    public String sendCouponSyn(@PathVariable long batchId, @PathVariable long userId) {
        try {
            log.info("batchId={}, userId={}", batchId, userId);
            couponSendService.sendUserCouponSyn(batchId, userId);
            return "优惠券发放成功";
        } catch (Exception e) {
            //发放优惠券给用户失败
            log.error("sendCouponSyn error,errorMessage:{}", e.getMessage());
            return "发放优惠券给用户失败,原因:" + e.getMessage();
        }
    }

    /**
     * 跳转券批次信息添加
     *
     * @return
     */
    @RequestMapping("/sendCouponSyn")
    public String sendCouponSyn() {
        return "send_coupon_syn";
    }

    /**
     * 发放优惠券给用户
     *
     * @param batchId
     * @param userId
     * @return
     */
    @RequestMapping("/sendCouponSynAction")
    public ModelAndView sendCouponSynAction(@RequestParam("batchId") long batchId,
                                            @RequestParam("userId") long userId) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            log.info("batchId={}, userId={}", batchId, userId);
            couponSendService.sendUserCouponSynWithLock(batchId, userId);
            modelAndView.addObject("resultInfo", "发放成功");
            modelAndView.setViewName("process_result");
            return modelAndView;
        } catch (Exception e) {
            //发放优惠券给用户失败
            log.error("sendCouponSyn error,errorMessage:{}", e.getMessage());
            modelAndView.addObject("resultInfo", "发放优惠券给用户失败,原因" + e.getMessage());
            modelAndView.setViewName("process_result");
            return modelAndView;
        }
    }
}
