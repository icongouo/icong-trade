package com.shangan.trade.coupon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Collections;

@Slf4j
public class RedisLockUtil {
    public static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final int MAX_TYR_COUNT = 3;

    @Autowired
    private JedisPool jedisPool;

    /**
     *  获取锁
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        try {
            Jedis jedisClient = jedisPool.getResource();
            String result = jedisClient.set(lockKey, requestId, "NX", "PX", expireTime);
            int tryCount = 0;
            do {
                if (LOCK_SUCCESS.equals(result)) {
                    return true;
                }
                Thread.sleep(20);
                tryCount++;
                // 当第一次加锁失败后，再次尝试获取锁，这里不能超过最大的次数
            } while (tryCount < MAX_TYR_COUNT);
            log.error("tryLock error,lockKey:{}", lockKey);
        } catch (Exception e) {
            log.error("tryLock error,lockKey:{}", lockKey, e);
        }
        return false;
    }

    /**
     * 解锁
     * 用lua 脚本保证 一致性
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result))
            return true;
        return false;
    }
}