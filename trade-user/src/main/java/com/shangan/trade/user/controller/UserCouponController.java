package com.shangan.trade.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class UserCouponController {

    /**
     * 跳转到用户列表页
     *
     * @return
     */
    @RequestMapping("/coupon/list/{userId}")
    public ModelAndView userCouponList(@PathVariable long userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("coupon_list");
        return modelAndView;
    }
}
