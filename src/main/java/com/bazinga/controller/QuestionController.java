package com.bazinga.controller;

import com.alibaba.fastjson.JSONObject;
import com.bazinga.aspect.LogAspect;
import com.bazinga.model.HostHolder;
import com.bazinga.model.Question;
import com.bazinga.service.QuestionService;
import com.bazinga.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import util.WendaUtil;

import java.util.Date;

/**
 * Created by bazinga on 2017/4/13.
 */
@Controller
public class QuestionController {

    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    public static String getJsonString(int code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/question/add",method={RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content")String content){

        try{

            Question question = new Question();

            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            if(hostHolder.getUsers() != null)
            question.setUserId(hostHolder.getUsers().getId());
            else{
                //999 返回到登录页面
                return WendaUtil.getJsonString(999);
               // question.setUserId(WendaUtil.Anonymous_USERID);
            }

            if(questionService.addQuestion(question) > 0){
                // 成功返回 0
                return WendaUtil.getJsonString(0);
            }

        }catch (Exception e){
            logger.error("增加题目失败" + e.getMessage());
        }
        // 失败返回 1
        return WendaUtil.getJsonString(1);
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model ,
                                 @PathVariable("qid")int qid){
        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        return "detail";
    }
}
