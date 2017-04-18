package com.bazinga.async.hanlder;

import com.bazinga.async.EventHandler;
import com.bazinga.async.EventModel;
import com.bazinga.async.EventType;
import com.bazinga.model.Message;
import com.bazinga.model.User;
import com.bazinga.service.MessageService;
import com.bazinga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import util.WendaUtil;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by bazinga on 2017/4/17.
 */
@org.springframework.stereotype.Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        // user4 为默认的系统用户
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
