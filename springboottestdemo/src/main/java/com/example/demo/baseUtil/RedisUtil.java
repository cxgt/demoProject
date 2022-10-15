package com.example.demo.baseUtil;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * redis 操作基础类
 *
 * @author chenxin
 * @date 2022/09/26 23:49
 */
@Component
public class RedisUtil {
//    @Value("$(spring.redis.host:127.0.0.1)")
    private String host ="10.124.145.5";
//    @Value("$(spring.redis.port:6479)")
    private int port=6479;

    private int expire = 0;
//    @Value("$(spring.redis.timedout:0)")
    private int timeout = 0;
//    @Value("$(spring.redis.passworld)")
    private String password = "ae65b41b3a9a44f3";

    private String user="testgdhbGD234";

    private  static JedisPool jedisPool = null;

    @PostConstruct
    public  void init(){
        if(jedisPool == null){
            if(password != null && !"".equals(password)){
                jedisPool=new JedisPool(new JedisPoolConfig(),host,port,timeout,user,password);
            }else if(timeout != 0){
                jedisPool=new JedisPool(new JedisPoolConfig(),host,port,timeout);
            }else{
                jedisPool=new JedisPool(new JedisPoolConfig(),host,port);
            }
        }
    }

    public static Object get(String key){
            byte[] bytes=getValue(key.getBytes());
            if(bytes==null) return null;
            return SerializeUtils.deserialize(bytes);
    }

    private static byte[] getValue(byte[] bytes) {
        Jedis jedis=null;
        byte[] bytes1=null;
        try{
            jedis=jedisPool.getResource();
            bytes1=jedis.get(bytes);
        }catch (Exception e){
            throw e;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return bytes1;
    }

    public static void put(String key,String value,int timeExpire){
        putDataWithSerialize(key.getBytes(), SerializeUtils.serialize(value),timeExpire);
    }

    private static void putDataWithSerialize(byte[] bytes, byte[] serialize, int timeExpire) {
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.set(bytes,serialize);
            if(timeExpire != 0){
                jedis.expire(bytes,timeExpire);
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }
}
