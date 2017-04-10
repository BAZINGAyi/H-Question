package com.bazinga;

import com.bazinga.dao.QuestionDAO;
import com.bazinga.dao.UserDAO;
import com.bazinga.model.Question;
import com.bazinga.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HQuestionApplication.class)
@Sql("/init-schema.sql")
public class initDatabaseTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void contextLoads() {
	}

	// 执行每个方法的时候需要加注解
	@Test
	public void initDatabase(){

		Random random = new Random();

		for(int i = 0; i < 11; i++){

			User user = new User();

			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
					random.nextInt(1000)));

			user.setName(String.format("User%d",i));

			user.setPassword("");

			user.setSalt("");

			userDAO.addUser(user);

			user.setPassword("xx");

			userDAO.updatePassword(user);

			Question question = new Question();

			question.setCommentCount(i);

			Date date = new Date();

			date.setTime(date.getTime() + 1000 * 3600 * i);

			question.setCreatedDate(date);

			question.setUserId(i + 1);

			question.setTitle(String.format("TITLE{%d}",i));

			question.setContent("Balalaalallalal Content" + i);

			questionDAO.addQuestion(question);

		}

		Assert.assertEquals("xx",userDAO.selectById(1).getPassword());

		userDAO.deleteById(2);

		Assert.assertNull(userDAO.selectById(2));

		// 注意使用 xml 的方式查询数据库，文件的包名要和定义的 java 文件一样，可以打开
		// 文件夹进行对比
		//System.out.print(questionDAO.selectLatestQuestions(0,0,10));

	}




}
