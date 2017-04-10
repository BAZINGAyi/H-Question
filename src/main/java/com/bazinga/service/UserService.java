package com.bazinga.service;

import com.bazinga.dao.UserDAO;
import com.bazinga.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bazinga on 2017/4/10.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getUser(int id){

        return userDAO.selectById(id);
    }
}
