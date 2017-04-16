package com.bazinga.controller;

import com.bazinga.aspect.LogAspect;
import com.bazinga.model.Question;
import com.bazinga.model.ViewObject;
import com.bazinga.service.QuestionService;
import com.bazinga.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bazinga on 2017/4/10.
 */
@Controller
public class HomeController {

    private static final Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {

        List<ViewObject> vos =  getQuestions(0,0,10);

        model.addAttribute("vos",vos);

        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userId,int offset, int limit){

        List<Question> questionList = questionService.getLatestQuestions(userId,offset,limit);

        List<ViewObject> vos = new ArrayList<ViewObject>();

        for (Question question : questionList){

            ViewObject vo = new ViewObject();

            vo.set("question",question);

            vo.set("user",userService.getUser(question.getUserId()));

            vos.add(vo);

        }

        return vos;
    }
}
