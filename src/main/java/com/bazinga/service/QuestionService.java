package com.bazinga.service;

import com.bazinga.dao.QuestionDAO;
import com.bazinga.dao.UserDAO;
import com.bazinga.model.Question;
import com.bazinga.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bazinga on 2017/4/10.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId,int offset, int limit ){

        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }
}
