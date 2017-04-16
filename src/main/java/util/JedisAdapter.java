package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bazinga.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * Created by bazinga on 2017/4/16.
 */

    // 用于记录默认的配置文件
    // 设置带过期功能用于验证码的设置
    // 数值的操作如阅读数
    // list 列表
    // hash 随时加随时减的属性
    // set 抽奖，随机取值
    // 优先队列，排行榜 排序
public class JedisAdapter {

    public static void print(int index, Object obj){
        System.out.println(String.format("%d %s",index,obj.toString()));
    }

    public static void main(String[] argv){
        // 选择 第九个数据库
          Jedis jedis = new Jedis("redis://localhost:6379/9");
        // 删除选择数据库的内容
        jedis.flushDB();
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newHello");
        print(2,jedis.get("newHello"));
        // 带过期时间 时间到自动删除
        jedis.setex("hello2",15,"world");

        // 数值的操作
        jedis.set("pv","100");
        jedis.incr("pv");
        print(3,jedis.get("pv"));
        jedis.decrBy("pv",5);
        // 正则表达式
        print(4,jedis.keys("*"));

        // LIST
        String listName = "list";
        jedis.del(listName);
        for (int i =0; i < 10; i++){
            jedis.lpush(listName,"a" + String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lindex(listName,3));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","bb"));
        print(11,jedis.lrange(listName,0,12));

        // hash
        String userKey = "userxx";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"phone","13123123");
        jedis.hset(userKey,"age","21");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14,jedis.hexists(userKey,"email"));
        print(15,jedis.hexists(userKey,"age"));
        print(16,jedis.hkeys(userKey));
        print(17,jedis.hvals(userKey));
        // 不存才覆盖
        jedis.hsetnx(userKey,"school","zd");
        jedis.hsetnx(userKey,"name","zyw");
        print(18,jedis.hgetAll(userKey));

        // set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i =0; i < 10; i++){
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * i));
        }
        print(20,jedis.smembers(likeKey1));
        print(21,jedis.smembers(likeKey2));
        // 求并集
        print(22,jedis.sunion(likeKey1,likeKey2));
        // 第一个集合有第二个没有
        print(23,jedis.sdiff(likeKey1,likeKey2));
        // 求交集
        print(25,jedis.sinter(likeKey1,likeKey2));
        print(26,jedis.sismember(likeKey1,"4"));
        print(26,jedis.sismember(likeKey1,"16"));
        // 删掉
        jedis.srem(likeKey1,"5");
        print(27,jedis.smembers(likeKey1));
        // 把 25 这个value 移动到 likeKey1 中
        jedis.smove(likeKey2,likeKey1,"25");
        print(28,jedis.smembers(likeKey1));
        print(29,jedis.scard(likeKey1));

        // 优先队列 Sort sets （带权重）
        String rankey = "ranKey";
        jedis.zadd(rankey,15,"jim");
        jedis.zadd(rankey,60,"bin");
        jedis.zadd(rankey,40,"Ben");
        jedis.zadd(rankey,60,"Ben2");
        jedis.zadd(rankey,10,"Lee");
        jedis.zadd(rankey,60,"Ben32");
        jedis.zadd(rankey,90,"My");
        jedis.zadd(rankey,100,"Mei");
        print(30,jedis.zcard(rankey));
        print(31,jedis.zcount(rankey,61,100));
        print(32,jedis.zscore(rankey,"Lee"));
        jedis.zincrby(rankey,2,"Lee");
        print(33,jedis.zscore(rankey,"Lee"));
        jedis.zincrby(rankey,2,"Lee1");
        print(34,jedis.zrange(rankey,0,100));
        print(35,jedis.zrevrange(rankey,1,4));
        for (Tuple tuple:jedis.zrangeByScoreWithScores(rankey,"60","100")){
            print(37,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }
        print(38,jedis.zrank(rankey,"Ben"));
        print(39,jedis.zrevrank(rankey,"Ben"));
        String setKey = "zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");
        jedis.zadd(setKey,1,"f");
        print(40,jedis.zlexcount(setKey,"-","+"));
        print(41,jedis.zlexcount(setKey,"[b","[d"));
        print(42,jedis.zlexcount(setKey,"(b","[d"));
        jedis.zrem(setKey,"b");
        print(43,jedis.zrange(setKey,0,10));
        jedis.zremrangeByLex(setKey,"(b","+");
        print(44,jedis.zrange(setKey,0,10));

        // 连接池
//        JedisPool jedisPool = new JedisPool();
//        for (int i = 0; i < 100; i++){
//            Jedis j = jedisPool.getResource();
//            print(45,j.get("pv"));
//            // 一定要还回去
//            j.close();
//        }

        // 缓存
        User user = new User();
        user.setName("sda");
        user.setHeadUrl("a.png");
        user.setSalt("apng");
        user.setPassword("dasd");
        user.setId(1);
        print(46,JSONObject.toJSON(user));
        jedis.set("user1", JSONObject.toJSONString(user));
        String value = jedis.get("user1");
        User user1 = JSON.parseObject(value,User.class);

        // 主从同步 操作对主服务器 从服务自动更新主服务的东西
    }
}
