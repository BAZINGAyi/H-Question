package com.bazinga.service;

import com.bazinga.dao.QuestionDAO;
import com.bazinga.dao.UserDAO;
import com.bazinga.model.Question;
import com.bazinga.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by bazinga on 2017/4/10.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId,int offset, int limit ){

        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }

    public int addQuestion(Question question){
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionDAO.addQuestion(question) > 0 ? question.getId():0;
    }

    public Question selectById(int qid){
        return questionDAO.selectById(qid);
    }
}
