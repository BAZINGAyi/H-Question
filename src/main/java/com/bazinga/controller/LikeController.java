package com.bazinga.controller;

import com.bazinga.async.EventModel;
import com.bazinga.async.EventProducer;
import com.bazinga.async.EventType;
import com.bazinga.model.Comment;
import com.bazinga.model.EntityType;
import com.bazinga.model.HostHolder;
import com.bazinga.service.CommentService;
import com.bazinga.service.LikeService;
import com.oracle.javafx.jmx.json.JSONReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.WendaUtil;

/**
 * Created by bazinga on 2017/4/16.
 */
@Controller

public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUsers() == null) {
            return WendaUtil.getJsonString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        EventModel eventModel = new EventModel(EventType.LIKE);
        eventModel.setActorId(hostHolder.getUsers().getId())
                .setEntityOwnerId(comment.getUserId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setExt("questionId",String.valueOf(comment.getEntityId()));

        eventProducer.fireEvent(eventModel);

        long likeCount = likeService.like(hostHolder.getUsers().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJsonString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUsers() == null) {
            return WendaUtil.getJsonString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUsers().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJsonString(0, String.valueOf(likeCount));
    }

}
