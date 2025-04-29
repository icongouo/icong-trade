package com.shangan.trade.order.controller;

import com.shangan.trade.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/createOrder")
    @ResponseBody
    public String createOrder(){
        orderService.createOrder(86869L,124326L,66789L);
        return "OK";
    }

    @RequestMapping("/pay")
    @ResponseBody
    public String pay(){
        orderService.paySuccess(86869L,124326L,66789L);
        return "OK";
    }

    @RequestMapping("/close")
    @ResponseBody
    public String close(){
        orderService.closeOrder(86869L,124326L,66789L);
        return "OK";
    }
}
