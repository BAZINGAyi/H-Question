package com.bazinga.controller;

/**
 * Created by bazinga on 2017/4/7.
 */

import com.bazinga.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {

    @RequestMapping(path = {"/","index"})
    @ResponseBody
    public String index(HttpSession httpSession) {

        return "Hello Spring-Boot " + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public  String profile(@PathVariable("userId") int userId,
                           @PathVariable("groupId") String groupId,
                           @RequestParam(value = "type",defaultValue = "1") int type,
                           @RequestParam(value = "key",defaultValue = "zhang",
                                   required = false)
                                       String key ){

        return String.format("Profile Page of %s / %d  type = %d " +
                "key = %s",groupId,userId,type,key);
    }

// 传递给模版需要使用 Model
    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})
    public String template(Model model) {

        model.addAttribute("value1", "vvvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("LEE"));
        return "home";
    }


    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String template(Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                           @CookieValue("JSESSIONID") String sessionId
   ) {

        //  request 是从网页上读取的
        StringBuilder sb = new StringBuilder();

        sb.append("SessionId : " + sessionId );

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        Enumeration<String> headrNames = request.getHeaderNames();
        while (headrNames.hasMoreElements()){
            String name = headrNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        if(request.getCookies() != null){

            for(Cookie cookie : request.getCookies()){
                sb.append("Cookie : " +
                        cookie.getName()+" " + cookie.getValue());
            }
        }

        // reponse 是返回给用户的
        response.addHeader("nowcodeId","hello");
        response.addCookie(new Cookie("username","nowcoder"));

        return sb.toString();

    }


    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,
                           HttpSession httpSession){

        httpSession.setAttribute("msg","jump from redirect");

        return "redirect:/";
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw  new IllegalArgumentException("参数不对");
    }

    @RequestMapping(path = {"/redirect1/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect1(@PathVariable("code") int code,
                                  HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect1");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) { //301    永久性的跳转  302 暂时的跳转
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}
