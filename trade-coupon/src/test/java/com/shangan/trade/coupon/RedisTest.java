package com.shangan.trade.coupon;
import com.shangan.trade.coupon.utils.RedisLock;
import com.shangan.trade.coupon.utils.RedisWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RedisTest {
    @Autowired
    public RedisWorker redisWorker;
    @Autowired
    public RedisLock redisLock;

    @Test
    void setValue() {
        redisWorker.setValue("testName","你好");
    }

    @Test
    void getValue() {
        System.out.println(redisWorker.getValueByKey("testName"));
    }

    @Test
    void getLock(){
        boolean res1 = redisLock.tryGetLock("test", "123", 1000);
        System.out.println(res1);
//        boolean redisLock_res = redisLock.releaseLock("test", "123");
//        System.out.println("redisLock_res: {}"+redisLock_res);
        boolean res2 = redisLock.tryGetLock("test", "123", 1000);
        System.out.println(res2);
    }
}