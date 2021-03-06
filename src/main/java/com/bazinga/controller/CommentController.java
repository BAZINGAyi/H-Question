package com.bazinga.controller;

import com.bazinga.aspect.LogAspect;
import com.bazinga.async.EventHandler;
import com.bazinga.async.EventModel;
import com.bazinga.async.EventProducer;
import com.bazinga.async.EventType;
import com.bazinga.model.Comment;
import com.bazinga.model.EntityType;
import com.bazinga.model.HostHolder;
import com.bazinga.service.CommentService;
import com.bazinga.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import util.WendaUtil;

import java.util.Date;

/**
 * Created by bazinga on 2017/4/15.
 */
@Controller
public class CommentController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);


    @RequestMapping(path = {"/addComment"},method ={RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            if (hostHolder != null) {
                comment.setUserId(hostHolder.getUsers().getId());
            } else {
                comment.setUserId(WendaUtil.Anonymous_USERID);
            }
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);
            // 这块应该使用数据库的事务操作
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(questionId,count);

            // 给发表该评论的用户的粉丝发送新鲜事
            eventProducer.fireEvent(new EventModel(EventType.COMMENT_MyFans)
                    .setActorId(comment.getUserId())
                    .setEntityId(questionId));

            // 给关注该问题的用户发表评论
            eventProducer.fireEvent(new EventModel(EventType.COMMENT_Focus_Question)
                    .setActorId(comment.getUserId())
                    .setEntityId(questionId));

        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
