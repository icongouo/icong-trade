package com.shangan.trade.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerController {

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
     * @return
     */
    @RequestMapping("/addCouponBatch")
    public String addGoods() {
        return "add_coupon_batch";
    }

    /**
     * 跳转到优惠券批次列表页
     * @return
     */
    @RequestMapping("couponBatchList")
    public String couponBatchList() {
        return "coupon_batch_list";
    }

}
