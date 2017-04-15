package com.bazinga.controller;

import com.bazinga.aspect.LogAspect;
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

    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);


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
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            commentService.addComment(comment);
            // 这块应该使用数据库的事务操作
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(questionId,count);
        }catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
