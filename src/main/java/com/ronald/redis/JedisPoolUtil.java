package com.ronald.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by wenye on 2016/12/6.
 */
public class JedisPoolUtil {

    private static JedisPool pool = null;
    private static String host = "192.168.48.140";
    private static int port = 6379;
    private static String ps = "888888";

    private static JedisPool getPool(){
        if(pool == null){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(10);
            config.setMaxWaitMillis(1000);
            config.setMaxTotal(100);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, host, port, 0, ps);

        }
        return pool;
    }

    public static Jedis getJedis(){
        getPool();
        Jedis resource = JedisPoolUtil.pool.getResource();
        return resource;
    }

    public static void close(Jedis jedis){
        if(pool != null && jedis != null){
//            pool.returnResource(jedis);
            jedis.close();
        }
    }
}
