package com.bazinga.async;

import com.alibaba.fastjson.JSONObject;
import com.bazinga.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisKeyUtil;

/**
 * Created by bazinga on 2017/4/17.
 */
// 加入事件 入口
@Service
public class EventProducer {
    @Autowired
    RedisService redisService;

    public boolean fireEvent(EventModel eventModel){
        try {
            // 将需要处理的事件序列化保存到 Redies 中（内存）
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            redisService.lpush(key,json);
            return true;
        }catch (Exception e){
            return  false;
        }
    }
}
