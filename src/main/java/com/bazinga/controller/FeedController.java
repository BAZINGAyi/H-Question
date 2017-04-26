package com.bazinga.controller;

import com.bazinga.model.EntityType;
import com.bazinga.model.Feed;
import com.bazinga.model.HostHolder;
import com.bazinga.service.FeedService;
import com.bazinga.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bazinga on 2017/4/26.
 */
@Controller
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    // 拉的模式
    @RequestMapping(path = {"/pullfeeds"},method = {RequestMethod.GET})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.getUsers() == null ? 0 : hostHolder.getUsers().getId();
        List<Integer> followees = new ArrayList<>();
        // 未登录
        if (localUserId == 0){

        }else {
            // 查找我关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER,
                    Integer.MAX_VALUE);
        }

        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);

        model.addAttribute("feeds",feeds);

        return "feeds";
    }
}
