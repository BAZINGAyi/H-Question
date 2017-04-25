package util;

import redis.clients.jedis.Jedis;

/**
 * Created by bazinga on 2017/4/19.
 */
public class ItemRedisTest {
    public static void main(String[] args){
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        // 优先队列 Sort sets （带权重）
        String rankey = "FOLLOWER:23:3";
        print(34,jedis.zrange(rankey,0,100));
    }

    public static void print(int index, Object obj){
        System.out.println(String.format("%d %s",index,obj.toString()));
    }
}
