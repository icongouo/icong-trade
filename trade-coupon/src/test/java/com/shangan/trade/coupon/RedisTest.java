package com.shangan.trade.coupon;
import com.shangan.trade.coupon.utils.RedisWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {
    @Autowired
    public RedisWorker redisWorker;

    @Test
    void setValue() {
        redisWorker.setValue("testName","你好");
    }

    @Test
    void getValue() {
        System.out.println(redisWorker.getValueByKey("testName"));
    }
}