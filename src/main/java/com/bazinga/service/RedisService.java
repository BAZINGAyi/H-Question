package com.bazinga.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 如果Application类所在的包为：
 * io.github.gefangshuai.app
 * ，则只会扫描io.github.gefangshuai.app
 * 包及其所有子包，如果service或dao所在包不在io.github.gefangshuai.app
 * 及其子包下，则不会被扫描！
 */

@Service
public class RedisService implements InitializingBean{
    // 用于初始化变量
    // 相当于 dao 层
    private static final Logger logger= LoggerFactory.getLogger(RedisService.class);

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            jedisPool = new JedisPool("redis://localhost:6379/9");
        }catch (Exception e){
            logger.error("初始化连接Rdies失败"+ e.getMessage());
        }


    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 关注赞和踩的结果所以选用 set 作为存储结构
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("Rides发送异常"+e.getMessage());
        }finally {
            if(jedis != null)
                jedis.close();
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 关注赞和踩的结果所以选用 set 作为存储结构
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("Rides移除异常"+e.getMessage());
        }finally {
            if(jedis != null)
                jedis.close();
        }
        return 0;
    }
    // 返回多少人
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 关注赞和踩的结果所以选用 set 作为存储结构
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("Rides移除异常"+e.getMessage());
        }finally {
            if(jedis != null)
                jedis.close();
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            // 关注赞和踩的结果所以选用 set 作为存储结构
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("Rides移除异常"+e.getMessage());
        }finally {
            if(jedis != null)
                jedis.close();
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


}
