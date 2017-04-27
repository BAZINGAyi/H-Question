package com.bazinga.async.hanlder;
import com.bazinga.async.EventHandler;
import com.bazinga.async.EventModel;
import com.bazinga.async.EventType;
import com.bazinga.model.EntityType;
import com.bazinga.model.Feed;
import com.bazinga.model.Message;
import com.bazinga.model.User;
import com.bazinga.service.FeedService;
import com.bazinga.service.MessageService;
import com.bazinga.service.RedisService;
import com.bazinga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.RedisKeyUtil;
import util.WendaUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class UnFollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    FeedService feedService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "取消关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "取消关注了你,http://127.0.0.1:8080/user/" + model.getActorId());

            // 如果取消关注，则清空 用户关注的人的所有 feed 的Id
            // 得到
            List<Feed> feedsId = feedService.getUserFeeds(model.getEntityId());
            if(feedsId != null){
                for (Feed feed : feedsId){
                    redisService.lremfeed(RedisKeyUtil.getTimelineKey
                                    (model.getActorId()),feed.getId());
                }
            }
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}
