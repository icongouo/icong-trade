package com.shangan.trade.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.shangan"})
@MapperScan({"com.shangan.trade.coupon.db.mappers"})
@SpringBootApplication
public class TradeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeAdminApplication.class, args);
    }

}
