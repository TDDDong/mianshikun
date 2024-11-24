package com.dd.mianshikun.constant;

/**
 * Redis常量
 */
public interface RedisConstant {

    /**
     * 用户签到记录的Redis Key前缀
     */
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";

    /**
     * 获取用户签到记录的redis key
     * @param year
     * @param userId
     * @return
     */
    static String getUserSignInRedisKeyPrefix(int year, long userId) {
        return String.format("%s:%s:%s", USER_SIGN_IN_REDIS_KEY_PREFIX, year, userId);
    }
}
